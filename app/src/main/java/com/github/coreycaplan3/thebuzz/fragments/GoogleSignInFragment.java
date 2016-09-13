package com.github.coreycaplan3.thebuzz.fragments;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.services.BuzzSignInService;
import com.github.coreycaplan3.thebuzz.utilities.visual.UiUtility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.github.coreycaplan3.thebuzz.constants.BuzzConstants.*;
import static com.github.coreycaplan3.thebuzz.services.BuzzSignInService.SIGN_IN;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.*;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class GoogleSignInFragment extends Fragment implements ConnectionCallbacks,
        OnConnectionFailedListener {

    public interface OnGoogleSignInListener {

        /**
         * Called when contact has been made with Google's servers and the sign in process has
         * migrated over to signing in with the Buzz.
         */
        void onStartSignInWithBuzzServers();

        /**
         * Called when the sign in process is completely finished.
         */
        void onGoogleSignInSuccessful();
    }

    public interface OnGoogleSignOutListener {

        void onGoogleSignOutComplete(boolean isSuccessful);
    }

    private static final String TAG = GoogleSignInFragment.class.getSimpleName();

    private static boolean sIsFirstTimeConnecting = true;
    private static GoogleApiClient sGoogleApiClient;
    private static GoogleSignInAccount sGoogleSignInAccount;

    private OnGoogleSignInListener mSignInListener;
    private OnGoogleSignOutListener mSignOutListener;

    public static GoogleSignInFragment newInstance() {
        return new GoogleSignInFragment();
    }

    /**
     * @return True if the user is currently signed in or false if the user is not.
     */
    public static boolean isSignedIn() {
        return sGoogleSignInAccount != null;
    }

    /**
     * @return The user's currently signed in account or null if the user isn't logged in.
     */
    @Nullable
    public static GoogleSignInAccount getGoogleSignInAccount() {
        return sGoogleSignInAccount;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
        try {
            mSignInListener = (OnGoogleSignInListener) context;
        } catch (ClassCastException ignored) {
            Log.d(TAG, "onAttach: Failed to cast OnGoogleSignInListener to context");
        }
        try {
            mSignOutListener = (OnGoogleSignOutListener) context;
        } catch (ClassCastException ignored) {
            Log.d(TAG, "onAttach: Failed to cast OnGoogleSignOutListener to context");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sGoogleApiClient == null) {
            // This is the first time the fragment has been instantiated, so let's build the client
            // and attempt to sign in silently once we're connected
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                    .requestEmail()
                    .requestProfile()
                    .requestId()
                    .requestServerAuthCode(getString(R.string.server_client_id))
                    .build();
            sGoogleApiClient = new GoogleApiClient.Builder(getContext(), this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sGoogleApiClient != null) {
            sGoogleApiClient.connect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_GOOGLE_SIGN_IN:
            case SIGN_IN_REQUIRED:
                handleSignInResult(data);
                break;
            case RC_RESOLVE_GOOGLE_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    sGoogleApiClient.connect();
                } else {
                    UiUtility.toast(getActivity(), R.string.error_cannot_connect_google_play);
                    Log.e(TAG, "onActivityResult: Cannot connect to GooglePlayServices!");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (sIsFirstTimeConnecting) {
            Log.d(TAG, "onConnected: Successfully connected to Google Play Services!");
            sIsFirstTimeConnecting = false;
            signInSilently();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), RC_RESOLVE_GOOGLE_CONNECTION);
                Log.e(TAG, "onConnectionFailed: Connection to Google Play Services failed, but " +
                        "attempting to solve problem now!");
            } catch (SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            UiUtility.toast(getActivity(), R.string.error_cannot_connect_google_play);
            Log.e(TAG, "onConnectionFailed: Cannot resolve problem and connect to " +
                    "GooglePlayServices");
        }
    }

    private void signInSilently() {
        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi
                .silentSignIn(sGoogleApiClient);
        if (pendingResult.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            sGoogleSignInAccount = pendingResult.get().getSignInAccount();
            if (mSignInListener != null) {
                mSignInListener.onGoogleSignInSuccessful();
            }
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    if (!googleSignInResult.isSuccess()) {
                        Log.d(TAG, "Google: Cached sign-in not available!");
                    } else {
                        Log.d(TAG, "Google: Received cached sign-in!");
                        sGoogleSignInAccount = googleSignInResult.getSignInAccount();
                        if (mSignInListener != null) {
                            mSignInListener.onGoogleSignInSuccessful();
                        }
                    }
                }
            });
        }
    }

    /**
     * Attempts to sign the user into his/her Google account
     */
    public void signIn() {
        if (!sGoogleApiClient.isConnected()) {
            sGoogleApiClient.connect();
            UiUtility.toast(getContext(), R.string.google_sign_in_failed_retry);
        } else {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(sGoogleApiClient);
            getActivity().startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
        }
    }

    /**
     * Attempts to sign the user out of his/her Google account
     */
    public void signOut() {
        if (!sGoogleApiClient.isConnected()) {
            UiUtility.toast(getContext(), R.string.google_sign_out_failed_retry);
            sGoogleApiClient.connect();
        } else {
            PendingResult<Status> result = Auth.GoogleSignInApi.signOut(sGoogleApiClient);
            result.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mSignOutListener.onGoogleSignOutComplete(status.isSuccess());
                }
            });
        }
    }

    /**
     * Handles the sign in result if it succeeded or fails. If it fails, we check the status codes
     * given to us at <a href="https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes.html">
     * Google's Website</a>.
     *
     * @param data The data passed back from {@link #onActivityResult(int, int, Intent)}
     */
    private void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            sGoogleSignInAccount = result.getSignInAccount();
            if (sGoogleSignInAccount != null) {
                String idToken = sGoogleSignInAccount.getIdToken();
                mSignInListener.onStartSignInWithBuzzServers();
                Intent intent = BuzzSignInService.createIntent(SIGN_IN, idToken);
                getActivity().startService(intent);
            } else {
                Log.e(TAG, "handleSignInResult: The Google Sign In account was null!");
            }
        } else {
            Status signInStatus = result.getStatus();
            switch (result.getStatus().getStatusCode()) {
                case TIMEOUT:
                case NETWORK_ERROR:
                    UiUtility.toast(getActivity(), R.string.google_sign_in_failed_no_connection);
                    Log.e(TAG, "handleSignInResult: Sign in failed due to network connectivity!");
                    break;
                case SIGN_IN_CANCELLED:
                    // Do nothing
                    Log.e(TAG, "handleSignInResult: Sign in canceled!");
                    break;
                case SIGN_IN_FAILED:
                    // Do nothing, we can't recover
                    Log.e(TAG, "handleSignInResult: Sign in failed and cannot recover!");
                    break;
                case SIGN_IN_REQUIRED:
                    if (signInStatus.hasResolution()) {
                        try {
                            signInStatus.startResolutionForResult(getActivity(), SIGN_IN_REQUIRED);
                            Log.e(TAG, "handleSignInResult: Sign in failed and is attempting to " +
                                    "recover!");
                        } catch (SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        UiUtility.toast(getActivity(), R.string.google_sign_in_failed);
                        Log.e(TAG, "handleSignInResult: Sign in failed and cannot recover!");
                    }
                    break;
                case INTERNAL_ERROR:
                case INVALID_ACCOUNT:
                    UiUtility.toast(getActivity(), R.string.google_sign_in_failed_retry);
                    Log.e(TAG, "handleSignInResult: Sign in failed, but retrying should fix the " +
                            "problem!");
                    break;
                default:
                    UiUtility.toast(getActivity(), R.string.google_sign_in_failed_unknown);
                    Log.e(TAG, "handleSignInResult: Sign in failed and cannot recover!");
                    break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sGoogleApiClient != null) {
            sGoogleApiClient.disconnect();
        }
    }
}
