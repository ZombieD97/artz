package com.home.artz.model.repository.artist

import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.SearchResult
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.network.APIService
import com.home.artz.model.repository.base.BaseRepository
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private var apiService: APIService,
    moshi: Moshi
) : BaseRepository(moshi), IArtistRepository {

    override suspend fun getArtistsBy(artworkId: String): List<Artist>? {
        val artists = handleRequest {
            apiService.getArtistsBy(artworkId)
        }?.embeddedResponse?.artists
        artists?.onEach { artist ->
            artist.links?.image?.squareImage =
                appendImageVersion(artist.links?.image?.url, ImageVersion.SQUARE)
        }
        return artists
    }

    override suspend fun getArtistBy(url: String): Artist? {
        val artist = handleRequest {
            apiService.getArtist(url)
        }
        artist?.links?.image?.squareImage =
            appendImageVersion(artist?.links?.image?.url, ImageVersion.SQUARE)
        return artist
    }

    override suspend fun searchArtists(searchText: String): List<SearchResult>? {
        val results =
            handleRequest { apiService.searchArtists(searchText) }?.embeddedResponse?.results
        return results?.filter { it.type == "artist" }
    }
}