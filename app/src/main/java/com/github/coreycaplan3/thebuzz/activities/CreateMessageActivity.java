package com.github.coreycaplan3.thebuzz.activities;

import android.support.annotation.NonNull;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.fragments.CreateMessageFragment.OnStartCreateMessageListener;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;
import com.github.coreycaplan3.thebuzz.services.post.PostRequestService;
import com.github.coreycaplan3.thebuzz.services.post.PostRequestUtility;

/**
 * Created by Corey on 8/10/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class CreateMessageActivity extends BaseActivity implements OnStartCreateMessageListener {


    @Override
    public void onCreateMessage(String title, String body) {
        progressDialog.setMessage(getString(R.string.posting_message));
        progressDialog.show();
        PostRequestUtility.startCreateMessageTask(title, body);
    }

    @Override
    public void onPostRequestComplete(@NonNull ServiceResult serviceResult) {
        switch (serviceResult.getServiceCode()) {

        }
    }
}
