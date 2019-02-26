/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nurasoft.doublehelix.audio.Audio_Activity;
import cn.nurasoft.doublehelix.chat.ChatAdapter;
import cn.nurasoft.doublehelix.chat.ChatModel;
import cn.nurasoft.doublehelix.chat.ItemModel;
import cn.nurasoft.doublehelix.game.Game_Activity;
import cn.nurasoft.doublehelix.picture.Picture_Activity;
import cn.nurasoft.doublehelix.text.Text_Activity;
import cn.nurasoft.doublehelix.video.Video_list_Activity;

/**
 * Created by Nueraihemaiti Tureke on 04/07/17.
 * Project name:DoubleHelix.
 * @author:Nueraihemaiti Tureke
 * @date 2018-09-01 11:53:37
 * description: main activity layout activator,and full functions for tab01 to tab04
 * version:6
 */

public class MainActivity extends Activity implements View.OnClickListener {

    public static String value;
    final Context context = this;
    ListView lV;  //for contact view
    DataBaseHelperClass dbhelper;
    SQLiteDatabase db;
    Button function;
    Button btn_text, btn_audio, btn_video, btn_photo, btnExit, btn_game;
    Button upload, download;
    ArrayList<String> arylst, arylst_name, arylst_phone;
    ImageView profile;
    //the below for tab01 chat
    RecyclerView recyclerView;
    ChatAdapter cadapter;
    private int trigger = -1; //see from lV.on touch listener even
    private String call_number;
    private List<View> views = new ArrayList<>();
    private ViewPager viewPager;

    private ImageView ivChat, ivContacts, ivMemory, ivSettings, ivCurrent;
    private TextView tvChat, tvContacts, tvMemory, tvSettings, tvCurrent;
    private EditText et;
    private TextView tvSend, first_name, last_name, postcode, address, phone_number;
    private String content;

    private ImageButton Chat, Dial, Message;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        LinearLayout llChat;
        LinearLayout llContacts;
        LinearLayout llMemory;
        LinearLayout llSettings;

        viewPager = findViewById(R.id.viewPager);

        llChat = findViewById(R.id.llChat);
        llContacts = findViewById(R.id.ll_Contacts);
        llMemory = findViewById(R.id.ll_Memory);
        llSettings = findViewById(R.id.llSettings);

        llChat.setOnClickListener(this);
        llContacts.setOnClickListener(this);
        llMemory.setOnClickListener(this);
        llSettings.setOnClickListener(this);

        ivChat = findViewById(R.id.IV_Chat);
        ivContacts = findViewById(R.id.IV_Contacts);
        ivMemory = findViewById(R.id.IV_Memory);
        ivSettings = findViewById(R.id.ivSettings);

        tvChat = findViewById(R.id.tv_Chat);
        tvContacts = findViewById(R.id.TV_Contacts);
        tvMemory = findViewById(R.id.tv_Memory);
        tvSettings = findViewById(R.id.tvSettings);

        /*
        set the Chat page default
         */
        ivChat.setSelected(true);
        tvChat.setSelected(true);

        ivCurrent = ivChat;
        tvCurrent = tvChat;


        LayoutInflater mInflater = LayoutInflater.from(this);
        View Chat_view = mInflater.inflate(R.layout.tab01, viewPager, false);
        View Contacts_view = mInflater.inflate(R.layout.tab02, viewPager, false);
        View Memory_view = mInflater.inflate(R.layout.tab03, viewPager, false);
        View Settings_view = mInflater.inflate(R.layout.tab04, viewPager, false);
        views.add(Chat_view);
        views.add(Contacts_view);
        views.add(Memory_view);
        views.add(Settings_view);

        final Page_Adapter adapter = new Page_Adapter(views);
        viewPager.setAdapter(adapter);

        dbhelper = new DataBaseHelperClass(MainActivity.this);
        db = dbhelper.getReadableDatabase();

