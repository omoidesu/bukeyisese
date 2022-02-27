package com.example.okhttpdemo;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.request.RequestOptions;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static RequestOptions getGlideOptions() {
        RequestOptions options = new RequestOptions();
        options.centerCrop().error(R.drawable.ic_baseline_image_128);
        return options;
    }

    public static RequestOptions getBackgroundOptions(){
        RequestOptions options = new RequestOptions();
        return options;
    }
}
