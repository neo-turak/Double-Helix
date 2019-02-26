/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.picture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import cn.nurasoft.doublehelix.R;


public class PictureViewer_Activity extends AppCompatActivity {

    ImageView picture;
    Button left, right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.picture_view_activity);
        picture = findViewById(R.id.view);

        Bundle bundle = getIntent().getExtras();
        Bitmap bitmap = bundle.getParcelable("bitmap");
        picture.setImageBitmap(bitmap);

        left = findViewById(R.id.leftRotate);
        right = findViewById(R.id.rightRotate);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picture.setPivotX(picture.getWidth() / 2);
                picture.setPivotY(picture.getHeight() / 2);
                picture.setRotation(90);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picture.setPivotX(picture.getWidth() / 2);
                picture.setPivotY(picture.getHeight() / 2);
                picture.setRotation(-90);
            }
        });
    }

}
