package com.github.coreycaplan3.thebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class GeneralUser implements Parcelable {

    private String mUserId;
    private String mName;
    private String mProfilePhotoUrl;
    private long mUpVoteCount;
    @NonNull
    private ArrayList<Message> mMessageList;

    /**
     * @param userId          The account's ID
     * @param name            The account's name
     * @param profilePhotoUrl The URL that points to the user's profile photo
     * @param upVoteCount     The number of up votes that the user has
     * @param messageList     The list of {@link Message} objects that the user has posted.
     */
    public GeneralUser(String userId, String name, String profilePhotoUrl, long upVoteCount,
                       @NonNull ArrayList<Message> messageList) {
        mUserId = userId;
        mName = name;
        mProfilePhotoUrl = profilePhotoUrl;
        mUpVoteCount = upVoteCount;
        mMessageList = messageList;
    }

    private GeneralUser(Parcel dest) {
        mUserId = dest.readString();
        mName = dest.readString();
        mProfilePhotoUrl = dest.readString();
        mUpVoteCount = dest.readLong();
        mMessageList = new ArrayList<>();
        dest.readList(mMessageList, Message.class.getClassLoader());
    }

    /**
     * @return The user's ID
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * @return The account's name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return The URL that points to the user's profile photo
     */
    public String getProfilePhotoUrl() {
        return mProfilePhotoUrl;
    }

    /**
     * @return The number of up votes that the user has
     */
    public long getUpVoteCount() {
        return mUpVoteCount;
    }

    /**
     * @return The list of {@link Message} objects that the user has posted.
     */
    @NonNull
    public ArrayList<Message> getMessageList() {
        return mMessageList;
    }

    public static final Creator<GeneralUser> CREATOR = new Creator<GeneralUser>() {
        @Override
        public GeneralUser createFromParcel(Parcel in) {
            return new GeneralUser(in);
        }

        @Override
        public GeneralUser[] newArray(int size) {
            return new GeneralUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserId);
        dest.writeString(mName);
        dest.writeString(mProfilePhotoUrl);
        dest.writeLong(mUpVoteCount);
        dest.writeList(mMessageList);
    }
}
