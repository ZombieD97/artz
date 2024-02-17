package com.home.artz.view.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.home.artz.data.model.Artwork
import com.home.artz.data.model.ArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(private val artworkRepository: ArtworkRepository): ViewModel() {

    var selectedArtwork = mutableStateOf<Artwork?>(null)
    private val _artworks = mutableStateOf<List<Artwork>>(emptyList())
    val artworks: State<List<Artwork>>
        get() = _artworks

    init {
        fetchArtworks()
    }

    private fun fetchArtworks() {
        viewModelScope.launch {
            _artworks.value = artworkRepository.getArtworks()
        }
    }

    fun modifyFavoriteStateOn(artwork: Artwork, isFavorite: Boolean) {
        //valami
    }
}