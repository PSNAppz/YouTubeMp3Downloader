package com.ekathvatech.onlinemp3downloader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by PSNAppZ on 8/23/17.
 */

public class PushNotificationService extends FirebaseMessagingService {
        private static final String TAG = "FCM Service";
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // TODO: Handle FCM messages here.
            // If the application is in the foreground handle both data and notification messages here.
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated.
            sendNotification(remoteMessage.getNotification().getBody());
        }

    public void sendNotification(String message) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

//Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.ic_music_video_white_24dp);
        mBuilder.setContentTitle("Online Mp3 Downloader");
        mBuilder.setContentText(message);
        mBuilder.setAutoCancel(true);


        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
}