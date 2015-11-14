package com.smapley.powerwork.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.smapley.powerwork.utils.MyData;
import com.smapley.powerwork.exception.BaseExceptionHandler;
import com.smapley.powerwork.exception.LocalFileHandler;

/**
 * Created by smapley on 15/10/22.
 */
public abstract class BaseApplication extends Application {
    public static final String TAG = "Application";
    public static Context applicationContext;

    //以键值对的形式存储用户名和密码
    public SharedPreferences sp_user;
    public SharedPreferences sp_set;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        if(getDefaultUncaughtExceptionHandler()==null){
            Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(applicationContext));
        }else{
            Thread.setDefaultUncaughtExceptionHandler(getDefaultUncaughtExceptionHandler());
        }

        //初始化键值对存储
        sp_user = getSharedPreferences(MyData.SP_USER, MODE_PRIVATE);
        sp_set = getSharedPreferences(MyData.SP_SET, MODE_PRIVATE);
    }

    public abstract BaseExceptionHandler getDefaultUncaughtExceptionHandler();

}
