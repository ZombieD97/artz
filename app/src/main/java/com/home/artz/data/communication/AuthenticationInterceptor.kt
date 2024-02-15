package com.home.artz.data.communication

import com.home.artz.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        request = request.newBuilder()
            .header("X-XAPP-Token", BuildConfig.XAPPTOKEN).build()
        return chain.proceed(request)
    }
}