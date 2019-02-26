/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.video;
/**written by:Nueraihemaiti Tueke
 * Date:11/07/2017
 * version:1
 * Description:for video_view_activity and some other operation
 */
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import cn.nurasoft.doublehelix.R;

/**
 * Created by Miro on 2017/7/10.
 **/

public class Video_View_Activity extends Activity implements View.OnClickListener {

    private Button video_view_play;//play
    private Button video_view_return;
    private Button video_view_pause;

    private VideoView videoView1;//video play controller
    private String file;//the file destination folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_activity);

        Bundle bundle = getIntent().getExtras();
        file = bundle.getString("url");//get the saved folder path

        init();
        setValue();
    }

    //initialize
    private void init() {
        video_view_play = findViewById(R.id.video_view_Play);
        video_view_pause = findViewById(R.id.video_view_Pause);
        video_view_return = findViewById(R.id.video_view_return);
        videoView1 = findViewById(R.id.videoView1);
    }

    //Settings
    private void setValue() {
        video_view_play.setOnClickListener(this);
        video_view_return.setOnClickListener(this);
        video_view_pause.setOnClickListener(this);

        videoView1.setVideoPath(file);

        Log.e("path",file.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_view_Play:
                videoView1.start();
                break;

            case R.id.video_view_Pause:
                videoView1.pause();
                break;
            case R.id.video_view_return:
                finish();
                finish();
                finish();
                break;
            default:
                break;
        }
    }

}
