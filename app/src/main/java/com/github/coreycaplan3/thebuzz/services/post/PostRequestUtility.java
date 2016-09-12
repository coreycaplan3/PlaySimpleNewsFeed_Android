package com.github.coreycaplan3.thebuzz.services.post;

import android.content.Intent;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.model.Message;

import static com.github.coreycaplan3.thebuzz.services.post.PostRequestService.*;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class PostRequestUtility {

    private PostRequestUtility() {
        //no instance
    }

    /**
     * Starts the {@link PostRequestService} to create a {@link Message}.
     *
     * @param title The title of the message.
     * @param body  The body of the message.
     */
    public static void startCreateMessageTask(String title, String body) {
        Intent intent = new Intent(BuzzApplication.context(), PostRequestService.class);
        intent.putExtra(KEY_ARGUMENT_ONE, title);
        intent.putExtra(KEY_ARGUMENT_TWO, body);
    }

    private static void startTask(int taskId, Intent intent) {
        intent.putExtra(KEY_TASK_ID, taskId);
        BuzzApplication.getInstance().startService(intent);
    }

}
