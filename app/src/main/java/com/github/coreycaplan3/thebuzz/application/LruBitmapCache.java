package com.github.coreycaplan3.thebuzz.application;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Corey Caplan on 9/11/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class: To createIntent a cache that can store the user's profile pictures on the device.
 * This way, the picture doesn't have to be downloaded every time the user signs into his/her
 * profile.
 */
class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    LruBitmapCache() {
        super(getCacheSize());
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return value.getAllocationByteCount();
        } else {
            return value.getRowBytes() * value.getHeight();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    // Returns a cache size equal to approximately three screens worth of images.
    private static int getCacheSize() {
        DisplayMetrics displayMetrics = BuzzApplication.getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }
}
