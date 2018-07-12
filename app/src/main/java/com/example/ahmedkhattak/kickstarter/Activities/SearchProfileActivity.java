package com.example.ahmedkhattak.kickstarter.Activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.ProfileAdapter;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;

/**
 * Created by Ebad Ali on 1/1/2018.
 */

// TODO do search profile here
public class SearchProfileActivity extends AppCompatActivity {


    private static final String TAG = "SearchProfileActivity";

    Toolbar searchProfileToolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ProfileAdapter recyclerViewAdapter;

    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

    }

    private void setUpToolbar(String title) {

        searchProfileToolbar = (Toolbar) findViewById(R.id.searchProfileToolbar);
        setSupportActionBar(searchProfileToolbar);

        if (!Objects.equals(title, "")) {
            setTitle(title);
        } else {
            setTitle("Profile");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void setUpErrorText() {
        errorText = findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }

    private void setUpSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refreshProfileClientCall();
            }
        });
    }


    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.profileRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAdapter = new ProfileAdapter(new ArrayList<Object>(), getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
