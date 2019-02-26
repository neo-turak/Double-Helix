/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.finger;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.nurasoft.doublehelix.MainActivity;
import cn.nurasoft.doublehelix.R;

/**
 * this class adapted from other designer.
 * Created by bamboy on 2017/3/13.
 */
public class FingerActivity extends AppCompatActivity {
    /**
     * Start recognition
     */
    private FingerUtil mFingerUtil;
    /**
     * fingerprint listener
     */
    private FingerprintManagerCompat.AuthenticationCallback mFingerListen;

    /**
     * start the fingerprint listen even
     */
    private Button btn_start;
    /**
     * show prompt information
     */
    private TextView tv_log;
    /**
     * show the animation image via ImageView
     */
    private FingerImageView iv_finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);

        btn_start = findViewById(R.id.btn_start);
        tv_log = findViewById(R.id.TV_Results);
        iv_finger = findViewById(R.id.iv_finger);

        // start the even
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // initialize
                    initFingerBtnClick();
                } catch (Exception e) {
                    tv_log.setText("No fingerprint sensor available");
                    // if result was failed, then show the failed animate.
                    iv_finger.end(false);
                }
                // show the result
                ObjectAnimator.ofFloat(tv_log, "alpha", 0, 0.7f).setDuration(150).start();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFingerUtil != null && mFingerListen != null) {
            mFingerUtil.startFingerListen(mFingerListen);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFingerUtil != null) {
            mFingerUtil.stopsFingerListen();
        }
    }

    /**
     * initialize the click even of the fingerprint button
     */
    @RequiresApi(api = Build.VERSION_CODES.M)

    private void initFingerBtnClick() {
        //test phone fingerprint hardware is exists or not
        if (!checkFingerModule()) {
            tv_log.setText("No fingerprint hardware available");
            // if not, show failed
            iv_finger.end(false);
            return;
        }

        // detect needs initialize or not
        if (mFingerListen == null || mFingerUtil == null) {
            initFinger();
            // show the image
            iv_finger.startGif();
        } else {
            // launch fingerprint sensor
            mFingerUtil.startFingerListen(mFingerListen);
            // fingerprint active success, show image
            iv_finger.startGif();
        }
    }

    /**
     * this for detect the fingerprint module
     *
     * @return return has or not
     */
    @RequiresApi(api = Build.VERSION_CODES.M)

    private boolean checkFingerModule() {
        try {
            return ((FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE)).isHardwareDetected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Initialize the fingerprint module
     */
    private void initFinger() {
        mFingerListen = new FingerprintManagerCompat.AuthenticationCallback() {

            /**
             * Successful fingerprint identification
             */
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                tv_log.setText("Authentication Succeeded!");
                iv_finger.end(true);
                Intent intent = new Intent(FingerActivity.this, MainActivity.class);
                intent.putExtra("ID", "4");
                startActivity(intent);
                finish();
            }

            /**
             * failed fingerprint identification
             */
            @Override
            public void onAuthenticationFailed() {
                tv_log.setText("Authentication failed!");
                iv_finger.end(false);
            }


            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                if (tv_log.getTag() != null && false == (Boolean) tv_log.getTag()) {
                    return;
                }
                switch (helpMsgId) {
                    case 1001:      // wait for press
                        tv_log.setText("Please touch your finger");
                        iv_finger.startGif();
                        break;
                    case 1002:      // pressing
                        tv_log.setText("Authenticating…");
                        break;
                    case 1003:      // finished press
                        tv_log.setText("Try again");
                        iv_finger.startGif();
                        break;
                }
            }

            /**
             * for avoid using multi times of the detect function.
             */
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                switch (errMsgId) {
                    case 5:      // able to use
                        tv_log.setTag(true);
                        break;
                    case 7:      // overtimes detection.
                        tv_log.setText("Overfailure！please try after" + errString + "seconds later!");
                        tv_log.setTag(false);
                        break;
                }
            }
        };
        mFingerUtil = new FingerUtil(this);
        mFingerUtil.startFingerListen(mFingerListen);
    }

}
