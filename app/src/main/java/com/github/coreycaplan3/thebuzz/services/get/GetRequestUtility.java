package com.github.coreycaplan3.thebuzz.services.get;

import android.content.Intent;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;

import static com.github.coreycaplan3.thebuzz.services.HttpRequestService.KEY_ARGUMENT_ONE;
import static com.github.coreycaplan3.thebuzz.services.HttpRequestService.KEY_TASK_ID;
import static com.github.coreycaplan3.thebuzz.services.get.GetRequestConstants.*;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class GetRequestUtility {

    private GetRequestUtility() {
        //no instance
    }

    /**
     * Starts a task that retrieves the main news feed.
     */
    public static void startGetNewsFeedTask() {
        Intent intent = new Intent(BuzzApplication.context(), GetRequestService.class);
        startTask(GET_MESSAGES, intent);
    }

    /**
     * Starts a task that retrieves a user's profile information.
     *
     * @param otherUserId The ID of the user whose information should be retrieved.
     */
    public static void startGetProfileTask(String otherUserId) {
        Intent intent = new Intent(BuzzApplication.context(), GetRequestService.class);
        intent.putExtra(KEY_ARGUMENT_ONE, otherUserId);
        startTask(GET_PROFILE, intent);
    }

    private static void startTask(int taskId, Intent intent) {
        intent.putExtra(KEY_TASK_ID, taskId);
        BuzzApplication.getInstance().startService(intent);
    }

}
