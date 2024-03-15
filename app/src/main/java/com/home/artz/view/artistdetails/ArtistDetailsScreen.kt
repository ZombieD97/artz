package com.home.artz.view.artistdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.home.artz.R
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.ArtistUrl
import com.home.artz.model.datamodel.ifNullOrBlank
import com.home.artz.view.ui.components.AsyncImagePlaceholder
import com.home.artz.view.ui.components.Loader
import com.home.artz.view.ui.components.clickableWithoutRipple
import com.home.artz.view.ui.theme.Black50
import com.home.artz.view.ui.theme.Black70
import com.home.artz.view.ui.theme.Blue
import com.home.artz.view.ui.theme.Italianno
import com.home.artz.view.ui.theme.White

@Composable
fun ArtistDetailsScreen(
    artist: Artist,
    onBackClicked: () -> Unit
) {
    val moreInfoUrl = remember {
        mutableStateOf<ArtistUrl?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        ArtistBaseInformation(artist = artist)
        ArtistDescription(artist = artist, moreInfoUrl = moreInfoUrl)
    }

    BackButton(onBackClicked)

    moreInfoUrl.value?.let {
        val showLoader = remember {
            mutableStateOf(true)
        }

        ArtistDetailsWebViewScreen(moreInfo = it, artist.name, onBackClicked = {
            moreInfoUrl.value = null
        }, onFinishedLoading = {
            showLoader.value = false
        })

        if (showLoader.value) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Loader()
            }
        }
    }

}

@Composable
private fun ArtistBaseInformation(artist: Artist) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    val paddingSmall = dimensionResource(id = R.dimen.padding_small)
    ConstraintLayout {
        val (imageRef, artistNameRef) = createRefs()
        val configuration = LocalConfiguration.current
        val imageSize = configuration.screenWidthDp.toFloat()

        AsyncImage(
            model = artist.links?.image?.squareImage,
            contentDescription = artist.name,
            contentScale = ContentScale.FillWidth,
            placeholder = AsyncImagePlaceholder(imageSize, imageSize),
            error = painterResource(id = R.drawable.icon_image_error_square),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Row(modifier = Modifier.constrainAs(artistNameRef) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            linkTo(
                start = parent.start,
                end = parent.end,
                bias = 0F,
                endMargin = paddingSmall,
                startMargin = paddingSmall
            )
            width = Dimension.fillToConstraints
            bottom.linkTo(imageRef.bottom, margin = paddingSmall)
        }) {
            Text(
                text = artist.name,
                color = White,
                fontSize = dimensionResource(id = R.dimen.artwork_title_size).value.sp,
                fontFamily = Italianno,
                modifier = Modifier
                    .background(Black70, RoundedCornerShape(paddingNormal))
                    .padding(start = paddingNormal, end = paddingNormal)
            )
        }
    }
}

@Composable
private fun ArtistDescription(artist: Artist, moreInfoUrl: MutableState<ArtistUrl?>) {
    val paddingSmall = dimensionResource(id = R.dimen.padding_small)

    Column(
        Modifier
            .padding(paddingSmall)
            .navigationBarsPadding()
    ) {
        Text(
            color = MaterialTheme.colorScheme.inverseSurface,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.date_title))
                }
                if (artist.birthday.isNullOrBlank() && artist.deathday.isNullOrBlank()) {
                    append(" ${stringResource(id = R.string.no_data)}")
                } else {
                    append(
                        if (artist.birthday.isNullOrBlank()) {
                            " ${stringResource(id = R.string.unknown)} - "
                        } else {
                            " ${artist.birthday} - "
                        }
                    )
                    if (!artist.deathday.isNullOrBlank()) append("${artist.deathday}")
                }
            }
        )

        Text(
            color = MaterialTheme.colorScheme.inverseSurface,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.gender_title))
                }
                append(" ${artist.gender.ifNullOrBlank(stringResource(id = R.string.no_data))}")
            }
        )

        Text(
            color = MaterialTheme.colorScheme.inverseSurface,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.hometown_title))
                }
                append(" ${artist.hometown.ifNullOrBlank(stringResource(id = R.string.no_data))}")
            }
        )

        Text(
            color = MaterialTheme.colorScheme.inverseSurface,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.nationality_title))
                }
                append(" ${artist.nationality.ifNullOrBlank(stringResource(id = R.string.no_data))}")
            }
        )

        Text(
            color = MaterialTheme.colorScheme.inverseSurface,
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.biography_title))
                }
                append(" ${artist.biography.ifNullOrBlank(stringResource(id = R.string.no_data))}")
            }
        )

        artist.links?.moreInfo?.let { url ->
            Text(
                color = Blue,
                textDecoration = TextDecoration.Underline,
                text = stringResource(id = R.string.more_info),
                modifier = Modifier
                    .padding(top = paddingSmall)
                    .clickable {
                        moreInfoUrl.value = url
                    }
            )
        }
    }
}

@Composable
private fun BackButton(onBackClicked: () -> Unit) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    val paddingSmall = dimensionResource(id = R.dimen.padding_small)
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                start = paddingNormal,
                top = paddingNormal,
                end = paddingNormal
            )
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
                .padding(paddingSmall)
                .clickableWithoutRipple {
                    onBackClicked.invoke()
                }
        )
    }
}