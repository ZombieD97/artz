package com.home.artz.model.repository.artwork

import androidx.paging.Pager
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.Artwork

interface IArtworkRepository {
    suspend fun fetchArtworks(init: Boolean): List<Artwork>?
    suspend fun addToFavorites(artwork: Artwork)
    suspend fun removeFromFavorites(artwork: Artwork)
}