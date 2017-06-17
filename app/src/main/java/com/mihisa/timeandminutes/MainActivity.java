package com.mihisa.timeandminutes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public String date;
    public int countMinut;
    public TextView textView;
    public EditText editText;
    public String[] times = new String[10000];
    public int[] minute = new int[10000];
    public static String time;
    public ArrayList<Contact> list;

    public int mSeconds;
    public boolean isRunning = false;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCEST = "mysettingstime";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_COUNTER_TIME = "timeStamp";
    private SharedPreferences mSettings;
    private SharedPreferences tSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            countMinut = savedInstanceState.getInt("countMinut");
        }
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.timeNow);
        data();
        textView.setText("Now: " + date + " " + countMinut + " minutes");
        batteryStatus();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        tSettings = getSharedPreferences(APP_PREFERENCEST, Context.MODE_PRIVATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("countMinut", countMinut);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            countMinut = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
        }
        if (tSettings.contains(APP_PREFERENCEST)) {
            time = tSettings.getString(APP_PREFERENCEST, "");
        }
        TextView last = (TextView) findViewById(R.id.timeWas);
        last.setText("Last: " + time + " " + countMinut + " minutes");
    }
    public void onClick(View view) {
        isRunning = true;
        runTimer();
    }

    public void onClickStop(View view) {
        isRunning = false;
        countMinut = 0;
    }
    public void data() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(calendar.getTime());
        date = formattedDate;
    }

    public void countTime(String date) {

    }

    public void batteryStatus() {
        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                TextView textBattery = (TextView) findViewById(R.id.percent);
                textBattery.setText(battery + "%");
                TextView textStatus = (TextView) findViewById(R.id.batteryStatus);
                // 1 = BATTERY_STATUS_UNKNOWN
                // 2 = BATTERY_STATUS_CHARGING
                // 3 = BATTERY_STATUS_DISCHARGING
                // 4 = BATTERY_STATUS_NOT_CHARGING
                // 5 = BATTERY_STATUS_FULL
                if (status == 5) {
                    textStatus.setText("Full");
                } else if (status == 3) {
                    textStatus.setText("Discharging");
                } else if (status == 2) {
                    textStatus.setText("Charging");
                } else if (status == 4) {
                    textStatus.setText("Not Charging");
                } else {
                    textStatus.setText("Unknown");
                }
            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }
    public void runTimer(){
        data();
        time = date;
        final TextView timeTextView = (TextView) findViewById(R.id.timeNow);
        final Handler handler = new Handler();
        final ArrayList<Contact> list = new ArrayList<Contact>();
        final ListView myList = (ListView) findViewById(R.id.listView);
        //final String count = String.valueOf(countMinut);
        final ListAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item, new String[]{Contact.TIME, Contact.MINUTES},
                new int[]{R.id.timeStamp, R.id.minutes});
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                data();
                time = date;
                final String count = String.valueOf(countMinut);
                list.add(new Contact(time, count));
                myList.setAdapter(adapter);
                timeTextView.setText("Now: " + date + " " + countMinut + " minutes");
                System.out.println(countMinut);
                System.out.println(count);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_COUNTER, countMinut);
                editor.apply();
                SharedPreferences.Editor editor1 = tSettings.edit();
                editor1.putString(APP_PREFERENCEST, time);
                editor1.apply();
                countMinut++;
                handler.postDelayed(this, 60000);

            } }
        });
    }

}
