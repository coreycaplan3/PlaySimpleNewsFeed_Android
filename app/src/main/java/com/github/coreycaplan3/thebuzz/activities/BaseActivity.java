package com.github.coreycaplan3.thebuzz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.fragments.FragmentTags;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment;
import com.github.coreycaplan3.thebuzz.receivers.GetRequestReceiver;
import com.github.coreycaplan3.thebuzz.receivers.GetRequestReceiver.OnGetRequestCompleteListener;
import com.github.coreycaplan3.thebuzz.receivers.PostRequestReceiver;
import com.github.coreycaplan3.thebuzz.receivers.PostRequestReceiver.OnPostRequestCompleteListener;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
abstract class BaseActivity extends AppCompatActivity implements OnGetRequestCompleteListener,
        OnPostRequestCompleteListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private AppBarLayout mAppBarLayout;

    private GetRequestReceiver mGetRequestReceiver;
    private PostRequestReceiver mPostRequestReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(GoogleSignInFragment.newInstance(), FragmentTags.TAG_GOOGLE_SIGN_IN)
                    .disallowAddToBackStack()
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupActionBar();

        mGetRequestReceiver = new GetRequestReceiver(this);
        mPostRequestReceiver = new PostRequestReceiver(this);

        // Register the receivers
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mGetRequestReceiver, GetRequestReceiver.INTENT_FILTER);
        manager.registerReceiver(mPostRequestReceiver, PostRequestReceiver.INTENT_FILTER);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !(this instanceof MainActivity)) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getGoogleSignInFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        fragment = getCurrentFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return this instanceof MainActivity;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    /**
     * @return The {@link GoogleSignInFragment} that is bound to this activity.
     */
    @Nullable
    protected final GoogleSignInFragment getGoogleSignInFragment() {
        return (GoogleSignInFragment) getSupportFragmentManager()
                .findFragmentByTag(FragmentTags.TAG_GOOGLE_SIGN_IN);
    }

    /**
     * @return The fragment that is currently in on-screen at the ID {@link R.id#activity_container}.
     */
    @Nullable
    protected final Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.activity_container);
    }

    /**
     * @param title The title that should be set on the {@link ActionBar}.
     */
    protected final void updateTitle(@StringRes int title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    /**
     * Disables scroll flags on the {@link AppBarLayout}.
     */
    protected final void disableScrollFlags() {
        if (mAppBarLayout != null) {
            for (int i = 0; i < mAppBarLayout.getChildCount(); i++) {
                View view = mAppBarLayout.getChildAt(i);
                ((AppBarLayout.LayoutParams) view.getLayoutParams()).setScrollFlags(0);
            }
        } else {
            Log.e(TAG, "disableScrollFlags: AppBarLayout was null!");
        }
    }

    /**
     * @param scrollFlags The scroll flags that should be enabled for all of the
     *                    {@link AppBarLayout}'s children. The scroll flags can be anything other
     *                    than {@link AppBarLayout.LayoutParams#SCROLL_FLAG_SCROLL} since that is
     *                    added by default.
     */
    protected final void enableScrollFlags(int scrollFlags) {
        if (mAppBarLayout != null) {
            for (int i = 0; i < mAppBarLayout.getChildCount(); i++) {
                View view = mAppBarLayout.getChildAt(i);
                ((AppBarLayout.LayoutParams) view.getLayoutParams())
                        .setScrollFlags(SCROLL_FLAG_SCROLL | scrollFlags);
            }
        } else {
            Log.e(TAG, "enableScrollFlags: AppBarLayout was null!");
        }
    }

    /**
     * @return The root view of this activity after doing a {@code findViewById} on
     * {@link R.id#activity_container}
     */
    protected final View getRootView() {
        return findViewById(R.id.activity_container);
    }

    @Override
    public void onGetRequestComplete(@NonNull ServiceResult serviceResult) {

    }

    @Override
    public void onPostRequestComplete(@NonNull ServiceResult serviceResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister the receivers
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);

        if (mGetRequestReceiver != null) {
            manager.unregisterReceiver(mGetRequestReceiver);
            mGetRequestReceiver = null;
        }

        if (mPostRequestReceiver != null) {
            manager.unregisterReceiver(mPostRequestReceiver);
            mPostRequestReceiver = null;
        }
    }
}
