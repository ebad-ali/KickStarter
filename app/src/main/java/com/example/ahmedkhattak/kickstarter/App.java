package com.example.ahmedkhattak.kickstarter;

import android.app.Application;

import com.example.ahmedkhattak.kickstarter.Network.CookieManager;
import com.example.ahmedkhattak.kickstarter.Network.NetworkConfig;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

/**
 * Created by Ahmed Khattak on 26/11/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        NetworkConfig.getInstance().setAppWebServiceBaseUrl("http://ennovayt.com/app/webservice/");
        NetworkConfig.getInstance().setAppWebServiceSignInUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "signin_web.php");
        NetworkConfig.getInstance().setAppWebServiceSignUpUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "signup_web.php");
        NetworkConfig.getInstance().setAppWebServiceHomeUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "home_page_php.php");
        NetworkConfig.getInstance().setAppWebServiceExploreUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "explore_web.php");
        NetworkConfig.getInstance().setAppWebServiceProfileUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "my_profile_web.php");
        NetworkConfig.getInstance().setAppWebServiceStartProjectUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "start_project_web.php");
        NetworkConfig.getInstance().setAppWebServiceEditProfileUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "edit_profile_web.php");
        NetworkConfig.getInstance().setAppWebServiceIndCategoryUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "ind_category_web.php");
        NetworkConfig.getInstance().setAppWebServiceProjectTypeUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "projectType_web.php");
        NetworkConfig.getInstance().setAppWebServiceSingleProjectViewUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "single_project_web.php");
        NetworkConfig.getInstance().setAppWebServiceNotificationUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "notification_web.php");
        NetworkConfig.getInstance().setAppWebServiceIndividualNotificationUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "ind_notifications_web.php");
        NetworkConfig.getInstance().setAppWebServiceBackProjectUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "temp_back_project_web.php");
        NetworkConfig.getInstance().setAppWebServiceBackProjectFundReceivedUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "back_project_web.php");
        NetworkConfig.getInstance().setAppWebServiceRateProjectUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "rating_web.php");

        NetworkConfig.getInstance().setAppWebServiceSearchUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "search_web.php");
        NetworkConfig.getInstance().setAppWebServiceAddBankInfoUrl(NetworkConfig.getInstance().getAppWebServiceBaseUrl() + "add_bank_info_web.php");

        NetworkConfig.getInstance().setImageBaseUrl("http://192.168.100.13/kickstarter/");

        CookieManager.getInstance().setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)));
    }

}
