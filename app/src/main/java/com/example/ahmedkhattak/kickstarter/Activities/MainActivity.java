package com.example.ahmedkhattak.kickstarter.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmedkhattak.kickstarter.AppModels.Category;
import com.example.ahmedkhattak.kickstarter.Fragments.NotificationFragment;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnFragmentInteractionListener;
import com.example.ahmedkhattak.kickstarter.Interfaces.OnToolBarChanged;
import com.example.ahmedkhattak.kickstarter.R;
import com.example.ahmedkhattak.kickstarter.Fragments.ExploreFragment;
import com.example.ahmedkhattak.kickstarter.Fragments.HomeFragment;
import com.example.ahmedkhattak.kickstarter.Fragments.ProfileFragment;
import com.example.ahmedkhattak.kickstarter.Fragments.StartProjectFragment;
import com.example.ahmedkhattak.kickstarter.UserAppStateManager;
import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener<Object>, OnToolBarChanged {

    Drawer navDrawer = null;
    Toolbar toolbar = null;
    String lastShownFragmentTag = null;
    private static final String TAG = "MainActivity";
    private boolean isMenuShowing = false;
    private boolean exit = false;

    // Fragments
    HomeFragment homeFragment = null;
    ProfileFragment profileFragment = null;
    ExploreFragment exploreFragment = null;
    StartProjectFragment startProjectFragment = null;

    MaterialDialog logoutDialog;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar();
        setUpNavDrawer();
        setUpLogoutDialog();
        DisplayFragment("HomeFragment", false, false);
    }


    private void setUpNavDrawer() {


        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withSelectionListEnabledForSingleProfile(false)
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav )
                .withAlternativeProfileHeaderSwitching(false)
                .build();



        PrimaryDrawerItem homeDrawerItem = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(0);
        PrimaryDrawerItem exploreDrawerItem = new PrimaryDrawerItem().withIdentifier(1).withName("Explore").withIcon(GoogleMaterial.Icon.gmd_search).withIdentifier(1);
        PrimaryDrawerItem profileDrawerItem = new PrimaryDrawerItem().withIdentifier(1).withName("Profile").withIcon(GoogleMaterial.Icon.gmd_person).withIdentifier(2);
        PrimaryDrawerItem startProjectDrawerItem = new PrimaryDrawerItem().withIdentifier(1).withName("Start project").withIcon(GoogleMaterial.Icon.gmd_edit).withIdentifier(3);
        PrimaryDrawerItem NotificationDrawerItem  = new PrimaryDrawerItem().withIdentifier(1).withName("Notification").withIcon(GoogleMaterial.Icon.gmd_notifications).withIdentifier(4);
        PrimaryDrawerItem searchDrawerItem  = new PrimaryDrawerItem().withIdentifier(1).withName("Search").withIcon(GoogleMaterial.Icon.gmd_search).withIdentifier(4);
        PrimaryDrawerItem logoutDrawerItem = new PrimaryDrawerItem().withIdentifier(1).withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(5);
        /*

        TODO is function me tu drawer item dalega note kari ke jo divider item hai wo bhi count hota jab neechay switch me jao ge
        aur jo image hai drawer pe wo bhi count hota is liyay swtich me count 0 se nae blke 1 se shuro hai is ka khyal rakhi

         */


        navDrawer = new DrawerBuilder().withActivity(this)
                .addDrawerItems(
                        homeDrawerItem,
                        profileDrawerItem,
                        exploreDrawerItem,
                        startProjectDrawerItem,
                        NotificationDrawerItem,
                        searchDrawerItem ,
                        new DividerDrawerItem(),
                        logoutDrawerItem
                )
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeader)
                .withSelectedItem(-1).withDelayOnDrawerClose(-1)
                .withDelayDrawerClickEvent(400)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        switch (position) {

                            // Home
                            case 1:
                                DisplayFragment("HomeFragment", false, true);
                                break;

                            // Profile
                            case 2:
                                DisplayFragment("ProfileFragment", false, true);
                                break;

                            // Explore categories and projects
                            case 3:
                                DisplayFragment("ExploreFragment", false, true);
                                break;

                            case 4:
                                DisplayFragment("StartProjectFragment", false, true);
                                break;

                            case 5:
                                DisplayFragment("NotificationFragment",false,true);
                                break;
                                
                            case 6:
                                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                                startActivity(i);

                                break;
                            /*
                            * TODO ider tu check krega click ko aur diplayfragment call karge
                            *
                            *
                            * */

                            //  Logout
                            case 8:
                                showLogoutDialog();
                                break;

                            default:
                                break;
                        }

                        return false;
                    }
                })
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        navDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);


    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void DisplayFragment(String fragmentTag, boolean shouldAddToBackStack, boolean isAnimated) {
        Fragment fragment = null;

        switch (fragmentTag) {

            /*
            * TODO is function me tune case banana hai pele string ider aur call ke doran match honi chaiyay
            * baqi neechay logic cherni ki zaroorat nae hai
            *
            * */


            case "HomeFragment":
                fragment = new HomeFragment();
                break;

            case "ExploreFragment":
                fragment = new ExploreFragment();
                break;

            case "ProfileFragment":
                fragment = new ProfileFragment();
                break;

            case "StartProjectFragment":
                fragment = new StartProjectFragment();
                break;

            case "NotificationFragment":
                fragment = new NotificationFragment();
                break;

            default:
                break;

        }


        if (fragment == null) {
            return;
        }

        if (isFragmentCurrentlyVisible(fragmentTag)) {
            return;
        }


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        if (isAnimated) {
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }


        // if there is a visible fragment hide it
        if (lastShownFragmentTag != null) {
            // hide last shown frag
            fragmentTransaction.hide(getSupportFragmentManager().findFragmentByTag(lastShownFragmentTag));
        }

        // show new frag that was clicked if it exists in memory else we add and then show
        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) != null) {
            fragmentTransaction.show(getSupportFragmentManager().findFragmentByTag(fragmentTag));
        } else {
            // add new frag
            fragmentTransaction.add(R.id.fragmentContainer, fragment, fragmentTag);
            fragmentTransaction.show(fragment);


        }


        if (shouldAddToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }

        lastShownFragmentTag = fragmentTag;


        fragmentTransaction.commit();


    }


    private boolean isFragmentCurrentlyVisible(String fragmentTag) {
        Fragment fragmentToTest;
        fragmentToTest = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragmentToTest != null && fragmentToTest.isVisible()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {

        if (exit)
            finish();
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    private void setUpLogoutDialog() {

        logoutDialog = new MaterialDialog.Builder(this)
                .title(R.string.logout)
                .content(R.string.logout_content)
                .negativeText("Cancel")
                .positiveText("Logout")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        Toast.makeText(MainActivity.this, "You are now logged out.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .build();

    }


    private void showLogoutDialog() {
        if (!logoutDialog.isShowing()) {
            logoutDialog.show();
        }
    }


    @Override
    public void onFragmentInteraction(String tag, Object data) {
        if (data.toString().equals("ChooseImage") && tag.equals("StartProjectFragment")) {
            startCropImageActivity(null);
        } else if (data != null && data instanceof Category && tag.equals("ExploreFragment")) {
            Category item = (Category) data;

            if (item.getCount().equals("nope")){


                Intent i = new Intent(this,ProjectTypeActivity.class);
                i.putExtra("type", item.getCategoryId());
                i.putExtra("title", item.getCategoryName());
                startActivity(i);

            } else {

                Intent i = new Intent(this,IndividualCategoryActivity.class);
                i.putExtra("id", item.getCategoryId());
                i.putExtra("title", item.getCategoryName());
                startActivity(i);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (mCropImageUri != null) {
                saveFile(mCropImageUri);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                UserAppStateManager.getInstance().setImageUri(result.getUri());
                mCropImageUri = result.getUri();
                requestPermission(result.getUri());

                // send uri to fragment

                // find fragment to send to
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment.getTag().equals("StartProjectFragment") && fragment instanceof StartProjectFragment) {
                        StartProjectFragment frag = (StartProjectFragment) fragment;
                        frag.setURI(mCropImageUri);
                    }
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
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
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    @Override
    public void onToolBarChanged(String tag, Object data, ToolbarOperations operations) {
        switch (operations) {
            case TitleChange:
                if (data instanceof String) {
                    String title = (String) data;
                    if (toolbar != null) {
                        toolbar.setTitle(title);
                    }
                    isMenuShowing = false;
                    invalidateOptionsMenu();
                }
                break;

            case CreateMenu:
                isMenuShowing = true;
                invalidateOptionsMenu();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if (!isMenuShowing) {
            return false;
        } else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_menu:
                Intent i = new Intent(this,EditProfileActivity.class);
                startActivity(i);
                break;

            case R.id.edit_bankinfo:
                Intent ii = new Intent(this,EditBankInfoActivity.class);
                startActivity(ii);
                break;

            default:
                break;
        }

        return true;
    }
}


