package com.pastpaperskenya.app.presentation;

import android.app.Application;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pastpaperskenya.app.business.util.network.NetworkChangeListener;

import dagger.hilt.android.HiltAndroidApp;


/**
 * Created by MD Sahidul Islam on 11/7/2016.
 */
@HiltAndroidApp
public class BaseApplication extends Application {

    public static NetworkChangeListener networkChangeListener;
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);

    }

    public static synchronized BaseApplication getInstance() {
        return baseApplication;
    }

    public void setNetworkChangedListener(NetworkChangeListener listener) {
        networkChangeListener = listener;
    }
}
