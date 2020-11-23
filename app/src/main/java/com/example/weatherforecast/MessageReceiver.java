package com.example.weatherforecast;

import android.app.ApplicationErrorReport;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MessageReceiver extends BroadcastReceiver {

    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BatteryManager.EXTRA_BATTERY_LOW.equals(action)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("Батарея разряжена");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("Батарея в норме!");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());
        }
    }
}
