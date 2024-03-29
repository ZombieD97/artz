package com.home.artz.view.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.view.ui.components.ArtworkList
import com.home.artz.view.ui.components.Loader
import com.home.artz.view.ui.theme.Tertiary

@Composable
fun DiscoverScreen(
    artworks: List<Artwork>?,
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
    ) {
        Icon(
            tint = Tertiary,
            modifier = Modifier.size(dimensionResource(id = R.dimen.empty_list_icon_size)),
            painter = painterResource(id = R.drawable.icon_empty_list),
            contentDescription = stringResource(
                id = R.string.empty_list_contentdesc
            )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = contentPadding.calculateBottomPadding() + dimensionResource(id = R.dimen.padding_normal)),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showLoader.value) Loader()
    }
}