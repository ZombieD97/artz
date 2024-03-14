package com.home.artz.model.datamodel

import androidx.annotation.StringRes

sealed class UserMessage {
    class ApiErrorMessage(val text: String): UserMessage()
    class GeneralMessage(@StringRes val resourceId: Int): UserMessage()
}