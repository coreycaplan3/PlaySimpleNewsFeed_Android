package com.github.coreycaplan3.thebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class Message implements Parcelable {

    /**
     * The message is currently up-voted
     */
    public static final int STATE_UP_VOTED = 1;

    /**
     * The message is currently down-voted
     */
    public static final int STATE_DOWN_VOTED = 2;

    /**
     * The message is neither up-voted nor down-voted
     */
    public static final int STATE_NEUTRAL_VOTED = 3;

    /**
     * An android-style enum that represents the possible states of a message with respect to the
     * current logged-in user
     */
    @IntDef({STATE_UP_VOTED, STATE_NEUTRAL_VOTED, STATE_DOWN_VOTED})
    public @interface VoteState {
    }

    private long mMessageId;
    private String mTitle;
    private String mBody;
    private int mNumberOfUpVotes;
    private String mPosterId;
    private String mPosterName;
    @VoteState
    private int mVoteState;

    /* Excluded from JSON serialization */
    private boolean mIsVoteInProgress;

    /**
     * @param messageId       The unique ID of the message.
     * @param title           The message's title.
     * @param body            The message's body.
     * @param numberOfUpVotes The number of up votes that the message has.
     * @param posterId        The ID of the user who posted the message.
     * @param posterName      The name of the user who posted the message.
     * @param voteState       The {@link VoteState} of the message.
     */
    public Message(long messageId, String title, String body, int numberOfUpVotes, String posterId,
                   String posterName, @VoteState int voteState) {
        mMessageId = messageId;
        mTitle = title;
        mBody = body;
        mNumberOfUpVotes = numberOfUpVotes;
        mPosterId = posterId;
        mPosterName = posterName;
        mVoteState = voteState;
    }

    @SuppressWarnings("WrongConstant")
    private Message(Parcel in) {
        mMessageId = in.readLong();
        mTitle = in.readString();
        mBody = in.readString();
        mNumberOfUpVotes = in.readInt();
        mVoteState = in.readInt();
        mPosterId = in.readString();
        mPosterName = in.readString();
        mIsVoteInProgress = in.readByte() != 0;
    }

    /**
     * @return The unique ID of the message.
     */
    public long getMessageId() {
        return mMessageId;
    }

    /**
     * @return The message's title.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return The message's body.
     */
    public String getBody() {
        return mBody;
    }

    /**
     * @return The ID of the user that created the message.
     */
    public String getPosterId() {
        return mPosterId;
    }

    /**
     * @return The name of the user that created the message.
     */
    public String getPosterName() {
        return mPosterName;
    }


    /**
     * @return The number of up votes that the message currently has.
     */
    public int getNumberOfUpVotes() {
        return mNumberOfUpVotes;
    }

    /**
     * Increments the number of up-votes for the given message.
     */
    public void incrementUpVotes() {
        mNumberOfUpVotes++;
    }

    /**
     * Decrements the number of up-votes for the given message.
     */
    public void decrementUpVotes() {
        mNumberOfUpVotes--;
    }

    /**
     * @return The {@link VoteState} of the message.
     */
    @VoteState
    public int getVoteState() {
        return mVoteState;
    }

    /**
     * @param voteState The new {@link VoteState} of this {@link Message}.
     */
    public void setVoteState(@VoteState int voteState) {
        mVoteState = voteState;
    }

    /**
     * @return True if the message is now being up-voted/down-voted/neutral-voted or false if it's not.
     */
    public boolean isVoteInProgress() {
        return mIsVoteInProgress;
    }

    /**
     * @param isInProgress True if the message is now being up-voted/down-voted/neutral-voted or false if it's not.
     */
    public void setIsInProgress(boolean isInProgress) {
        mIsVoteInProgress = isInProgress;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mMessageId);
        dest.writeString(mTitle);
        dest.writeString(mBody);
        dest.writeInt(mNumberOfUpVotes);
        dest.writeInt(mVoteState);
        dest.writeString(mPosterId);
        dest.writeString(mPosterName);
        dest.writeByte((byte) (mIsVoteInProgress ? 1 : 0));
    }

}
