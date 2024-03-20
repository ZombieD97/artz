package com.home.artz.model.repository.artwork

import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.network.APIService
import com.home.artz.model.Constants
import com.home.artz.model.Constants.Companion.ARTWORK_PAGE_SIZE
import com.home.artz.model.repository.base.BaseRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtworkRepository @Inject constructor(
    private val apiService: APIService,
    private val database: ArtzDatabase,
    moshi: Moshi
) : BaseRepository(moshi), IArtworkRepository {

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
        val response = handleRequest {
            apiService.getArtworks(url)
        }
        val artworks = response?.embeddedResponse?.artworks?.filter { it.links.imageLinks?.imageUrl != null } ?: return null

        if (artworks.isEmpty()) return null

        nextPageLink = response.paginationLinks.nextPage.imageUrl

        val favoriteArtworkIds = getFavoriteArtworks().map { favorite -> favorite.id }

        return artworks.onEach { artwork ->
            artwork.isFavorite = favoriteArtworkIds.contains(artwork.id)
            artwork.links.imageLinks?.imageUrl?.let { imageUrl ->
                artwork.links.imageLinks.mediumImageUrl = appendImageVersion(imageUrl, ImageVersion.MEDIUM)
                artwork.links.imageLinks.largeImageUrl = appendImageVersion(imageUrl, ImageVersion.LARGE)
            }
        }
    }

    override suspend fun getFavoriteArtworks(): List<Artwork> {
        return database.favoritesDao().getFavorites()
    }

    override suspend fun saveToFavorites(artwork: Artwork) {
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