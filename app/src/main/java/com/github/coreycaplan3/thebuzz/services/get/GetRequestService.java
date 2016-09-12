package com.github.coreycaplan3.thebuzz.services.get;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.coreycaplan3.thebuzz.model.BuzzAccount;
import com.github.coreycaplan3.thebuzz.model.NetworkResult;
import com.github.coreycaplan3.thebuzz.network.MessageHandler;
import com.github.coreycaplan3.thebuzz.network.UserHandler;
import com.github.coreycaplan3.thebuzz.receivers.GetRequestReceiver;
import com.github.coreycaplan3.thebuzz.services.HttpRequestService;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;

import org.json.JSONArray;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class GetRequestService extends HttpRequestService {

    private static final String TAG = GetRequestService.class.getSimpleName();

    public GetRequestService() {
        super(TAG);
    }

    @Override
    protected Intent getIntentForBroadcastReceiver(ServiceResult serviceResult) {
        return GetRequestReceiver.createIntent(serviceResult);
    }

    @Override
    protected NetworkResult performNetworkOperation(int taskId, Intent intent) {
        BuzzAccount account = BuzzAccount.getCurrentAccount();
        String accountId = account == null ? null : account.getAccountId();
        String idToken = account == null ? null : account.getToken();

        switch (taskId) {
            case GetRequestConstants.GET_MESSAGES:
                return new MessageHandler(accountId, idToken)
                        .getMessages();
            case GetRequestConstants.GET_PROFILE:
                String otherUserId = intent.getStringExtra(KEY_ARGUMENT_ONE);
                if (otherUserId == null) {
                    throw new IllegalArgumentException("OTHER USER MUST NOT BE NULL!");
                }
                return new UserHandler(accountId, idToken)
                        .getUserInformation(otherUserId);
            default:
                throw new IllegalArgumentException("Invalid task, found: " + taskId);
        }
    }

    @NonNull
    @Override
    protected Bundle parseSuccessfulNetworkResult(int taskId, String json) {
        Bundle bundle = new Bundle();
        switch (taskId) {
            case GetRequestConstants.GET_MESSAGES:
                parseMessages(bundle, json);
                break;
            case GetRequestConstants.GET_PROFILE:
                parseProfile(bundle, json);
                break;
            default:
                throw new IllegalArgumentException("Invalid task, found: " + taskId);
        }
        return bundle;
    }

    private void parseMessages(Bundle bundle, String json) {

    }

    private void parseProfile(Bundle bundle, String json) {

    }

}
