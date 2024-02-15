package com.home.artz.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.artz.data.model.ArtworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArtworkViewModel @Inject constructor(private val artworkRepository: ArtworkRepository): ViewModel() {
    fun getArtworks() {
        viewModelScope.launch {
            artworkRepository.getArtworks()
        }
    }
}