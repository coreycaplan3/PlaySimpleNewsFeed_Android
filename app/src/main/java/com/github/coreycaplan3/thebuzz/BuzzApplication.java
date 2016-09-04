package com.github.coreycaplan3.thebuzz;

import android.app.Application;
import android.content.Context;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class BuzzApplication extends Application {

    private static BuzzApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static synchronized BuzzApplication getInstance() {
        return sApplication;
    }

    public static synchronized Context context() {
        return sApplication.getApplicationContext();
    }

}
