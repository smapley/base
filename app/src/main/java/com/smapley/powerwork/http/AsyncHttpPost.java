package com.smapley.powerwork.http;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;

/**
 * Created by smapley on 15/10/24.
 */
public class AsyncHttpPost {
    //线程通信
    private Handler handler;

    //访问路径
    private String url="";

    //请求参数
    private RequestParams params=new RequestParams();

    public AsyncHttpPost(Handler handler){
        this.handler=handler;
    }
}
