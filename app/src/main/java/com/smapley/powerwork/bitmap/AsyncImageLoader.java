package com.smapley.powerwork.bitmap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.cache.LruDiskCache;
import com.lidroid.xutils.cache.LruMemoryCache;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by smapley on 15/10/22.
 * <p/>
 * 异步加载图片工具类
 */
public class AsyncImageLoader {

    private static AsyncImageLoader imageLoader;
    private Context context;

    //异步任务执行者
    private Executor executor;
    //加载任务的集合
    public Set<BitmapWorkerTask> taskCollection;
    //内存缓存
    public LruMemoryCache memoryCache;
    //硬盘缓存
    private File cacheDir;
    public LruDiskCache diskCache;

    //加载中显示图片
    public Bitmap loadingBitmap;
    //加载失败显示的图片
    public Bitmap loadfailBitmap;

    public static AsyncImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            imageLoader = new AsyncImageLoader(context);
        }
        return imageLoader;
    }

    private AsyncImageLoader(Context context) {
        this.context = context;
        //初始化线程池 核心线程3个 线程池大小为 200
        executor = new ThreadPoolExecutor(3, 200, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        //初始化任务集合
        taskCollection = new HashSet<BitmapWorkerTask>();

        //获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        //设置内存缓存大小为程序最大可用内存的1/8
        memoryCache = new LruMemoryCache(cacheSize);

        try {
            //获取图片缓存路径
            cacheDir= getDiskCacheDir(context,"bitmap");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            //创建LruDiskCache实例，初始化硬盘缓存
            diskCache = LruDiskCache.open(cacheDir,getAppVersion(context),1,10*1024*1024);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadBitmaps(View view, ImageView imageView, String imageUrl){
        loadBitmaps(view,imageView,imageUrl,0,0);
    }

    /**
     * 加载bitmap对象 此方法会在memoryCache中查找制定url地址的bitmap对象，
     * 如果返回null就会开启异步线程去硬盘缓存中查找
     * 如果还是返回null就回去重新下载图片
     */
    public void loadBitmaps(View view, ImageView imageView, String imageUrl,int reqWidth,int reqHeight) {
        if (imageView != null && loadingBitmap != null) {
            //设置加载中图片
            imageView.setImageBitmap(loadingBitmap);
        }
        try {
            Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
            if (bitmap == null) {
                BitmapWorkerTask task = new BitmapWorkerTask(imageLoader, view,reqWidth,reqHeight);
                taskCollection.add(task);
                task.executeOnExecutor(executor, imageUrl);
            } else {
                if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置加载中显示图片
     */
    public void setloadingDrawable(int resourceId) {
        loadingBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    /**
     * 设置加载失败显示图片
     */
    public void setLoadFailDrawable(int resourceId) {
        loadfailBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
    }


    /**
     * 将图片添加到memoryCache缓存中
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    /**
     * 从memoryCache中获取一种图片，如果不存在就返回null
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return (Bitmap) memoryCache.get(key);
    }

    /**
     * 使用MD5算法对传入的可以进行加密并返回，避免url命名文件时存在不合法字符
     */
    public String hashKeyForDisk(String key){
        String cacheKey;
        try{
            final MessageDigest mDigest=MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey=bytesToHexString(mDigest.digest());
        }catch (NoSuchAlgorithmException e){
            cacheKey=String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     *
     */
    private String bytesToHexString(byte[] bytes){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<bytes.length;i++){
            String hex=Integer.toHexString(0xFF & bytes[i]);
            if(hex.length()==1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     */
    public File getDiskCacheDir(Context context ,String uniqueName){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageEmulated()){
            cachePath=context.getExternalCacheDir().getPath();
        }else{
            cachePath=context.getCacheDir().getPath();
        }
        return new File(cachePath+File.separator+uniqueName);
    }

    /**
     * 获取当亲应用程序的版本号
     */
    public int getAppVersion(Context context){
        try{
            PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *返回当前缓存文件大小，以byte为单位
     */
    public long getCacheSize(){
        if(diskCache!=null){
            return diskCache.size();
        }
        return 0;
    }

    /**
     * 将缓存记录同步到journal文件中
     */
    public void fluchCache(){
        if(diskCache!=null){
            try{
                diskCache.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        if(diskCache !=null){
            try{
                diskCache.delete();
                //回复LruDiskCache实例，初始化硬盘缓存
                diskCache=LruDiskCache.open(cacheDir,getAppVersion(context),1,20*1024*1024);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭缓存
     */
    public void closeCache(){
        if(diskCache!=null){
            try{
                diskCache.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }
}
