/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix;

/**
 * @author:Nueraihemaiti Tureke
 * @date 2018-09-01 11:53:37
 * description: Login activity layout activator
 * version:6
 */
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import cn.nurasoft.doublehelix.finger.FingerActivity;


public class Login_Activity extends AppCompatActivity {

    final Context context = this;
    Button login;
    EditText usrname, pswd;
    Button finger;
    DataBaseHelperClass DBHC; //use other java class(database_helper_class)
    SQLiteDatabase db;
    String ID;
    Typeface typeface;
    AssetManager am;
    TextView ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        usrname = findViewById(R.id.ET_username);
        pswd = findViewById(R.id.login_edtPwd);
        login = findViewById(R.id.login_btnLogin);
        finger = findViewById(R.id.finger);
        am = context.getApplicationContext().getAssets();
        ForgetPassword=findViewById(R.id.ForgerPassword);

        DBHC = new DataBaseHelperClass(Login_Activity.this);
        db = DBHC.getReadableDatabase();

        typeface = Typeface.createFromAsset(am, String.format(Locale.ENGLISH, "fonts/%s", "FUR.ttf"));
        login.setTypeface(typeface);
        usrname.setTypeface(typeface);

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Forget Password");
                builder.setMessage("Please contact with University of hertfordshire Computer Sciense lab");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setIcon(R.mipmap.ic_launcher);
                final Dialog message = builder.create();
                message.show();
            }
        });

        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, FingerActivity.class);
                startActivity(intent);
                finish();
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr, pwd;
                usr = usrname.getText().toString();
                pwd = pswd.getText().toString();

                if (usr.equals("") || pwd.equals("")) {
                    if (usr.equals("")) {
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), "Empty username!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if (pwd.equals("")) {
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), "Empty password!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Log.e(usr, pwd);
                    DBHC.openDataBase();
                    ID = DBHC.check_usr_pwd(usr, pwd);

                    if (ID == null) {
                        Dialog alertDialog = new AlertDialog.Builder(context).
                                setTitle("Login Failed").
                                setMessage("Username or password incorrect!").

                                setPositiveButton("Gotcha", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setIcon(R.mipmap.ic_launcher).
                                create();
                        alertDialog.show();
                        usrname.setText("");
                        pswd.setText("");
                        usrname.requestFocus();
                        DBHC.close();
                    } else {
                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

}
