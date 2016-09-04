package com.github.coreycaplan3.thebuzz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.github.coreycaplan3.thebuzz.services.ServiceResult;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class GetRequestReceiver extends BroadcastReceiver {

    /**
     * An interface used to transfer {@link #onReceive(Context, Intent)}} results to the
     * implementor.
     */
    public interface OnGetRequestCompleteListener {

        /**
         * Called when a Get Request has completed.
         *
         * @param serviceResult The {@link ServiceResult} that has the response from the operation.
         */
        void onGetRequestComplete(@NonNull ServiceResult serviceResult);
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

    private static final String FILTER_GET_REQUEST_RECEIVER = "filter_get_request";
    private static final String BUNDLE_SERVICE_RESULT = "SERVICE_RESULT";

    public static final IntentFilter INTENT_FILTER = new IntentFilter(FILTER_GET_REQUEST_RECEIVER);

    private OnGetRequestCompleteListener mListener;

    public GetRequestReceiver(@NonNull OnGetRequestCompleteListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceResult result = intent.getParcelableExtra(BUNDLE_SERVICE_RESULT);
        mListener.onGetRequestComplete(result);
    }

}
