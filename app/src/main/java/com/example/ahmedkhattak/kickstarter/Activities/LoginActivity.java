package com.example.ahmedkhattak.kickstarter.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.Utils.UtilityEmailRegex;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private AppCompatEditText emailInputEditTextView;
    private AppCompatEditText passwordInputEditTextView;
    private AppCompatButton loginActionButton;
    private AppCompatTextView accountSignUpActionTextView;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private OkHttpClient client;
    MaterialDialog loadingDialog;
    MaterialDialog errorDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpUI();
        setUpListeners();
        buildErrorDialog();
        buildLoadingDialog();
        setUpOkHttpClient();

        Log.d(TAG, "onCreate: " + NetworkConfig.getInstance().getAppWebServiceSignInUrl());




    }


    private void setUpUI(){
        emailInputEditTextView = findViewById(R.id.emailInputEditTextView);
        passwordInputEditTextView = findViewById(R.id.passwordInputEditTextView);
        loginActionButton = findViewById(R.id.loginActionButton);
        accountSignUpActionTextView = findViewById(R.id.accountSignUpActionTextView);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
    }

    private void setUpListeners(){

        loginActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              login();
            }
        });


        accountSignUpActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });
    }

    private void login(){

        // get input.
        String email = emailInputEditTextView.getText().toString();
        String password = passwordInputEditTextView.getText().toString();

        // validate it
        boolean res = UtilityEmailRegex.validateEmail(email);

        // if email is invalid do nothing and show error
        if (email.length() == 0 || !res) {
            emailTextInputLayout.setError("Please enter valid email !");
            return;
        }

        // remove email error
        emailTextInputLayout.setError(null);

        // if password is invalid do nothing and show error
        if (password.length() == 0){
            passwordTextInputLayout.setError("Please enter valid password !");
            return;
        }

        // remove password error
        passwordTextInputLayout.setError(null);


        // input is validated now fire off request and then wait for response


        try {
            showLoadingDialog();
            loginCall(email, password);
        }catch (Exception x){
            hideLoadingDialog();
            showErrorDialog("Exception", x.getMessage());
        }





    }

    private void openSignUpActivity(){
        Intent i = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(i);
    }

    private void openMainActivity(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


    private void setUpOkHttpClient(){


        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }


    private void loginCall(String email, String password) throws  Exception{

        RequestBody formBody = new FormBody.Builder()
                .add("task", "task")
                .add("password",password)
                .add("email",email)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceSignInUrl())
                .post(formBody)

                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final String  message = e.getMessage();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        hideLoadingDialog();
                        showErrorDialog("Exception", message);
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
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
                                showErrorDialog("Login Error", "Invalid email or password !");
                            }
                        });

                    } else if  ( statusInt > 0){

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                UserAppStateManager.getInstance().setUserID(statusInt);
                                openMainActivity();

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


    private void buildLoadingDialog(){
        loadingDialog = new MaterialDialog.Builder(this)
                .title(R.string.logging_in)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();
    }


    private void showLoadingDialog(){
        if (!loadingDialog.isShowing()){
            loadingDialog.show();
        }

    }

    private void  hideLoadingDialog(){
        if (loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }


    private void buildErrorDialog(){
        errorDialog = new MaterialDialog.Builder(this)
                .title(R.string.login_error)
                .content(R.string.login_error)
                .negativeText("OK")
                .build();


    }

    private void showErrorDialog(String title, String content){
        if (!errorDialog.isShowing()) {
            errorDialog.setTitle(title);
            errorDialog.setContent(content);
            errorDialog.show();
        }
    }

    private void  hideErrorDialog(){
        if (errorDialog.isShowing()){
            errorDialog.dismiss();
        }
    }





}
