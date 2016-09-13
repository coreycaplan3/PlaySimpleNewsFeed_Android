package com.github.coreycaplan3.thebuzz.fragments;

import android.app.IntentService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.thebuzz.R;
import com.github.coreycaplan3.thebuzz.receivers.GetRequestReceiver.OnGetRequestCompleteListener;
import com.github.coreycaplan3.thebuzz.receivers.PostRequestReceiver.OnPostRequestCompleteListener;
import com.github.coreycaplan3.thebuzz.services.ServiceResult;
import com.github.coreycaplan3.thebuzz.utilities.ConnectivityUtility;
import com.github.coreycaplan3.thebuzz.utilities.visual.UiUtility;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * Created by Corey Caplan on 9/13/16.
 * Project: PlaySimpleNewsFeed_Android
 * <p></p>
 * Purpose of Class:
 */
public abstract class BaseFragment extends Fragment implements OnGetRequestCompleteListener,
        OnPostRequestCompleteListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private boolean isInitialLoad;

    private View mRecyclerContainer;
    private View mNoConnectionView;
    private View mEmptyStateView;
    private View mProgressView;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        isInitialLoad = savedInstanceState == null;
        View view = createView(inflater, container, savedInstanceState);
        mRecyclerContainer = view.findViewById(R.id.template_recycler_container);
        mNoConnectionView = view.findViewById(R.id.template_no_connection_container);
        mEmptyStateView = view.findViewById(R.id.template_empty_state_container);
        mProgressView = view.findViewById(R.id.template_recycler_progress_bar);
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.template_recycler_swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.template_recycler_recycler_view);

        setupClickListeners();

        setupViews();
        return view;
    }

    private void setupClickListeners() {
        mNoConnectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        mEmptyStateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    private void setupViews() {
        if (isInitialLoad) {
            if (ConnectivityUtility.hasConnection()) {
                onRefresh();
            } else {
                onNoConnection();
            }
        } else if (!containsDataSet()) {
            onEmptySet();
        } else {
            onFilledSet();
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI
     */
    @NonNull
    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container,
                                    @Nullable Bundle savedInstanceState);

    @Override
    public final void onRefresh() {
        if (containsDataSet()) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            mRecyclerContainer.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setVisibility(View.GONE);
            mNoConnectionView.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.GONE);
        }
        refreshData();
    }

    /**
     * @return True if the data-set is valid and contains data or false if it doesn't. This method
     * is only used in the following situation:
     * When {@link #onNoConnection()} is called, and this method returns false, then the views will
     * switch to display a "can't connect" view. If this method returns true, only a snackbar is
     * displayed, saying that there's no connection. The idea is that we don't want to disrupt the
     * app's flow and switch views if there is already a loaded data-set.
     */
    public abstract boolean containsDataSet();

    /**
     * Called when a refresh is requested. This method's implementation should only invoke an
     * {@link IntentService} whose task is to retrieve data from the network.
     */
    public abstract void refreshData();

    @Override
    public void onGetRequestComplete(@NonNull ServiceResult serviceResult) {
        Log.d(TAG, "onGetRequestComplete: Default implementation called");
    }

    @Override
    public void onPostRequestComplete(@NonNull ServiceResult serviceResult) {
        Log.d(TAG, "onPostRequestComplete: Default implementation called");
    }

    /**
     * Called when the device finished refreshing its data to adjust the UI, if necessary.
     *
     * @param hasConnection True if the device has a connection or false if the refresh failed due
     *                      to device connectivity. This parameter doesn't do anything if
     *                      {@link #containsDataSet()} returns true.
     */
    public final void onEndRefresh(boolean hasConnection) {
        if (containsDataSet()) {
            onFilledSet();
        } else if (hasConnection) {
            onEmptySet();
        } else {
            onNoConnection();
        }
    }

    /**
     * Called when the device is able to successfully connect to the network and refresh data.
     */
    public final void onRestoreConnection() {
        onRegularSet();
    }

    /**
     * Called when the data set goes from an empty to a filled state. Switches the UI accordingly.
     */
    public final void onFilledSet() {
        onRegularSet();
    }

    /**
     * Called when there is an empty data-set and adjusts the UI accordingly to be better looking.
     */
    public final void onEmptySet() {
        if (!isInitialLoad) {
            mEmptyStateView.setVisibility(View.VISIBLE);
            mNoConnectionView.setVisibility(View.GONE);
            mRecyclerContainer.setVisibility(View.GONE);
        } else {
            mRecyclerContainer.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.GONE);
            mNoConnectionView.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the device has no connection and attempts to refresh data. Adjusts the UI
     * to let the user beware that the
     */
    public final void onNoConnection() {
        if (containsDataSet()) {
            UiUtility.snackbar(mRecyclerContainer, R.string.error_no_connection, LENGTH_LONG);
        } else {
            mNoConnectionView.setVisibility(View.VISIBLE);
            mEmptyStateView.setVisibility(View.GONE);
            mRecyclerContainer.setVisibility(View.GONE);
        }
    }

    private void onRegularSet() {
        mRecyclerContainer.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        mProgressView.setVisibility(View.GONE);
        mNoConnectionView.setVisibility(View.GONE);
        mEmptyStateView.setVisibility(View.GONE);
    }

}
