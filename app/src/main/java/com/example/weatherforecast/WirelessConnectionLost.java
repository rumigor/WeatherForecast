package com.example.weatherforecast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import androidx.core.app.NotificationCompat;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;


public class WirelessConnectionLost extends BroadcastReceiver {
    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action)
        {
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                int extra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
                if(extra==WifiManager.WIFI_STATE_ENABLED)
                {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3")
                            .setSmallIcon(R.drawable.title_small)
                            .setContentTitle("Weather Forecast")
                            .setContentText(context.getString(R.string.wififound));
                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(messageId++, builder.build());
                }else if ((extra== WIFI_STATE_DISABLED))  {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3")
                            .setSmallIcon(R.drawable.title_small)
                            .setContentTitle("Weather Forecast")
                            .setContentText(context.getString(R.string.wifiLost));
                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(messageId++, builder.build());
            }
                break;
        }




    }
}
