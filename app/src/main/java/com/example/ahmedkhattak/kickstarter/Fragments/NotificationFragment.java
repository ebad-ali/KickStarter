package com.example.ahmedkhattak.kickstarter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ahmedkhattak.kickstarter.AppModels.Notification;
import com.example.ahmedkhattak.kickstarter.AppModels.Notifications;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnFragmentInteractionListener;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnToolBarChanged;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.NotificationAdapter;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class NotificationFragment extends Fragment {

    private ArrayList<Object> notificationList;
    private static final String TAG = "NotificationFragment";

    // recycler view specifics
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private NotificationAdapter recyclerViewAdapter;
    private OnFragmentInteractionListener<Object> onFragmentInteractionListener = null;
    private OnToolBarChanged onToolBarChanged = null;


    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    // TODO Notification fragment here

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeToolBarTitle("Notification");

        setUpProgressBar();
        setUpSwipeRefresh();
        setUpErrorText();
        setUpOkHttpClient();
        setUpRecyclyerView();

    }

    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }


    private void setUpErrorText() {
        errorText = getView().findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }


    private void setUpSwipeRefresh() {
        swipeRefresh = getView().findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProfileClientCall();
            }
        });
    }


    private void setUpProgressBar() {
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpRecyclyerView() {
        recyclerView = getView().findViewById(R.id.notificationRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new NotificationAdapter(new ArrayList<Object>(), getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener<Object>) context;

        }

        if (context instanceof OnToolBarChanged) {
            onToolBarChanged = (OnToolBarChanged) context;
            changeToolBarTitle("Notification");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            changeToolBarTitle("Notification");


        }
    }

    private void changeToolBarTitle(String title) {
        if (onToolBarChanged != null) {
            onToolBarChanged.onToolBarChanged(TAG, title, ToolbarOperations.TitleChange);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        initialProfileClientCall();
    }

    private void initialProfileClientCall() {

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(UserAppStateManager.getInstance().getUserID()))
                .build();


        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceNotificationUrl())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setEnabled(true);
                        errorText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                swipeRefresh.setEnabled(true);
                            }
                        });

                        throw new IOException("Unexpected code " + response);
                    }
                    if (responseBody == null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                swipeRefresh.setEnabled(true);
                            }
                        });
                        return;
                    }

                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();
                    Notifications notifications = gson.fromJson(result, Notifications.class);

                    notificationList = new ArrayList<Object>();
                    notificationList.clear();
                    notificationList.addAll(notifications.getNotifications());
                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(notificationList);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            swipeRefresh.setEnabled(true);
                        }
                    });
                }


            }
        });

    }


    private void refreshProfileClientCall() {

        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(UserAppStateManager.getInstance().getUserID()))
                .build();


        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceNotificationUrl())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefresh.isEnabled()) {
                            swipeRefresh.setRefreshing(false);
                        }
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setEnabled(true);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                swipeRefresh.setEnabled(true);
                            }
                        });

                        throw new IOException("Unexpected code " + response);
                    }
                    if (responseBody == null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                if (swipeRefresh.isEnabled()) {
                                    swipeRefresh.setRefreshing(false);
                                }
                                progressBar.setVisibility(View.GONE);
                                swipeRefresh.setEnabled(true);
                            }
                        });
                        return;
                    }

                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();
                    Notifications notifications = gson.fromJson(result, Notifications.class);

                    notificationList = new ArrayList<Object>();
                    notificationList.clear();
                    notificationList.addAll(notifications.getNotifications());
                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(notificationList);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (swipeRefresh.isEnabled()) {
                                swipeRefresh.setRefreshing(false);
                            }
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            errorText.setVisibility(View.GONE);
                            swipeRefresh.setEnabled(true);
                        }
                    });
                }
            }
        });


    }


}
