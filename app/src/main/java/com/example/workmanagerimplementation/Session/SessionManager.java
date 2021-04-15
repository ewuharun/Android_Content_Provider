package com.example.workmanagerimplementation.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Md.harun or rashid on 15,April,2021
 * BABL, Bangladesh,
 */
public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    private static String isLoggedIn="isLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("sessionManager",PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void saveLoginSession(String role_code,String employee_id){
        editor.putBoolean(isLoggedIn,true);
        editor.putString("role_code",role_code);
        editor.putString("employee_id",employee_id);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean(isLoggedIn,false);
    }

    public void delteSession(){
        editor.clear();
        editor.commit();
    }
}
