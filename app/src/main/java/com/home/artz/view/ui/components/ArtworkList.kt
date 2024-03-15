package com.home.artz.view.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import coil.compose.AsyncImage
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.view.ui.theme.Accent
import com.home.artz.view.ui.theme.Black50

@Composable
fun ArtworkList(
    artworks: List<Artwork>?,
    contentPadding: PaddingValues,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit,
    onScrollEnded: (() -> Unit)? = null,
    emptyListContent: @Composable ColumnScope.() -> Unit
) {
    val padding = PaddingValues(
        top = dimensionResource(id = R.dimen.statusbar_padding),
        bottom = contentPadding.calculateBottomPadding() + dimensionResource(id = R.dimen.padding_normal),
        start = contentPadding.calculateStartPadding(LayoutDirection.Rtl),
        end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
    )
    when {
        artworks == null -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                emptyListContent.invoke(this)
            }
        }

        artworks.isEmpty() -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Loader()
            }
        }

        else -> {
            StaggeredArtworkGrid(
                artworks = artworks,
                contentPadding = padding,
                onArtworkClicked = onArtworkClicked,
                onFavoriteButtonClicked = onFavoriteButtonClicked,
                onScrollEnded = onScrollEnded
            )
        }
    }
}

@Composable
private fun StaggeredArtworkGrid(
    artworks: List<Artwork>, contentPadding: PaddingValues, onArtworkClicked: (Artwork) -> Unit,
    onFavoriteButtonClicked: (Artwork, Boolean) -> Unit,
    onScrollEnded: (() -> Unit)? = null
) {
    val showFavoriteDialogForArtwork = rememberSaveable {
        mutableStateOf<Artwork?>(null)
    }
    val scrollState = rememberLazyStaggeredGridState()
    val configuration = LocalConfiguration.current
    val defaultImageWidth = configuration.screenWidthDp * 0.5F
    val defaultImageHeight = configuration.screenHeightDp * 0.3F
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
        contentPadding = contentPadding,
        state = scrollState,
        verticalItemSpacing = dimensionResource(id = R.dimen.padding_normal),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_normal)),
        modifier = Modifier.fillMaxSize(),
        content = {
            items(artworks) { artwork ->
                artwork.imageLinks.artworkUrl?.mediumImage.let { artworkUrl ->
                    AsyncImage(
                        model = artworkUrl,
                        contentScale = ContentScale.Crop,
                        placeholder = AsyncImagePlaceholder(
                            defaultImageWidth,
                            defaultImageHeight
                        ),
                        error = painterResource(id = R.drawable.icon_image_error),
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .clickable {
                                onArtworkClicked(
                                    artwork
                                )
                            }
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_normal)))
                    )
                    val favoriteIcon: Int
                    val favoriteIconContentdesc: Int
                    if (!artwork.isFavorite) {
                        favoriteIcon = R.drawable.icon_favorite
                        favoriteIconContentdesc = R.string.favorite_icon_contentdesc
                    } else {
                        favoriteIcon = R.drawable.icon_favorite_filled
                        favoriteIconContentdesc =
                            R.string.favorite_icon_filled_contentdesc
                    }
                    val favoriteButtonPadding =
                        dimensionResource(id = R.dimen.padding_normal)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = favoriteButtonPadding,
                                end = favoriteButtonPadding
                            ),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            tint = Accent,
                            painter = painterResource(id = favoriteIcon),
                            contentDescription = stringResource(favoriteIconContentdesc),
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.favorite_icon_size))
                                .background(Black50, CircleShape)
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .clickableWithoutRipple {
                                    if (!artwork.isFavorite) {
                                        onFavoriteButtonClicked(artwork, true)
                                    } else {
                                        showFavoriteDialogForArtwork.value = artwork
                                    }
                                }
                        )
                    }
                }
            }
        }
    )
    if (onScrollEnded != null) {
        val previousCanForwardScroll = remember {
            mutableStateOf(true)
        }
        val isScrollEnded =
            scrollState.canScrollForward != previousCanForwardScroll.value && //Stop scrollState being changed multiple times, with the same value
                    !scrollState.canScrollForward && //Reached the end of the current page
                    scrollState.canScrollBackward && // so the user can scroll back, but not forward
                    scrollState.firstVisibleItemIndex != 0 //We are not at the initial state
        if (isScrollEnded) onScrollEnded.invoke()
        previousCanForwardScroll.value = scrollState.canScrollForward
    }
    if (showFavoriteDialogForArtwork.value != null) {
        FavoriteRemoveConfirmDialog(showFavoriteDialogForArtwork) {
            onFavoriteButtonClicked(it, false)
        }
    }
}

@Composable
private fun FavoriteRemoveConfirmDialog(
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