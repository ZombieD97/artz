package com.home.artz.view.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ifNullOrBlank
import com.home.artz.view.main.Loader
import com.home.artz.view.ui.theme.Accent
import com.home.artz.view.ui.theme.Black50
import com.home.artz.view.ui.theme.White

@Composable
fun DetailsScreen(
    artwork: Artwork,
    largeImage: MutableState<ImageBitmap?>,
    onBackClicked: () -> Unit
) {
    val image = remember { largeImage }
    val showImageZoomScreen = remember {
        mutableStateOf(false)
    }
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    val paddingSmall = dimensionResource(id = R.dimen.padding_small)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image.value != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ConstraintLayout {
                    val (imageRef, imageDataRef) = createRefs()
                    Image(
                        bitmap = image.value!!,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(imageRef) {}
                    )

                    var artists = ""
                    artwork.artists?.forEachIndexed { index, artist ->
                        artists += artist.name
                        if (index != artwork.artists!!.lastIndex) artists += ", "
                    }
                    Column(
                        modifier = Modifier
                            .constrainAs(imageDataRef) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(
                                start = paddingSmall,
                                end = paddingSmall,
                                bottom = paddingSmall
                            )
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = artwork.title,
                            color = White,
                            fontSize = dimensionResource(id = R.dimen.artwork_title_size).value.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.italianno_regular,
                                    FontWeight.Normal
                                )
                            ),
                            modifier = Modifier
                                .background(Black50, RoundedCornerShape(paddingNormal))
                                .padding(start = paddingNormal, end = paddingNormal)
                        )
                        Spacer(modifier = Modifier.height(paddingSmall))
                        Text(
                            text = artists.ifBlank { stringResource(id = R.string.unknown_artist) },
                            color = White,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .background(Black50, RoundedCornerShape(paddingNormal))
                                .padding(start = paddingNormal, end = paddingNormal)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingSmall)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.collecting_institution_title))
                            }
                            append(
                                " ${
                                    artwork.collectingInstitution.ifNullOrBlank(stringResource(id = R.string.no_data))
                                }"
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.image_rights_title))
                            }
                            append(
                                " ${
                                    artwork.imageRights.ifNullOrBlank(stringResource(id = R.string.no_data))
                                }"
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.image_dimensions_title))
                            }
                            append(
                                " ${
                                    artwork.dimensions?.dimensions?.size.ifNullOrBlank(
                                        stringResource(id = R.string.no_data)
                                    )
                                }"
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.iconicity_title))
                            }
                            append(" ${artwork.iconicity}")
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.sale_status_title))
                            }
                            append(
                                " ${
                                    artwork.saleMessage.ifNullOrBlank(stringResource(id = R.string.no_data))
                                }"
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.date_title))
                            }
                            append(
                                " ${
                                    artwork.date.ifNullOrBlank(stringResource(id = R.string.no_data))
                                }"
                            )
                        }
                    )
                    Text(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(id = R.string.medium_title))
                            }
                            append(
                                " ${
                                    artwork.medium.ifNullOrBlank(stringResource(id = R.string.no_data))
                                }"
                            )
                        }
                    )
                }
            }
        } else {
            Loader()
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = paddingNormal,
                top = paddingNormal,
                end = paddingNormal
            )
            .statusBarsPadding()
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Icon(
            tint = White,
            painter = painterResource(id = R.drawable.icon_back),
            contentDescription = stringResource(
                id = R.string.icon_back_contentdesc
            ),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.details_icons_size))
                .background(Black50, CircleShape)
                .padding(paddingSmall)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null // Remove ripple effect
                ) {
                    onBackClicked.invoke()
                }
        )
        if (image.value != null) {
            Icon(
                tint = White,
                painter = painterResource(id = R.drawable.icon_zoom),
                contentDescription = stringResource(
                    id = R.string.zoom_icon_contentdesc
                ),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.details_icons_size))
                    .background(Black50, CircleShape)
                    .padding(paddingSmall)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null // Remove ripple effect
                    ) {
                        showImageZoomScreen.value = true
                    }
            )
        }
    }

    if (showImageZoomScreen.value) {
        image.value?.let { imageToZoom ->
            DetailsZoomScreen(image = imageToZoom) {
                showImageZoomScreen.value = false
            }
        }
    }

    BackHandler {
        if (showImageZoomScreen.value) showImageZoomScreen.value = false else onBackClicked.invoke()
    }
}