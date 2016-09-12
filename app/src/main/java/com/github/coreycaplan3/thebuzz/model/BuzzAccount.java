package com.github.coreycaplan3.thebuzz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Corey Caplan on 9/10/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class BuzzAccount implements Parcelable {

    private String mAccountId;
    private String mName;
    private String mToken;
    private String mProfilePhotoUrl;
    private long mUpVoteCount;

    @Nullable
    private static BuzzAccount sBuzzAccount;

    @Nullable
    public static BuzzAccount getCurrentAccount() {
        return sBuzzAccount;
    }

    /**
     * Logs the user into the Buzz and initializes the global singleton.
     *
     * @param accountId       The account's ID
     * @param name            The account's name
     * @param token           The account's ID token according to Google
     * @param profilePhotoUrl The URL that points to the user's profile photo
     * @param upVoteCount     The number of up votes that the user has
     */
    public static BuzzAccount login(String accountId, String name, String token,
                                    String profilePhotoUrl, long upVoteCount) {
        sBuzzAccount = new BuzzAccount(accountId, name, token, profilePhotoUrl, upVoteCount);
        return sBuzzAccount;
    }

    private BuzzAccount(String accountId, String name, String token, String profilePhotoUrl,
                        long upVoteCount) {
        mAccountId = accountId;
        mName = name;
        mToken = token;
        mProfilePhotoUrl = profilePhotoUrl;
        mUpVoteCount = upVoteCount;
    }

    private BuzzAccount(Parcel in) {
        mAccountId = in.readString();
        mName = in.readString();
        mToken = in.readString();
        mProfilePhotoUrl = in.readString();
        mUpVoteCount = in.readLong();
    }

    /**
     * @return The account's ID
     */
    public String getAccountId() {
        return mAccountId;
    }

    /**
     * @return The account's name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return The account's ID token according to Google
     */
    public String getToken() {
        return mToken;
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

    public static final Creator<BuzzAccount> CREATOR = new Creator<BuzzAccount>() {
        @Override
        public BuzzAccount createFromParcel(Parcel in) {
            return new BuzzAccount(in);
        }

        @Override
        public BuzzAccount[] newArray(int size) {
            return new BuzzAccount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAccountId);
        dest.writeString(mName);
        dest.writeString(mToken);
        dest.writeString(mProfilePhotoUrl);
        dest.writeLong(mUpVoteCount);
    }
}
