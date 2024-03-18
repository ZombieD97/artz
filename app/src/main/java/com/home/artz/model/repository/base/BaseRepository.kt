package com.home.artz.model.repository.base

import com.home.artz.R
import com.home.artz.model.datamodel.ApiError
import com.home.artz.model.datamodel.UserMessage
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response
import java.net.UnknownHostException

abstract class BaseRepository(
    private val moshi: Moshi
) : IBaseRepository {

    private val _userErrorMessage: MutableSharedFlow<UserMessage?> = MutableSharedFlow()
    override val userErrorMessage: SharedFlow<UserMessage?> = _userErrorMessage

    override suspend fun <T> handleRequest(responseToGet: (suspend () -> Response<T>)): T? {
        return try {
            val response = responseToGet.invoke()
            if (response.isSuccessful) {
                response.body()
            } else {
                val responseErrorString = response.errorBody()?.string()
                val message = responseErrorString?.let {
                    moshi.adapter(ApiError::class.java).fromJson(
                        it
                    )?.message
                }
                _userErrorMessage.emit(if (message != null) {
                    UserMessage.ApiErrorMessage(message)
                } else {
                    UserMessage.GeneralMessage(R.string.something_went_wrong)
                })
                null
            }
        } catch (exp: UnknownHostException) {
            _userErrorMessage.emit(UserMessage.GeneralMessage(R.string.no_internet))
            null
        } catch (exp: Throwable) {
            _userErrorMessage.emit(UserMessage.GeneralMessage(R.string.something_went_wrong))
            null
        }
    }
}