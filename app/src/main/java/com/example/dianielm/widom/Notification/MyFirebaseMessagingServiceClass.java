package com.example.dianielm.widom.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dianielm.widom.NotificationsActivity;
import com.example.dianielm.widom.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Daniel on 24/10/2016.
 *
 * Klasa odpowiedzialna za notyfikacje
 * Wywoływana, gdy wiadomość zostanie odebrana.
 * RemoteMessage - obiekt reprezentujący komunikatu otrzymanego z Firebase Cloud Messaging.
 */

public class MyFirebaseMessagingServiceClass extends FirebaseMessagingService {
    public static final String TAG = "NOTIFICATION";
    //TAG'a używamy np gdy chcemy przekazać wiadomość

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Wyświetlam loga od kogo dostaliśmy wiadomość
        String from = remoteMessage.getFrom();
        Log.d(TAG, "Message received from: " + from);
        //Sprawdzamy czy wiadomość zawiera jakies dane
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification: " + remoteMessage.getNotification().getBody());
            //Wywoływana metoda sendNotification zadeklarowana poniżej. Tworzy ona widok powiadomienia.
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        //Sprawdzenie czy wiadomość zawiera właściwe dane powiadomienia
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }

    }

    /**
     * Tworzenie i pokazanie prostego powiadomienia zawierającego odebraną wiadomość FCM
     * Ciało wiadomości i jej odbieranie przez FCM.
     */
    private void sendNotification(String title, String body) {

        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Buduje notyfikacje z atrzybutami icon, title, text itd.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_border_color_black_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}