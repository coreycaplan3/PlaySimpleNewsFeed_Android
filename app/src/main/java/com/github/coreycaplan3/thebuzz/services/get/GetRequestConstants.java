package com.github.coreycaplan3.thebuzz.services.get;

import android.support.annotation.IntDef;

import com.github.coreycaplan3.thebuzz.services.ServiceResult;

import static com.github.coreycaplan3.thebuzz.services.ServiceResult.*;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public final class GetRequestConstants {

    public static final int GET_MESSAGES = 1;
    public static final int GET_PROFILE = 2;

    @IntDef({GET_MESSAGES, GET_PROFILE})
    public @interface GetConstant {}

    private GetRequestConstants() {
        //no instance
    }

}
