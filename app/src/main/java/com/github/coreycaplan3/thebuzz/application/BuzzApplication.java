package com.github.coreycaplan3.thebuzz.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.github.coreycaplan3.thebuzz.utilities.verification.FormValidation;

import java.io.File;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class BuzzApplication extends Application {

    private static final String TAG = BuzzApplication.class.getSimpleName();

    private static BuzzApplication sApplication;

    /**
     * 15 MB
     */
    private static final int DISK_CACHE_SIZE = 15 * 1024 * 1024;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

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

    /**
     * @return The application context.
     */
    public static synchronized Context getContext() {
        return sApplication.getApplicationContext();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            initRequestQueue();
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
        }
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(FormValidation.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Static Methods
    ///////////////////////////////////////////////////////////////////////////

    private static void initRequestQueue() {
        Context context = getContext();
        File cacheDir = new File(context.getCacheDir(), "volley");

        Network network = new BasicNetwork(new HurlStack());

        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DISK_CACHE_SIZE);
        getInstance().mRequestQueue = new RequestQueue(diskBasedCache, network);

        getInstance().mRequestQueue.start();
    }

}
