package com.home.artz.view.discover

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.home.artz.model.datamodel.Artwork
import com.home.artz.view.main.ArtworkList

@Composable
fun DiscoverScreen(
    artworks: List<Artwork>,
    contentPadding: PaddingValues,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit
) {
    ArtworkList(
        artworks = artworks,
        contentPadding = contentPadding,
        onArtworkClicked = onArtworkClicked,
        onFavoriteButtonClicked = onFavoriteButtonClicked
    )
}