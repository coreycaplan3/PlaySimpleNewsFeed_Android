package com.github.coreycaplan3.thebuzz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.github.coreycaplan3.thebuzz.services.ServiceResult;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class GoogleSignInReceiver extends BroadcastReceiver {

    /**
     * An interface used to transfer {@link #onReceive(Context, Intent)}} results to the
     * implementor. This interface is used to transfer the results of network calls that result in
     * the user signing in or out with The Buzz.
     */
    public interface OnGoogleSignInStatusChangeListener {

        /**
         * Called when the app finishes contacting The Buzz servers for the sign in/out process.
         *
         * @param serviceResult The {@link ServiceResult} that has the response from the operation.
         */
        void onGoogleSignInWithBuzzComplete(@NonNull ServiceResult serviceResult);
    }

    /**
     * Creates a new intent that can be broadcasted via the {@link LocalBroadcastManager}.
     *
     * @param serviceResult The {@link ServiceResult} object that wraps the result of the operation.
     * @return An {@link Intent} that can be broadcasted via the {@link LocalBroadcastManager}.
     */
    public static Intent createIntent(@NonNull ServiceResult serviceResult) {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_SERVICE_RESULT, serviceResult);
        return intent;
    }

    private static final String FILTER_GOOGLE_SIGN_IN_RECEIVER = "filter_google_sin_in";
    private static final String BUNDLE_SERVICE_RESULT = "SERVICE_RESULT";

    public static final IntentFilter INTENT_FILTER = new IntentFilter(FILTER_GOOGLE_SIGN_IN_RECEIVER);

    private OnGoogleSignInStatusChangeListener mListener;

    public GoogleSignInReceiver(@NonNull OnGoogleSignInStatusChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceResult result = intent.getParcelableExtra(BUNDLE_SERVICE_RESULT);
        mListener.onGoogleSignInWithBuzzComplete(result);
    }

}