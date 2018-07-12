package com.example.ahmedkhattak.kickstarter.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.example.ahmedkhattak.kickstarter.AppModels.TempBackedProject;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BackProjectsActivity extends AppCompatActivity {


    Toolbar toolbar = null;

    String project_id, projectTitle;

    private ArrayList<TempBackedProject> tempBackedProjectList;
    private static final String TAG = "BackProjectsActivity";

    ArrayList<String> paymentArray;

    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    private TextView bankAccountNoTextView, branchNoTextView, cnicNoTextView, PhoneNoTextView;
    private ScrollView scrollView;
    AppCompatSpinner backedProjectspinner;

    AppCompatEditText amountInputEditText;
    AppCompatEditText commentInputEditText;


    AppCompatButton submitButton;

    String amount, comment, paymentType;

    MaterialDialog loadingDialog;
    MaterialDialog errorDialog;
    MaterialDialog successDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_project);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            project_id = bundle.getString("project_id");
            projectTitle = bundle.getString("project_title");
        }

        setUpToolBar();
        setUpErrorText();
        setUpOkHttpClient();
        setUpSwipeRefresh();
        SetUpUI();
        setUpProgressBar();
        buildLoadingDialog();
        buildErrorDialog();
        buildSuccessfulDialog();

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
                refreshProfileClientCall();
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
        toolbar.setTitle(projectTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void SetUpUI() {
        scrollView = findViewById(R.id.scollViewContainer);
        scrollView.setVisibility(View.GONE);

        bankAccountNoTextView = findViewById(R.id.bankAccountNoTextView);
        branchNoTextView = findViewById(R.id.branchNoTextView);
        cnicNoTextView = findViewById(R.id.cnicNoTextView);
        PhoneNoTextView = findViewById(R.id.PhoneNoTextView);

        submitButton = findViewById(R.id.submitActionButton);
        submitButton.setOnClickListener(submitButtonListener);

        amountInputEditText = findViewById(R.id.amountInputEditText);
        commentInputEditText = findViewById(R.id.commentInputEditText);

        backedProjectspinner = findViewById(R.id.backedProjectspinner);

        paymentArray = new ArrayList<>();
        paymentArray.add("Bank Transfer");
        paymentArray.add("Easy Paisa");

        ArrayAdapter<String> backedProjectspinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, paymentArray);


        backedProjectspinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backedProjectspinner.setAdapter(backedProjectspinnerAdapter);

        backedProjectspinner.setOnItemSelectedListener(backedProjectspinnerListener);

    }

    AdapterView.OnItemSelectedListener backedProjectspinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            // Toast.makeText(getApplicationContext(), paymentArray.get(i), Toast.LENGTH_SHORT).show();
            paymentType = paymentArray.get(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            submit();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        initialProfileClientCall();
    }

    private void initialProfileClientCall() {

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", project_id)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceBackProjectUrl())
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
                        swipeRefresh.setEnabled(true);


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
                                swipeRefresh.setEnabled(true);

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
                                swipeRefresh.setEnabled(true);

                            }
                        });
                        return;
                    }

                    String result = responseBody.string();
                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();

                    final TempBackedProject tempBackedProject = gson.fromJson(result, TempBackedProject.class);

                    tempBackedProjectList = new ArrayList<TempBackedProject>();
                    tempBackedProjectList.clear();
                    tempBackedProjectList.add(tempBackedProject);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            swipeRefresh.setEnabled(false);



                            bankAccountNoTextView.setText(tempBackedProjectList.get(0).getBank().get(0).getBankAccount());
                            branchNoTextView.setText(tempBackedProjectList.get(0).getBank().get(0).getBankBranchNo());

                            cnicNoTextView.setText(tempBackedProjectList.get(0).getEasyPaisa().get(0).getUserCNIC());
                            PhoneNoTextView.setText(tempBackedProjectList.get(0).getEasyPaisa().get(0).getUserPhone());

                            scrollView.setVisibility(View.VISIBLE);


                        }
                    });
                }


            }
        });


    }


    private void submit() {
        amount = amountInputEditText.getText().toString();
        comment = commentInputEditText.getText().toString();

        if (amount.length() == 0) {
            amountInputEditText.setError("Please Enter Amount !");
            return;
        }

        amountInputEditText.setError(null);


        if (comment.length() == 0) {
            commentInputEditText.setError("Please Enter Comment !");
            return;
        }
        commentInputEditText.setError(null);


        try {
            showLoadingDialog();
            submitClientCall(amount, comment, paymentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitClientCall(String amount, String comment, String paymentType) throws Exception {

        String pay = "";

        if (paymentType.equals("Easy Paisa")) {
            pay = "EasyPaisa";
        } else if (paymentType.equals("Bank Transfer")) {
            pay = "BankTransfer";
        }

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", project_id)
                .add("fund_id", amount)
                .add("method_id", pay)
                .add("comment_id", comment)
                .add("user_id", String.valueOf(UserAppStateManager.getInstance().getUserID()))
                .add("submit", "submit")
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceBackProjectUrl())
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


                    String result =  responseBody.string();

                    Gson gson = new Gson();

                    Status status = null;
                    try {
                       // TODO IDER WICH HOTA KETA JSON IS NOT IN CORRECT FORMAT !!!!
                        status = gson.fromJson(result, Status.class);
                    } catch (Exception x){

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showErrorDialog("Server Error", "An Unknown error occurred ");
                            }
                        });
                        return;

                    }

                    final int statusInt;

                    try {
                        statusInt = Integer.parseInt(status.getStatus());
                        Log.e("statusbackproject",""+statusInt );

                    }catch (Exception x) {


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
                                showErrorDialog("Server Error", "Could not backproject");
                            }
                        });

                    } else if  ( statusInt > 0){

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
                                showErrorDialog("Server Error", "Contact Admin !");
                            }
                        });

                    }

                }
            }
        });

    }


    private void refreshProfileClientCall() {

        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", project_id)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceBackProjectUrl())
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

                    final TempBackedProject tempBackedProject = gson.fromJson(result, TempBackedProject.class);

                    tempBackedProjectList = new ArrayList<TempBackedProject>();
                    tempBackedProjectList.clear();
                    tempBackedProjectList.add(tempBackedProject);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (swipeRefresh.isEnabled()) {
                                swipeRefresh.setRefreshing(false);
                            }
                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            swipeRefresh.setEnabled(true);

                            bankAccountNoTextView.setText(tempBackedProjectList.get(0).getBank().get(0).getBankAccount());
                            branchNoTextView.setText(tempBackedProjectList.get(0).getBank().get(0).getBankBranchNo());

                            cnicNoTextView.setText(tempBackedProjectList.get(0).getEasyPaisa().get(0).getUserCNIC());
                            PhoneNoTextView.setText(tempBackedProjectList.get(0).getEasyPaisa().get(0).getUserPhone());

                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
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
                .title("Form submitted successfully")
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


}
