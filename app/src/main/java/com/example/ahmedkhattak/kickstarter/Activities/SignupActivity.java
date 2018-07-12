package com.example.ahmedkhattak.kickstarter.Activities;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.Utils.UtilityEmailRegex;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignupActivity extends AppCompatActivity {


    private static final String TAG = "SignupActivity";



    private OkHttpClient client;
    MaterialDialog loadingDialog;
    MaterialDialog errorDialog;

    TextInputLayout nameTextInputLayout;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;

    AppCompatEditText nameInputEditTextView;
    AppCompatEditText emailInputEditTextView;
    AppCompatEditText passwordInputEditTextView;

    TextView loginActionTextView;

    AppCompatButton signUpActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUI();
        setUpListeners();
        buildErrorDialog();
        buildLoadingDialog();
        setUpOkHttpClient();
    }




    private void setUpUI() {

        nameInputEditTextView = findViewById(R.id.nameInputEditTextView);
        emailInputEditTextView =  findViewById(R.id.emailInputEditTextView);
        passwordInputEditTextView =  findViewById(R.id.passwordInputEditTextView);

        nameTextInputLayout =  findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout =  findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout =  findViewById(R.id.passwordTextInputLayout);

        loginActionTextView = findViewById(R.id.loginActionTextView);

        signUpActionButton = findViewById(R.id.signUpActionButton);

        Log.d(TAG, "setUpUI: " + NetworkConfig.getInstance().getAppWebServiceSignUpUrl());

    }


    private void setUpListeners() {

        signUpActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


        loginActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }


    private void signUp(){

        String name = nameInputEditTextView.getText().toString();
        String email = emailInputEditTextView.getText().toString();
        String password = passwordInputEditTextView.getText().toString();

        // validate it
        boolean res = UtilityEmailRegex.validateEmail(email);



        // if name is invalid do nothing and show error
        if (name.length() == 0){
            nameTextInputLayout.setError("Please enter valid name !");
            return;
        }

        // remove name error
        nameTextInputLayout.setError(null);
        

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

        try {
            showLoadingDialog();
            signUpCall(name,email, password);
        }catch (Exception x){
            hideLoadingDialog();
            showErrorDialog("Exception", x.getMessage());
        }
    }


    private void  signUpCall(String name, String email, String password){

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("password",password)
                .add("email",email)
                .build();

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceSignUpUrl())
                .post(formBody)

                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e)
            {
                final Exception ee = e;
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        hideLoadingDialog();
                        showErrorDialog("Exception", ee.getMessage());
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showErrorDialog("Exception", "Request was unsuccessful");
                            }
                        });

                        throw new IOException("Unexpected code " + response);
                    }


                    if (responseBody == null) {
                        return;
                    }

                    String result =  responseBody.string();
                    final String toastResult = result;




                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(SignupActivity.this, toastResult, Toast.LENGTH_SHORT).show();
                        }
                    });



                    if (result.equals("0")) {




                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                showErrorDialog("Signup Error", "Could not Signup !");
                            }
                        });

                    } else if  (result.equals("1") ){

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideLoadingDialog();
                                Toast.makeText(SignupActivity.this, "Signup successful you can now login !", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });


                    } else{

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
                .title(R.string.signin_up)
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
