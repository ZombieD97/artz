package com.home.artz.view.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork

@Composable
fun ArtworkList(
    artworks: List<Artwork>,
    contentPadding: PaddingValues,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit,
    onScrollEnded: (() -> Unit)? = null
) {
    if (artworks.isEmpty()) return
    val showFavoriteDialogForArtwork = rememberSaveable {
        mutableStateOf<Artwork?>(null)
    }
    val scrollState = rememberLazyStaggeredGridState()
    val configuration = LocalConfiguration.current
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
        contentPadding = contentPadding,
        state = scrollState,
        verticalItemSpacing = dimensionResource(id = R.dimen.padding_normal),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_normal)),
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_normal)),
        content = {
            items(artworks) { artwork ->
                artwork.imageLinks.artworkUrl?.let { artworkUrl ->
                    AsyncImage(
                        model = artworkUrl.href,
                        contentScale = ContentScale.Crop,
                        placeholder = ArtworkLoadingPlaceholder(configuration.screenWidthDp, configuration.screenHeightDp),
                        contentDescription = artwork.contentDescription,
                        modifier = Modifier
                            .clickable { onArtworkClicked(artwork) }
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_normal)))
                    )

                    val favoriteIcon: Int
                    val favoriteIconContentdesc: Int
                    if (!artwork.isFavorite) {
                        favoriteIcon = R.drawable.icon_favorite
                        favoriteIconContentdesc = R.string.favorite_icon_contentdesc
                    } else {
                        favoriteIcon = R.drawable.icon_favorite_filled
                        favoriteIconContentdesc = R.string.favorite_icon_filled_contentdesc
                    }
                    val favoriteButtonPadding = dimensionResource(id = R.dimen.padding_normal)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = favoriteButtonPadding, end = favoriteButtonPadding),
                        horizontalArrangement = Arrangement.End
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .size(dimensionResource(id = R.dimen.favorite_icon_size))
                                .background(colorResource(id = R.color.black_50), CircleShape)
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null // Remove ripple effect
                                ) {
                                    if (!artwork.isFavorite) {
                                        onFavoriteButtonClicked(artwork, true)
                                    } else {
                                        showFavoriteDialogForArtwork.value = artwork
                                    }
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = favoriteIcon),
                                contentDescription = stringResource(favoriteIconContentdesc)
                            )
                        }
                    }
                }
            }
        }
    )
    if (onScrollEnded != null) {
        val isScrollEnded = remember {
            mutableStateOf(false)
        }
        isScrollEnded.value = !scrollState.canScrollForward && scrollState.canScrollBackward && scrollState.firstVisibleItemIndex != 0
        if (isScrollEnded.value) onScrollEnded.invoke()
    }
    if (showFavoriteDialogForArtwork.value != null) {
        FavoriteRemoveConfirmDialog(showFavoriteDialogForArtwork) {
            onFavoriteButtonClicked(it, false)
        }
    }
}

@Composable
fun FavoriteRemoveConfirmDialog(
    showFavoriteDialogForArtwork: MutableState<Artwork?>,
    onConfirmClicked: (Artwork) -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.favorite_dialog_remove_title))
        },
        onDismissRequest = { showFavoriteDialogForArtwork.value = null },
        confirmButton = {
            TextButton(
                onClick = {
                    showFavoriteDialogForArtwork.value?.let { onConfirmClicked.invoke(it) }
                    showFavoriteDialogForArtwork.value = null
                }
            ) {
                Text(stringResource(id = R.string.favorite_dialog_confirm_button_title))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showFavoriteDialogForArtwork.value = null
                }
            ) {
                Text(stringResource(id = R.string.favorite_dialog_dismiss_button_title))
            }
        }
    )
}