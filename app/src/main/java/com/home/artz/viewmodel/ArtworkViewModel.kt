package com.home.artz.viewmodel

import androidx.annotation.StringRes
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

    private val artworks = mutableStateOf<List<Artwork>>(emptyList())
    val cachedArtworks = mutableStateOf<List<Artwork>>(emptyList())
    var selectedArtwork = mutableStateOf<Artwork?>(null)

    @StringRes
    val userMessage = mutableStateOf<Int?>(null)

    init {
        fetchArtworks()
    }

    private fun fetchArtworks() {
        viewModelScope.launch {
            artworks.value = artworkRepository.fetchArtworks()
            cachedArtworks.value = artworks.value
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

    fun setArtworkSelected(artwork: Artwork) {
        selectedArtwork.value = artwork
    }

    fun clearUserMessage() {
        userMessage.value = null
    }
}