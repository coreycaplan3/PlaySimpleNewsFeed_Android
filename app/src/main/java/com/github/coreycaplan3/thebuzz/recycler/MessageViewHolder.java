package com.github.coreycaplan3.thebuzz.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.model.Message;

import java.text.NumberFormat;

/**
 * Created by Corey Caplan on 9/13/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */

class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @SuppressWarnings("deprecation")
    private final int TRANSPARENT_COLOR = BuzzApplication.getContext().getResources().getColor(android.R.color.transparent);
    @SuppressWarnings("deprecation")
    private final int RED_COLOR = BuzzApplication.getContext().getResources().getColor(android.R.color.holo_red_light);
    @SuppressWarnings("deprecation")
    private final int BLUE_COLOR = BuzzApplication.getContext().getResources().getColor(android.R.color.holo_blue_light);

    interface OnProfileClickListener {

        /**
         * @param index The index at which the profile was clicked
         */
        void onProfileClick(int index);
    }

    interface OnVoteClickListener {

        /**
         * @param index The index at which the up vote was clicked
         */
        void onUpVoteClick(int index);

        /**
         * @param index The index at which the down vote was clicked
         */
        void onDownVoteClick(int index);
    }

    private OnProfileClickListener mProfileClickListener;
    private OnVoteClickListener mVoteClickListener;

    private TextView mTitleTextView;
    private TextView mBodyTextView;
    private TextView mPointsTextView;
    private ImageButton mUpVoteButton;
    private ImageButton mDownVoteButton;
    private Button mProfileButton;

    /**
     * The constructor to invoke from the main news feed since profile clicks are relevant.
     */
    MessageViewHolder(View itemView, OnVoteClickListener voteClickListener,
                      OnProfileClickListener profileClickListener) {
        this(itemView);
        mProfileClickListener = profileClickListener;
        mVoteClickListener = voteClickListener;
    }

    /**
     * The constructor to invoke from the user's profile since profile clicks are irrelevant.
     */
    MessageViewHolder(View itemView, OnVoteClickListener voteClickListener) {
        this(itemView);
        mVoteClickListener = voteClickListener;
    }

    private MessageViewHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.recycler_message_title_text_view);
        mBodyTextView = (TextView) itemView.findViewById(R.id.recycler_message_body_text_view);
        mPointsTextView = (TextView) itemView.findViewById(R.id.recycler_message_points_text_view);
        mUpVoteButton = (ImageButton) itemView.findViewById(R.id.recycler_message_up_vote_button);
        mDownVoteButton = (ImageButton) itemView.findViewById(R.id.recycler_message_down_vote_button);
        mProfileButton = (Button) itemView.findViewById(R.id.recycler_message_profile_button);

        if(mProfileClickListener == null) {
            mProfileButton.setVisibility(View.GONE);
        } else {
            mProfileButton.setOnClickListener(this);
        }

        mUpVoteButton.setOnClickListener(this);
        mDownVoteButton.setOnClickListener(this);

    }

    void onBind(Message m) {
        mTitleTextView.setText(m.getTitle());
        mBodyTextView.setText(m.getBody());
        mPointsTextView.setText(NumberFormat.getInstance().format(m.getNumberOfUpVotes()));
        mProfileButton.setText(m.getPosterName());

        switch (m.getVoteState()) {
            case Message.STATE_UP_VOTED:
                mUpVoteButton.getBackground().setTint(RED_COLOR);
                mDownVoteButton.getBackground().setTint(TRANSPARENT_COLOR);
                break;
            case Message.STATE_DOWN_VOTED:
                mUpVoteButton.getBackground().setTint(TRANSPARENT_COLOR);
                mDownVoteButton.getBackground().setTint(BLUE_COLOR);
                break;
            case Message.STATE_NEUTRAL_VOTED:
                mUpVoteButton.getBackground().setTint(TRANSPARENT_COLOR);
                mDownVoteButton.getBackground().setTint(TRANSPARENT_COLOR);
                break;
            default:
                throw new RuntimeException("Invalid state found " + m.getVoteState());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recycler_message_profile_button:
                mProfileClickListener.onProfileClick(getAdapterPosition());
                break;
            case R.id.recycler_message_up_vote_button:
                mVoteClickListener.onUpVoteClick(getAdapterPosition());
                break;
            case R.id.recycler_message_down_vote_button:
                mVoteClickListener.onDownVoteClick(getAdapterPosition());
                break;
            default:
                throw new RuntimeException("Invalid VIEW!");
        }
    }
}