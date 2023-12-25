package com.example.bookingapp.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;

public class TokenManager {

    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_JWT_TOKEN = "jwtToken";

    private SharedPreferences preferences;
    private static User loggedInUser;
    public static String jwt;



    public TokenManager(){

    }
    public static User getLoggedInUser(){
        return TokenManager.loggedInUser;
    }

    public static  void setLoggedInUser(User user){
        TokenManager.loggedInUser=user;

    }
    public static void setJwtToken(String jwtToken) {
        TokenManager.jwt = jwtToken;
    }
    public static String getJwtToken() {
        return TokenManager.jwt;
    }



//    public void setLoggedInUser(User loggedInUser){
//        this.loggedInUser=loggedInUser;
//    }



//    public void setJwt(String jwtToken) {
//
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_JWT_TOKEN, jwtToken);
//        editor.apply();
//    }

//    public String getJwt() {
//        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
//        return preferences.getString(KEY_JWT_TOKEN, null);
//    }
//
//    public void clearToken() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(KEY_JWT_TOKEN);
//        editor.apply();
//    }
}
