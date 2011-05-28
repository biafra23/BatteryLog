package com.jaeckel.battery;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class EditPreferences extends PreferenceActivity {


    final private static int MENU_MAP = 0;
    final private static int MENU_SAVE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        this.startService(new Intent(this, BatteryReceiverService.class));

//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        if (prefs.getBoolean("auto_start_service", false)) {
//            // create BroadcastReceiver
//            br = new BatteryBroadcastReceiver();
//            IntentFilter intentFilter = createBatteryIntentFilter();
//            // register battery handler here
//            this.registerReceiver(br, intentFilter);
//            Toast.makeText(this, "Registered receiver ", Toast.LENGTH_SHORT).show();
//        }

    }

//    private IntentFilter createBatteryIntentFilter() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        return intentFilter;
//    }
}