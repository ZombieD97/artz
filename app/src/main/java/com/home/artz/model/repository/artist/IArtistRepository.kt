package com.home.artz.model.repository.artist

import com.home.artz.model.datamodel.Artist

interface IArtistRepository {
    suspend fun getArtistsBy(artworkId: String): List<Artist>?
}