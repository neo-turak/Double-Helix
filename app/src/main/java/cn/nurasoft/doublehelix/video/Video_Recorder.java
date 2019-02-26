/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.video;

/**Recourse:Github
 * Licence:Apache 2.0
 *url:https://github.com/Gentleman-jun/VideoRecordDemo
 * @author :liuzhongjun
 * @date: 2016-3-16
 * Modified:Nueraihemaiti Tureke
 * Date:2018-07-15 01:14:28
 * Modified:40%
 * Version:2
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OutputFormat;
import android.media.MediaRecorder.VideoEncoder;
import android.media.MediaRecorder.VideoSource;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.nurasoft.doublehelix.R;

/*
 * Created by Miro on 2017/7/10.
 **/

/**
 * play video class
 */
public class Video_Recorder extends LinearLayout implements OnErrorListener {

    private SurfaceView Surface_View;
    private SurfaceHolder Surface_Holder;
    private ProgressBar bar;

    private MediaRecorder me;
    private Camera camera;
    private Timer mTimer;// timer
    private OnRecordFinishListener recordFinishListener;// after finish record return listener

    private int Width;// video resolution width
    private int Height;// video resolution height
    private boolean isOpenCamera;// or not open the camera when start
    private int RecordMaxTime;// the max record time
    private int TimeCount;// time count
    private File RecordFile = null;// file

    public Video_Recorder(Context context) {
        this(context, null);
    }

    public Video_Recorder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public Video_Recorder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Video_Recorder, defStyle, 0);
        Width = a.getInteger(R.styleable.Video_Recorder__width, 640);// video resolution default width 320
        Height = a.getInteger(R.styleable.Video_Recorder__height, 480);//video resolution default height 240

        isOpenCamera = a.getBoolean(
                R.styleable.Video_Recorder_is_open_camera, true);// default open camera
        RecordMaxTime = a.getInteger(
                R.styleable.Video_Recorder_record_max_time, 10);// set 10 seconds.

        LayoutInflater.from(context)
                .inflate(R.layout.video_view, this);
        Surface_View = findViewById(R.id.surfaceview);
        bar = findViewById(R.id.progressBar);
        bar.setMax(RecordMaxTime);// set the progressbar max value

        Surface_Holder = Surface_View.getHolder();
        Surface_Holder.addCallback(new CustomCallBack());
        Surface_Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        a.recycle();
    }

    /**
     * initialize the camera
     */
    private void initCamera() throws IOException {
        if (camera != null) {
            freeCameraResource();
        }
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (camera == null)
            return;

        setCameraParams();
        camera.setDisplayOrientation(90);//change orientation;
        camera.setPreviewDisplay(Surface_Holder);
        camera.startPreview();
        camera.unlock();
    }

    /**
     * set camera orientation;
     */
    private void setCameraParams() {
        if (camera != null) {
            Parameters params = camera.getParameters();
            params.set("orientation", "portrait");
            camera.setParameters(params);
        }
    }

    /**
     * release the camera resources.
     */
    private void freeCameraResource() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    private void createRecordDir() {
        //save the video file
        File sampleDir = new File("/data/data/cn.nurasoft.doublehelix/video/");//the save address
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHMMss");  //save by time format                                                                                by datetime format.
        Date date = new Date();
        String name = dateFormater.format(date);
        // create file
        try {
            RecordFile = File.createTempFile(name, ".mp4", sampleDir);// mp4 video file format
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecord() throws IOException {
        me = new MediaRecorder();
        me.reset();
        if (camera != null)
            me.setCamera(camera);
        me.setOnErrorListener(this);
        me.setPreviewDisplay(Surface_Holder.getSurface());
        me.setVideoSource(VideoSource.CAMERA);// video resource
        me.setAudioSource(AudioSource.MIC);// audio resource
        me.setOutputFormat(OutputFormat.MPEG_4);// video output format
        me.setAudioEncoder(AudioEncoder.AMR_NB);// audio output format
        me.setVideoSize(Width, Height);// screen resolution
        // me.setVideoFrameRate(16);// video frame rate,
        me.setVideoEncodingBitRate(1024 * 1024 * 100);// set encoding bit rate,after that the video clear to watch
        me.setOrientationHint(90);// output rotate 90 degree,record with portrait screen
        me.setVideoEncoder(VideoEncoder.MPEG_4_SP);// video record format
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        me.setOutputFile(RecordFile.getAbsolutePath());
        me.prepare();
        try {
            me.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.recordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// open front camera
                initCamera();
            initRecord();
            TimeCount = 0;// set the counter to 0
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    TimeCount++;
                    bar.setProgress(TimeCount);// set progress bar
                    if (TimeCount == RecordMaxTime) {// if get the max record time, stop recording
                        stop();
                        if (recordFinishListener != null)
                            recordFinishListener.onRecordFinish();
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * stop record,release source
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    public void stopRecord() {
        bar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (me != null) {
            // would not crash after set the parameters.
            me.setOnErrorListener(null);
            me.setPreviewDisplay(null);
            try {
                me.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * release system resource
     */
    private void releaseRecord() {
        if (me != null) {
            me.setOnErrorListener(null);
            try {
                me.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        me = null;
    }

    public int getTimeCount() {
        return TimeCount;
    }

    //return the record file
    public File getRecordFile() {
        return RecordFile;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * after finish record return to API
     */
    public interface OnRecordFinishListener {
        void onRecordFinish();
    }

    /**
     *
     */
    private class CustomCallBack implements Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }

    }
}
