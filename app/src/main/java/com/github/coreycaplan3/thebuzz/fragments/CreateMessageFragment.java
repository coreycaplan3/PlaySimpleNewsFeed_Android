package com.github.coreycaplan3.thebuzz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.utilities.verification.FormValidation;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class CreateMessageFragment extends Fragment implements View.OnClickListener {

    /**
     * An interface used to transfer message creation to the activity.
     */
    public interface OnStartCreateMessageListener {

        /**
         * Called when a message is going to be created, so the activity's progress dialog can be
         * initialized and shown.
         *
         * @param title The title of the message.
         * @param body  The body of the message.
         */
        void onCreateMessage(String title, String body);
    }

    private static final int TITLE_MAX_LENGTH = BuzzApplication.context().getResources().getInteger(R.integer.message_title_max_count);
    private static final int BODY_MAX_LENGTH = BuzzApplication.context().getResources().getInteger(R.integer.message_body_max_count);
    private static final int MIN_LENGTH = 3;

    private TextInputLayout mTitleTextInputLayout;
    private TextInputLayout mBodyTextInputLayout;
    private EditText mTitleEditText;
    private EditText mBodyEditText;

    private OnStartCreateMessageListener mCreateMessageListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCreateMessageListener = (OnStartCreateMessageListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);
        mTitleTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.fragment_create_message_title_layout);
        mBodyTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.fragment_create_message_body_layout);
        mTitleEditText = (EditText) view.findViewById(R.id.fragment_create_message_title_edit_text);
        mBodyEditText = (EditText) view.findViewById(R.id.fragment_create_message_body_edit_text);

        view.findViewById(R.id.fragment_create_message_create_card).setOnClickListener(this);
        return view;
    }

    private TextWatcher mTitleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            FormValidation.validateText(mTitleTextInputLayout, s.toString(), MIN_LENGTH,
                    TITLE_MAX_LENGTH);
        }
    };

    private TextWatcher mBodyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            FormValidation.validateText(mBodyTextInputLayout, s.toString(), MIN_LENGTH,
                    BODY_MAX_LENGTH);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_create_message_create_card:
                boolean isValid = true;
                String titleText = mTitleEditText.getText().toString();
                String bodyText = mBodyEditText.getText().toString();

                if (!FormValidation.validateText(mTitleTextInputLayout, titleText, MIN_LENGTH,
                        TITLE_MAX_LENGTH)) {
                    isValid = false;
                }
                if (!FormValidation.validateText(mBodyTextInputLayout, bodyText, MIN_LENGTH,
                        BODY_MAX_LENGTH)) {
                    isValid = false;
                }

                if (!isValid) {
                    return;
                }

                mCreateMessageListener.onCreateMessage(titleText, bodyText);
                break;
            default:
                throw new IllegalArgumentException("Invalid view!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCreateMessageListener = null;
    }
}
