package com.home.artz.view.discover

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.home.artz.R
import com.home.artz.data.model.Artwork

@Composable
fun DiscoverScreen(
    artworks: List<Artwork>,
    contentPadding: PaddingValues,
    onArtworkClicked: (Artwork) -> Unit,
    onFavoriteClicked: (Artwork, Boolean) -> Unit
) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalItemSpacing = dimensionResource(id = R.dimen.padding_normal),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_normal)),
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_normal)),
        content = {
            items(artworks) { artwork ->
                AsyncImage(
                    model = artwork.imageLinks.artworkUrl.href,
                    contentScale = ContentScale.Crop,
                    contentDescription = artwork.contentDescription,
                    modifier = Modifier
                        .clickable { onArtworkClicked(artwork) }
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_normal)))
                )

                val isFavorite = remember {
                    mutableStateOf(artwork.isFavorite)
                }
                val favoriteIcon: Int
                val favoriteIconContentdesc: Int
                if (!isFavorite.value) {
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
                                indication = null) {
                                isFavorite.value = !isFavorite.value
                                onFavoriteClicked(artwork, isFavorite.value)
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
    )
}