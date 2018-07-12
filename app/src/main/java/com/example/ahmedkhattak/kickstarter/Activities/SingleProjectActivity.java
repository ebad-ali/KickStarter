package com.example.ahmedkhattak.kickstarter.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.AppModels.Explore;
import com.example.ahmedkhattak.kickstarter.AppModels.IndividualCategory;
import com.example.ahmedkhattak.kickstarter.AppModels.SingleProjectView;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.google.gson.Gson;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SingleProjectActivity extends AppCompatActivity {

    private static final String TAG = "SingleProjectActivity";
    Toolbar toolbar = null;

    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;


    SingleProjectView singleProjectView;

    String id, title;

    AppCompatImageView projectImage;
    TextView projectTitleTextView;
    TextView categoryTextView;
    TextView projectAuthorNameTextView;
    TextView projectDateTextView;
    TextView projectDescriptionTextView;
    TextView backersTextView;
    AppCompatButton backProjectActionButton;

    ScrollView scollViewContainer;
    AppCompatRatingBar ratingInput;

    AppCompatButton rateProjectActionButton;

    Dialog ratingDialog;
    RatingBar ratingBar;


    MaterialDialog loadingDialog;
    MaterialDialog errorDialog;
    MaterialDialog successDialog;

    private void populateUI(String imageUrl, String title, String category,
                            String author, String date, String description,
                            String backers, int rating) {
        /* TODO http://www.jsonschema2pojo.org/ ye link hai jider to json ko java objects me badle ga
            Ye settings lagane uder jo setting mention nae hai usko uncheck kro
            package aur class name apni mrzi se rakhle...

            Target language : JAVA
            Source type : JSON
            Annotation style : Gson

            include getters and setters : Check
            include constructors : Check
            use double numbers : Check
        */


        /* TODO jo bhi server se image url arae hai uske sath "http://" prepend krna hai mene sirf ider aur HomeFragment me ki hai
           tune har jaga ye krna jidr bhi imageview pari wi hai adapters me jake changing krni
           Glide.with(this).load("http://" + imageUrl).into(projectImage);
        */

        Log.d(TAG, "populateUI: " + "http://ennovayt.com/" + imageUrl);

        GlideApp.with(this).load("http://ennovayt.com/" + imageUrl).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(projectImage);
        projectDateTextView.setText(date);
        projectDescriptionTextView.setText(description);
        projectAuthorNameTextView.setText(author);
        projectTitleTextView.setText(title);
        categoryTextView.setText(category);
        backersTextView.setText(backers);
        ratingInput.setRating(rating);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_project);
        setUpToolBar();
        setUpUI();
        setUpListeners();
        setUpErrorText();
        setUpOkHttpClient();
        setUpProgressBar();
        setUpSwipeRefresh();
        setId();
        setUpRating();
        buildLoadingDialog();
        buildErrorDialog();
        buildSuccessfulDialog();
        initialSingleProjectClientCall();
    }

    private void setUpUI() {
        projectImage = findViewById(R.id.projectImage);
        projectTitleTextView = findViewById(R.id.projectTitleTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        projectAuthorNameTextView = findViewById(R.id.projectAuthorNameTextView);
        projectDateTextView = findViewById(R.id.projectDateTextView);
        projectDescriptionTextView = findViewById(R.id.projectDescriptionTextView);
        backersTextView = findViewById(R.id.backersTextView);
        backProjectActionButton = findViewById(R.id.backProjectActionButton);
        scollViewContainer = findViewById(R.id.scollViewContainer);
        ratingInput = findViewById(R.id.ratingInput);

        rateProjectActionButton = findViewById(R.id.rateProjectActionButton);
        rateProjectActionButton.setOnClickListener(rateProjectActionButtonListener);
    }

    private void setUpListeners() {

        backProjectActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SingleProjectActivity.this, "back !!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SingleProjectActivity.this, BackProjectsActivity.class);
                i.putExtra("project_id", id);
                i.putExtra("project_title", title);
                startActivity(i);
            }
        });
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
                refreshSingleProjectClientCall();
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


    View.OnClickListener rateProjectActionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // call dialog with rating here

            setUpRatingDialog();
        }
    };

    private void setUpRatingDialog() {

        ratingDialog = new Dialog(SingleProjectActivity.this, R.style.AlertDialogStyle);
        ratingDialog.setContentView(R.layout.rating_dialog);
        ratingDialog.setCancelable(true);
        ratingBar = ratingDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(1);


        AppCompatButton updateButton = ratingDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = (int) ratingBar.getRating();
                if (ratingBar.getRating() == 0.0) {
                    Toast.makeText(getApplicationContext(), "Please Rate more than one star ", Toast.LENGTH_SHORT).show();
                } else {
                    showLoadingDialog();
                    initialRateProjectClientCall("" + a);
                    ratingDialog.dismiss();
                }
            }
        });
        //now that the dialog is set up, it's time to show it
        ratingDialog.show();
    }


    private void initialRateProjectClientCall(String rating) {

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", id)
                .add("user_id", String.valueOf(UserAppStateManager.getInstance().getUserID()))
                .add("rating", rating)
                .add("author_id", singleProjectView.getProjectData().get(0).getUserId())
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceRateProjectUrl())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final String message = e.getMessage();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        hideLoadingDialog();
                        showErrorDialog("Exception", message);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {

                        throw new IOException("Unexpected code " + response);
                    }

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }


                    if (responseBody == null) {
                        return;
                    }

                    String result = responseBody.string();

                    Gson gson = new Gson();
                    Status status = gson.fromJson(result, Status.class);

                    final int statusInt;

                    try {
                        statusInt = Integer.parseInt(status.getStatus());
                    } catch (Exception x) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showErrorDialog("Server Error", "An Unknown error occurred ");
                            }
                        });
                        return;
                    }
                    if (statusInt > 0) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showSuccessfullDialog();
                            }
                        });

                    } else {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showErrorDialog("Server Error", "An Unknown error occurred ");
                            }
                        });
                    }

                }
            }
        });


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


    private void setId() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            title = extras.getString("title");
            changeToolBarTitle(extras.getString("title"));
        }
    }


    private void initialSingleProjectClientCall() {


        RequestBody formBody = new FormBody.Builder()
                .add("project_id", id)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceSingleProjectViewUrl())
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

                                swipeRefresh.setEnabled(true);
                            }
                        });
                        return;
                    }


                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);

                    Gson gson = new Gson();
                    try {
                        singleProjectView = gson.fromJson(result, SingleProjectView.class);

                    } catch (Exception x) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);

                                swipeRefresh.setEnabled(true);
                            }
                        });
                        return;
                    }

                    Log.d(TAG, "onResponse: " + singleProjectView);

                    String imageUrl = "nil";
                    String title = "nil";
                    String category = "nil";
                    String author = "nil";
                    String date = "nil";
                    String description = "nil";

                    if (!singleProjectView.getProjectData().isEmpty()) {
                        imageUrl = singleProjectView.getProjectData().get(0).getImage();
                        title = singleProjectView.getProjectData().get(0).getProjectTitle();
                        category = singleProjectView.getProjectData().get(0).getCategoryName();
                        author = singleProjectView.getProjectData().get(0).getName();
                        date = singleProjectView.getProjectData().get(0).getProjectDate();
                        description = singleProjectView.getProjectData().get(0).getProjectDescription();
                    }


                    String backers = "0";
                    if (!singleProjectView.getProjectBackersData().isEmpty()) {
                        backers = singleProjectView.getProjectBackersData().get(0).getBackers();
                    }


                    float rating = 1;
                    try {
                        rating = Float.parseFloat(singleProjectView.getProjectRating().get(0).getRating());
                    } catch (Exception c) {
                        // do nothing and display 0 rating
                    }


                    final int finalRating = (int) rating;


                    final String finalImageUrl = imageUrl;
                    final String finalTitle = title;
                    final String finalCategory = category;
                    final String finalAuthor = author;
                    final String finalDate = date;
                    final String finalDescription = description;
                    final String finalBackers = backers;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {


                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            scollViewContainer.setVisibility(View.VISIBLE);
                            swipeRefresh.setEnabled(false);
                            disableActions();
                            populateUI(finalImageUrl, finalTitle, finalCategory, finalAuthor, finalDate, finalDescription, finalBackers, finalRating);

                        }
                    });

                }
            }
        });
    }


    private void refreshSingleProjectClientCall() {


        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", id)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceSingleProjectViewUrl())
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
                        singleProjectView = gson.fromJson(result, SingleProjectView.class);
                    } catch (Exception x) {
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

                    Log.d(TAG, "onResponse: " + singleProjectView);


                    final String imageUrl = singleProjectView.getProjectData().get(0).getImage();
                    final String title = singleProjectView.getProjectData().get(0).getProjectTitle();
                    final String category = singleProjectView.getProjectData().get(0).getCategoryName();
                    final String author = singleProjectView.getProjectData().get(0).getName();
                    final String date = singleProjectView.getProjectData().get(0).getProjectDate();
                    final String description = singleProjectView.getProjectData().get(0).getProjectDescription();
                    final String backers = singleProjectView.getProjectBackersData().get(0).getBackers();

                    float rating = 1;
                    try {
                        rating = Float.parseFloat(singleProjectView.getProjectRating().get(0).getRating());
                    } catch (Exception c) {
                        // do nothing and display 0 rating
                    }


                    final int finalRating = (int) rating;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (swipeRefresh.isEnabled()) {
                                swipeRefresh.setRefreshing(false);
                            }
                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            swipeRefresh.setEnabled(false);
                            scollViewContainer.setVisibility(View.VISIBLE);
                            populateUI(imageUrl, title, category, author, date, description, backers, finalRating);


                        }
                    });

                }
            }
        });
    }


    private void setUpRating() {
        ratingInput.setStepSize(1);
        ratingInput.setMax(5);
    }

    private void buildErrorDialog() {
        errorDialog = new MaterialDialog.Builder(this)
                .title("Server Error")
                .content("Server Error")
                .negativeText("OK")
                .build();
    }

    private void showErrorDialog(String title, String content) {
        if (!errorDialog.isShowing()) {
            errorDialog.setTitle(title);
            errorDialog.setContent(content);
            errorDialog.show();
        }
    }

    private void hideErrorDialog() {
        if (errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
    }

    private void buildLoadingDialog() {
        loadingDialog = new MaterialDialog.Builder(this)
                .title("Submitting Rating")
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();
    }

    private void showLoadingDialog() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void buildSuccessfulDialog() {

        successDialog = new MaterialDialog.Builder(this)
                .title("Rating Submitted successfully !")
                .positiveText("OK")
                .cancelable(false)
                .build();


    }

    private void showSuccessfullDialog() {
        if (!successDialog.isShowing()) {
            successDialog.show();
        }
    }

    private void hideSuccessfulDialog() {
        if (successDialog.isShowing()) {
            successDialog.dismiss();

        }
    }

    private void disableActions(){
        if (!singleProjectView.getProjectData().isEmpty()){
            if(singleProjectView.getProjectData().get(0).getUserId().equals(String.valueOf(UserAppStateManager.getInstance().getUserID()))){
                rateProjectActionButton.setEnabled(false);
                backProjectActionButton.setEnabled(false);

            }
        }
    }

}
