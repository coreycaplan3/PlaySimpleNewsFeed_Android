package com.github.coreycaplan3.thebuzz.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.activities.ProfileActivity;
import com.github.coreycaplan3.thebuzz.model.GeneralUser;
import com.github.coreycaplan3.thebuzz.model.Message;
import com.github.coreycaplan3.thebuzz.recycler.MessageRecyclerAdapter;
import com.github.coreycaplan3.thebuzz.recycler.MessageRecyclerAdapter.OnMessageProfileClickListener;
import com.github.coreycaplan3.thebuzz.recycler.MessageRecyclerAdapter.OnMessageVoteClickListener;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;
import com.github.coreycaplan3.thebuzz.services.get.GetRequestUtility;
import com.github.coreycaplan3.thebuzz.services.post.PostRequestUtility;

import java.util.ArrayList;

/**
 * Created by Corey on 8/11/2016.
 * Project: TheBuzz
 * <p></p>
 * Purpose of Class:
 */
public class MessagesFragment extends BaseFragment implements OnMessageVoteClickListener,
        OnMessageProfileClickListener {

    public static final int TYPE_NEWS_FEED = 1;
    public static final int TYPE_PROFILE = 2;

    /**
     * The method to invoke when accessing the messages of another user's profile.
     *
     * @param otherUserId The ID of the user whose messages will be displayed in this fragment.
     * @return An instance of {@link MessagesFragment}.
     */
    public static MessagesFragment newInstance(String otherUserId) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE_TYPE, TYPE_PROFILE);
        bundle.putString(KEY_OTHER_USER_ID, otherUserId);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * The method to invoke when displaying messages in the news feed.
     *
     * @return An instance of {@link MessagesFragment}.
     */
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE_TYPE, TYPE_NEWS_FEED);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mMessageType;
    @Nullable
    private ArrayList<Message> mMessages;

    /**
     * The ID of the user whose messages are being retrieved
     */
    @Nullable
    private String mOtherUserId;
    @Nullable
    private GeneralUser mGeneralUser;

    private MessageRecyclerAdapter mAdapter;

    private static final String KEY_MESSAGE_TYPE = "MESSAGE_TYPE";
    private static final String KEY_MESSAGES = "MESSAGES";
    private static final String KEY_OTHER_USER_ID = "OTHER_USER_ID";
    private static final String KEY_GENERAL_USER = "GENERAL_USER";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMessages = savedInstanceState.getParcelableArrayList(KEY_MESSAGES);
            mMessageType = savedInstanceState.getInt(KEY_MESSAGE_TYPE);
            mOtherUserId = savedInstanceState.getString(KEY_OTHER_USER_ID);
            mGeneralUser = savedInstanceState.getParcelable(KEY_GENERAL_USER);
        } else {
            mMessageType = getArguments().getInt(KEY_MESSAGE_TYPE, -1);
            mOtherUserId = getArguments().getString(KEY_OTHER_USER_ID);
            if (mMessageType == -1) {
                throw new RuntimeException("Invalid message type, found " + mMessageType);
            }
        }
    }

    @NonNull
    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // Set up the recycler view adapter
        if (mMessageType == TYPE_NEWS_FEED) {
            mAdapter = new MessageRecyclerAdapter(this, this);
        } else if (mMessageType == TYPE_PROFILE) {
            mAdapter = new MessageRecyclerAdapter(this);
        } else {
            throw new RuntimeException("Invalid message type, found " + mMessageType);
        }
        if (mMessages != null) {
            mAdapter.setMessages(mMessages);
        }
        if (mGeneralUser != null) {
            mAdapter.setMessages(mGeneralUser.getMessageList());
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setItemAnimator(); TODO

        return view;
    }

    @Override
    public boolean containsDataSet() {
        return mMessages != null && mMessages.size() > 0;
    }

    @Override
    public void refreshData() {
        switch (mMessageType) {
            case TYPE_NEWS_FEED:
                GetRequestUtility.startGetNewsFeedTask();
                break;
            case TYPE_PROFILE:
                GetRequestUtility.startGetProfileTask(mOtherUserId);
                break;
            default:
                throw new RuntimeException("Invalid message type, found: " + mMessageType);
        }
    }

    @Override
    public void onMessageProfileClick(String posterId, String name) {
        startActivity(ProfileActivity.createIntent(posterId, name));
    }

    @Override
    public void onMessageUpVoted(long messageId) {
        PostRequestUtility.startUpVoteMessageTask(messageId);
    }

    @Override
    public void onMessageDownVoted(long messageId) {
        PostRequestUtility.startDownVoteMessageTask(messageId);
    }

    @Override
    public void onMessageNeutralVoted(long messageId) {
        PostRequestUtility.startNeutralVoteMessageTask(messageId);
    }

    @Override
    public void onGetRequestComplete(@NonNull ServiceResult serviceResult) {
        super.onGetRequestComplete(serviceResult);
        //TODO
    }

    @Override
    public void onPostRequestComplete(@NonNull ServiceResult serviceResult) {
        super.onPostRequestComplete(serviceResult);
        //TODO
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MESSAGE_TYPE, mMessageType);
        outState.putParcelableArrayList(KEY_MESSAGES, mMessages);
        outState.putString(KEY_OTHER_USER_ID, mOtherUserId);
        outState.putParcelable(KEY_GENERAL_USER, mGeneralUser);
    }
}
