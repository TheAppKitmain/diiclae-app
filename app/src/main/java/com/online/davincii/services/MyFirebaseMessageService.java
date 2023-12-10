package com.online.davincii.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.online.davincii.R;
import com.online.davincii.activities.ActivityChat;
import com.online.davincii.activities.DashboardScreen;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.activities.SoldProductActivity;
import com.online.davincii.utils.BaseUtil;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    static int count = 0;
    private String senderId;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        senderId = String.valueOf(BaseUtil.getSenderAccountId(getApplicationContext()));
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            showNotification(remoteMessage);
        }
    }

    public void showNotification(RemoteMessage message) {
        count++;
        Intent intent = null;
        String title = "", body = "", name = "";
        String id = message.getData().get("sender_id");

        if (message.getData().get("type") != null) {
            if (message.getData().get("bookingtype").equals("Sold")){
                title = message.getData().get("title");
                body = message.getData().get("body");
                intent = new Intent(this, SoldProductActivity.class);
                intent.putExtra("notification", "");
            }else{
                intent = new Intent(this, DashboardScreen.class);
                intent.putExtra("id", message.getData().get("booking_id"));
                title = message.getData().get("title");
                body = message.getData().get("body");
            }
        }  else  {
            assert id != null;
            if (!id.equals(senderId)) {
                title = message.getData().get("title");
                body = message.getData().get("message");
                String s_id = message.getData().get("sender_id");
                name = message.getData().get("name");
                intent = new Intent(this, ActivityChat.class);
                intent.putExtra("id", s_id);
                intent.putExtra("notification", "");
                intent.putExtra("image", String.valueOf(message.getData().get("image")));
                intent.putExtra("name", name);
            }
        }
        if (intent == null) {
            return;
        }
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId())
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(count);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext())
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(count);
        }

        builder = builder
                .setSmallIcon(R.drawable.diiclae_color_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSound(alarmSound)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000).
                setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(count);
        notificationManager.notify(1, builder.build());

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}
