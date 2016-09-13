package com.github.coreycaplan3.thebuzz.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.model.Message;
import com.github.coreycaplan3.thebuzz.recycler.MessageViewHolder.OnProfileClickListener;
import com.github.coreycaplan3.thebuzz.recycler.MessageViewHolder.OnVoteClickListener;

import java.util.ArrayList;

/**
 * Created by Corey Caplan on 9/13/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class MessageRecyclerAdapter extends Adapter<MessageViewHolder> {

    /**
     * An interface used to transfer profile click events back to the caller.
     */
    public interface OnMessageProfileClickListener {

        /**
         * Called when the user who posted the message is clicked in the list.
         *
         * @param posterId The ID of the user whose message was clicked.
         * @param name     The name of the user whose message was clicked.
         */
        void onMessageProfileClick(String posterId, String name);
    }

    /**
     * An interface used to transfer vote click events to the implementor.
     */
    public interface OnMessageVoteClickListener {

        /**
         * Called when a message is up voted.
         *
         * @param messageId The ID of the message that was up voted.
         */
        void onMessageUpVoted(long messageId);

        /**
         * Called when a message is down voted.
         *
         * @param messageId The ID of the message that was down voted.
         */
        void onMessageDownVoted(long messageId);

        /**
         * Called when a message is neutral voted.
         *
         * @param messageId The ID of the message that was neutral voted.
         */
        void onMessageNeutralVoted(long messageId);
    }

    private OnMessageVoteClickListener mVoteClickListener;
    private OnMessageProfileClickListener mProfileClickListener;

    @NonNull
    private ArrayList<Message> mMessages = new ArrayList<>();

    /**
     * The constructor to invoke from the main news feed, since profile clicks are relevant.
     */
    public MessageRecyclerAdapter(OnMessageVoteClickListener voteClickListener,
                                  OnMessageProfileClickListener profileClickListener) {
        this();
        mVoteClickListener = voteClickListener;
        mProfileClickListener = profileClickListener;
    }

    /**
     * The constructor to invoke from the profile page, since profile clicks are irrelevant.
     */
    public MessageRecyclerAdapter(OnMessageVoteClickListener voteClickListener) {
        this();
        mVoteClickListener = voteClickListener;
    }

    private MessageRecyclerAdapter() {
        setHasStableIds(true);
    }

    public void setMessages(ArrayList<Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_message_item, parent, false);
        if(mProfileClickListener == null) {
            return new MessageViewHolder(view, mViewHolderVoteClickListener);
        } else {
            return new MessageViewHolder(view, mViewHolderVoteClickListener,
                    mViewHolderProfileClickListener);
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.onBind(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public long getItemId(int position) {
        return mMessages.get(position).getMessageId();
    }

    private OnVoteClickListener mViewHolderVoteClickListener = new OnVoteClickListener() {
        @Override
        public void onUpVoteClick(int index) {
            Message message = mMessages.get(index);
            switch (message.getVoteState()) {
                case Message.STATE_UP_VOTED:
                case Message.STATE_DOWN_VOTED:
                    mVoteClickListener.onMessageNeutralVoted(message.getMessageId());
                    break;
                case Message.STATE_NEUTRAL_VOTED:
                    mVoteClickListener.onMessageUpVoted(message.getMessageId());
                    break;
                default:
                    throw new RuntimeException("Invalid state: " + message.getVoteState());
            }
        }

        @Override
        public void onDownVoteClick(int index) {
            Message message = mMessages.get(index);
            switch (message.getVoteState()) {
                case Message.STATE_UP_VOTED:
                case Message.STATE_DOWN_VOTED:
                    mVoteClickListener.onMessageNeutralVoted(message.getMessageId());
                    break;
                case Message.STATE_NEUTRAL_VOTED:
                    mVoteClickListener.onMessageDownVoted(message.getMessageId());
                    break;
                default:
                    throw new RuntimeException("Invalid state: " + message.getVoteState());
            }
        }
    };

    private OnProfileClickListener mViewHolderProfileClickListener = new OnProfileClickListener() {
        @Override
        public void onProfileClick(int index) {
            Message m = mMessages.get(index);
            mProfileClickListener.onMessageProfileClick(m.getPosterId(), m.getPosterName());
        }
    };

}
