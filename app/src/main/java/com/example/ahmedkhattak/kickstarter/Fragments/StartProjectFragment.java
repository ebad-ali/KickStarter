package com.example.ahmedkhattak.kickstarter.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.ahmedkhattak.kickstarter.AppModels.Category;
import com.example.ahmedkhattak.kickstarter.AppModels.Explore;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnFragmentInteractionListener;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnToolBarChanged;
import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class StartProjectFragment extends Fragment {

    private static final String TAG = "StartProjectFragment";

    private static Boolean isAleadyOn = false;

    TextInputLayout titleTextInputLayout;
    TextInputLayout locationTextInputLayout;
    TextInputLayout companyTextInputLayout;
    TextInputLayout fundTargetTextInputLayout;
    TextInputLayout descriptionTextInputLayout;
    TextInputLayout videoTextInputLayout;

    AppCompatSpinner categoryInputSpinner;
    AppCompatEditText titleInputEditText;
    AppCompatEditText locationInputEditText;
    AppCompatEditText companyInputEditText;
    AppCompatEditText fundTargetInputEditText;
    AppCompatEditText descriptionInputEditText;
    AppCompatEditText videoInputEditText;

    AppCompatButton submitActionButton;
    AppCompatButton projectImageSelectActionButton;
    AppCompatImageView projectImage;
    ScrollView scrollView;


    private String title;
    private String category_;
    private String location;
    private String company;
    private String fundTarget;
    private String description;
    private String video;
    private Uri uri;


    private OkHttpClient client;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    List<Category> categoryList;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");


    private OnFragmentInteractionListener<Object> onFragmentInteractionListener = null;
    private OnToolBarChanged onToolBarChanged = null;

    MaterialDialog submittingDialog;
    MaterialDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUI();
        setUpErrorText();
        setUpProgressBar();
        setUpSwipeRefresh();
        setUpOkHttpClient();
        buildErrorDialog();
        buildSubmittingDialog();
        setUpListeners();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener<Object>) context;

        }

        if (context instanceof OnToolBarChanged) {
            onToolBarChanged = (OnToolBarChanged) context;
            changeToolBarTitle("Start a project");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            changeToolBarTitle("Start a project");
        }
    }


    private void changeToolBarTitle(String title) {
        if (onToolBarChanged != null) {
            onToolBarChanged.onToolBarChanged(TAG, title, ToolbarOperations.TitleChange);
        }
    }


    private void setUpUI() {

        titleTextInputLayout = getView().findViewById(R.id.titleTextInputLayout);
        locationTextInputLayout = getView().findViewById(R.id.locationTextInputLayout);
        companyTextInputLayout = getView().findViewById(R.id.companyTextInputLayout);
        fundTargetTextInputLayout = getView().findViewById(R.id.fundTargetTextInputLayout);
        descriptionTextInputLayout = getView().findViewById(R.id.descriptionTextInputLayout);
        videoTextInputLayout = getView().findViewById(R.id.videoTextInputLayout);

        categoryInputSpinner = getView().findViewById(R.id.categoryInputSpinner);

        titleInputEditText = getView().findViewById(R.id.titleInputEditText);
        locationInputEditText = getView().findViewById(R.id.locationInputEditText);
        companyInputEditText = getView().findViewById(R.id.companyInputEditText);
        fundTargetInputEditText = getView().findViewById(R.id.fundTargetInputEditText);
        descriptionInputEditText = getView().findViewById(R.id.descriptionInputEditText);
        videoInputEditText = getView().findViewById(R.id.videoInputEditText);
        projectImage = getView().findViewById(R.id.projectImage);


        submitActionButton = getView().findViewById(R.id.submitActionButton);
        projectImageSelectActionButton = getView().findViewById(R.id.projectImageSelectActionButton);

        scrollView = getView().findViewById(R.id.scollViewContainer);

    }


    private void setUpListeners() {
        submitActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        projectImageSelectActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentInteractionListener.onFragmentInteraction("StartProjectFragment", "ChooseImage");
            }
        });
    }

    private void submit() {


        title = titleInputEditText.getText().toString();
        category_ = categoryInputSpinner.getSelectedItem().toString();
        location = locationInputEditText.getText().toString();
        company = companyInputEditText.getText().toString();
        fundTarget = fundTargetInputEditText.getText().toString();
        description = descriptionInputEditText.getText().toString();
        video = videoInputEditText.getText().toString();


        if (title.length() == 0) {
            titleTextInputLayout.setError("Please enter valid title !");
            return;
        }

        titleTextInputLayout.setError(null);


        if (category_.length() == 0) {
            Toast.makeText(getContext(), "Please select valid category", Toast.LENGTH_SHORT).show();
            return;
        }


        if (location.length() == 0) {
            locationTextInputLayout.setError("Please enter valid location !");
            return;
        }

        locationTextInputLayout.setError(null);

        if (company.length() == 0) {
            companyTextInputLayout.setError("Please enter valid company !");
            return;
        }

        companyTextInputLayout.setError(null);


        if (fundTarget.length() == 0) {
            fundTargetTextInputLayout.setError("Please enter valid Fund target !");
            return;
        }

        fundTargetTextInputLayout.setError(null);


        if (video.length() == 0) {
            videoTextInputLayout.setError("Please enter valid video !");
            return;
        }

        videoTextInputLayout.setError(null);


        if (description.length() == 0) {
            descriptionTextInputLayout.setError("Please enter valid description !");
            return;
        }


        descriptionTextInputLayout.setError(null);


        if (uri == null || uri.toString().length() == 0) {
            Toast.makeText(getContext(), "Please choose an image !", Toast.LENGTH_SHORT).show();
            return;
        }

        showSubittingDialog();
        submitClientCall(title, category_, location, company, description, fundTarget, video, uri.getPath());


    }


    private void submitClientCall(String title, String category, String location, String company,
                                  String description, String fund, String video, String imagePath) {

        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("submit", "submit")
                .addFormDataPart("title_id", title)
                .addFormDataPart("cat_id", category)
                .addFormDataPart("loc_id", location)
                .addFormDataPart("company_id", company)
                .addFormDataPart("desc_id", description)
                .addFormDataPart("fund_id", fund)
                .addFormDataPart("video_id", video)
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
                .build();


        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceStartProjectUrl())
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final Exception x = e;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        hideSubmittingDialog();
                        showErrorDialog("Exception", x.getMessage());

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

                                hideSubmittingDialog();
                                showErrorDialog("Exception", "Server error occurred.");

                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }

                    if (responseBody == null) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                hideSubmittingDialog();
                                showErrorDialog("Exception", "Server error occurred.");

                            }
                        });
                        return;
                    }

                    final String result = responseBody.string();


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

                            hideSubmittingDialog();
                            clearFields();

                            Toast.makeText(getContext(), "Form submitted", Toast.LENGTH_SHORT).show();


                        }
                    });


                }
            }
        });
    }

    private void clearFields() {
        titleInputEditText.setText(null);
        locationInputEditText.setText(null);
        companyInputEditText.setText(null);
        fundTargetInputEditText.setText(null);
        descriptionInputEditText.setText(null);
        videoInputEditText.setText(null);
        uri = null;
        Glide.with(getContext()).load(R.drawable.gradient).into(projectImage);
    }


    private void setUpProgressBar() {
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.animate();
    }

    private void setUpSwipeRefresh() {
        swipeRefresh = getView().findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStartProjectClientCall();
            }
        });
    }

    private void setUpErrorText() {
        errorText = getView().findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
    }

    private void setUpOkHttpClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }

    private void setUpSpinnerAdapter() {

        final ArrayList<String> catgoriesArray = new ArrayList<>();
        for (Category category : categoryList) {
            catgoriesArray.add(category.getCategoryName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, catgoriesArray);


        categoryInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), catgoriesArray.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryInputSpinner.setAdapter(spinnerArrayAdapter);
    }


    private void initialStartProjectClientCall() {
        initialExploreClientCall();
    }


    private void refreshStartProjectClientCall() {
        refreshExploreClientCall();
    }


    // Two calls are made in a chain if any one the call fails the whole chain fails only when all calls are successful
    // is the chain successful there are two chains one for the initial call and one for refreshes

    private void initialExploreClientCall() {


        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceExploreUrl())
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

                    Explore explore = gson.fromJson(result, Explore.class);

                    categoryList = explore.getCategories();


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {


                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            setUpSpinnerAdapter();
                            swipeRefresh.setEnabled(false);
                        }
                    });

                }
            }
        });
    }


    private void refreshExploreClientCall() {


        if (swipeRefresh.isEnabled()) {
            swipeRefresh.setRefreshing(true);
        }

        Request request = new Request.Builder()
                .url(NetworkConfig.getInstance().getAppWebServiceExploreUrl())
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

                    Explore explore = gson.fromJson(result, Explore.class);

                    categoryList = explore.getCategories();


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (swipeRefresh.isEnabled()) {
                                swipeRefresh.setRefreshing(false);
                            }
                            progressBar.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            swipeRefresh.setEnabled(false);
                            scrollView.setVisibility(View.VISIBLE);
                            setUpSpinnerAdapter();

                        }
                    });

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isAleadyOn) {
            initialStartProjectClientCall();
            isAleadyOn = true;
        }

    }


    public void setURI(Uri uri) {
        this.uri = uri;
        Glide.with(getContext()).load(uri).into(projectImage);
    }


    private void buildSubmittingDialog() {
        submittingDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.submitting)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();
    }


    private void showSubittingDialog() {
        if (!submittingDialog.isShowing()) {
            submittingDialog.show();
        }

    }

    private void hideSubmittingDialog() {
        if (submittingDialog.isShowing()) {
            submittingDialog.dismiss();
        }
    }


    private void buildErrorDialog() {
        errorDialog = new MaterialDialog.Builder(getContext())
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
