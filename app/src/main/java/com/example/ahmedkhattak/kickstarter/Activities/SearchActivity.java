package com.example.ahmedkhattak.kickstarter.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.ByContentCreators;
import com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators.SearchResult;
import com.example.ahmedkhattak.kickstarter.AppModels.ByProject.ByProject;
import com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.ByFunders;
import com.example.ahmedkhattak.kickstarter.AppModels.SearchByName.ByName;
import com.example.ahmedkhattak.kickstarter.Enums.SearchType;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.SearchAdapter;
import com.example.ahmedkhattak.kickstarter.Utils.ItemClickSupport;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    Toolbar searchToolbar = null;
    EditText searchText;
    ImageView searchClearButton;


    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    // default is by name
    SearchType searchType = SearchType.ByName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    SearchAdapter recyclerViewAdapter;

    private final SearchType[] searchTypes = SearchType.values();

    MaterialDialog searchTypeDialog;

    ArrayList<Object> searchResultList = new ArrayList<>();
    ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult> byProjectList = new ArrayList<com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult>();

    Boolean isSearching = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpToolBar();
        setUpUI();
        setUpProgressBar();
        setUpSwipeRefresh();
        setUpErrorText();
        buildSearchTypeDialog();
        setUpRecyclyerView();
        setUpOkHttpClient();
        setUpRecyclerViewClickListener();
    }

    private void setUpRecyclyerView(){
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new SearchAdapter(new ArrayList<Object>(), this, searchType);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                // socket timeout
                .build();
    }


    private void submit(String searchTerm, SearchType searchType){

      if (searchTerm.length() == 0 || searchTerm.isEmpty()) {
          Toast.makeText(this, "Search term is empty", Toast.LENGTH_SHORT).show();
          return;
      }

      if (searchType == null){
          Toast.makeText(this, "Search Type is null", Toast.LENGTH_SHORT).show();
          return;
      }

      try {
          searchCall(searchTerm, searchType);
      }catch (Exception x){
          isSearching = false;
          errorText.setVisibility(View.VISIBLE);
          errorText.setText(x.getMessage());

      }
    }


    // TODO Search Click Listener
    private void setUpRecyclerViewClickListener() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.ByProject.SearchResult){

                    Toast.makeText(SearchActivity.this, ""+byProjectList.get(position).getProjectTitle(), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(SearchActivity.this, SingleProjectActivity.class);
                    String id = byProjectList.get(position).getProjectId();
                    String title = byProjectList.get(position).getProjectTitle();
                    i.putExtra("id", id);
                    i.putExtra("title", title);
                    startActivity(i);

                } else if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders.SearchResult){
                    Toast.makeText(SearchActivity.this, "BYFUNDERS", Toast.LENGTH_SHORT).show();
                } else if (searchResultList.get(position) instanceof SearchResult) {
                    Toast.makeText(SearchActivity.this, "BYCONTENTREATORS", Toast.LENGTH_SHORT).show();
                }  else if (searchResultList.get(position) instanceof com.example.ahmedkhattak.kickstarter.AppModels.SearchByName.SearchResult) {
                    Toast.makeText(SearchActivity.this, "BYNAME", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void searchCall(final String searchTerm, final SearchType searchType){


        final SearchType searchTypeFinal = searchType;

        recyclerView.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        RequestBody formBody = new FormBody.Builder()
                .add("search_type", searchType.toString())
                .add("search_item",searchTerm)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceSearchUrl())
                .post(formBody)
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
                isSearching = false;
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
                        isSearching = false;
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

                    searchResultList = new ArrayList<>();
                    searchResultList.clear();

                    byProjectList = new ArrayList<>();
                    byProjectList.clear();

                    try {
                        switch (searchTypeFinal) {

                            case ByContentCreators:
                                ByContentCreators byContentCreators = gson.fromJson(result, ByContentCreators.class);
                                searchResultList.addAll(byContentCreators.getSearchResult());
                                break;

                            case ByFunders:
                                ByFunders byFunders = gson.fromJson(result, ByFunders.class);
                                searchResultList.addAll(byFunders.getSearchResult());
                                break;

                            case ByName:
                                ByName byName = gson.fromJson(result, ByName.class);
                                searchResultList.addAll(byName.getSearchResult());
                                break;

                            case ByProject:
                                ByProject byProject = gson.fromJson(result, ByProject.class);
                                searchResultList.addAll(byProject.getSearchResult());
                                byProjectList.addAll(byProject.getSearchResult());
                                break;

                        }


                        recyclerViewAdapter.setSearchType(searchTypeFinal);
                        recyclerViewAdapter.setContext(SearchActivity.this);
                        recyclerViewAdapter.swap(searchResultList);

                    } catch (Exception x){

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            }});

                        isSearching = false;

                        return;
                    }




                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            isSearching = false;

                        }
                    });

                }
            }
        });

    }





    private void setUpProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setEnabled(false);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // TODO add refresh
            }
        });
    }

    private void setUpErrorText() {
        errorText = findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }


    private void setUpToolBar() {
        searchToolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setUpUI() {
        searchText = findViewById(R.id.search_view);
        searchClearButton = findViewById(R.id.search_clear);


        searchClearButton.setOnClickListener(searchClearButtonListener);


        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (searchText != null || !searchText.getText().toString().isEmpty()) {
                        //TODO search logic here

                        if (!isSearching) {
                            submit(searchText.getText().toString(), searchType);
                            isSearching = true;
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter something ", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                return false;
            }
        });

    }


    // Clear the text of editText
    private View.OnClickListener searchClearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchText.setText(null);
        }
    };


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;

            case  R.id.searchType:
                showSearchTypeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchtype, menu);
        return true;
    }

    private void buildSearchTypeDialog(){
        searchTypeDialog =new MaterialDialog.Builder(this)
                .title("Choose search type")
                .items(R.array.searchTypes)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(searchType.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT).show();
                        searchType = searchTypes[which];

                        Toast.makeText(SearchActivity.this, searchType.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .build();
    }


    private void showSearchTypeDialog(){
        if (!searchTypeDialog.isShowing()){
            searchTypeDialog.setSelectedIndex(searchType.ordinal());
            searchTypeDialog.show();
        }

    }

    private void  hideSearchTypeDialog(){
        if (searchTypeDialog.isShowing()){
            searchTypeDialog.dismiss();
        }
    }



}
