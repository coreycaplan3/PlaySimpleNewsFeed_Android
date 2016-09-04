package com.github.coreycaplan3.thebuzz.utilities.visual;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.coreycaplan3.thebuzz.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;

/**
 * Created by Corey on 10/5/2015.
 * Project: MeetUp
 * Purpose of Class: To simplify and speed up the code necessary to do tedious stuff to the UI. Some
 * of the methods here are to help prevent the developer from calling essential methods in certain
 * situations, which could lead to bugs. For example, forgetting to call <b>show</b> on a
 * {@link Toast} or {@link Snackbar} can be problematic.
 */
@SuppressWarnings("unused")
final public class UiUtility {

    private static int sScreenHeight = -1;
    private static int sScreenWidth = -1;

    private UiUtility() {
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void toast(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void snackbar(View root, @StringRes int message, int duration) {
        Snackbar.make(root, message, duration).show();
    }

    public static void snackbar(View root, @StringRes int message, int duration,
                                @StringRes int actionMessage, OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(root, message, duration);
        snackbar.setAction(actionMessage, clickListener);
        snackbar.show();
    }

    public static void noConnectionSnackbar(View root, OnClickListener clickListener) {
        snackbar(root, R.string.error_no_connection, Snackbar.LENGTH_LONG, R.string.retry,
                clickListener);
    }

    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static int getScreenHeight() {
        if (sScreenHeight != -1) {
            return sScreenHeight;
        } else {
            WindowManager windowManager = (WindowManager) BuzzApplication.context()
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenHeight = size.y;
            return sScreenHeight;
        }
    }


    public static int getScreenWidth() {
        if (sScreenWidth != -1) {
            return sScreenWidth;
        } else {
            WindowManager windowManager = (WindowManager) BuzzApplication.context()
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenWidth = size.x;
            return sScreenWidth;
        }
    }

}
