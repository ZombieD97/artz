package com.home.artz.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.UserMessage
import com.home.artz.model.repository.artist.IArtistRepository
import com.home.artz.model.repository.artwork.IArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(
    private val artworkRepository: IArtworkRepository,
    private val artistRepository: IArtistRepository
) : BaseViewModel(artworkRepository) {

    val cachedArtworks = mutableStateOf<List<Artwork>?>(emptyList())
    val favoriteArtworks: List<Artwork>?
        get() {
            val artworks = cachedArtworks.value?.filter { it.isFavorite }
            return if (artworks?.isEmpty() == true) null else artworks
        }
    var selectedArtwork = mutableStateOf<Artwork?>(null)
    var selectedArtworkLargeImage = mutableStateOf<Bitmap?>(null)

    private val _showPagingLoader = mutableStateOf(false)
    val showPagingLoader: State<Boolean> = _showPagingLoader

    init {
        fetchArtworks(true)
    }

    private fun fetchArtworks(init: Boolean) {
        viewModelScope.launch {
            val artworks = artworkRepository.fetchArtworks(init)
            if (init) {
                cachedArtworks.value = artworks
            } else {
                cachedArtworks.value?.let {
                    val combinedList = mutableListOf<Artwork>().apply {
                        addAll(it)
                        artworks?.let { newArtworks -> addAll(newArtworks) }
                    }
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
                userMessage.value = UserMessage.GeneralMessage(R.string.artwork_added_to_favorites)
            } else {
                artworkRepository.removeFromFavorites(artwork)
                artwork.isFavorite = false
                userMessage.value = UserMessage.GeneralMessage(R.string.artwork_removed_from_favorites)
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
                            userMessage.value = UserMessage.GeneralMessage(R.string.something_went_wrong)
                        }
                    }
                }
            }
        }
    }

    fun loadMoreArtworks() {
        _showPagingLoader.value = true
        fetchArtworks(false)
    }
}