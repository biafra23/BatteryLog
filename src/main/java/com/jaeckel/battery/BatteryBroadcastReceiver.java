package com.jaeckel.battery;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private String filename = "/sdcard/battery.log";
    private int nRef = 1;
    private final String TAG = "BatteryBroadcastReceiver";

    @Override
    public void onReceive(Context c, Intent intent) {

        Log.i(TAG, "        Context:" + c);
        Log.i(TAG, "received intent: " + intent);
        Log.i(TAG, "received intent.getType(): " + intent.getType());
        Log.i(TAG, "received intent.describeContents(): " + intent.describeContents());
        Log.i(TAG, "received Intent.getDataString(): " + intent.getDataString());
        if (intent.getExtras() != null) {


            Log.i(TAG, "extra: " + intent.getExtras().keySet());

            Bundle extras = intent.getExtras();
            Log.i(TAG, "extra.describeContents(): " + extras.describeContents());

            for (String key : extras.keySet()) {
                Log.i(TAG, key + ": " + extras.get(key));
            }
            writeLine(new Date() + " | status: " + extras.get("status")
                    + " level: " + extras.get("level")
                    + " temperature: " + extras.get("temperature")
                    + " plugged: " + extras.get("plugged")
                    + " scale: " + extras.get("scale")
                    + " health: " + extras.get("health")
                    + " voltage: " + extras.get("voltage")


            );

            NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

            int icon = R.drawable.icon;
            String tickerText = "Battery " + extras.get("level") + "% ";
            long when = System.currentTimeMillis();
            Notification notification = new Notification(icon, tickerText, when);
            Intent i = new Intent(c, BatteryLog.class);
            PendingIntent pi = PendingIntent.getActivity(c, 0, i, 0);
            if (extras.getInt("level") == 2) {
                notification.sound = Uri.fromFile(new File("/system/media/audio/ringtones/Ring_Classic_02.ogg"));
            }

            if (extras.getInt("level") == 100) {
                notification.defaults = Notification.DEFAULT_SOUND;
            }
            notification.setLatestEventInfo(c, "Battery changed",
                    extras.get("level") + "% "
                            + convTemp(extras.getInt("temperature")) + "° "
                            + extras.get("plugged"), pi);

            nm.notify(nRef, notification);

        } else {

            c.startService(new Intent(c, BatteryReceiverService.class));

        }
    }

    private double convTemp(int temp) {
        return temp / 10.0;
    }

    private void writeLine(String log) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            out.write(log);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't open logfile " + filename + " for writing", e);
        }
    }
}
