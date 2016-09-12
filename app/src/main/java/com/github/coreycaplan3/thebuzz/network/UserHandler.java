package com.github.coreycaplan3.thebuzz.network;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.github.coreycaplan3.thebuzz.model.GeneralUser;
import com.github.coreycaplan3.thebuzz.model.NetworkResult;

import java.util.ArrayList;

/**
 * Created by Corey Caplan on 9/11/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class UserHandler extends AbstractNetworkHandler {

    /**
     * @param user The user whose profile photo should be retrieved
     * @return The user's profile photo or null if the operation fails.
     */
    public static String getUserProfilePhotoUrl(GeneralUser user) {
        String serverMethod = "profilePhoto";
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("profile_photo_url", user.getProfilePhotoUrl()));

        return BASE_URL + serverMethod + buildGetParameters(pairs);
    }

    /**
     * @param accountId The ID of the account that is currently logged in or null if there is no
     *                  one logged in now.
     * @param idToken   The id_token of the account that is currently logged in or null if there is
     *                  no one logged in now.
     */
    public UserHandler(@Nullable String accountId, @Nullable String idToken) {
        super(accountId, idToken);
    }

    /**
     * @return A {@link NetworkResult} object that contains the result of the operation and whether
     * or not it was successful.
     */
    public NetworkResult loginWithGoogle() {
        String serverMethod = "loginWithGoogle";
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("id_token", idToken));

        //Send the request
        return sendPost(serverMethod, pairs);
    }

    /**
     * @param userId The ID of the user whose information should be found.
     * @return A {@link NetworkResult} object that contains the result of the operation and whether
     * or not it was successful.
     */
    public NetworkResult getUserInformation(String userId) {
        String serverMethod = "user/" + userId;

        if (accountId == null || idToken == null) {
            // The user is logged in, lets add his account information to the query
            ArrayList<Pair<String, String>> pairs = new ArrayList<>();
            pairs.add(new Pair<>("account_id", accountId));
            pairs.add(new Pair<>("id_token", idToken));

            return sendGet(serverMethod, buildGetParameters(pairs));
        } else {
            return sendGet(serverMethod);
        }
    }

    /**
     * @param profilePhoto A {@link Bitmap} object that points to the user's newly selected profile
     *                     photo.
     * @return A {@link NetworkResult} object that wraps the result of the operation.
     */
    public NetworkResult setUserProfilePhoto(Bitmap profilePhoto) {
        ArrayList<Pair<String, String>> headerPairs = new ArrayList<>();
        headerPairs.add(new Pair<>("Content-Type", "image/jpeg"));
        headerPairs.add(new Pair<>("Content-Length", String.valueOf(profilePhoto.getByteCount())));
        headerPairs.add(new Pair<>("Authorization", idToken));

        // Add the Authorization header
        return sendPostWithFileUpload("updateProfilePhoto", headerPairs, profilePhoto);
    }

}
