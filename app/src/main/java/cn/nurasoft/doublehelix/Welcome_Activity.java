/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by neo on 04/07/17.
 * Project name:DoubleHelix.
 **/

public class Welcome_Activity extends Activity {

    DataBaseHelperClass dbhelper;
    SQLiteDatabase db;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title and the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*Title belongs to View,after hide the title bar, but the title words still shows, so need to hide*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        check_permission();
        handler.sendEmptyMessageDelayed(0, 3000); //after 3 seconds show the login window

        dbhelper = new DataBaseHelperClass(Welcome_Activity.this);
        db = dbhelper.getReadableDatabase();
        dbhelper.createDataBase();
        dbhelper.onUpgrade(db, 1, 2);  //check database file and also upgrade.
    }

    public void check_permission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //     if (checkSelfPermission() != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAPTURE_VIDEO_OUTPUT,
                        Manifest.permission.CAPTURE_AUDIO_OUTPUT,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_SETTINGS
                }, 100);
                Log.e("Permission:", "Permission request Done!");
            }
        }
        Log.e("Permission:", "ALL DONE!:)");
    }

    public void getHome() {
        Intent intent = new Intent(Welcome_Activity.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }
}


