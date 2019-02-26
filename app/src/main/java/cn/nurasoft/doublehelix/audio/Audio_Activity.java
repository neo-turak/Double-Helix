/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.audio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.nurasoft.doublehelix.R;

/**
 * Created by Miro on 2017/7/11.
 **/

public class Audio_Activity extends Activity implements View.OnClickListener {
    final Context context = this;
    //timer
    Integer i = 0;  //for time;
    AudioSorter timeline = new AudioSorter();
    private Button start;
    private Button stop;
    private Button play;
    private Timer timer;
    private TimerTask timerTask;
    private Button pause_play;
    private Button stop_play;
    private Button update;
    private ProgressBar prgbar;
    //progressbar
    private TextView prgtxt;
    // media recorder
    private MediaRecorder mediaRecorder;
    // save by file type
    private File recordFile;
    private AudioPlayer player;
    private ListView time, id;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(android.os.Message msg) {
            prgtxt.setText(msg.arg1 + "");
            prgbar.setProgress(msg.arg1);
            startTime();
        }
    };

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_activity);
        try {
            File destDir = new File("/data/data/cn.nurasoft.doublehelix/audio/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } catch (Exception e) {
            Log.e("Error Creating folder:", e.getMessage());
        }


        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");  //save record                                                                                by datetime format.
            Date date = new Date();
            String filename = dateFormater.format(date);
            recordFile = new File("/data/data/cn.nurasoft.doublehelix/audio/", filename + ".amr");  //save the file
            Log.e("Save to", recordFile.toString());
            // recordFile = new File("/mnt/sdcard",filename+".amr");
        } catch (Exception e) {
            Log.e("save file:", e.toString());
        }
        initView();
        Listener();

        time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = timeline.data.get(i);
                String result;
                //decode
                value = value.replace("-", "");
                value = value.replace(" ", "");
                value = value.replace(":", "");
                result = value + ".amr";
                Toast.makeText(Audio_Activity.this, result.toString() + " is playing!", Toast.LENGTH_SHORT).show();

                player.playRecordFile(new File("/data/data/cn.nurasoft.doublehelix/audio/", result));
            }
        });


    }

    private void initView() {
        time = findViewById(R.id.timeline_date);
        id = findViewById(R.id.audio_id);
        update = findViewById(R.id.update);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        play = findViewById(R.id.PLAY);
        pause_play = findViewById(R.id.PAUSE_PLAY);
        stop_play = findViewById(R.id.STOP_PLAY);
        prgbar = findViewById(R.id.progressBar1);
        prgtxt = findViewById(R.id.progresstxt);
    }

    private void Listener() {
        update.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);
        pause_play.setOnClickListener(this);
        stop_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        player = new AudioPlayer(Audio_Activity.this);
        int Id = v.getId();
        Toast toast;
        switch (Id) {
            case R.id.update:
                //the below for set the data to list view
                timeline.data.clear();
                timeline.id.clear();
                timeline.filelist.clear();
                timeline.GetCount();
                timeline.DateSort();
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1,
                        timeline.data
                );
                time.setAdapter(adapter1);
                ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1,
                        timeline.id
                );
                id.setAdapter(adapter2);
                break;

            case R.id.start:
                startRecording();
                timer = new Timer();
                toast = Toast.makeText(getApplicationContext(), "Record Starting!", Toast.LENGTH_LONG);
                toast.show();
                start.setEnabled(false);
                i = 0;
                startTime();
                break;
            case R.id.stop:
                stopRecording();
                toast = Toast.makeText(getApplicationContext(), "Record was Stopped!Time:" + i + "Seconds", Toast.LENGTH_LONG);
                toast.show();
                start.setEnabled(true);
                stopTime();
                break;
            case R.id.PLAY:
                playRecording();
                toast = Toast.makeText(getApplicationContext(), "The record is playing!", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.PAUSE_PLAY:
                pauseplayer();
                toast = Toast.makeText(getApplicationContext(), " Paused!", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.STOP_PLAY:
                stopplayer();
                toast = Toast.makeText(getApplicationContext(), " Stop playing!", Toast.LENGTH_LONG);
                toast.show();
                break;

        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        try {
            if (recordFile.exists()) {
                //  recordFile.delete();
            }
        } catch (Exception e) {
            Log.e("Error:", e.toString());
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
        Log.e("The OutPut folder:", recordFile.getAbsolutePath());

        try {
            // prepare to record
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopRecording() {

        try {
            if (recordFile != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
            }
        } catch (Exception e) {
            Log.e("Stop:", e.toString());
        }

    }

    private void playRecording() {
        player.playRecordFile(recordFile);
    }

    private void pauseplayer() {
        player.pausePlayer();
    }

    private void stopplayer() {
        player.stopPlayer();
    }

    /**
     * use timer control the progress bar
     */
    private void startTime() {
        if (timer == null) {
            timer = new Timer();
        }

        timerTask = new TimerTask() {

            @Override
            public void run() {
                i++;//auto add 1
                Message message = Message.obtain();
                message.arg1 = i;
                mHandler.sendMessage(message);//send message
            }
        };
        timer.schedule(timerTask, 1000);//1000ms execute once.
    }

    private void stopTime() {
        if (timer != null)
            timer.cancel();
    }
}
