package com.github.coreycaplan3.thebuzz.services.get;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class GetRequestService extends IntentService {

    private static final String TAG = GetRequestService.class.getSimpleName();

    public GetRequestService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
