package com.foodemporium.models;


/**
 * Created by Upendranath on 5/30/2017.
 */

public class AppUserModel {


    private String userName = "";
    private String password = "";
    private String displayName = "";
    private String userIDValue = "";
    private String siteIDValue = "";
    private String siteName = "";
    private String siteURL = "";
    private String webAPIUrl = "";


    private static AppUserModel instance;

    public static synchronized AppUserModel getInstance() {
        if (instance == null) {
            instance = new AppUserModel();
        }
        return instance;
    }

}
