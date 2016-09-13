package com.github.coreycaplan3.thebuzz.network;

import android.support.v4.util.Pair;

import com.github.coreycaplan3.thebuzz.model.Message;
import com.github.coreycaplan3.thebuzz.model.NetworkResult;

import java.util.ArrayList;

/**
 * Created by Corey Caplan on 9/11/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public class MessageHandler extends AbstractNetworkHandler {

    public MessageHandler(String accountId, String idToken) {
        super(accountId, idToken);
    }

    /**
     * Retrieves the news feed from the server.
     *
     * @return Retrieves the news feed from the network and returns a list in the form of
     * {@link Message} objects. The list will never be null, but may be empty.
     */
    public NetworkResult getMessages() {
        String serverMethod = "messages";

        if (accountId != null && idToken != null) {
            // Add the current account so we can get up-vote status for each message
            ArrayList<Pair<String, String>> pairs = new ArrayList<>();
            pairs.add(new Pair<>("account_id", accountId));
            pairs.add(new Pair<>("id_token", idToken));
            return sendGet(serverMethod, buildGetParameters(pairs));
        } else {
            return sendGet(serverMethod);
        }
    }

    /**
     * @param title  The title of the new post.
     * @param body   The body of the new post.
     * @return True if the POST was successful or false if it was not.
     */
    public NetworkResult createMessage(String title, String body) {
        String serverMethod = "createPost";

        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("title", title));
        pairs.add(new Pair<>("body", body));
        pairs.add(new Pair<>("account_id", accountId));
        pairs.add(new Pair<>("id_token", idToken));

        return sendPost(serverMethod, pairs);
    }

    /**
     * Increments the number of up-votes a message has.
     *
     * @param messageId The ID of the message.
     * @return True if the POST was successful or false if it was not.
     */
    public NetworkResult upVoteMessage(long messageId) {
        String serverMethod = "upVote";
        return voteMessage(serverMethod, messageId);
    }

    /**
     * Decrements the number of down-votes a message has.
     *
     * @param messageId The ID of the message.
     * @return True if the POST was successful or false if it was not.
     */
    public NetworkResult downVoteMessage(long messageId) {
        String serverMethod = "downVote";
        return voteMessage(serverMethod, messageId);
    }

    public NetworkResult neutralVoteMessage(long messageId) {
        String serverMethod = "neutralVote";
        return voteMessage(serverMethod, messageId);
    }

    private NetworkResult voteMessage(String serverMethod, long messageId) {
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("message_id", String.valueOf(messageId)));
        pairs.add(new Pair<>("accound_id", accountId));
        pairs.add(new Pair<>("id_token", idToken));

        return sendPost(serverMethod, pairs);
    }

}
