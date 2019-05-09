package com.example.termtracker_c196.Helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AlarmHelper extends ContextWrapper {
    public static final String CHANNEL_1_ID = "channel_1";
    public static final String CHANNEL_2_ID = "channel_2";
    public static final String channel1name = "WGU Course Notifications";
    public static final String channel2name = "WGU Assessment Notifications";
    private NotificationManager manager;

    public AlarmHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,
                    channel1name, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("This channel shows course notifications for the WGU Progress Tracker App");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,
                    channel2name, NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("This channel shows assessment notifications for the WGU Progress Tracker App");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

            getManager().createNotificationChannel(channel1);
            getManager().createNotificationChannel(channel2);

        }
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public NotificationCompat.Builder getChannel1Notifcation(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(message);

    }

    public NotificationCompat.Builder getChanne21Notifcation(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setContentTitle(title)
                .setContentText(message);

    }
}
