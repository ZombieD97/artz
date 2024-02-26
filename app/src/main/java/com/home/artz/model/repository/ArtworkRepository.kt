package com.home.artz.model.repository

import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtworkRepository @Inject constructor(
    private var apiService: APIService,
    private var database: ArtzDatabase
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
            nextPageLink = response.paginationLinks.nextPage.href

            val favoriteArtworkIds = database.favoritesDao().getFavorites().map { it.id }

            return artworks.onEach { artwork ->
                artwork.isFavorite = favoriteArtworkIds.contains(artwork.id)
                artwork.appendImageVersion(ImageVersion.MEDIUM)
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