package com.example.ahmedkhattak.kickstarter.Activities;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EditBankInfoActivity extends AppCompatActivity {


    private static final String TAG = "EditBankInfoActivity";
    private MaterialDialog errorDialog;
    private MaterialDialog submittingDialog;
    Toolbar toolbar = null;
    private OkHttpClient client;


    TextInputLayout bankBranchInputLayout;
    TextInputLayout bankAccNoInputLayout;
    TextInputLayout cnicInputLayout;
    TextInputLayout easyPaisaPhnoInputLayout;

    AppCompatEditText bankBranchEditText;
    AppCompatEditText bankAccNoEditText;
    AppCompatEditText cnicEditText;
    AppCompatEditText easyPaisaPhnoEditText;

    String branch;
    String accountNo;
    String cnic;
    String easyPaisaPhoneNum;


    AppCompatButton submitActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank_info);
        setUpUI();
        setUpToolBar();
        setUpOkHttpClient();
        buildErrorDialog();
        buildLoadingDialog();
        setUpListeners();
    }

    private void setUpUI() {
        bankBranchInputLayout = findViewById(R.id.bankBranchInputLayout);
        bankAccNoInputLayout = findViewById(R.id.bankAccNoInputLayout);
        cnicInputLayout = findViewById(R.id.cnicInputLayout);
        easyPaisaPhnoInputLayout = findViewById(R.id.easyPaisaPhnoInputLayout);

        bankBranchEditText = findViewById(R.id.bankBranchEditText);
        bankAccNoEditText = findViewById(R.id.bankAccNoEditText);
        cnicEditText = findViewById(R.id.cnicEditText);
        easyPaisaPhnoEditText = findViewById(R.id.easyPaisaPhnoEditText);

        submitActionButton = findViewById(R.id.submitActionButton);

    }

    private void setUpListeners() {
        submitActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
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

    private void submit() {

        easyPaisaPhoneNum = easyPaisaPhnoEditText.getText().toString();
        cnic = cnicEditText.getText().toString();
        branch = bankBranchEditText.getText().toString();
        accountNo = bankAccNoEditText.getText().toString();


        if (branch.length() == 0){
            bankBranchInputLayout.setError("Please enter valid branch");
            return;
        }

        bankBranchInputLayout.setError(null);


        if (accountNo.length() == 0){
            bankAccNoInputLayout.setError("Please enter valid account number");
            return;
        }

        bankAccNoInputLayout.setError(null);





        if (cnic.length() == 0){
            cnicInputLayout.setError("Please enter valid cnic");
            return;
        }

        cnicInputLayout.setError(null);


        if (easyPaisaPhoneNum.length() == 0){
            easyPaisaPhnoInputLayout.setError("Please enter valid Easy Paisa Phone Number");
            return;
        }

        easyPaisaPhnoInputLayout.setError(null);




       try
       {
           showLoadingDialog();
        submitCall(String.valueOf(UserAppStateManager.getInstance().getUserID()),branch,accountNo,cnic,easyPaisaPhoneNum);
       } catch (Exception x) {
           final Exception xx = x;
           new Handler(Looper.getMainLooper()).post(new Runnable() {
               @Override
               public void run() {

                   hideLoadingDialog();
                   showErrorDialog("Server Error", xx.getMessage());
               }
           });
       }







    }

    private void clearEntries(){
        easyPaisaPhnoEditText.setText(null);
        bankAccNoEditText.setText(null);
        bankBranchEditText.setText(null);
        cnicEditText.setText(null);

    }

    private void submitCall(String userId, String branch, String accountNo, String cnic, String easyPaisaPhoneNum ){

        RequestBody formBody = new FormBody.Builder()
                .add("user_id", userId)
                .add("bank_branch_id",branch)
                .add("bank_acc_id",accountNo)
                .add("cnic_id",cnic)
                .add("phone_id",easyPaisaPhoneNum)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceAddBankInfoUrl())
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        hideLoadingDialog();
                        showErrorDialog("Server Error", "An Unknown error occurred ");
                    }
                });


            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                    {

                        throw new IOException("Unexpected code " + response);
                    }

                    if (responseBody == null) {

                        return;
                    }


                    String result = responseBody.string();

                    Log.d(TAG, "onResponse: " + result);
                    Gson gson = new Gson();
                    Status status = gson.fromJson(result,Status.class);

                    final int statusInt;

                    try {
                        statusInt = Integer.parseInt(status.getStatus());
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
                                showErrorDialog("Server Error", "An Unknown error occured");
                            }
                        });

                    } else if  ( statusInt > 0){

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                clearEntries();
                                Toast.makeText(EditBankInfoActivity.this, "Form submitted", Toast.LENGTH_LONG).show();



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


    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void buildLoadingDialog() {
        submittingDialog = new MaterialDialog.Builder(this)
                .title(R.string.submitting)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();
    }


    private void showLoadingDialog() {
        if (!submittingDialog.isShowing()) {
            submittingDialog.show();
        }

    }

    private void hideLoadingDialog() {
        if (submittingDialog.isShowing()) {
            submittingDialog.dismiss();
        }
    }


    private void buildErrorDialog() {
        errorDialog = new MaterialDialog.Builder(this)
                .title(R.string.login_error)
                .content(R.string.login_error)
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

}
