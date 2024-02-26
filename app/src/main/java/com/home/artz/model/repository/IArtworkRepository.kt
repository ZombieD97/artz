package com.home.artz.model.repository

import androidx.paging.Pager
import com.home.artz.model.datamodel.Artwork

interface IArtworkRepository {
    suspend fun fetchArtworks(init: Boolean): List<Artwork>?
    suspend fun addToFavorites(artwork: Artwork)
    suspend fun removeFromFavorites(artwork: Artwork)
}