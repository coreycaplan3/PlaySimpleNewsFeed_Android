package com.github.coreycaplan3.thebuzz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.model.BuzzAccount;
import com.github.coreycaplan3.thebuzz.model.GeneralUser;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class ProfileActivity extends BaseActivity {

    private String mUserId;
    private String mName;
    @Nullable
    private GeneralUser mGeneralUser;

    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_GENERAL_USER = "GENERAL_USER";

    /**
     * Creates an intent that can be used to start the {@link ProfileActivity}.
     *
     * @param userId The ID of the user whose profile will be displayed in this activity.
     * @param name   The name of the user whose profile will be displayed in this activity.
     * @return An intent that can be used to start the {@link ProfileActivity}.
     */
    public static Intent createIntent(String userId, String name) {
        Intent intent = new Intent(BuzzApplication.context(), ProfileActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        intent.putExtra(KEY_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUserId = savedInstanceState.getString(KEY_USER_ID);
            mName = savedInstanceState.getString(KEY_NAME);
            mGeneralUser = savedInstanceState.getParcelable(KEY_GENERAL_USER);
        } else {
            mUserId = getIntent().getStringExtra(KEY_USER_ID);
            mName = getIntent().getStringExtra(KEY_NAME);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_USER_ID, mUserId);
        outState.putString(KEY_NAME, mName);
        outState.putParcelable(KEY_GENERAL_USER, mGeneralUser);
    }
}
