package com.github.coreycaplan3.thebuzz.services.post;

import android.support.annotation.IntDef;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class PostRequestConstants {

    public static final int POST_CREATE_MESSAGE = 1;
    public static final int POST_UP_VOTE_MESSAGE = 2;
    public static final int POST_DOWN_VOTE_MESSAGE = 3;
    public static final int POST_NEUTRAL_VOTE_MESSAGE = 4;
    public static final int POST_UPLOAD_PROFILE_PHOTO = 5;

    @IntDef({POST_CREATE_MESSAGE, POST_UP_VOTE_MESSAGE, POST_DOWN_VOTE_MESSAGE,
            POST_NEUTRAL_VOTE_MESSAGE, POST_UPLOAD_PROFILE_PHOTO})
    public @interface PostConstant{}

    private PostRequestConstants() {
        //no instance
    }

}
