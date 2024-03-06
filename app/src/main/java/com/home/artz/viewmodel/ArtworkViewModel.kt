package com.home.artz.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.repository.artist.ArtistRepository
import com.home.artz.model.repository.artwork.IArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class ArtworkViewModel @Inject constructor(
    private val artworkRepository: IArtworkRepository,
    private val artistRepository: ArtistRepository) : ViewModel() {

    val cachedArtworks = mutableStateOf<List<Artwork>>(emptyList())
    var selectedArtwork = mutableStateOf<Artwork?>(null)
    var selectedArtworkLargeImage = mutableStateOf<Bitmap?>(null)

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
        selectedArtworkLargeImage.value = null
        selectedArtwork.value = artwork
        viewModelScope.launch {
            selectedArtwork.value?.let {
                it.artists = artistRepository.getArtistsBy(it.id)
                val imageUrl = selectedArtwork.value?.imageLinks?.artworkUrl?.largeImageUrl
                    ?: selectedArtwork.value?.imageLinks?.artworkUrl?.mediumImage
                imageUrl?.let {
                    withContext(Dispatchers.IO) {
                        try {
                            val url = URL(it)
                            selectedArtworkLargeImage.value = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        } catch (e: IOException) {
                            userMessage.value = R.string.something_went_wrong
                        }
                    }
                }
            }
        }
    }

    fun clearUserMessage() {
        userMessage.value = null
    }

    fun loadNextPage() {
        _showPagingLoader.value = true
        fetchArtworks(false)
    }
}