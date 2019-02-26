/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cn.nurasoft.doublehelix.R;


/**
 * @author Miro  play the record
 */

public class AudioPlayer {

    private static MediaPlayer mediaPlayer;

    private Context mcontext;

    AudioPlayer(Context context) {
        this.mcontext = context;
    }

    // play record file
    public void playRecordFile(File file) {
        if (file.exists() && file != null) {
            if (mediaPlayer == null) {
                Uri uri = Uri.fromFile(file);
                mediaPlayer = MediaPlayer.create(mcontext, uri);
            }
            mediaPlayer.start();

            //listen MediaPlayer finish play
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer paramMediaPlayer) {
                    // TODO Auto-generated method stub
                    //show a toast
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.ok),
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    // pause play
    public void pausePlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.e("TAG", "pause playing");
        }

    }

    // stop recording
    public void stopPlayer() {
        //set the playing progress bar to 0, not necessary to awake STOP()
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                Log.e("TAG", "Stop playing");
            }
        } catch (Exception e) {
            Log.e("Tag", e.getMessage());
        }
    }

}
