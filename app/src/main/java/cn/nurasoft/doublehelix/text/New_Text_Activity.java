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

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.nurasoft.doublehelix.DataBaseHelperClass;
import cn.nurasoft.doublehelix.R;


public class New_Text_Activity extends AppCompatActivity {

    Button btn_return, submit;
    EditText textView;
    DataBaseHelperClass dbase;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_text_activity);

        submit = findViewById(R.id.submit);
        btn_return = findViewById(R.id.btn_return);
        textView = findViewById(R.id.new_text);
        dbase = new DataBaseHelperClass(New_Text_Activity.this);
        db = dbase.getReadableDatabase();


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this is for return button ,back to previous activity.
                New_Text_Activity.this.finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = textView.getText().toString();

                dbase.openDataBase();
                dbase.Set_Text(str);
                dbase.close();

                Toast toast;
                toast = Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }
}
