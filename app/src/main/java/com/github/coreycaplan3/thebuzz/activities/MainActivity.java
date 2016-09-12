package com.github.coreycaplan3.thebuzz.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.custom.transitions.transitions.FabTransform;
import com.github.coreycaplan3.thebuzz.fragments.FragmentTags;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment.OnGoogleSignInListener;
import com.github.coreycaplan3.thebuzz.fragments.GoogleSignInFragment.OnGoogleSignOutListener;
import com.github.coreycaplan3.thebuzz.fragments.MessagesFragment;
import com.github.coreycaplan3.thebuzz.utilities.visual.UiUtility;

public class MainActivity extends BaseActivity implements OnGoogleSignInListener,
        OnGoogleSignOutListener {

    private boolean mIsSignedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment fragment = MessagesFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_container, fragment, FragmentTags.TAG_MESSAGES)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsSignedIn != GoogleSignInFragment.isSignedIn()) {
            mIsSignedIn = !mIsSignedIn;
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (mIsSignedIn) {
            getMenuInflater().inflate(R.menu.menu_signed_in, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_not_signed_in, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_sign_in:
                intent = new Intent(this, NotSignedInActivity.class);
                startActivity(intent, options.toBundle());
                return true;
            case R.id.menu_sign_out:
                signOut();
                return true;
            case R.id.menu_view_profile:
                intent = ProfileActivity.createIntent();
                startActivity(intent, options.toBundle());
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("deprecation")
    public void onCreateMessageFabClick(View view) {
        Intent intent;
        if (GoogleSignInFragment.isSignedIn()) {
            intent = new Intent(this, CreateMessageActivity.class);
        } else {
            intent = new Intent(this, NotSignedInActivity.class);
        }

        int fabColor = getResources().getColor(R.color.colorAccent);
        int fabIcon = R.drawable.ic_mode_edit_white_24dp;
        FabTransform.addExtras(intent, fabColor, fabIcon);

        String transitionName = getString(R.string.transition_shared_element);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName);

        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onStartSignInWithBuzzServers() {
        progressDialog.setMessage(getString(R.string.sign_in_with_buzz));
        progressDialog.show();
    }

    @Override
    public void onGoogleSignInSuccessful() {
        mIsSignedIn = true;
        supportInvalidateOptionsMenu();
    }

    private void signOut() {
        progressDialog.setMessage(getString(R.string.signing_out));
        progressDialog.show();
        getGoogleSignInFragment().signOut();
    }

    @Override
    public void onGoogleSignOutComplete(boolean isSuccessful) {
        progressDialog.dismiss();
        if (isSuccessful) {
            mIsSignedIn = false;
            invalidateOptionsMenu();
            UiUtility.snackbar(getRootView(), R.string.sign_out_successful, Snackbar.LENGTH_LONG);
        } else {
            UiUtility.snackbar(getRootView(), R.string.sign_out_unsuccessful, Snackbar.LENGTH_LONG,
                    R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOut();
                        }
                    });
        }
    }

}
