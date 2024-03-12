package com.home.artz.view.artistdetails

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.home.artz.R
import com.home.artz.model.datamodel.ArtistUrl

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArtistDetailsWebViewScreen(
    moreInfo: ArtistUrl,
    artistName: String,
    onBackClicked: () -> Unit,
    onFinishedLoading: () -> Unit
) {
    var webView: WebView? = null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Header(artistName = artistName, onBackClicked = onBackClicked)
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            onFinishedLoading.invoke()
                        }
                    }
                    webView = this
                }
            },
            update = {
                webView = it
                it.loadUrl(moreInfo.url)
            }
        )

        BackHandler {
            if (webView?.canGoBack() == true) webView?.goBack() else onBackClicked.invoke()
        }
    }
}

@Composable
private fun Header(artistName: String, onBackClicked: () -> Unit) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    val paddingLarge = dimensionResource(id = R.dimen.padding_large)
    ConstraintLayout(
        Modifier.padding(
            start = paddingNormal,
            end = paddingNormal,
            top = paddingLarge,
            bottom = paddingLarge
        )
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val (backIconRef, artistNameRef) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.icon_back),
            tint = MaterialTheme.colorScheme.inverseSurface,
            contentDescription = stringResource(
                id = R.string.icon_back_contentdesc
            ),
            modifier = Modifier
                .constrainAs(backIconRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null // Remove ripple effect
                ) {
                    onBackClicked.invoke()
                }
        )
        Text(
            text = artistName,
            color = MaterialTheme.colorScheme.inverseSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(artistNameRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        )
    }
}