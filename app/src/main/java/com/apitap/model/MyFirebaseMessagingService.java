package com.apitap.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.SplashActivity;
import com.apitap.views.fragments.FragmentHome;
import com.apitap.views.fragments.Fragment_Ads;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Created by rishav on 6/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String msg= "A merchant you are following as a favorite has posted new items, specials, or ads. Please check them out in ApiTap to see what new they have available.";
    String amount,requestid;
    NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From:" + remoteMessage.getFrom());

        Log.e(TAG, "message"+remoteMessage.getData());

        //sendNotification(remoteMessage.getFrom() +"aPITAP");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
          //  Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
            //resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
            //showNotificationMessage(getApplicationContext(), "Tidif",noti_type, timestamp, resultIntent);
            //startActivity(resultIntent);


        } else {

            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
            LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
            showNotificationMessage(getApplicationContext(), "ApiTap", msg, Utils.GetToday(), resultIntent);

        }

        EventBus.getDefault().post(new Event(Constants.FCM_NOTIFICATION, ""));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),"New Arrivals Received",Toast.LENGTH_SHORT).show();
            }
        });


        // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());

            //  {"response":{"id":"157","ride_request_id":"2","pickup_cordinates":"30.709818962798344,76.69353794306517",
            // "driver_cordinates":"30.7097677,76.693525","drop_cordinates":"30.714478,76.7148927",
            // "name":"Shami","profile_pic":"5210_1507533841.png","datetime":"23\/05\/2018 04:11PM",
            // "mobile":"9501875983","company_name":"Cabscout1"}}



        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Calling method to generate notification
          //  sendNotification(remoteMessage.getBody());
    }
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("ApiTap Push Notification")
                .setContentText("A merchant you are following as a favorite has posted new items, specials, or ads. Please check them out in ApiTap to see what new they have available.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
              //  .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}