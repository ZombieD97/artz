package com.home.artz.view.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.view.ui.components.ArtworkList
import com.home.artz.view.ui.theme.Accent

@Composable
fun FavoriteScreen(
    artworks: List<Artwork>?,
    contentPadding: PaddingValues,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit
) {
    ArtworkList(
        artworks = artworks,
        contentPadding = contentPadding,
        onArtworkClicked = onArtworkClicked,
        onFavoriteButtonClicked = onFavoriteButtonClicked
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.empty_list_icon_size)),
            tint = Accent,
            painter = painterResource(id = R.drawable.icon_empty_favorites),
            contentDescription = stringResource(
                id = R.string.empty_list_contentdesc
            )
        )
    }
}