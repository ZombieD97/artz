package com.home.artz.data.model

import com.home.artz.data.communication.CommunicationService
import javax.inject.Inject

class ArtworkRepository @Inject constructor(private var communicationService: CommunicationService): IArtworkRepository {

    suspend fun getArtworks() {
        communicationService.getArtworks()
    }
}