package com.home.artz.model.repository.artwork

import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.network.APIService
import com.home.artz.model.repository.Constants
import com.home.artz.model.repository.artist.ArtistRepository
import com.home.artz.model.repository.artist.IArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtworkRepository @Inject constructor(
    private val apiService: APIService,
    private val database: ArtzDatabase
) : IArtworkRepository {

    private val ARTWORK_PAGE_SIZE = 30
    private var nextPageLink: String? = null

    override suspend fun fetchArtworks(init: Boolean): List<Artwork>? {
        return if (init) {
            val url = Constants.BASE_URL + "api/artworks?size=" + ARTWORK_PAGE_SIZE
            getArtworks(url)
        } else {
            nextPageLink?.let { getArtworks(it) }
        }
    }

    private suspend fun getArtworks(url: String): List<Artwork>? {
        val response = apiService.getArtworks(url).body()
        val artworks = response?.embeddedResponse?.artworks
        return artworks?.let {
            nextPageLink = response.paginationLinks.nextPage.imageUrl

            val favoriteArtworkIds = database.favoritesDao().getFavorites().map { it.id }

            return artworks.onEach { artwork ->
                artwork.isFavorite = favoriteArtworkIds.contains(artwork.id)
                artwork.imageLinks.artworkUrl?.imageUrl?.let {
                    artwork.imageLinks.artworkUrl.mediumImage = appendImageVersion(it, ImageVersion.MEDIUM)
                    artwork.imageLinks.artworkUrl.largeImageUrl = appendImageVersion(it, ImageVersion.LARGE)
                }
            }
        }
    }

    override suspend fun addToFavorites(artwork: Artwork) {
        withContext(Dispatchers.IO) {
            database.favoritesDao().insert(artwork)
        }
    }

    override suspend fun removeFromFavorites(artwork: Artwork) {
        withContext(Dispatchers.IO) {
            database.favoritesDao().delete(artwork)
        }
    }
}