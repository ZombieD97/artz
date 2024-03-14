package com.home.artz.model.repository.base

import com.home.artz.model.datamodel.UserMessage
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

interface IBaseRepository {

    val userErrorMessage: SharedFlow<UserMessage?>
    suspend fun <T> handleRequest(responseToGet: (suspend () -> Response<T>)): T?
}