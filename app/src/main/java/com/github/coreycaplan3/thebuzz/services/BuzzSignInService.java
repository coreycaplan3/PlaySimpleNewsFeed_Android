package com.github.coreycaplan3.thebuzz.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.receivers.GoogleSignInReceiver;
import com.github.coreycaplan3.thebuzz.utilities.ConnectivityUtility;

import static com.github.coreycaplan3.thebuzz.services.ServiceResult.*;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class BuzzSignInService extends IntentService {

    private static final String TAG = BuzzSignInService.class.getSimpleName();

    public static final int SIGN_IN = 382;
    public static final int SILENT_SIGN_IN = 137;
    public static final int SIGN_OUT = 207;

    @IntDef({SIGN_IN, SILENT_SIGN_IN, SIGN_OUT})
    public @interface BuzzOperation {
    }

    private static final String KEY_OPERATION = "OPERATION";
    private static final String KEY_ID_TOKEN = "ID_TOKEN";

    /**
     * Creates the {@link Intent} used to start this {@link IntentService}.
     *
     * @param operation The operation that should be performed by this service.
     * @param idToken   The token used by the user after he/she signed in with Google.
     * @return An intent that can be used to start this service.
     */
    public static Intent createIntent(@BuzzOperation int operation, String idToken) {
        Intent intent = new Intent(BuzzApplication.context(), BuzzSignInService.class);
        intent.putExtra(KEY_OPERATION, operation);
        intent.putExtra(KEY_ID_TOKEN, idToken);
        return intent;
    }

    public BuzzSignInService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int operation = intent.getIntExtra(KEY_OPERATION, 0);
        String idToken = intent.getStringExtra(KEY_ID_TOKEN);
        if (operation == 0 || idToken == null) {
            throw new IllegalArgumentException("Invalid argument(s), use createIntent to " +
                    "properly pass data to this service!");
        }

        if(!ConnectivityUtility.hasConnection()) {
            ServiceResult serviceResult = new ServiceResult(null, RESULT_NO_CONNECTION, 1);
            Intent result = GoogleSignInReceiver.createIntent(serviceResult);
            LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            return;
        }

        ServiceResult serviceResult;
        switch (operation) {
            case SIGN_IN:
                serviceResult = signInWithBuzz(idToken);
                break;
            case SILENT_SIGN_IN:
                serviceResult = silentSignInWithBuzz(idToken);
                break;
            case SIGN_OUT:
                serviceResult = signOutWithBuzz(idToken);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation code, found : " + operation);
        }
        Intent result = GoogleSignInReceiver.createIntent(serviceResult);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    private ServiceResult signInWithBuzz(String idToken) {
        return null;
    }

    private ServiceResult silentSignInWithBuzz(String idToken) {
        return null;
    }

    private ServiceResult signOutWithBuzz(String idToken) {
        return null;
    }

}
