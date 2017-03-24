package com.moyersoftware.bru.service.firebase;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.MainActivity;
import com.moyersoftware.bru.main.OnTapFragment;
import com.moyersoftware.bru.util.Util;

import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TYPE_ON_TAP_UPDATED = "on_tap_updated";
    private static final int ON_TAP_NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            JSONObject message = new JSONObject(remoteMessage.getData());
            if (message.getString("type").equals(TYPE_ON_TAP_UPDATED)) {
                sendBroadcast(new Intent(OnTapFragment.UPDATE_BEERS_INTENT));

                if (!Util.isLoggedIn() || !Util.notificationsEnabled()) return;
                showBeersUpdatedNotification();
            }
        } catch (Exception e) {
            Util.Log("Can't parse FCM message: " + e);
        }
    }

    private void showBeersUpdatedNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("On Tap beers updated!");
        notificationBuilder.setContentText("Tap to see the list.");
        notificationBuilder.setWhen(0);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setSmallIcon(R.drawable.notification_default);
        notificationBuilder.setColor(ContextCompat.getColor(this, R.color.colorAccent));

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.putExtra(MainActivity.OPEN_ON_TAP_EXTRA, true);
        PendingIntent bookingsPendingIntent =
                PendingIntent.getActivity(
                        this,
                        1,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(bookingsPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from
                (getApplicationContext());
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(ON_TAP_NOTIFICATION_ID, notification);
    }
}
