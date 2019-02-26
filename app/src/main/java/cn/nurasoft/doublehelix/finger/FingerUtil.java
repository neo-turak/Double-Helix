/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.finger;

import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * adapter class.
 * Created by bamboy on 2017/3/13.
 */
public class FingerUtil {
    private CancellationSignal signal;
    private FingerprintManagerCompat fingerprintManager;

    public FingerUtil(Activity activity) {
        signal = new CancellationSignal();
        fingerprintManager = FingerprintManagerCompat.from(activity);
    }

    public void startFingerListen(FingerprintManagerCompat.AuthenticationCallback callback) {
        fingerprintManager.authenticate(null, 0, signal, callback, null);
    }

    public void stopsFingerListen() {
        signal.cancel();
        signal = null;
    }
}
