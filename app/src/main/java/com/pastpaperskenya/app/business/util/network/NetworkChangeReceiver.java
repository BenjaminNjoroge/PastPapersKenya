package com.pastpaperskenya.app.business.util.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.pastpaperskenya.app.presentation.BaseApplication;


/**
 * Created by MD Sahidul Islam on 11/7/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static NetworkChangeListener networkChangeListener;

    public NetworkChangeReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (networkChangeListener != null) {
            networkChangeListener.onNetworkConnectionChanged(isNetworkConnected());
        }
    }

    public static boolean isNetworkConnected() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        isConnected = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        isConnected = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        isConnected = true;
                    }
                }
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isConnected = true;
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isConnected = true;
                    }
                }
            }
        }

        return isConnected;
    }
}
