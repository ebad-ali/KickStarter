package com.example.ahmedkhattak.kickstarter.Network;

/**
 * Created by Ahmed Khattak on 26/11/2017.
 */

public class NetworkConfig {
    private static final NetworkConfig ourInstance = new NetworkConfig();

    public static NetworkConfig getInstance() {
        return ourInstance;
    }

    private NetworkConfig() {
    }


    private String appWebServiceSignInUrl;
    private String appWebServiceSignUpUrl;


    private String appWebServiceBaseUrl;
    private String appWebServiceHomeUrl;
    private String appWebServiceExploreUrl;
    private String appWebServiceProfileUrl;
    private String appWebServiceEditProfileUrl;
    private String appWebServiceStartProjectUrl;
    private String appWebServiceIndCategoryUrl;
    private String appWebServiceProjectTypeUrl;
    private String appWebServiceSingleProjectViewUrl;
    private String appWebServiceNotificationUrl;
    private String appWebServiceIndividualNotificationUrl;
    private String appWebServiceBackProjectUrl;
    private String appWebServiceBackProjectFundReceivedUrl;
    private String appWebServiceRateProjectUrl;

    public String getAppWebServiceSearchUrl() {
        return appWebServiceSearchUrl;
    }

    public void setAppWebServiceSearchUrl(String appWebServiceSearchUrl) {
        this.appWebServiceSearchUrl = appWebServiceSearchUrl;
    }

    private String appWebServiceSearchUrl;

    public String getAppWebServiceAddBankInfoUrl() {
        return appWebServiceAddBankInfoUrl;
    }

    public void setAppWebServiceAddBankInfoUrl(String appWebServiceAddBankInfoUrl) {
        this.appWebServiceAddBankInfoUrl = appWebServiceAddBankInfoUrl;
    }

    private String  appWebServiceAddBankInfoUrl;

    public String getAppWebServiceRateProjectUrl() {
        return appWebServiceRateProjectUrl;
    }

    public void setAppWebServiceRateProjectUrl(String appWebServiceRateProjectUrl) {
        this.appWebServiceRateProjectUrl = appWebServiceRateProjectUrl;
    }

    public String getAppWebServiceBackProjectFundReceivedUrl() {
        return appWebServiceBackProjectFundReceivedUrl;
    }

    public void setAppWebServiceBackProjectFundReceivedUrl(String appWebServiceBackProjectFundReceivedUrl) {
        this.appWebServiceBackProjectFundReceivedUrl = appWebServiceBackProjectFundReceivedUrl;
    }

    public String getAppWebServiceBackProjectUrl() {
        return appWebServiceBackProjectUrl;
    }

    public void setAppWebServiceBackProjectUrl(String appWebServiceBackProjectUrl) {
        this.appWebServiceBackProjectUrl = appWebServiceBackProjectUrl;
    }

    public String getAppWebServiceIndividualNotificationUrl() {
        return appWebServiceIndividualNotificationUrl;
    }

    public void setAppWebServiceIndividualNotificationUrl(String appWebServiceIndividualNotificationUrl) {
        this.appWebServiceIndividualNotificationUrl = appWebServiceIndividualNotificationUrl;
    }

    private String imageBaseUrl;


    public String getAppWebServiceNotificationUrl() {
        return appWebServiceNotificationUrl;
    }

    public void setAppWebServiceNotificationUrl(String appWebServiceNotificationUrl) {
        this.appWebServiceNotificationUrl = appWebServiceNotificationUrl;
    }

    public String getAppWebServiceSingleProjectViewUrl() {
        return appWebServiceSingleProjectViewUrl;
    }

    public void setAppWebServiceSingleProjectViewUrl(String appWebServiceSingleProjectViewUrl) {
        this.appWebServiceSingleProjectViewUrl = appWebServiceSingleProjectViewUrl;
    }



    public String getAppWebServiceProjectTypeUrl() {
        return appWebServiceProjectTypeUrl;
    }

    public void setAppWebServiceProjectTypeUrl(String appWebServiceProjectTypeUrl) {
        this.appWebServiceProjectTypeUrl = appWebServiceProjectTypeUrl;
    }




    public String getAppWebServiceIndCategoryUrl() {
        return appWebServiceIndCategoryUrl;
    }

    public void setAppWebServiceIndCategoryUrl(String appWebServiceIndCategoryUrl) {
        this.appWebServiceIndCategoryUrl = appWebServiceIndCategoryUrl;
    }











    public String getAppWebServiceEditProfileUrl() {
        return appWebServiceEditProfileUrl;
    }

    public void setAppWebServiceEditProfileUrl(String appWebServiceEditProfileUrl) {
        this.appWebServiceEditProfileUrl = appWebServiceEditProfileUrl;
    }



    public String getAppWebServiceStartProjectUrl() {
        return appWebServiceStartProjectUrl;
    }

    public void setAppWebServiceStartProjectUrl(String appWebServiceStartProjectUrl) {
        this.appWebServiceStartProjectUrl = appWebServiceStartProjectUrl;
    }




    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }




    public String getAppWebServiceBaseUrl() {
        return appWebServiceBaseUrl;
    }

    public void setAppWebServiceBaseUrl(String appWebServiceBaseUrl) {
        this.appWebServiceBaseUrl = appWebServiceBaseUrl;
    }

    public String getAppWebServiceHomeUrl() {
        return appWebServiceHomeUrl;
    }

    public void setAppWebServiceHomeUrl(String appWebServiceHomeUrl) {
        this.appWebServiceHomeUrl = appWebServiceHomeUrl;
    }

    public String getAppWebServiceExploreUrl() {
        return appWebServiceExploreUrl;
    }

    public void setAppWebServiceExploreUrl(String appWebServiceExploreUrl) {
        this.appWebServiceExploreUrl = appWebServiceExploreUrl;
    }

    public String getAppWebServiceProfileUrl() {
        return appWebServiceProfileUrl;
    }

    public void setAppWebServiceProfileUrl(String appWebServiceProfileUrl) {
        this.appWebServiceProfileUrl = appWebServiceProfileUrl;
    }

    public String getAppWebServiceSignUpUrl() {
        return appWebServiceSignUpUrl;
    }

    public void setAppWebServiceSignUpUrl(String appWebServiceSignUpUrl) {
        this.appWebServiceSignUpUrl = appWebServiceSignUpUrl;
    }


    public String getAppWebServiceSignInUrl() {
        return appWebServiceSignInUrl;
    }

    public void setAppWebServiceSignInUrl(String appWebServiceSignInUrl) {
        this.appWebServiceSignInUrl = appWebServiceSignInUrl;
    }


}
