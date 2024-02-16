package com.home.artz.data.model

import com.home.artz.data.communication.CommunicationService
import javax.inject.Inject

class ArtworkRepository @Inject constructor(private var communicationService: CommunicationService): IArtworkRepository {

    suspend fun getArtworks(): List<Artwork> {
        val artworks = communicationService.getArtworks().body()?.embeddedResponse?.artworks ?: emptyList()
        return artworks.onEach {
            it.imageLinks.artworkUrl.href = it.imageLinks.artworkUrl.href.removeSuffix("{image_version}.jpg") + "medium.jpg"
        }
    }
}