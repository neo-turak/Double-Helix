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
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import cn.nurasoft.doublehelix.R;

/**
 * adapted class
 * Created by bamboy on 2017/3/14.
 */
public class FingerImageView extends android.support.v7.widget.AppCompatImageView {

    public FingerImageView(Context context) {
        super(context);
    }

    public FingerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FingerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startGif() {
        // initialize
        setAlpha(0);
        setScaleX(1f);
        setScaleY(1f);

        // set animation
        setBackgroundResource(R.drawable.finger_gif);
        // start the animation
        ((AnimationDrawable) getBackground()).start();

        // start animating frequently
        ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(200).start();
    }

    public void end(final boolean isSuccess) {
        // initialize
        setAlpha(0);
        setScaleX(1.54f);
        setScaleY(1.5f);

        // setup animation
        setBackgroundResource(isSuccess ? R.drawable.finger_succeeded : R.drawable.finger_failed);
        // start animation
        ((AnimationDrawable) getBackground()).start();

        // start animation gradually
        ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(200).start();
    }

}
