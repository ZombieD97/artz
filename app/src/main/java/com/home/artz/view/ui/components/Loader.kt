package com.home.artz.view.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.home.artz.R

@Composable
fun Loader() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.progress_bar_size))
            .background(MaterialTheme.colorScheme.background, CircleShape)
            .padding(dimensionResource(id = R.dimen.padding_normal)),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}