        final Intent intent = getIntent();
        value = intent.getStringExtra("ID");  //get the ID of the user for get the related information
        Log.e("ID:", value);


        btn_audio = Memory_view.findViewById(R.id.btnAudio);
        btn_photo = Memory_view.findViewById(R.id.btnphoto);
        btn_text = Memory_view.findViewById(R.id.btnText);
        btn_video = Memory_view.findViewById(R.id.btnVideo);
        btn_game = Memory_view.findViewById(R.id.btngame);


        //for personal information settings.
        profile = Settings_view.findViewById(R.id.profile);
        btnExit = Settings_view.findViewById(R.id.Exit);
        first_name = Settings_view.findViewById(R.id.first_name);
        last_name = Settings_view.findViewById(R.id.last_name);
        postcode = Settings_view.findViewById(R.id.Postcode);
        phone_number = Settings_view.findViewById(R.id.Phone);
        address = Settings_view.findViewById(R.id.Address);
        upload = Settings_view.findViewById(R.id.upload);
        download = Settings_view.findViewById(R.id.download);

        //set personal information end!

        //for chatting system
        recyclerView = Chat_view.findViewById(R.id.RecyclerView);
        et = Chat_view.findViewById(R.id.et);
        tvSend = Chat_view.findViewById(R.id.tvSend);

