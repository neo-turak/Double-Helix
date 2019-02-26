/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix;

/** written by:Nueraihemaiti Tureke
 * Date:2017-07-21 01:18:52
 * description:The local database handler.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DataBaseHelperClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db3";
    private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "";
    private static SQLiteDatabase sqLiteDatabase;
    private String TAG = "Double Helix:";
    private Context context;

    public DataBaseHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/";
        Log.e(TAG, DB_PATH);
        this.context = context;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    void createDataBase() {
        boolean databasesExist = checkDataBase();
        if (!databasesExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.e(TAG, "Database created successfully!");
            } catch (IOException me) {
                throw new Error("Copy datafile has error!");
            }
        }
    }

    private boolean checkDataBase() {
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        Log.v("Databases exists!", databaseFile + "   " + databaseFile.exists());
        return databaseFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];   //the buffer of copy data
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DATABASE_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (sqLiteDatabase != null)
            super.close();
    }

    String check_usr_pwd(String username, String password) {  //check username and password from database.
        String ID = null;
        String query = "select * from users where username ='" + username + "' and password ='" + password + "'";
        //   SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                ID = cursor.getString(0);
            }
            cursor.close();
        }
        Log.e(TAG, "ID:" + ID);
        return ID;

    }

    String[] get_user_Details(String ID) {
        String get_details[] = new String[5];
        try {
            String query = "select * from users_details where userID=" + ID;
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    get_details[0] = cursor.getString(1);
                    get_details[1] = cursor.getString(2);
                    get_details[2] = cursor.getString(3);
                    get_details[3] = cursor.getString(4);
                    get_details[4] = cursor.getString(5);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DATABASE 2:", e.getMessage());
        }

        return get_details;
    }

    ArrayList<String> checkRelation(String user_id) {  //check username and password from database.
        ArrayList<String> result = new ArrayList<>();
        String query = "select contact_id from user_relationship where user_id =" + user_id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        cursor.moveToFirst();
        {
            for (int i = 0; i < cursor.getCount(); i++) {
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    ArrayList<String> name_list(ArrayList<String> name_list) {   //return contact name list;
        ArrayList<String> result = new ArrayList<>();
        String query;
        for (int i = 0; i < name_list.size(); i++) {
            query = "Select first_name from users_details where userID=" + name_list.get(i);
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            result.add(cursor.getString(0));
            cursor.close();
        }
        return result;
    }

    ArrayList<String> phone_list(ArrayList<String> phone_list) {   //return contact phone list;
        ArrayList<String> result = new ArrayList<>();
        String query;
        for (int i = 0; i < phone_list.size(); i++) {
            query = "Select contact_number from users_details where userID=" + phone_list.get(i);
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            result.add(cursor.getString(0));
            cursor.close();
        }
        return result;
    }


    public HashMap<String, String> Get_text(int id) {
        HashMap<String, String> result = new HashMap<>();
        String query;
        query="SELECT main_context,date_time,ID FROM main_media WHERE data_id="+id+" ORDER BY id DESC";

        Log.e("query:", query);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            result.put(cursor.getString(1), cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public void Set_Text(String text) {

        Calendar cal = Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH)+1;
        int d=cal.get(Calendar.DAY_OF_MONTH);
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int M=cal.get(Calendar.MINUTE);

        String dt = d + "-" + m + "-" + y + " " + h + ":" + M;
        String query = "INSERT into main_media(data_id,type,main_context,date_time) VALUES(4,'Text','" + text + "','" + dt + "')";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        if (ii > i)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
