package com.github.coreycaplan3.thebuzz.model;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by Corey Caplan on 9/11/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */

public class NetworkResult {

    public static final int OPERATION_SUCCESSFUL = 1;
    public static final int OPERATION_NO_CONNECTION = 2;
    public static final int OPERATION_BAD_RESULT = 3;
    public static final int OPERATION_CANCELED = 4;
    public static final int OPERATION_SERVER_INVALID = 5;
    public static final int OPERATION_INVALID_URL= 6;

    @IntDef({OPERATION_SUCCESSFUL, OPERATION_NO_CONNECTION, OPERATION_BAD_RESULT,
            OPERATION_CANCELED, OPERATION_SERVER_INVALID, OPERATION_INVALID_URL})
    public @interface OperationCode{}

    @OperationCode
    private int mOperationCode;
    @Nullable
    private String mResponse;

    public NetworkResult(@OperationCode int operationCode) {
        mOperationCode = operationCode;
        mResponse = null;
    }

    public NetworkResult(@OperationCode int operationCode, @Nullable String response) {
        mOperationCode = operationCode;
        mResponse = response;
    }

    public int getOperationCode() {
        return mOperationCode;
    }

    @Nullable
    public String getResponse() {
        return mResponse;
    }
}
