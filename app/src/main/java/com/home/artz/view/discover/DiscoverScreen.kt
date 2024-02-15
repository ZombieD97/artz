package com.home.artz.view.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun DiscoverScreen(onClick: () -> Unit) {
    Row {
        Text(text = "Discover", modifier = Modifier.clickable { onClick.invoke() })
    }
}