        Chat = Contacts_view.findViewById(R.id.IB_Chat);
        Dial = Contacts_view.findViewById(R.id.IB_Dial);
        Message = Contacts_view.findViewById(R.id.IB_Message);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cadapter = new ChatAdapter());
        cadapter.replaceAll(TestData.getTestAdData());
        initData();
        //end for chatting system.


        FloatingActionButton actionButton = Contacts_view.findViewById(R.id.floatingActionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dbhelper.openDataBase();
        arylst = dbhelper.checkRelation(value);
        dbhelper.close();
        Log.e("Relationship", arylst.size() + "");
        arylst_name = dbhelper.name_list(arylst);
        Log.e("Name list", arylst_name.size() + "");
        arylst_phone = dbhelper.phone_list(arylst);
        Log.e("Phone", arylst_phone.size() + "");

        lV = Contacts_view.findViewById(R.id.lv);
        final SimpleAdapter contact_adapter = new SimpleAdapter(this, contact(), R.layout.contect_list,
                new String[]{"title", "info", "icon"}, new int[]{R.id.title, R.id.info, R.id.img});
        lV.setAdapter(contact_adapter);

        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast toast;
              call_number = contact().get(i).get("info").toString();
                // toast.show();

                if (trigger != i && trigger != -1) {
                    adapterView.getChildAt(trigger).setBackgroundColor(Color.WHITE);
                    Chat.setVisibility(View.INVISIBLE);
                    Dial.setVisibility(View.INVISIBLE);
                    Message.setVisibility(View.INVISIBLE);
                }
                view.setBackgroundColor(Color.CYAN);
                Chat.setVisibility(View.VISIBLE);
                Dial.setVisibility(View.VISIBLE);
                Message.setVisibility(View.VISIBLE);
                if (trigger == i) {
                    adapterView.getChildAt(trigger).setBackgroundColor(Color.WHITE);
                    Chat.setVisibility(View.INVISIBLE);
                    Dial.setVisibility(View.INVISIBLE);
                    Message.setVisibility(View.INVISIBLE);
                    trigger = -1;
                } else {
                    trigger = i;
                }

            }

        });
        Dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dial = new Intent(Intent.ACTION_CALL);
                dial.setData(Uri.parse("tel:" + call_number));

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(dial);
            }
        });

        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(call_number, null, "Have a good day\n sended by Double Helix app", null, null);
                Toast toast;
                toast = Toast.makeText(MainActivity.this, "Sended Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        switch (Integer.parseInt(value)) {
            case 1:
            case 3:
            case 5:
                profile.setImageResource(R.drawable.profile5);
                break;
            case 6:
            case 2:
            case 4:
                profile.setImageResource(R.drawable.profile20);
                break;
            default:
                break;
        }


        View.OnTouchListener onTouchListener = new View.OnTouchListener() {  //change button color when press and up
            @Override
            public boolean onTouch(View view, MotionEvent Event) {
                if (Event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundResource(R.drawable.color1);
                } else if (Event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundResource(R.drawable.color2);
                }
                return false;
            }
        };

        btn_audio.setOnTouchListener(onTouchListener);
        btn_text.setOnTouchListener(onTouchListener);
        btn_photo.setOnTouchListener(onTouchListener);
        btn_text.setOnTouchListener(onTouchListener);
        btn_video.setOnTouchListener(onTouchListener);
        btn_game.setOnTouchListener(onTouchListener);
        btnExit.setOnTouchListener(onTouchListener);

        btn_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, Audio_Activity.class);
                startActivity(intent1);
            }

        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, Text_Activity.class);
                startActivity(intent1);
            }

        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, Picture_Activity.class);
                startActivity(intent1);
            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, Video_list_Activity.class);
                startActivity(intent1);
            }
        });

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, Game_Activity.class);
                startActivity(intent1);
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Are you sure?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());   //get PID
                        System.exit(0);   //normal quit,*java、c#standard exit，return value is 1 means normal quit.
                    }
                });
                builder.create();
                builder.show();
            }
        });


    }


    ArrayList<Map<String, Object>> contact() {  //This for contact list;
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 0; i < arylst.size(); i++) {
            map = new HashMap<>();
            map.put("title", arylst_name.get(i));
            map.put("info", arylst_phone.get(i));
            map.put("icon", TestData.randomNum());
            list.add(map);
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    } //for below menu action

    private void changeTab(int id) {
        ivCurrent.setSelected(false);
        tvCurrent.setSelected(false);
        switch (id) {
            case R.id.llChat:
                viewPager.setCurrentItem(0);
            case 0:
                ivChat.setSelected(true);
                ivCurrent = ivChat;
                tvChat.setSelected(true);
                tvCurrent = tvChat;
                break;
            case R.id.ll_Contacts:
                viewPager.setCurrentItem(1);
            case 1:
                ivContacts.setSelected(true);
                ivCurrent = ivContacts;
                tvContacts.setSelected(true);
                tvCurrent = tvContacts;
                break;
            case R.id.ll_Memory:
                viewPager.setCurrentItem(2);
            case 2:
                ivMemory.setSelected(true);
                ivCurrent = ivMemory;
                tvMemory.setSelected(true);
                tvCurrent = tvMemory;
                break;
            case R.id.llSettings:
                viewPager.setCurrentItem(3);
            case 3:
                ivSettings.setSelected(true);
                ivCurrent = ivSettings;
                tvSettings.setSelected(true);
                tvCurrent = tvSettings;
                //initialize
                first_name.setText("0");
                last_name.setText("0");
                address.setText("0");
                postcode.setText("0");
                phone_number.setText("0");

                try {
                    first_name.setText(dbhelper.get_user_Details(value)[0]);
                    last_name.setText(dbhelper.get_user_Details(value)[1]);
                    address.setText(dbhelper.get_user_Details(value)[2]);
                    postcode.setText(dbhelper.get_user_Details(value)[3]);
                    phone_number.setText(dbhelper.get_user_Details(value)[4]);

                } catch (Exception e) {
                    Log.e("Error case 3:", e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    private void initData() {

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> data = new ArrayList<>();
                ChatModel model = new ChatModel();
                model.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/y-nghia-icon-facebook-100x100.jpg");
                model.setContent(content);
                data.add(new ItemModel(ItemModel.CHAT_B, model));
                cadapter.addAll(data);
                et.setText("");
                Hide_KeyBoard(et);
            }
        });

    }

    private void Hide_KeyBoard(View v) {
        InputMethodManager keyboard = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (keyboard.isActive()) {
            keyboard.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

}

