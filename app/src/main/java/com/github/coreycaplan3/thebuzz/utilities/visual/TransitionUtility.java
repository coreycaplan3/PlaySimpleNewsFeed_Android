package com.github.coreycaplan3.thebuzz.utilities.visual;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;

import com.github.coreycaplan3.thebuzz.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;

/**
 * Created by Corey on 6/2/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: A utility class used to ease the creation of Transitions between activities.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public final class TransitionUtility {

    private static final long TRANSITION_LENGTH = BuzzApplication.context().getResources().getInteger(R.integer.activity_transition_duration);

    private TransitionUtility() {
    }

    public static Slide createSlideTransition() {
        return createSlideTransition(0);
    }

    public static Slide createSlideTransition(long startDelay) {
        Slide slide = new Slide();
        slide.setDuration(TRANSITION_LENGTH);
        slide.excludeTarget(Toolbar.class, true);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.setStartDelay(startDelay);
        return slide;
    }

}
