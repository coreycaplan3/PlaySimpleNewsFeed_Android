package com.github.coreycaplan3.thebuzz.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.coreycaplan3.thebuzz.BuzzApplication;

/**
 * Created by Corey on 5/7/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: To easily check different connection types such as GPS settings and  regular
 * device connectivity.
 */
public final class ConnectivityUtility {

    private ConnectivityUtility() {
    }

    /**
     * Checks if the given device is connected to any form of wireless network.
     *
     * @return True if it's connected to a network or false if it is not.
     */
    public static boolean hasConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BuzzApplication.context()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
