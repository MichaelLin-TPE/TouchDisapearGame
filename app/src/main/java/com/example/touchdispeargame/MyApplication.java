package com.example.touchdispeargame;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication instance = null;

    public static MyApplication getInstance(){
        if (instance == null){
            instance = new MyApplication();
            return instance;
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
