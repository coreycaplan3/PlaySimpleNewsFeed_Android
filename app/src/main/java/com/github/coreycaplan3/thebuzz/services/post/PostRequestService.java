package com.github.coreycaplan3.thebuzz.services.post;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class PostRequestService extends IntentService {

    private static final String TAG = PostRequestService.class.getSimpleName();

    public PostRequestService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
