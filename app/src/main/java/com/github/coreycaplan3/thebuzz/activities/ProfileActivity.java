package com.github.coreycaplan3.thebuzz.activities;

import android.content.Intent;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.model.BuzzAccount;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class ProfileActivity extends BaseActivity {

    public static Intent createIntent() {
        if(BuzzAccount.getCurrentAccount() == null) {
            throw new IllegalStateException("Activity cannot be started if account is null!");
        }
        return new Intent(BuzzApplication.context(), ProfileActivity.class);
    }

}
