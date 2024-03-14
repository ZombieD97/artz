package com.home.artz.view.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.home.artz.model.datamodel.UserMessage


@Composable
fun UserMessage(message: UserMessage) {
    val context = LocalContext.current
    val messageText = when (message) {
        is UserMessage.ApiErrorMessage -> message.text
        is UserMessage.GeneralMessage -> stringResource(id = message.resourceId)
    }
    Toast.makeText(context, messageText, Toast.LENGTH_LONG).show()
}