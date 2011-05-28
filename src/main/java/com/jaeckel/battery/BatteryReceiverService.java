package com.jaeckel.battery;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BatteryReceiverService extends Service {


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub


        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("----> SERVICE.onCreate()");

        // create BroadcastReceiver
        BroadcastReceiver br = new BatteryBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        // register battery handler here
        registerReceiver(br, intentFilter);
    }


}

