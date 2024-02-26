package com.home.artz.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.repository.IArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(private val artworkRepository: IArtworkRepository): ViewModel() {

    val cachedArtworks = mutableStateOf<List<Artwork>>(emptyList())
    var selectedArtwork = mutableStateOf<Artwork?>(null)

    @StringRes
    val userMessage = mutableStateOf<Int?>(null)
    private val _showPagingLoader = mutableStateOf(false)
    val showPagingLoader: State<Boolean> = _showPagingLoader

    init {
        fetchArtworks(true)
    }

    private fun fetchArtworks(init: Boolean) {
        viewModelScope.launch {
            artworkRepository.fetchArtworks(init)?.let {
                if (init) {
                    cachedArtworks.value = it
                } else {
                    val combinedList = mutableListOf<Artwork>().apply {
                        addAll(cachedArtworks.value)
                        addAll(it)
                    } // :(
                    cachedArtworks.value = combinedList.toList()
                    _showPagingLoader.value = false
                }
            }
        }
    }

    fun modifyFavoriteStateOn(artwork: Artwork, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                artworkRepository.addToFavorites(artwork)
                artwork.isFavorite = true
                userMessage.value = R.string.artwork_added_to_favorites
            } else {
                artworkRepository.removeFromFavorites(artwork)
                artwork.isFavorite = false
                userMessage.value = R.string.artwork_removed_from_favorites
            }
        }
    }

    fun setSelectedArtwork(artwork: Artwork) {
        selectedArtwork.value = artwork
    }

    fun clearUserMessage() {
        userMessage.value = null
    }

    fun loadNextPage() {
        _showPagingLoader.value = true
        fetchArtworks(false)
    }
}