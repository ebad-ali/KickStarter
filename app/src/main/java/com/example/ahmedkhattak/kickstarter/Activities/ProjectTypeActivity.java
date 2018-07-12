package com.example.ahmedkhattak.kickstarter.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedkhattak.kickstarter.AppModels.DataProject;
import com.example.ahmedkhattak.kickstarter.AppModels.Datum;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.ProjectTypeAdapter;
import com.example.ahmedkhattak.kickstarter.Utils.ItemClickSupport;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProjectTypeActivity extends AppCompatActivity {

    private static final String TAG = "ProjectTypeActivity";


    DataProject dataProject;

    Toolbar toolbar = null;

    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;
    private String type;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ProjectTypeAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_type);
        setUpToolBar();
        setType();
        setUpErrorText();
        setUpOkHttpClient();
        setUpProgressBar();
        setUpSwipeRefresh();
        setUpRecyclyerView();
        setUpRecyclerViewClicklListener();
        initialProjectTypeClientCall();
    }



    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }

    private void setUpProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProjectTypeClientCall();
            }
        });
    }

    private void setUpErrorText() {
        errorText = findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void changeToolBarTitle(String title) {

        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    private void setType() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            changeToolBarTitle(extras.getString("title"));
        }
    }

    private void setUpRecyclerViewClicklListener() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Intent i = new Intent(ProjectTypeActivity.this,SingleProjectActivity.class);
                String id = dataProject.getData().get(position).getProjectId();
                String title = dataProject.getData().get(position).getProjectTitle();
                i.putExtra("id", id);
                i.putExtra("title", title);
                startActivity(i);
            }
        });
    }

    private void setUpRecyclyerView() {
        recyclerView = findViewById(R.id.projectTypeRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new ProjectTypeAdapter(new DataProject(new ArrayList<Datum>()), this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    private void initialProjectTypeClientCall() {


        RequestBody formBody = new FormBody.Builder()
                .add("type", type)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceProjectTypeUrl())
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

                    try {
                        dataProject = gson.fromJson(result, DataProject.class);
                    } catch (JsonParseException x) {
                        //

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

                    recyclerViewAdapter.setContext(ProjectTypeActivity.this);
                    recyclerViewAdapter.swap(dataProject);

                    Log.d(TAG, "onResponse: " + dataProject);

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


    private void refreshProjectTypeClientCall() {


        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }


        RequestBody formBody = new FormBody.Builder()
                .add("type", type)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceProjectTypeUrl())
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
                                if (swipeRefresh.isEnabled()) {
                                    swipeRefresh.setRefreshing(false);
                                }
                                progressBar.setVisibility(View.GONE);
                                swipeRefresh.setEnabled(true);
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }
                    if (responseBody == null) {
                        return;
                    }
                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);


                    Gson gson = new Gson();

                    try {
                        dataProject = gson.fromJson(result, DataProject.class);
                    } catch (JsonParseException x) {
                        //

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

                    recyclerViewAdapter.setContext(ProjectTypeActivity.this);
                    recyclerViewAdapter.swap(dataProject);

                    Log.d(TAG, "onResponse: " + dataProject);

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
