package com.home.artz.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ArtworkCreators
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
    val cachedFavoriteArtworks = mutableStateOf<List<Artwork>?>(emptyList())

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
                cachedFavoriteArtworks.value = artworkRepository.getFavoriteArtworks().ifEmpty { null }
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
        artwork.links.imageLinks?.let {
            viewModelScope.launch {
                if (isFavorite) {
                    artwork.isFavorite = true
                    fetchCreatorsIfNeeded(artwork)

                    val favoriteArtworks = cachedFavoriteArtworks.value?.toMutableList() ?: mutableListOf()
                    favoriteArtworks.add(artwork)

                    artworkRepository.saveToFavorites(artwork)
                    cachedFavoriteArtworks.value = favoriteArtworks
                    cachedArtworks.value?.firstOrNull { it.id == artwork.id }?.isFavorite = true

                    userMessage.value = UserMessage.GeneralMessage(R.string.artwork_added_to_favorites)
                } else {
                    artwork.isFavorite = false

                    val favoriteArtworks = cachedFavoriteArtworks.value?.toMutableList() ?: mutableListOf()
                    favoriteArtworks.removeIf { it.id == artwork.id }

                    artworkRepository.removeFromFavorites(artwork)
                    cachedFavoriteArtworks.value = favoriteArtworks.ifEmpty { null }
                    cachedArtworks.value?.firstOrNull { it.id == artwork.id }?.isFavorite = false

                    userMessage.value =
                        UserMessage.GeneralMessage(R.string.artwork_removed_from_favorites)
                }
            }
        }
    }

    fun setSelectedArtwork(artwork: Artwork) {
        artwork.links.imageLinks?.let {
            viewModelScope.launch {
                selectedArtworkLargeImage.value = null

                fetchCreatorsIfNeeded(artwork)
                selectedArtwork.value = artwork

                val imageUrl =
                    artwork.links.imageLinks.largeImageUrl ?: artwork.links.imageLinks.mediumImage
                imageUrl?.let {
                    withContext(Dispatchers.IO) {
                        try {
                            val url = URL(it)
                            selectedArtworkLargeImage.value =
                                BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        } catch (e: IOException) {
                            userMessage.value =
                                UserMessage.GeneralMessage(R.string.something_went_wrong)
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchCreatorsIfNeeded(artwork: Artwork) {
        if (artwork.creators.isNullOrEmpty()) {
            artwork.creators = artistRepository.getArtistsBy(artwork.id)?.map {
                ArtworkCreators(it.id, it.name)
            }
        }
    }

    fun loadMoreArtworks() {
        _showPagingLoader.value = true
        fetchArtworks(false)
    }
}