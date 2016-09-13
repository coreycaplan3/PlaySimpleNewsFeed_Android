package com.github.coreycaplan3.thebuzz.services.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.coreycaplan3.thebuzz.model.BuzzAccount;
import com.github.coreycaplan3.thebuzz.model.NetworkResult;
import com.github.coreycaplan3.thebuzz.network.MessageHandler;
import com.github.coreycaplan3.thebuzz.network.UserHandler;
import com.github.coreycaplan3.thebuzz.receivers.PostRequestReceiver;
import com.github.coreycaplan3.thebuzz.services.HttpRequestService;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.github.coreycaplan3.thebuzz.services.post.PostRequestConstants.*;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class PostRequestService extends HttpRequestService {

    private static final String TAG = PostRequestService.class.getSimpleName();

    public PostRequestService() {
        super(TAG);
    }

    @Override
    protected Intent getIntentForBroadcastReceiver(ServiceResult serviceResult) {
        return PostRequestReceiver.createIntent(serviceResult);
    }

    @Override
    protected NetworkResult performNetworkOperation(int taskId, Intent intent) {
        BuzzAccount account = BuzzAccount.getCurrentAccount();
        if (account == null) {
            throw new IllegalStateException("Cannot perform post request with being logged in!");
        }
        String accountId = account.getAccountId();
        String idToken = account.getToken();

        long messageId;
        switch (taskId) {
            case POST_CREATE_MESSAGE:
                String title = intent.getStringExtra(KEY_ARGUMENT_ONE);
                String body = intent.getStringExtra(KEY_ARGUMENT_TWO);
                if (title == null || body == null) {
                    throw new IllegalArgumentException("title and body cannot be null!");
                }

                return new MessageHandler(accountId, idToken)
                        .createMessage(title, body);
            case POST_UP_VOTE_MESSAGE:
                messageId = intent.getLongExtra(KEY_ARGUMENT_ONE, -1);
                if (messageId == -1) {
                    throw new IllegalArgumentException("message ID cannot be -1!");
                }

                return new MessageHandler(accountId, idToken)
                        .upVoteMessage(messageId);
            case POST_DOWN_VOTE_MESSAGE:
                messageId = intent.getLongExtra(KEY_ARGUMENT_ONE, -1);
                if (messageId == -1) {
                    throw new IllegalArgumentException("message ID cannot be -1!");
                }

                return new MessageHandler(accountId, idToken)
                        .downVoteMessage(messageId);
            case POST_NEUTRAL_VOTE_MESSAGE:
                messageId = intent.getLongExtra(KEY_ARGUMENT_ONE, -1);
                if (messageId == -1) {
                    throw new IllegalArgumentException("message ID cannot be -1!");
                }

                return new MessageHandler(accountId, idToken)
                        .neutralVoteMessage(messageId);
            case POST_UPLOAD_PROFILE_PHOTO:
                Bitmap profilePhoto = intent.getParcelableExtra(KEY_ARGUMENT_ONE);
                if (profilePhoto == null) {
                    throw new IllegalArgumentException("PROFILE PHOTO cannot be null!");
                }

                return new UserHandler(accountId, idToken)
                        .setUserProfilePhoto(profilePhoto);
            default:
                throw new IllegalArgumentException("Invalid TASK ID, found: " + taskId);
        }
    }

    @NonNull
    @Override
    protected Bundle parseSuccessfulNetworkResult(int taskId, String json) {
        Bundle bundle = new Bundle();
        // TODO parsing
        switch (taskId) {
            case POST_CREATE_MESSAGE:
                break;
            case POST_UP_VOTE_MESSAGE:
                break;
            case POST_DOWN_VOTE_MESSAGE:
                break;
            case POST_NEUTRAL_VOTE_MESSAGE:
                break;
            case POST_UPLOAD_PROFILE_PHOTO:
                parseProfilePhoto(bundle, json);
                break;
            default:
                throw new IllegalArgumentException("Invalid TASK ID, found: " + taskId);
        }
        return bundle;
    }

    private static void parseProfilePhoto(Bundle bundle, String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            String profilePhotoUrl = object.getString("profile_photo_url");
            bundle.putString(ServiceResult.KEY_RESULT_ONE, profilePhotoUrl);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
