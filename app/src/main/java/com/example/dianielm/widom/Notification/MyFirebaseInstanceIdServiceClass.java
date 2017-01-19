package com.example.dianielm.widom.Notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Daniel on 24/10/2016.
 *
 * Klasa odpowiedzialna za notyfikacje
 */

public class MyFirebaseInstanceIdServiceClass extends FirebaseInstanceIdService {
    //TAG'a używamy np gdy chcemy przekazać wiadomość
    public static final String TAG = "NOTIFICATION";

    @Override
    public void onTokenRefresh() {

        //Dostań zaaktualizowany InstanceID token.
        //Wyświetla się on w konsoli AndroidStudio, na ten token można wysłać notyfikacje w konsoli firebase
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);

        sendRegistrationToServer(token);
    }

    //W tej metodzie można wysłać token na serwer
    //Aby wysłać notyfikacje do poszczególnego urządzenia jest potrzebny taki token
    private void sendRegistrationToServer(String token) {

    }
}