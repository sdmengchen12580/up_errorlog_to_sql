package com.example.yunwen.textuptxt;

import android.app.Application;

/**
 * Created by yunwen on 2017/9/22.
 */

public class Mapplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        new Up_Log_Message(this);
    }
}
