package com.home.artz.model.network

import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.ArtistsEmbeddedResponse
import com.home.artz.model.datamodel.ArtworksEmbeddedResponse
import com.home.artz.model.datamodel.SearchEmbeddedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    //region Artworks
    @GET
    suspend fun getArtworks(@Url url: String): Response<ArtworksEmbeddedResponse>
    //endregion

    //region Artists
    @GET("api/artists")
    suspend fun getArtistsBy(@Query("artwork_id") artworkId: String): Response<ArtistsEmbeddedResponse>

    @GET
    suspend fun getArtist(@Url url: String): Response<Artist>
    //endregion

    //region Search
    @GET("api/search")
    suspend fun searchArtists(@Query("q") searchText: String): Response<SearchEmbeddedResponse>
    //endregion
}