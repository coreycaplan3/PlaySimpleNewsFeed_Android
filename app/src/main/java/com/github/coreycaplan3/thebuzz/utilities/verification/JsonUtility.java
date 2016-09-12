package com.github.coreycaplan3.thebuzz.utilities.verification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Corey on 5/19/2016.
 * <p></p>
 * A utility class used for parsing JSON!
 */
@SuppressWarnings("unused")
public final class JsonUtility {

    @SuppressWarnings("unused")
    private static final String TAG = JsonUtility.class.getSimpleName();

    private JsonUtility() {
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The String being requested or <b>null</b> if it cannot be parsed or was not found.
     */
    @Nullable
    public static String getString(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return null;
        }
        if (jsonObject.isNull(field)) {
            return null;
        }
        try {
            return jsonObject.getString(field);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The double being requested or <b>-1</b> if it cannot be parsed or was not found.
     */
    public static double getDouble(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return -1;
        }
        if (jsonObject.isNull(field)) {
            return -1;
        }
        try {
            return jsonObject.getDouble(field);
        } catch (JSONException e) {
            return -1;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return 1 if the result is true, 0 if it's false, or <b>-1</b> if the {@link JSONObject}
     * cannot be parsed or was not found.
     */
    public static int getBoolean(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return -1;
        }
        if (jsonObject.isNull(field)) {
            return -1;
        }
        try {
            return jsonObject.getBoolean(field) ? 1 : 0;
        } catch (JSONException e) {
            Log.e(TAG, "getBoolean: ", e);
            return -1;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The int being requested or <b>-1</b> if it cannot be parsed or was not found.
     */
    public static int getInt(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return -1;
        }
        if (jsonObject.isNull(field)) {
            return -1;
        }
        try {
            return jsonObject.getInt(field);
        } catch (JSONException e) {
            return -1;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The long being requested or <b>-1</b> if it cannot be parsed or was not found.
     */
    public static long getLong(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return -1;
        }
        if (jsonObject.isNull(field)) {
            return -1;
        }
        try {
            return jsonObject.getLong(field);
        } catch (JSONException e) {
            return -1;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The {@link JSONObject} being requested or <b>null</b> if it cannot be parsed or was
     * not found.
     */
    @Nullable
    public static JSONObject getJsonObject(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return null;
        }
        if (jsonObject.isNull(field)) {
            return null;
        }
        try {
            return jsonObject.getJSONObject(field);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * @param jsonObject The {@link JSONObject} that needs to be parsed.
     * @param field      The field that should be retrieved from this {@link JSONObject}.
     * @return The {@link JSONArray} being requested or <b>null</b> if it cannot be parsed or was
     * not found.
     */
    @Nullable
    public static JSONArray getJsonArray(@NonNull JSONObject jsonObject, String field) {
        if (!jsonObject.has(field)) {
            return null;
        }
        if (jsonObject.isNull(field)) {
            return null;
        }
        try {
            return jsonObject.getJSONArray(field);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * @param jsonArray The {@link JSONArray} that needs to be parsed.
     * @param position  The position that should be retrieved from this {@link JSONArray}.
     * @return The object being requested or <b>null</b> if it cannot be parsed or was
     * not found.
     */
    @Nullable
    public static Object getFromJsonArray(@NonNull JSONArray jsonArray, int position) {
        try {
            return jsonArray.get(position);
        } catch (JSONException e) {
            return null;
        }
    }

}
