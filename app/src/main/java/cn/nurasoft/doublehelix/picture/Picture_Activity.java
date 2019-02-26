/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.picture;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nurasoft.doublehelix.R;

/*
 *created by Miro on 29/06/2017
 */
public class Picture_Activity extends Activity {
    Button button, btn_return;
    ImageView view;
    ListView listView;
    Picture_Sorter sorter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        button = findViewById(R.id.button1);
        view = findViewById(R.id.imageView1);
        btn_return = findViewById(R.id.btnreturn);
        listView = findViewById(R.id.photo_list);

        sorter = new Picture_Sorter();
        sorter.Sorted_Data();
        sorter.Sort();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sorter.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = adapterView.getItemAtPosition(i).toString();
                s = s.replace("-", "");
                s = s.replace(" ", "");
                s = s.replace(":", "");
                s = s + ".jpg";
                Bitmap bitmap = BitmapFactory.decodeFile("/data/data/cn.nurasoft.doublehelix/image/" + s);
                Intent intent = new Intent();
                intent.setClass(Picture_Activity.this, PictureViewer_Activity.class);
                intent.putExtra("bitmap", bitmap);
                startActivity(intent);
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picture_Activity.this.finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT >= 23) {  //check permission
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        startActivityForResult(intent, 1);
                    }
                } else {
                    startActivityForResult(intent, 1);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHMMss");  //save by time format                                                                                by datetime format.
            Date date = new Date();
            String filename = dateFormater.format(date);
            String name = filename + ".jpg";

            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// get the return data of the camera，and convert to Bitmap photo format

            FileOutputStream b = null;
            @SuppressLint("SdCardPath")
            File file = new File("/data/data/cn.nurasoft.doublehelix/image/");
            file.mkdirs();// create folder
            @SuppressLint("SdCardPath")
            String fileName = "/data/data/cn.nurasoft.doublehelix/image/" + name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// write to file
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                // view.setImageBitmap(bitmap);// show the picture on the ImageView
                Intent intent = new Intent();
                intent.setClass(Picture_Activity.this, PictureViewer_Activity.class);
                intent.putExtra("bitmap", bitmap);
                startActivity(intent);

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }

        }


    }
}
