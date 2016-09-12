package com.github.coreycaplan3.thebuzz.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.github.coreycaplan3.thebuzz.model.NetworkResult;
import com.github.coreycaplan3.thebuzz.services.ServiceResult.ServiceCodeType;

import static com.github.coreycaplan3.thebuzz.model.NetworkResult.*;

/**
 * Created by Corey Caplan on 9/12/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */

public abstract class HttpRequestService extends IntentService {

    public static final String KEY_TASK_ID = "TASK_ID";
    public static final String KEY_ARGUMENT_ONE = "ARGUMENT_ONE";

    public HttpRequestService(String name) {
        super(name);
    }

    @Override
    protected final void onHandleIntent(Intent intent) {
        int taskId = intent.getIntExtra(KEY_TASK_ID, -1);
        if (taskId == -1) {
            throw new IllegalArgumentException("Invalid TASK_ID!");
        }

        Bundle resultBundle = new Bundle();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        NetworkResult networkResult = performNetworkOperation(taskId, intent);
        if (networkResult.getOperationCode() == OPERATION_SUCCESSFUL) {
            resultBundle = parseSuccessfulNetworkResult(taskId, networkResult.getResponse());
        }

        @ServiceCodeType int serviceCode = convertNetworkResultToServiceCode(networkResult);
        ServiceResult serviceResult = new ServiceResult(resultBundle, serviceCode, taskId);
        Intent resultIntent = getIntentForBroadcastReceiver(serviceResult);

        broadcastManager.sendBroadcast(resultIntent);
    }

    protected abstract Intent getIntentForBroadcastReceiver(ServiceResult serviceResult);

    protected abstract NetworkResult performNetworkOperation(int taskId, Intent intent);

    @NonNull
    protected abstract Bundle parseSuccessfulNetworkResult(int taskId, String json);

    @ServiceCodeType
    private static int convertNetworkResultToServiceCode(NetworkResult networkResult) {
        switch (networkResult.getOperationCode()) {
            case OPERATION_SUCCESSFUL:
                return ServiceResult.RESULT_SUCCESS;
            case OPERATION_NO_CONNECTION:
                return ServiceResult.RESULT_NO_CONNECTION;
            case OPERATION_BAD_RESULT:
            case OPERATION_CANCELED:
            case OPERATION_SERVER_INVALID:
            case OPERATION_INVALID_URL:
                return ServiceResult.RESULT_SERVER_ERROR;
            default:
                throw new IllegalArgumentException("Found: " + networkResult.getOperationCode());
        }
    }

}
