package com.home.artz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.SearchResult
import com.home.artz.model.repository.artist.IArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: IArtistRepository
) : BaseViewModel(artistRepository) {

    var selectedArtist = mutableStateOf<Artist?>(null)
    val searchResults = mutableStateOf<List<SearchResult>?>(null)
    val popularArtists = mutableStateOf<List<Artist>?>(emptyList())

    init {
        fetchPopularArtists()
    }

    private fun fetchPopularArtists() {
        viewModelScope.launch {
            val popularArtistsToSearch = listOf(
                "Rembrandt Van Rijn",
                "Johannes Vermeer",
                "Leonardo da Vinci",
                "Pablo Picasso",
                "Salvador Dali",
                "Michelangelo Buonarroti"
            )
            val artists = mutableListOf<Artist>()
            popularArtistsToSearch.forEach { artistName ->
                artistRepository.searchArtists(artistName)?.first()?.let { searchResult ->
                    artistRepository.getArtist(searchResult.searchLink.link.url)
                        ?.let { artists.add(it) }
                }
            }
            popularArtists.value = artists.ifEmpty { null }
        }
    }

    fun searchArtists(searchText: String) {
        viewModelScope.launch {
            searchResults.value = artistRepository.searchArtists(searchText)
        }
    }

    fun searchResultSelected(searchResult: SearchResult) {
        viewModelScope.launch {
            artistRepository.searchArtists(searchResult.title)?.first()?.let { searchResult ->
                artistRepository.getArtist(searchResult.searchLink.link.url)
                    ?.let { selectedArtist.value = it }
            }
        }
    }

    fun clearSearch() {
        searchResults.value = null
    }
}