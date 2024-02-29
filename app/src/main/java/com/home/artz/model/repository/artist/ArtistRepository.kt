package com.home.artz.model.repository.artist

import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.network.APIService
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private var apiService: APIService
) : IArtistRepository {

    override suspend fun getArtistsBy(artworkId: String): List<Artist>? {
        return apiService.getArtistsBy(artworkId).body()?.embeddedResponse?.artists
    }
}