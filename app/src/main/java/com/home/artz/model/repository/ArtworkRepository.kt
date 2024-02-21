package com.home.artz.model.repository

import android.util.Log
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

    override suspend fun fetchArtworks(): List<Artwork> {
        val artworks =
            apiService.getArtworks().body()?.embeddedResponse?.artworks ?: emptyList()
        val favoriteArtworkIds = database.favoritesDao().getFavorites().map { it.id }

        return artworks.onEach { artwork ->
            artwork.isFavorite = favoriteArtworkIds.contains(artwork.id)
            artwork.appendImageVersion(ImageVersion.MEDIUM)
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