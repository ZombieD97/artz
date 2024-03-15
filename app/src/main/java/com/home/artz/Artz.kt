package com.home.artz

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Artz : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCenter.start(
            this, BuildConfig.APPSECRET,
            Analytics::class.java, Crashes::class.java
        )
    }
}