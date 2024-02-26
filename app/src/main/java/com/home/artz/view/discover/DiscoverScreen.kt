package com.home.artz.view.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.view.main.ArtworkList

@Composable
fun DiscoverScreen(
    artworks: List<Artwork>,
    contentPadding: PaddingValues,
    showPagingLoader: State<Boolean>,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit,
    onScrollEnded: () -> Unit
) {
    val showLoader = remember {
        showPagingLoader
    }
    ArtworkList(
        artworks = artworks,
        contentPadding = contentPadding,
        onArtworkClicked = onArtworkClicked,
        onFavoriteButtonClicked = onFavoriteButtonClicked,
        onScrollEnded = onScrollEnded
    )
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = contentPadding.calculateBottomPadding() + dimensionResource(id = R.dimen.padding_normal)),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showLoader.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.progress_bar_size))
                    .background(Color.White, CircleShape)
                    .padding(dimensionResource(id = R.dimen.padding_normal)),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}