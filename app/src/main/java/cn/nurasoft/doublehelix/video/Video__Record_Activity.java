/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.video;
/**
 * Created by: Nueraihemaiti Tureke
 * Date: 2017/7/10.
 * version:3
 * Description:active the video_record_activity layout and for other operations
 **/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.nurasoft.doublehelix.R;


public class Video__Record_Activity extends Activity {


    private Video_Recorder video_recorder;//video record control
    private Button btn_start;//video start button
    private Button btn_return;
    private boolean finished = true;
    private boolean success = false;//avoid after finish recording jump to other view
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (success) {
                finishActivity();
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_record_activity);

        video_recorder = findViewById(R.id.movieRecorderView);
        btn_start = findViewById(R.id.shoot_button);
        btn_return = findViewById(R.id.btn_return);

        //user hold the button event handler
        btn_start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//user click start button
                    btn_start.setBackgroundResource(R.drawable.bg_movie_add_shoot_select);
                    video_recorder.record(new Video_Recorder.OnRecordFinishListener() {

                        @Override
                        public void onRecordFinish() {
                            if (!success && video_recorder.getTimeCount() < 10) {//judge the video time is not under 10 seconds
                                success = true;
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {//when user UP the button
                    btn_start.setBackgroundResource(R.drawable.bg_movie_add_shoot);
                    if (video_recorder.getTimeCount() > 3) {//judge the time greater than 3 seconds or not
                        if (!success) {
                            success = true;
                            handler.sendEmptyMessage(1);
                        }
                    } else {
                        success = false;
                        if (video_recorder.getRecordFile() != null)
                            video_recorder.getRecordFile().delete();//delete the too short video
                        video_recorder.stop();//stop recording
                        Toast.makeText(Video__Record_Activity.this, "recorded video too short!", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Video__Record_Activity.this.finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        finished = true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        finished = false;
        success = false;
        video_recorder.stop();//stop record
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //after finish recording jump, function
    private void finishActivity() {
        if (finished) {
            video_recorder.stop();
            Intent intent = new Intent(this, Video_list_Activity.class);
            startActivity(intent);
        }
        success = false;
    }


}

