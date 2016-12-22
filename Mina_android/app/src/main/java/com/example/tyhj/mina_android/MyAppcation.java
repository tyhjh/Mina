package com.example.tyhj.mina_android;

import android.app.Application;
import android.content.Intent;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Tyhj on 2016/12/21.
 */

public class MyAppcation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "Yi6HruJsj4h2bufroQKC9kJT-gzGzoHsz", "2nWoF8MhHN6kibFs72bVhLWV");
    }
}
