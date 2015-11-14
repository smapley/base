package com.smapley.powerwork.exception;

import android.os.*;
import android.text.AndroidCharacter;

import java.text.DateFormat;

/**
 * Created by smapley on 15/10/22.
 */
public abstract class BaseExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG="CrashHandler";
    protected static final DateFormat dateFoemat=DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL);
    /**
     * 未捕获异常跳转
     */


    public void uncaughtException(Thread thread,final Throwable ex){

        //如果正确处理了未捕获异常
        if(handleException(ex)){
            try{
                //如果处理了，让程序继续运行3秒再推出，保证错误日志的保存
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    /**
     * 自定义错误处理，收集错误信息 发送错误报告等操作均在此完成
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     */
    public abstract boolean handleException(Throwable ex);

}
