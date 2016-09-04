package com.github.coreycaplan3.thebuzz.custom.transitions.transitions;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.thebuzz.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.utilities.visual.AnimationUtility;

/**
 * Created by Corey on 6/4/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: An extension to {@link ChangeBounds} that also morphs the views background
 * (color & corner radius).
 */
public class MorphTransform extends ChangeBounds {

    private static final String EXTRA_SHARED_ELEMENT_START_COLOR = "EXTRA_SHARED_ELEMENT_START_COLOR";
    private static final String EXTRA_SHARED_ELEMENT_START_CORNER_RADIUS = "EXTRA_SHARED_ELEMENT_START_CORNER_RADIUS";
    private static final long DEFAULT_DURATION = BuzzApplication.context().getResources().getInteger(R.integer.activity_transition_duration);

    private final int mStartColor;
    private final int mEndColor;
    private final int mStartCornerRadius;
    private final int mEndCornerRadius;
    private final boolean mIsReturnTransition;

    public MorphTransform(@ColorInt int startColor, @ColorInt int endColor,
                          int startCornerRadius, int endCornerRadius, boolean isReturnTransition) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        this.mStartCornerRadius = startCornerRadius;
        this.mEndCornerRadius = endCornerRadius;
        this.mIsReturnTransition = isReturnTransition;
        setDuration(DEFAULT_DURATION);
        setPathMotion(new GravityArcMotion());
    }

    /**
     * Configure {@code intent} with the extras needed to initialize this transition.
     */
    public static void addExtras(@NonNull Intent intent, @ColorInt int startColor,
                                 int startCornerRadius) {
        intent.putExtra(EXTRA_SHARED_ELEMENT_START_COLOR, startColor);
        intent.putExtra(EXTRA_SHARED_ELEMENT_START_CORNER_RADIUS, startCornerRadius);
    }

    /**
     * Configure {@link MorphTransform}s & set as {@code activity}'s shared element enter and return
     * transitions.
     *
     * @return True if the transition was setup properly or false if it wasn't.
     */
    public static boolean setup(@NonNull Activity activity, @Nullable View target,
                                @ColorInt int endColor, int endCornerRadius) {
        final Intent intent = activity.getIntent();
        if (intent == null ||
                !intent.hasExtra(EXTRA_SHARED_ELEMENT_START_COLOR) ||
                !intent.hasExtra(EXTRA_SHARED_ELEMENT_START_CORNER_RADIUS)) {
            return false;
        }

        final int startColor = activity.getIntent().
                getIntExtra(EXTRA_SHARED_ELEMENT_START_COLOR, Color.TRANSPARENT);
        final int startCornerRadius =
                intent.getIntExtra(EXTRA_SHARED_ELEMENT_START_CORNER_RADIUS, 0);

        final MorphTransform sharedEnter =
                new MorphTransform(startColor, endColor, startCornerRadius, endCornerRadius, false);
        // Reverse the start/end params for the return transition
        final MorphTransform sharedReturn =
                new MorphTransform(endColor, startColor, endCornerRadius, startCornerRadius, true);
        if (target != null) {
            sharedEnter.addTarget(target);
            sharedReturn.addTarget(target);
        }
        activity.getWindow().setSharedElementEnterTransition(sharedEnter);
        activity.getWindow().setSharedElementReturnTransition(sharedReturn);
        return true;
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues,
                                   TransitionValues endValues) {
        final Animator changeBounds = super.createAnimator(sceneRoot, startValues, endValues);
        if (changeBounds == null) return null;

        TimeInterpolator interpolator = getInterpolator();
        if (interpolator == null) {
            interpolator = AnimationUtility.getFastOutSlowInInterpolator(sceneRoot.getContext());
        }

        final MorphDrawable background = new MorphDrawable(mStartColor, mStartCornerRadius);
        endValues.view.setBackground(background);

        final Animator color = ObjectAnimator.ofArgb(background, MorphDrawable.COLOR, mEndColor);
        final Animator corners =
                ObjectAnimator.ofFloat(background, MorphDrawable.CORNER_RADIUS, mEndCornerRadius);

        // ease in the dialog's child views (fade in & staggered slide up)
        if (endValues.view instanceof ViewGroup && mIsReturnTransition) {
            final ViewGroup vg = (ViewGroup) endValues.view;
            final long duration = getDuration() / 2;
            float offset = vg.getHeight() / 3;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                v.setTranslationY(offset);
                v.setAlpha(0f);
                v.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(duration)
                        .setStartDelay(duration)
                        .setInterpolator(interpolator);
                offset *= 1.8f;
            }
        }

        final AnimatorSet transition = new AnimatorSet();
        transition.playTogether(changeBounds, corners, color);
        transition.setDuration(getDuration());
        transition.setInterpolator(interpolator);
        return transition;
    }

}