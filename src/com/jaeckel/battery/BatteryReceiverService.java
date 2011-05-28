package com.jaeckel.battery;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryReceiverService extends Service {

    private final String TAG = "BatteryReceiverService";
    private Handler mHandler = new Handler();

    private BatteryBroadcastReceiver br;

    private boolean registered = false;
    private IntentFilter intentFilter;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "----> SERVICE.onCreate()");

        toastMsg("Service created");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);

        Log.d(TAG, "Starting BatteryReceiverService");
        // create BroadcastReceiver
        br = new BatteryBroadcastReceiver();
        intentFilter = createBatteryIntentFilter();

        if (prefs.getBoolean("auto_start_service", false)) {
            // register battery handler here
            this.registerReceiver(br, intentFilter);
            registered = true;
        }
    }


    private IntentFilter createBatteryIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        return intentFilter;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

            toastMsg("BatteryReceiverService: Prefs changed");
            if ("auto_start_service".equals(s)) {

                if (sharedPreferences.getBoolean("auto_start_service", false)) {
                    toastMsg("BatteryReceiverService: Prefs changed: auto_start_service: " + true);
                    BatteryReceiverService.this.registerReceiver(br, intentFilter);
                    registered = true;

                } else {

                    toastMsg("BatteryReceiverService: Prefs changed: auto_start_service: " + false);
                    if (registered) {
                        BatteryReceiverService.this.unregisterReceiver(br);
                        registered = false;
                    }


                }
            }


        }
    };

    private void toastMsg(final String msg) {
        mHandler.post(
                new Runnable() {
                    public void run() {
                        Toast.makeText(BatteryReceiverService.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

