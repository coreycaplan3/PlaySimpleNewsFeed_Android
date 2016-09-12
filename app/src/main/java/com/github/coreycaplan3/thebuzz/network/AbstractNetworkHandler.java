package com.github.coreycaplan3.thebuzz.network;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.model.NetworkResult;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Corey Caplan on 9/11/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
abstract class AbstractNetworkHandler {

    private static final long TIMEOUT_SECONDS = 15;

    static final String BASE_URL = "https://keen-cse216.herokuapp.com/";

    @Nullable
    String accountId;
    @Nullable
    String idToken;

    AbstractNetworkHandler(@Nullable String accountId, @Nullable String idToken) {
        this.accountId = accountId;
        this.idToken = idToken;
    }

    /**
     * @param pairs The list of pairs that correspond to post keys and their values
     * @return A formatted String for sending form data to the server. IE arg1=x&arg2=y...
     * @throws IllegalArgumentException if the two arrays aren't the same size.
     */
    static String buildPostParameters(ArrayList<Pair<String, String>> pairs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pairs.size(); i++) {
            try {
                builder.append(pairs.get(i).first).append("=")
                        .append(URLEncoder.encode(pairs.get(i).second, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if (i < pairs.size() - 1) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    /**
     * @param pairs The list of pairs that correspond to post keys and their values
     * @return A formatted String for sending form data to the server. IE ?arg1=x&arg2=y...&argn=n
     * @throws IllegalArgumentException if the two arrays aren't the same size.
     */
    static String buildGetParameters(List<Pair<String, String>> pairs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pairs.size(); i++) {
            //Append the GET delimiter
            if (i == 0) {
                builder.append("?");
            } else {
                builder.append("&");
            }

            // Append key=ENCODE(value)
            try {
                builder.append(pairs.get(i).first).append("=")
                        .append(URLEncoder.encode(pairs.get(i).second, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();
    }

    /**
     * Like {@link #sendGet(String)} but appends parameters onto the constructed url. The parameters should be of the
     * form "?arg1=value1&arg2=value2...&argN=valueN"
     *
     * @param serverMethod The method on the server to which the GET should be sent. For example, if attempting to
     *                     retrieve messages, the route would be "messages".
     * @param parameters   The parameters to be appended
     * @return The raw response from the GET, which can be parsed to either a JSON object or JSON array. Can be null if
     * the connection fails.
     */
    NetworkResult sendGet(String serverMethod, String parameters) {
        return sendGet(serverMethod + parameters);
    }

    static NetworkResult sendGet(String url) {
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(Method.GET, url, future, future);
        RequestQueue queue = BuzzApplication.getInstance().getRequestQueue();
        queue.add(stringRequest);
        return parseNetworkResultFromRequest(future);
    }

    /**
     * @param serverMethod The method on the server to which the POST should be sent. For example,
     *                     if attempting to create a messages, the route would be "createPost".
     * @param pairs        The parameters that should be appended onto the URL. The parameters
     *                     should be of the form "fieldOneName=fieldOneValue&
     *                     fieldTwoName=fieldTwoValue...fieldXName&fieldXValue" without any spaces.
     * @return A {@link NetworkResult} object that contains the result of the operation.
     */
    static NetworkResult sendPost(String serverMethod, final ArrayList<Pair<String, String>> pairs) {
        String url = BASE_URL + serverMethod;
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(Method.POST, url, future, future) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < pairs.size(); i++) {
                    Pair<String, String> pair = pairs.get(i);
                    params.put(pair.first, pair.second);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        RequestQueue queue = BuzzApplication.getInstance().getRequestQueue();
        queue.add(stringRequest);
        return parseNetworkResultFromRequest(future);
    }

    /**
     * @param serverMethod    The method on the server to which the POST should be sent. For
     *                        example, if attempting to create a messages, the route would be
     *                        "createPost".
     * @param httpHeaderPairs A list of strings that has all of the HTTP request header
     *                        values. The required ones include <i>Content-Type</i>,
     *                        <i>Content-Length</i>, and <i>Authorization</i>.
     * @param bitmap          The bitmap to be uploaded to the server.
     * @return The HTTP response code that was returned after sending the POST request.
     * @throws IllegalArgumentException If the size of the header keys/values lists are not the same.
     */
    static NetworkResult sendPostWithFileUpload(String serverMethod,
                                                final List<Pair<String, String>> httpHeaderPairs,
                                                final Bitmap bitmap) {
        String url = BASE_URL + serverMethod;
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Method.POST, url, future, future) {

            @Override
            public byte[] getBody() {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < httpHeaderPairs.size(); i++) {
                    Pair<String, String> pair = httpHeaderPairs.get(i);
                    map.put(pair.first, pair.second);
                }
                return map;
            }

        };

        RequestQueue queue = BuzzApplication.getInstance().getRequestQueue();
        queue.add(request);
        return parseNetworkResultFromRequest(future);
    }

    /*
     * Private Methods
     */

    private static NetworkResult parseNetworkResultFromRequest(RequestFuture<String> future) {
        try {
            String result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return new NetworkResult(NetworkResult.OPERATION_SUCCESSFUL, result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (e instanceof TimeoutException) {
                return new NetworkResult(NetworkResult.OPERATION_NO_CONNECTION);
            } else if (e instanceof InterruptedException) {
                return new NetworkResult(NetworkResult.OPERATION_CANCELED);
            }
            Exception exception = (Exception) e.getCause();
            if (exception instanceof NoConnectionError || exception instanceof TimeoutError ||
                    exception instanceof NetworkError) {
                return new NetworkResult(NetworkResult.OPERATION_NO_CONNECTION);
            } else {
                return new NetworkResult(NetworkResult.OPERATION_SERVER_INVALID);
            }
        }
    }

}
