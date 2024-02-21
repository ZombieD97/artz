package com.home.artz.model.repository

import com.home.artz.model.datamodel.Artwork

interface IArtworkRepository {
    suspend fun fetchArtworks(): List<Artwork>
    suspend fun addToFavorites(artwork: Artwork)
    suspend fun removeFromFavorites(artwork: Artwork)
}