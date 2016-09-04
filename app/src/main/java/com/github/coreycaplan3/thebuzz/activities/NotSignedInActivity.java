package com.github.coreycaplan3.thebuzz.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.custom.transitions.transitions.FabTransform;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment.OnGoogleSignInListener;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class NotSignedInActivity extends BaseActivity implements OnGoogleSignInListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_signed_in);
        setupTransition();
    }

    private void setupTransition() {
        if (!FabTransform.isSetup(this, findViewById(R.id.activity_container))) {
            Fade fadeIn = new Fade(Visibility.MODE_IN);
            Fade fadeOut = new Fade(Visibility.MODE_OUT);
            Slide slideIn = new Slide(Gravity.BOTTOM);
            Slide slideOut = new Slide();

            long transitionDuration = 500L;
            fadeIn.setDuration(transitionDuration);
            fadeOut.setDuration(transitionDuration);
            slideIn.setInterpolator(new OvershootInterpolator());
            slideIn.setDuration(transitionDuration);
            slideOut.setDuration(transitionDuration);
            slideOut.setInterpolator(new LinearOutSlowInInterpolator());

            TransitionSet enterSet = new TransitionSet();
            enterSet.excludeTarget(android.R.id.statusBarBackground, true);
            enterSet.addTransition(fadeIn);
            enterSet.addTransition(slideIn);
            enterSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

            TransitionSet exitSet = new TransitionSet();
            exitSet.excludeTarget(android.R.id.statusBarBackground, true);
            exitSet.addTransition(fadeOut);
            exitSet.addTransition(slideOut);
            exitSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

            getWindow().setEnterTransition(enterSet);
            getWindow().setExitTransition(exitSet);
        }
    }

    public void onOutsideSignInDialogClick(View view) {
        finishAfterTransition();
    }

    public void onGoogleSignInClick(View view) {
        GoogleSignInFragment fragment = getGoogleSignInFragment();
        if (fragment != null) {
            fragment.signIn();
        }
    }

    @Override
    public void onGoogleSignInSuccessful() {
        finishAfterTransition();
    }

}
