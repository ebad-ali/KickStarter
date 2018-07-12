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

import com.example.ahmedkhattak.kickstarter.AppModels.Category;
import com.example.ahmedkhattak.kickstarter.AppModels.Explore;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnFragmentInteractionListener;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnToolBarChanged;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.ExploreAdapter;
import com.example.ahmedkhattak.kickstarter.Utils.ItemClickSupport;
import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ExploreFragment extends Fragment {


    private List<Category> categoryArrayList;

    private static final String TAG = "ExploreFragment";

    // recycler view specifics
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ExploreAdapter recyclerViewAdapter;
    private OnFragmentInteractionListener<Object> onFragmentInteractionListener = null;
    private OnToolBarChanged onToolBarChanged = null;

    private static Boolean isAleadyOn = false;


    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;


    public ExploreFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeToolBarTitle("Explore");
        setUpErrorText();
        setUpProgressBar();
        setUpSwipeRefresh();
        setUpRecyclyerView();
        setUpRecyclerViewClicklListener();
        setUpOkHttpClient();
    }




    private void setUpRecyclyerView(){
        recyclerView = getView().findViewById(R.id.exploreRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new ExploreAdapter(new ArrayList<Category>(), getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    private  void setUpRecyclerViewClicklListener(){
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onFragmentInteractionListener.onFragmentInteraction(TAG,categoryArrayList.get(position));
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
            changeToolBarTitle("Explore");
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            changeToolBarTitle("Explore");

        }
    }

    private void changeToolBarTitle(String title){
        if (onToolBarChanged != null){
            onToolBarChanged.onToolBarChanged(TAG,title, ToolbarOperations.TitleChange);
        }
    }


    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }

    private void setUpProgressBar(){
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpSwipeRefresh(){
        swipeRefresh= getView().findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshExploreClientCall();
            }
        });
    }

    private void setUpErrorText(){
        errorText = getView().findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }



    private void initialExploreClientCall(){




        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceExploreUrl())
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




                    Explore explore = gson.fromJson(result, Explore.class);

                    categoryArrayList = new ArrayList<>();
                    categoryArrayList.add( new Category("nope","Projects we love","project we love"));
                    categoryArrayList.add( new Category("nope","Nearly funded","project nearly funded"));
                    categoryArrayList.add( new Category("nope","Just launched","project just launched"));
                    categoryArrayList.add( new Category("nope","Everything","project everything"));
                    categoryArrayList.addAll(explore.getCategories());

                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(categoryArrayList);


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




    private void refreshExploreClientCall(){


        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceExploreUrl())
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


                    Explore explore = gson.fromJson(result, Explore.class);

                    categoryArrayList = new ArrayList<>();
                    categoryArrayList.add( new Category("nope","Projects we love","project we love"));
                    categoryArrayList.add( new Category("nope","Nearly funded","project nearly funded"));
                    categoryArrayList.add( new Category("nope","Just launched","project just launched"));
                    categoryArrayList.add( new Category("nope","Everything","project everything"));
                    categoryArrayList.addAll(explore.getCategories());



                    recyclerViewAdapter.setContext(getContext());
                    recyclerViewAdapter.swap(categoryArrayList);




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


    @Override
    public void onResume() {
        super.onResume();
        if (!isAleadyOn) {
            try {

                initialExploreClientCall();

            } catch (Exception x) {

            }
            isAleadyOn = true;
        }
    }

}
