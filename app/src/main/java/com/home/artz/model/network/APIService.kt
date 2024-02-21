package com.home.artz.model.network

import com.home.artz.model.datamodel.DiscoverEmbeddedResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("/api/artworks?size=30")
    suspend fun getArtworks(): Response<DiscoverEmbeddedResponse>
}