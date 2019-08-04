package com.moyersoftware.bru.service.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.MainActivity;
import com.moyersoftware.bru.main.OnTapFragment;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TYPE_ON_TAP_UPDATED = "on_tap_updated";
    private static final int ON_TAP_NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Util.Log("Received FCM message");

        try {
            Util.Log("FCM message: " + new Gson().toJson(new JSONObject(remoteMessage.getData())));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bru updates";
            String description = "On tap beer updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1");
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

    @Override
    public void onNewToken(String refreshedToken) {
        if (!Util.isLoggedIn() || !Util.notificationsEnabled()) return;

        updateFcmToken(refreshedToken);
    }

    private void updateFcmToken(String refreshedToken) {
        Call<Void> call = ApiFactory.getApiService().updateGoogleToken(refreshedToken,
                Util.getProfile().getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
