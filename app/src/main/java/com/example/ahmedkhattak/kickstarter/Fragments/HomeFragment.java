package com.example.ahmedkhattak.kickstarter.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.ahmedkhattak.kickstarter.Activities.SingleProjectActivity;
import com.example.ahmedkhattak.kickstarter.AppModels.Project;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnFragmentInteractionListener;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnToolBarChanged;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.HomeAdapter;
import com.example.ahmedkhattak.kickstarter.AppModels.Hero;
import com.example.ahmedkhattak.kickstarter.Utils.ItemClickSupport;
import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFragment";

    private static Boolean isAleadyOn = false;

    private OnFragmentInteractionListener<Object> onFragmentInteractionListener = null;
    private OnToolBarChanged onToolBarChanged = null;

    private ArrayList<Object> homePageProjectsList = new ArrayList<>();

    // recycler view specifics
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private HomeAdapter recyclerViewAdapter;
    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    public HomeFragment() {

    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeToolBarTitle("Home");
        setUpProgressBar();
        setUpSwipeRefresh();
        setUpRecyclyerView();
        setUpRecyclerViewClicklListener();
        setUpOkHttpClient();
        setUpErrorText();


    }

    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }


    private void refreshHomeClientCall(){


        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceHomeUrl())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefresh.isEnabled()) {
                            swipeRefresh.setRefreshing(false);
                        }
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setEnabled(true);
                    }});
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (swipeRefresh.isEnabled()) {
                                    swipeRefresh.setRefreshing(false);
                                }
                                progressBar.setVisibility(View.GONE);
                                swipeRefresh.setEnabled(true);
                            }});
                        throw new IOException("Unexpected code " + response);
                    }
                    if (responseBody == null) {
                        return;
                    }
                    String result =  responseBody.string();
                    Log.d(TAG, "onResponse: " + result );
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.Project>>(){}.getType();
                    ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.Project> projectArrayList =  gson.fromJson(result, collectionType);
                    homePageProjectsList = new ArrayList<>();
                    homePageProjectsList.clear();
                    homePageProjectsList.add(new Hero());
                    homePageProjectsList.addAll(projectArrayList);
                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(homePageProjectsList);
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





    private void initialHomeClientCall(){




        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceHomeUrl())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setEnabled(true);
                        errorText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }});
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                swipeRefresh.setEnabled(true);
                            }});
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
                            }});
                        return;
                    }
                    String result =  responseBody.string();
                    Log.d(TAG, "onResponse: " + result );
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.Project>>(){}.getType();
                    ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.Project> projectArrayList =  gson.fromJson(result, collectionType);
                    homePageProjectsList = new ArrayList<>();
                    homePageProjectsList.clear();
                    homePageProjectsList.add(new Hero());
                    homePageProjectsList.addAll(projectArrayList);
                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(homePageProjectsList);


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


    private void setUpRecyclyerView(){
        recyclerView = getView().findViewById(R.id.homeRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new HomeAdapter(new ArrayList<Object>(), getActivity());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setUpRecyclerViewClicklListener() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                if (homePageProjectsList.get(position) instanceof Project) {

                    Project project = (Project) homePageProjectsList.get(position);

                    Intent i = new Intent(getActivity(), SingleProjectActivity.class);
                    String id = project.getProjectId();
                    String title = project.getProjectTitle();
                    i.putExtra("id", id);
                    i.putExtra("title", title);
                    startActivity(i);
                }

            }
        });
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener<Object>)context;

        }

        if (context instanceof  OnToolBarChanged){
            onToolBarChanged = (OnToolBarChanged) context;
            changeToolBarTitle("Home");
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            changeToolBarTitle("Home");


        }
    }


    private void changeToolBarTitle(String title){
        if (onToolBarChanged != null){
            onToolBarChanged.onToolBarChanged(TAG,title, ToolbarOperations.TitleChange);
        }
    }

    private void setUpProgressBar(){
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpSwipeRefresh(){
        swipeRefresh= getView().findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setEnabled(false);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHomeClientCall();
            }
        });
    }

    private void setUpErrorText(){
        errorText = getView().findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAleadyOn) {
            initialHomeClientCall();
            isAleadyOn  = true;
        }
    }
}
