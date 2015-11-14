package com.smapley.powerwork.utils;

import android.content.Context;
import android.os.Environment;

/**
 * Created by smapley on 15/10/22.
 */
public class JFileKit {


    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     */
    public static String getDiskCacheDir(Context context){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||Environment.isExternalStorageEmulated()){
            cachePath=context.getExternalCacheDir().getAbsolutePath();
        }else{
            cachePath=context.getCacheDir().getAbsolutePath();
        }

        return cachePath;
    }

}
