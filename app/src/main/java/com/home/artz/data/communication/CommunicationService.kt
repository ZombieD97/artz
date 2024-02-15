package com.home.artz.data.communication

import com.home.artz.data.model.Artwork
import retrofit2.Response
import retrofit2.http.GET

interface CommunicationService {

    @GET("/api/artworks?size=30")
    suspend fun getArtworks(): Response<Artwork>
}