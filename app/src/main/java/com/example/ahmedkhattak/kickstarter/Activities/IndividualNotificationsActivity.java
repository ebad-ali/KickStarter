package com.example.ahmedkhattak.kickstarter.Activities;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.AppModels.IndividualNotification;
import com.example.ahmedkhattak.kickstarter.AppModels.IndividualNotifications;
import com.example.ahmedkhattak.kickstarter.AppModels.Notifications;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.example.ahmedkhattak.kickstarter.GlideGeneratedApi.GlideApp;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IndividualNotificationsActivity extends AppCompatActivity {


    // TODO individual notifications activity here

    private ArrayList<IndividualNotifications> individualNotificationList;
    private static final String TAG = "IndividualNotificationsActivity";


    Toolbar IndividualNotificationToolbar;
    String project_id, projectTitle,authorID,backerID,amount,tempBackID;


    RelativeLayout relativeLayout;
    TextView totalTargetTextView, FundedTextView, backedUserNameTextView, transactionPINTextView, errorText, ProjectNameTextView;

    private OkHttpClient client;
    private ProgressBar progressBar;

    AppCompatButton fundReceivedButton;

    MaterialDialog loadingDialog;
    MaterialDialog errorDialog;
    MaterialDialog successDialog;

    AppCompatImageView projectImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_notifications);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            project_id = bundle.getString("project_id");
            projectTitle = bundle.getString("project_title");
            authorID = bundle.getString("author_id");
            backerID = bundle.getString("backer_id");
            amount = bundle.getString("amount_id");
            tempBackID = bundle.getString("temp_back_id");
        }

        setUpToolbar(projectTitle);
        setUpOkHttpClient();
        setUpProgressBar();
        setUpUI();
        setUpErrorText();
        buildLoadingDialog();
        buildErrorDialog();
        buildSuccessfulDialog();

    }

    private void setUpToolbar(String title) {

        IndividualNotificationToolbar = (Toolbar) findViewById(R.id.individualNotificationToolbar);
        setSupportActionBar(IndividualNotificationToolbar);

        if (!Objects.equals(title, "")) {
            setTitle(title);
        } else {
            setTitle("Notifications");
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

    private void setUpUI() {
        relativeLayout = findViewById(R.id.relativeLayoutNotification);
        relativeLayout.setVisibility(View.GONE);

        totalTargetTextView = findViewById(R.id.totalTargetTextView);
        FundedTextView = findViewById(R.id.FundedTextView);
        backedUserNameTextView = findViewById(R.id.backedUserNameTextView);
        transactionPINTextView = findViewById(R.id.transactionPINTextView);
        ProjectNameTextView = findViewById(R.id.ProjectNameTextView);

        projectImageView = findViewById(R.id.projectImageView);

        fundReceivedButton = findViewById(R.id.fundReceivedActionButton);

        fundReceivedButton.setOnClickListener(fundReceivedActionButtonListener);

    }

    private void setUpErrorText() {
        errorText = findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        initialProfileClientCall();

    }


    private void initialProfileClientCall() {

        RequestBody formBody = new FormBody.Builder()
                .add("id", tempBackID)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceIndividualNotificationUrl())
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
                        errorText.setVisibility(View.VISIBLE);

                    }
                });
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);

                            }
                        });

                        throw new IOException("Unexpected code " + response);
                    }
                    if (responseBody == null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                errorText.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                        return;
                    }

                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();

                    IndividualNotifications individualNotifications = gson.fromJson(result, IndividualNotifications.class);

                    individualNotificationList = new ArrayList<IndividualNotifications>();
                    individualNotificationList.clear();
                    individualNotificationList.add(individualNotifications);


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);

                            Boolean check;

                            check = individualNotificationList.get(0).isAlreadyBackedProject();

                            if(check)
                            {
                                fundReceivedButton.setVisibility(View.GONE);
                            }

                            totalTargetTextView.setText(individualNotificationList.get(0).getIndividual().get(0).getFundTargetamount());
                            FundedTextView.setText(individualNotificationList.get(0).getIndividual().get(0).getAmount());
                            backedUserNameTextView.setText(individualNotificationList.get(0).getIndividual().get(0).getName());
                            transactionPINTextView.setText(individualNotificationList.get(0).getIndividual().get(0).getTransactionPin());
                            ProjectNameTextView.setText(("backed your " + individualNotificationList.get(0).getIndividual().get(0).getProjectTitle() + " Project"));

                            String imageUrl = individualNotificationList.get(0).getIndividual().get(0).getImage();

                            Log.e("imageUri","http://ennovayt.com/"+individualNotificationList.get(0).getIndividual().get(0).getImage());

                            GlideApp.with(IndividualNotificationsActivity.this).load("http://ennovayt.com/" + imageUrl).placeholder(R.drawable.gradient).error(R.drawable.gradient).into(projectImageView);


                            relativeLayout.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    View.OnClickListener fundReceivedActionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                showLoadingDialog();
                fundReceivedClientCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void fundReceivedClientCall() throws Exception {

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", project_id)
                .add("backer_id", backerID)
                .add("author_id",authorID)
                .add("amount_id", amount)
                .add("temp_back_id", tempBackID)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceBackProjectFundReceivedUrl())
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

                    if (statusInt < 1) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // wrong credentials
                                hideLoadingDialog();
                                showErrorDialog("Server Error", "An Unknown error occurred ");
                            }
                        });

                    } else if (statusInt > 0) {

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
                .title("Posting")
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
                .title("Fund Received successfully")
                .positiveText("OK")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        // TODO
                        finish();
                    }
                })
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
