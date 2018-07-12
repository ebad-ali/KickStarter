package com.example.ahmedkhattak.kickstarter.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.App;
import com.example.ahmedkhattak.kickstarter.AppModels.Status;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.RecyclerViewAdapters.ExploreAdapter;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar = null;

    private static final String TAG = "EditProfileActivity";

    TextInputLayout nameTextInputLayout;
    TextInputLayout emailTextInputLayout;
    TextInputLayout locationTextInputLayout;
    TextInputLayout companyTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextInputLayout biographyInputLayout;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private static final int REQUEST_WRITE_PERMISSION = 786;

    private Uri uri;
    private String name;
    private String email;
    private String location;
    private String company;
    private String  password;
    private String  biography;




    AppCompatEditText nameInputEditText;
    AppCompatEditText emailInputEditText;
    AppCompatEditText locationInputEditText;
    AppCompatEditText companyInputEditText;
    AppCompatEditText passwordInputEditText;
    AppCompatEditText biographyInputEditText;

    AppCompatButton profileImageSelectActionButton;
    AppCompatButton submitActionButton;
    AppCompatImageView profileImage;

    private MaterialDialog errorDialog;
    private MaterialDialog submittingDialog;

    private OkHttpClient client;


    private void setUpUI(){

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        locationTextInputLayout = findViewById(R.id.locationTextInputLayout);
        companyTextInputLayout = findViewById(R.id.companyTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        biographyInputLayout = findViewById(R.id.biographyInputLayout);


        nameInputEditText = findViewById(R.id.nameInputEditText);
        emailInputEditText = findViewById(R.id.emailInputEditText);
        locationInputEditText = findViewById(R.id.locationInputEditText);
        companyInputEditText = findViewById(R.id.companyInputEditText);
        passwordInputEditText = findViewById(R.id.passwordInputEditText);
        biographyInputEditText = findViewById(R.id.biographyInputEditText);


        submitActionButton = findViewById(R.id.submitActionButton);
        profileImageSelectActionButton = findViewById(R.id.profileImageSelectActionButton);
        profileImage = findViewById(R.id.profileImage);



    }


    private void submit(){

        name = nameInputEditText.getText().toString();
        email = emailInputEditText.getText().toString();
        location = locationInputEditText.getText().toString();
        company = companyInputEditText.getText().toString();
        password = passwordInputEditText.getText().toString();
        biography = biographyInputEditText.getText().toString();

        if (name.length() == 0){
            nameTextInputLayout.setError("Please enter valid name");
            return;
        }

        nameTextInputLayout.setError(null);


        if (email.length() == 0){
            emailTextInputLayout.setError("Please enter valid email");
            return;
        }

        emailTextInputLayout.setError(null);

        if (location.length() == 0){
            locationTextInputLayout.setError("Please enter valid location");
            return;
        }

        locationTextInputLayout.setError(null);

        if (company.length() == 0){
            companyTextInputLayout.setError("Please enter valid company");
            return;
        }

        companyTextInputLayout.setError(null);


        if (password.length() == 0){
            passwordTextInputLayout.setError("Please enter valid password");
            return;
        }

        passwordTextInputLayout.setError(null);


        if (biography.length() == 0){
            biographyInputLayout.setError("Please enter valid biography");
            return;
        }

        biographyInputLayout.setError(null);


        if (uri == null || uri.toString().length() == 0){
            Toast.makeText(this, "Please choose an image !", Toast.LENGTH_SHORT).show();
            return;
        }



        try {
            showLoadingDialog();
            submitCall(name, email, location, company, password, biography, uri.getPath());
        } catch (final Exception x){
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


    private void submitCall(String name, String email, String location, String company, String  password, String  biography, String imagePath){


        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("submit", "submit")
                .addFormDataPart("user_id", String.valueOf(UserAppStateManager.getInstance().getUserID()))
                .addFormDataPart("name_id", name)
                .addFormDataPart("email_id", email)
                .addFormDataPart("loc_id", location)
                .addFormDataPart("company_id", company)
                .addFormDataPart("password_id", password )
                .addFormDataPart("biography_id", biography )
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
                .build();




        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceEditProfileUrl())
                .post(requestBody)
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
                                Toast.makeText(EditProfileActivity.this, "Form submitted", Toast.LENGTH_LONG).show();


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




    private void clearEntries(){

        nameInputEditText.setText(null);
        emailInputEditText.setText(null);
        locationInputEditText.setText(null);
        companyInputEditText.setText(null);
        passwordInputEditText.setText(null);
        biographyInputEditText.setText(null);
        uri = null;
        Glide.with(this).load(R.drawable.gradient).into(profileImage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setUpToolBar();
        setUpUI();
        setUpListeners();
        buildErrorDialog();
        buildLoadingDialog();
        setUpOkHttpClient();
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



    private void setUpListeners(){
        
        submitActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        

        profileImageSelectActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity(null);
            }
        });

    }


    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Edit Profile");
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



    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setMultiTouchEnabled(true)
                .setActivityTitle("Set Profile Picture")
                .setInitialCropWindowPaddingRatio(0)
                .start(this);
    }

    private void saveFile(Uri sourceuri) {

        String sourceFilename = sourceuri.getPath();
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "profile.jpg";


        File file = new File(destinationFilename);
        if (file.exists()) {
            file.delete();
            Log.wtf("Its deleted", "yes");
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addImageToGallery(destinationFilename, this);
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                Glide.with(this).load(uri).into(profileImage);
                requestPermission(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (uri != null) {
                saveFile(uri);
            }
        }
    }

    private void requestPermission(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            saveFile(uri);
        }
    }


    private void buildLoadingDialog(){
        submittingDialog = new MaterialDialog.Builder(this)
                .title(R.string.submitting)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();
    }


    private void showLoadingDialog(){
        if (!submittingDialog.isShowing()){
            submittingDialog.show();
        }

    }

    private void  hideLoadingDialog(){
        if (submittingDialog.isShowing()){
            submittingDialog.dismiss();
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


