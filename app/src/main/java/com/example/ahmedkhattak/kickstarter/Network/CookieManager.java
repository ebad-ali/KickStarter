package com.example.ahmedkhattak.kickstarter.Network;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

/**
 * Created by Ahmed Khattak on 26/11/2017.
 */

public class CookieManager {
    private static final CookieManager ourInstance = new CookieManager();

    public static CookieManager getInstance() {
        return ourInstance;
    }

    private CookieManager() {
    }

    ClearableCookieJar cookieJar;

    public ClearableCookieJar getCookieJar() {
        return cookieJar;
    }

    public void setCookieJar(ClearableCookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }






}
