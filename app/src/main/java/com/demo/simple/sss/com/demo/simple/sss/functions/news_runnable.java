package com.demo.simple.sss.com.demo.simple.sss.functions;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/4/10.
 */
public class news_runnable implements Runnable{

    public NewsHandle newsHandle=new NewsHandle();
    public news_runnable() {
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */

    @Override
    public void run() {

        Message newmessage=newsHandle.obtainMessage();
        Bundle newsbundle=new Bundle();
        //读取服务器数据
        //?????
        newmessage.setData(newsbundle);
        newsHandle.sendMessage(newmessage);
    }
}
