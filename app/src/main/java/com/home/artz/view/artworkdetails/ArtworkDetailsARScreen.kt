package com.home.artz.view.artworkdetails

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.home.artz.R
import com.home.artz.view.ui.components.clickableWithoutRipple
import com.home.artz.view.ui.theme.Accent
import com.home.artz.view.ui.theme.Black50
import com.home.artz.view.ui.theme.White
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ImageNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener

@Composable
fun ARScreen(image: Bitmap, onBackClicked: () -> Unit) {
    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine = engine)
    val imageNode = ImageNode(bitmap = image, materialLoader = materialLoader)
    var frame by remember { mutableStateOf<Frame?>(null) }
    val childNodes = rememberNodes()
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    val showUserMessage = remember {
        mutableStateOf(true)
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        childNodes = childNodes,
        materialLoader = materialLoader,
        onSessionUpdated = { _, updatedFrame ->
            frame = updatedFrame
        },
        onTrackingFailureChanged = {
            trackingFailureReason = it
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { motionEvent, _ ->
                val hitResults = frame?.hitTest(motionEvent)
                val anchor = hitResults?.firstOrNull {
                    it.isValid(depthPoint = false, point = false)
                }?.createAnchorOrNull()

                if (anchor != null) {
                    imageNode.rotation = Rotation(x = 270F)
                    val anchorNode =
                        AnchorNode(engine = engine, anchor = anchor).also { anchorNode ->
                            anchorNode.addChildNode(imageNode)
                        }
                    childNodes.add(anchorNode)
                    showUserMessage.value = false
                }
            }
        )
    )

    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = paddingNormal,
                    top = paddingNormal,
                    end = paddingNormal
                )
                .statusBarsPadding()
        ) {
            Icon(
                tint = White,
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = stringResource(
                    id = R.string.icon_back_contentdesc
                ),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.details_icons_size))
                    .background(Black50, CircleShape)
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickableWithoutRipple {
                        onBackClicked.invoke()
                    }
            )
        }
        if (showUserMessage.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Black50),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.8F)
                        .padding(paddingNormal),
                    color = White,
                    text = trackingFailureReason?.getDescription(LocalContext.current) ?: stringResource(
                        id = R.string.ar_searching_for_surface
                    )
                )
                Text(
                    modifier = Modifier
                        .weight(0.2F)
                        .padding(paddingNormal)
                        .clickable { showUserMessage.value = false },
                    color = Accent,
                    text = stringResource(id = R.string.ar_hide_help)
                )
            }
        }
    }

    BackHandler {
        onBackClicked.invoke()
    }
}