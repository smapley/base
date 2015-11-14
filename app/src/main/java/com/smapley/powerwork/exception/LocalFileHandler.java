package com.smapley.powerwork.exception;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.smapley.powerwork.utils.JFileKit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * Created by smapley on 15/10/22.
 */
public class LocalFileHandler extends BaseExceptionHandler{

    private Context context;
    public LocalFileHandler(Context context){
        this.context=context;
    }

    /**
     * 自定义错误处理，手机错误信息，发送错误报告等操作均在此完成，
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     */
    @Override
    public boolean handleException(Throwable ex) {
        if(ex==null){
            return false;
        }

        new Thread(){
            public void run(){
                Looper.prepare();
                Toast.makeText(context,"很抱歉，程序出现异常，正在从错误中恢复",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        //保存日志
        saveLog(ex);
        return true;
    }

    //保存错误日志
    public void saveLog(Throwable ex){
        try{
            File errFile=new File(JFileKit.getDiskCacheDir(context)+"/log/crash.log");
            if(!errFile.exists()){
                errFile.createNewFile();
            }

            OutputStream outputStream = new FileOutputStream(errFile,true);

            outputStream.write(("\n\n------错误分割线"+dateFoemat.format(new Date())+"------\n\n").getBytes());

            PrintStream stream=new PrintStream(outputStream);

            ex.printStackTrace(stream);

            stream.flush();

            outputStream.flush();

            stream.close();

            outputStream.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
