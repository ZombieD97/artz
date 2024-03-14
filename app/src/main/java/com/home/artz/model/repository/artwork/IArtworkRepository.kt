package com.home.artz.model.repository.artwork

import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.repository.base.IBaseRepository

interface IArtworkRepository: IBaseRepository {
    suspend fun fetchArtworks(init: Boolean): List<Artwork>?
    suspend fun addToFavorites(artwork: Artwork)
    suspend fun removeFromFavorites(artwork: Artwork)
}