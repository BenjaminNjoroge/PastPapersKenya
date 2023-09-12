package com.pastpaperskenya.papers.presentation;

import android.app.Application;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pastpaperskenya.papers.business.util.network.NetworkChangeListener;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;


/**
 * Created by MD Sahidul Islam on 11/7/2016.
 */
@HiltAndroidApp
public class BaseApplication extends Application {

    public static String appId= "pastpaperskenya-gmmjz";
    public static App app;
    public static NetworkChangeListener networkChangeListener;
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appId).build());

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

    }

    public static synchronized BaseApplication getInstance() {
        return baseApplication;
    }

    public void setNetworkChangedListener(NetworkChangeListener listener) {
        networkChangeListener = listener;
    }
}
