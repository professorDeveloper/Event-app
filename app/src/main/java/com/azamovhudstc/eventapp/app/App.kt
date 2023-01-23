package com.azamovhudstc.eventapp.app

import android.app.Application
import com.azamovhudstc.eventapp.data.local.shared.LocalData
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalData.getInstance(this)
    }
}