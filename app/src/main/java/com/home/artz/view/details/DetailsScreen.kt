package com.home.artz.view.details

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailsScreen(onClick: () -> Unit) {
    Text(text = "Back", modifier = Modifier.clickable { onClick.invoke() })
}