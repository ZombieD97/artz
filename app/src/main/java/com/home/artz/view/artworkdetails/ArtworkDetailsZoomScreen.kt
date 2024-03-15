package com.home.artz.view.artworkdetails

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.home.artz.R
import com.home.artz.view.ui.components.clickableWithoutRipple
import com.home.artz.view.ui.components.combinedClickableWithoutRipple
import com.home.artz.view.ui.theme.Black50
import com.home.artz.view.ui.theme.Black95
import com.home.artz.view.ui.theme.White

@Composable
fun ArtworkDetailsZoomScreen(image: ImageBitmap, onCloseClicked: () -> Unit) {
    var imageZoomScale by remember { mutableFloatStateOf(1f) }
    var imagePanOffset by remember { mutableStateOf(Offset.Zero) }
    val detailsIconsPadding = dimensionResource(id = R.dimen.padding_normal)
    val config = LocalConfiguration.current

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .combinedClickableWithoutRipple(
                onDoubleClick = {
                    // reset zoom
                    imageZoomScale = 1f
                    imagePanOffset = Offset.Zero
                }, onClick = {})
            .background(Black95)
    ) {
        val imageTransformState = rememberTransformableState { zoomChange, offsetChange, _ ->
            imageZoomScale = (imageZoomScale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (imageZoomScale - 1) * constraints.maxWidth
            val extraHeight = (imageZoomScale - 1) * constraints.maxHeight

            val maxOffsetX = extraWidth / 2
            val maxOffsetY = extraHeight / 2

            imagePanOffset = Offset(
                x = (imagePanOffset.x + imageZoomScale * offsetChange.x).coerceIn(
                    -maxOffsetX,
                    maxOffsetX
                ),
                y = (imagePanOffset.y + imageZoomScale * offsetChange.y).coerceIn(
                    -maxOffsetY,
                    maxOffsetY
                )
            )
        }

        val isOrientationPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT

        Image(
            bitmap = image,
            contentScale = if (isOrientationPortrait) ContentScale.FillWidth else ContentScale.FillHeight,
            contentDescription = null,
            modifier = Modifier
                .conditional(isOrientationPortrait, modifier = {
                    fillMaxWidth(0.8F)
                }, elseModifier = {
                    fillMaxHeight(0.8F)
                })
                .graphicsLayer(
                    scaleX = imageZoomScale,
                    scaleY = imageZoomScale,
                    translationX = imagePanOffset.x,
                    translationY = imagePanOffset.y
                )
                .transformable(state = imageTransformState)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = detailsIconsPadding,
                top = detailsIconsPadding
            )
            .statusBarsPadding()
    ) {
        Icon(
            tint = White,
            painter = painterResource(id = R.drawable.icon_close),
            contentDescription = stringResource(
                id = R.string.close_icon_contentdesc
            ),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.details_icons_size))
                .background(Black50, CircleShape)
                .padding(dimensionResource(id = R.dimen.padding_small))
                .clickableWithoutRipple {
                    onCloseClicked.invoke()
                }
        )
    }

    BackHandler {
        onCloseClicked.invoke()
    }
}

fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
    elseModifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        then(elseModifier(Modifier))
    }
}