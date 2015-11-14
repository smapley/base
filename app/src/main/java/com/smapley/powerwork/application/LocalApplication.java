package com.smapley.powerwork.application;

import android.location.Location;
import android.util.DisplayMetrics;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.smapley.powerwork.exception.BaseExceptionHandler;
import com.smapley.powerwork.exception.LocalFileHandler;
import com.smapley.powerwork.utils.JFileKit;

import java.io.File;

/**
 * Created by smapley on 15/10/22.
 */
public class LocalApplication extends BaseApplication {

    private static LocalApplication instance;

    public DbUtils dbUtils=null;
    public HttpUtils httpUtils=null;

    //当前屏幕的高宽
    public int screenW=0;
    public int screenH=0;

    //单例模式中获取唯一的MyApplication实例
    public static LocalApplication getInstance(){
        if(instance==null){
            instance=new LocalApplication();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化数据库
        dbUtils=DbUtils.create(this);

        //初始化网络模块
        httpUtils=new HttpUtils();

        //创建app崩溃日志记录
        File appFolder=new File(JFileKit.getDiskCacheDir(this)+"/log");
        if(!appFolder.exists()){
            appFolder.mkdirs();
        }

        instance=this;

        //得到屏幕的宽度和高度
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenW=displayMetrics.widthPixels;
        screenH=displayMetrics.heightPixels;
    }

    @Override
    public BaseExceptionHandler getDefaultUncaughtExceptionHandler() {
        return new LocalFileHandler(applicationContext);
    }
}
