package com.smapley.powerwork.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.DiscretePathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.smapley.powerwork.application.LocalApplication;
import com.smapley.powerwork.bitmap.AsyncImageLoader;
import com.smapley.powerwork.utils.ActivityStack;

import java.security.KeyStore;

/**
 * Created by smapley on 15/10/22.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean isCreate = false;
    protected HttpUtils httpUtils;
    protected DbUtils dbUtils;
    protected SharedPreferences sp_user;
    protected SharedPreferences sp_set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().addActivity(this);
        ViewUtils.inject(this);
        httpUtils = LocalApplication.getInstance().httpUtils;
        dbUtils = LocalApplication.getInstance().dbUtils;
        sp_user = LocalApplication.getInstance().sp_user;
        sp_set = LocalApplication.getInstance().sp_set;
        isCreate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCreate) {
            isCreate = false;
            initParams();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //将内存中的操作记录同步到日志文件中
        AsyncImageLoader.getInstance(this).fluchCache();
    }

    @Override
    protected void onDestroy() {
        //Activity堆栈管理
        ActivityStack.getInstance().removeActivity(this);
        //结束加载任务
        AsyncImageLoader.getInstance(this).cancelAllTasks();
        super.onDestroy();
    }

    /**
     * 参数设置
     */
    protected abstract void initParams();


    protected void showToast(int data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
    protected void showToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

}
