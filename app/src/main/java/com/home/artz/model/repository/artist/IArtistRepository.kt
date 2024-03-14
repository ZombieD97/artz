package com.home.artz.model.repository.artist

import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.SearchResult
import com.home.artz.model.repository.base.IBaseRepository

interface IArtistRepository: IBaseRepository {
    suspend fun getArtistsBy(artworkId: String): List<Artist>?
    suspend fun searchArtists(searchText: String): List<SearchResult>?
    suspend fun getArtist(url: String): Artist?
}