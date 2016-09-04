package com.github.coreycaplan3.thebuzz.utilities.visual;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.github.coreycaplan3.thebuzz.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;

import java.util.ArrayList;

/**
 * Created by Corey on 6/4/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: A utility class used to make working with animations more efficient.
 */
public final class AnimationUtility {

    private AnimationUtility() {
    }

    private static final long ANIMATION_DURATION_SHORT = BuzzApplication.context().getResources().getInteger(R.integer.activity_transition_duration);

    private static Interpolator fastOutSlowIn;
    private static Interpolator fastOutLinearIn;
    private static Interpolator linearOutSlowIn;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }

    public static Interpolator getFastOutLinearInInterpolator(Context context) {
        if (fastOutLinearIn == null) {
            fastOutLinearIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_linear_in);
        }
        return fastOutLinearIn;
    }

    public static Interpolator getLinearOutSlowInInterpolator(Context context) {
        if (linearOutSlowIn == null) {
            linearOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.linear_out_slow_in);
        }
        return linearOutSlowIn;
    }

    /**
     * @param view The view on which the circular REVEAL will be created.
     * @return An animator that can be used for revealing the given view.
     */
    public static Animator getCircularReveal(final View view) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });

        animator.setDuration(ANIMATION_DURATION_SHORT);

        return animator;
    }

    /**
     * @param view The view on which the circular HIDE reveal will be created.
     * @return An animator that can be used for hiding the given view.
     */
    public static Animator getCircularHide(final View view) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        animator.setDuration(ANIMATION_DURATION_SHORT);

        return animator;
    }

    /**
     * An implementation of {@link Property} to be used specifically with fields of
     * type
     * {@code float}. This type-specific subclass enables performance benefit by allowing
     * calls to a {@link #set(Object, Float) set()} function that takes the primitive
     * {@code float} type and avoids autoboxing and other overhead associated with the
     * {@code Float} class.
     *
     * @param <T> The class on which the Property is declared.
     **/
    public static abstract class FloatProperty<T> extends Property<T, Float> {
        public FloatProperty(String name) {
            super(Float.class, name);
        }

        /**
         * A type-specific override of the {@link #set(Object, Float)} that is faster when dealing
         * with fields of type {@code float}.
         */
        public abstract void setValue(T object, float value);

        @Override
        final public void set(T object, Float value) {
            setValue(object, value);
        }
    }

    /**
     * An implementation of {@link Property} to be used specifically with fields of
     * type
     * {@code int}. This type-specific subclass enables performance benefit by allowing
     * calls to a {@link #set(Object, Integer) set()} function that takes the primitive
     * {@code int} type and avoids autoboxing and other overhead associated with the
     * {@code Integer} class.
     *
     * @param <T> The class on which the Property is declared.
     */
    public static abstract class IntProperty<T> extends Property<T, Integer> {

        public IntProperty(String name) {
            super(Integer.class, name);
        }

        /**
         * A type-specific override of the {@link #set(Object, Integer)} that is faster when dealing
         * with fields of type {@code int}.
         */
        public abstract void setValue(T object, int value);

        @Override
        final public void set(T object, Integer value) {
            setValue(object, value);
        }

    }

    /**
     * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
     * <p>
     * Interrupting Activity transitions can yield an OperationNotSupportedException when the
     * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
     */
    public static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners = new ArrayMap<>();

        public NoPauseAnimator(Animator animator) {
            mAnimator = animator;
        }

        @Override
        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper);
                mAnimator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }

        @Override
        public void end() {
            mAnimator.end();
        }

        @Override
        public long getDuration() {
            return mAnimator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return mAnimator.getInterpolator();
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            mAnimator.setInterpolator(timeInterpolator);
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<>(mListeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return mAnimator.getStartDelay();
        }

        @Override
        public void setStartDelay(long delayMS) {
            mAnimator.setStartDelay(delayMS);
        }

        @Override
        public boolean isPaused() {
            return mAnimator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return mAnimator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return mAnimator.isStarted();
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener listener);

        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override
        public void removeAllListeners() {
            mListeners.clear();
            mAnimator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = mListeners.get(listener);
            if (wrapper != null) {
                mListeners.remove(listener);
                mAnimator.removeListener(wrapper);
            }
        }

        @Override
        public Animator setDuration(long durationMS) {
            mAnimator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setTarget(Object target) {
            mAnimator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            mAnimator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            mAnimator.setupStartValues();
        }

        @Override
        public void start() {
            mAnimator.start();
        }
    }

    static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }

}
