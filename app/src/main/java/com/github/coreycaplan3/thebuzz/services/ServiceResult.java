package com.github.coreycaplan3.thebuzz.services;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.github.coreycaplan3.thebuzz.services.get.GetRequestConstants;
import com.github.coreycaplan3.thebuzz.services.post.PostRequestConstants;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class ServiceResult implements Parcelable {

    public static final Creator<ServiceResult> CREATOR = new Creator<ServiceResult>() {
        @Override
        public ServiceResult createFromParcel(Parcel parcel) {
            return new ServiceResult(parcel);
        }

        @Override
        public ServiceResult[] newArray(int size) {
            return new ServiceResult[size];
        }
    };

    @IntDef({RESULT_SUCCESS, RESULT_NO_CONNECTION, RESULT_SERVER_ERROR})
    public @interface ServiceCodeType {
    }

    /**
     * The service executed the request successfully
     */
    public static final int RESULT_SUCCESS = 1;

    /**
     * An error that indicates there was no internet connection or a timeout occurred.
     */
    public static final int RESULT_NO_CONNECTION = 2;

    /**
     * An error that indicates there is a misconfiguration between the client and the server. This
     * is most likely because of mismatched JSON fields.
     */
    public static final int RESULT_SERVER_ERROR = 3;

    public static final String KEY_RESULT_ONE = "RESULT_ONE";

    @ServiceCodeType
    private final int mServiceCode;
    private final int mTaskId;
    @NonNull
    private final Bundle mBundle;

    public ServiceResult(@NonNull Bundle bundle, @ServiceCodeType int serviceCode, int taskId) {
        mBundle = bundle;
        mServiceCode = serviceCode;
        mTaskId = taskId;
    }

    /**
     * @return A bundle that contains the exact parameters that were sent, when the GET request or
     * POST request was made.
     */
    @NonNull
    public Bundle getBundle() {
        return mBundle;
    }

    /**
     * @return The result type of the request.
     */
    @ServiceCodeType
    public int getServiceCode() {
        return mServiceCode;
    }

    /**
     * @return The ID of the task that made the request.
     * @see GetRequestConstants
     * @see PostRequestConstants
     */
    public int getTaskId() {
        return mTaskId;
    }

    @SuppressWarnings("WrongConstant")
    private ServiceResult(Parcel in) {
        mBundle = (Bundle) in.readValue(Bundle.class.getClassLoader());
        mServiceCode = in.readInt();
        mTaskId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(mBundle);
        parcel.writeInt(mServiceCode);
        parcel.writeInt(mTaskId);
    }

}
