package com.home.artz.model.network

import com.home.artz.model.datamodel.ArtistsEmbeddedResponse
import com.home.artz.model.datamodel.ArtworksEmbeddedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getArtworks(@Url url: String): Response<ArtworksEmbeddedResponse>

    @GET("api/artists")
    suspend fun getArtistsBy(@Query("artwork_id") artworkId: String): Response<ArtistsEmbeddedResponse>
}