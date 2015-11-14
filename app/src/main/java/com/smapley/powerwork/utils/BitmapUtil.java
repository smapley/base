package com.smapley.powerwork.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.crypto.spec.OAEPParameterSpec;

/**
 * Created by smapley on 15/10/23.
 */
public class BitmapUtil {

    public static int caculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        //原图片的高度和宽度
        final int height = options.outHeight;
        final int wight=options.outWidth;
        int inSampleSize=1;
        if(height>reqHeight||wight>reqWidth){
            //计算出实际宽高和目标宽高的比率
            final int heightRatio=Math.round((float)height/(float)reqHeight);
            final int widthRatio=Math.round((float)wight/(float)reqWidth);
            //选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高
            inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据指定比例缩放图片
     */
    public static Bitmap decodeSampledBitmap(FileDescriptor fileDescriptor,int reqWidth,int reqHeight){
        //第一次解析inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options =new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        //调用上面定义的方法计算inSamplesize值
        options.inSampleSize=caculateInSampleSize(options,reqWidth,reqHeight);
        //使用获取到的inSampleSize值在此解析图片
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }

    public static Bitmap decodeSampledBitmap(InputStream inputStream,int reqWidth,int reqHeight) throws FileNotFoundException{
        //第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeStream(inputStream,null,options);
        //调用上面定义的计算inSampleSize值
        options.inSampleSize=caculateInSampleSize(options,reqWidth,reqHeight);
        //使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeStream(inputStream,null,options);
    }
}
