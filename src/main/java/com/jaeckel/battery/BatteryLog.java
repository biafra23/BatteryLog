package com.jaeckel.battery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class BatteryLog extends ListActivity {

    private String[] mStrings = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        System.out.println(this.getClass().getName() + ": started");
        startService(new Intent(this, BatteryReceiverService.class));

        readData();

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings));
        getListView().setTextFilterEnabled(true);
//
//        // create BroadcastReceiver
//        BroadcastReceiver br = new MyBatteryBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        // register battery handler here
//        registerReceiver(br, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        readData();
    }

    public void readData() {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("/sdcard/battery.log"));
            String str;
            while ((str = in.readLine()) != null) {

                String dateTime = str.substring(0, str.indexOf(" GMT")) + "\n";
                String level = str.substring(str.indexOf("level: ") + 7, str.indexOf(" temp")) + "%";
                String plugged = str.substring(str.indexOf("plugged: ") + 9, str.indexOf(" scale"));

                lines.add(0, dateTime + " " + level + " " + (plugged.equals("0") ? "unplugged" : "plugged"));
            }
            in.close();
        } catch (IOException e) {
        }

        mStrings = lines.toArray(new String[lines.size()]);

    }

}

//class MyBatteryBroadcastReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context c, Intent intent) {
//        ((BatteryLog) c).readData();
//
//    }
//}
