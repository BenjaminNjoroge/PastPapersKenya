package com.pastpaperskenya.app.presentation

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        val config: PRDownloaderConfig= PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()

        PRDownloader.initialize(applicationContext, config)
    }
}