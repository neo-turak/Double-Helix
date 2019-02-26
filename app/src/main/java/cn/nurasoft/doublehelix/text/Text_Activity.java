/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.text;

/**
 * @author Nueraihemaiti Tureke
 * @Date 2017-08-25 11:50:12
 * version:4
 * description:Activate the new_text_activity layout
 */

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import cn.nurasoft.doublehelix.DataBaseHelperClass;
import cn.nurasoft.doublehelix.R;


public class Text_Activity extends AppCompatActivity {
    RecyclerView Rv_TextList;
    Button btn_return, new_text;
    DataBaseHelperClass dbase;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);

        dbase = new DataBaseHelperClass(Text_Activity.this);
        db = dbase.getReadableDatabase();
        dbase.openDataBase();
        TextListAdapter.init(dbase.Get_text(4));
        dbase.close();


        btn_return = findViewById(R.id.btnreturn);
        new_text = findViewById(R.id.new_text);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this is for return button ,back to previous activity.
                Text_Activity.this.finish();
            }
        });
        new_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Text_Activity.this, New_Text_Activity.class);
                startActivity(intent);
                Text_Activity.this.finish();
            }
        });

        Rv_TextList = findViewById(R.id.rv_text_list);
        Rv_TextList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Rv_TextList.setAdapter(new TextListAdapter(this));
    }


}
