package com.home.artz.model.repository.artist

import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.SearchResult
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.network.APIService
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private var apiService: APIService
) : IArtistRepository {

    override suspend fun getArtistsBy(artworkId: String): List<Artist>? {
        val artists = apiService.getArtistsBy(artworkId).body()?.embeddedResponse?.artists
        artists?.onEach { artist ->
            artist.links?.image?.squareImage = appendImageVersion(artist.links?.image?.url, ImageVersion.SQUARE)
        }
        return artists
    }

    override suspend fun getArtist(url: String): Artist? {
        val artist = apiService.getArtist(url).body()
        artist?.links?.image?.squareImage = appendImageVersion(artist?.links?.image?.url, ImageVersion.SQUARE)
        return artist
    }

    override suspend fun searchArtists(searchText: String): List<SearchResult>? {
        val results = apiService.searchArtists(searchText).body()?.embeddedResponse?.results
        return results?.filter { it.type == "artist" }
    }
}