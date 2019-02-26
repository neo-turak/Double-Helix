/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.video;
/**written by:Nueraihemaiti Tureke
 * Date:2018-08-11 01:11:41
 * version:2
 * Description: for video_list_activity layout and other operations
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import cn.nurasoft.doublehelix.R;


public class Video_list_Activity extends Activity {

    Button video_new, video_rtn;
    ListView video_list;
    Video_Sorter sorter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_activity);

        video_new = findViewById(R.id.new_video);
        video_rtn = findViewById(R.id.video_rtn);
        video_list = findViewById(R.id.video_list);
        sorter = new Video_Sorter();
        sorter.Sorted_Data();
        sorter.Sort();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sorter.list);
        video_list.setAdapter(adapter);

        video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                s = s.replace("-", "");
                s = s.replace(" ", "");
                s = s.replace(":", "");
                s = s + ".mp4";
                String url = "/data/data/cn.nurasoft.doublehelix/video/" + s;
                Intent intent = new Intent();
                intent.setClass(Video_list_Activity.this, Video_View_Activity.class);
                intent.putExtra("url", url);
                Log.e("URL:", url);
                startActivity(intent);
                finish();
            }
        });

        video_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Video_list_Activity.this, Video__Record_Activity.class);
                startActivity(intent);
            }
        });
        video_rtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
