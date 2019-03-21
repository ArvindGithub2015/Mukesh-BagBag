package zuzusoft.com.bagbag.helper.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import zuzusoft.com.bagbag.get_start.GetStartActivity;
import zuzusoft.com.bagbag.home.HomeActivity;


/**
 * Created by mukeshnarayan on 19/05/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

        private NotificationUtils notificationUtils;

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.e(TAG, "From: " + remoteMessage.getFrom());

            if (remoteMessage == null)
                return;

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                //I don't need notification since I have data payload.
                //handleNotification(remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        //Log.e(TAG, "push json: " + json.toString());

        try {

            //JSONObject data = json.getJSONObject("data");

            //String timestamp = data.getString("timestamp");
            JSONObject payloadJObj = json.getJSONObject("payload");

            Log.e(TAG, "payload: " + payloadJObj.toString());
           // Log.e(TAG, "timestamp: " + timestamp);

            FCMPayload paylaod = new FCMPayload();

            paylaod.setInBagId(payloadJObj.getString("in_bag_id"));
            paylaod.setInUserId(payloadJObj.getString("in_user_id"));
            paylaod.setInUserImage(payloadJObj.getString("in_user_image"));
            paylaod.setInUserName(payloadJObj.getString("in_user_name"));
            paylaod.setMyId(payloadJObj.getString("my_id"));
            paylaod.setMyName(payloadJObj.getString("my_name"));
            paylaod.setMyBagId(payloadJObj.getString("my_bag_id"));
            paylaod.setMyImage(payloadJObj.getString("my_image"));
            paylaod.setMyBagImage(payloadJObj.getString("my_bag_image"));
            paylaod.setInBagImage(payloadJObj.getString("in_bag_image"));
            paylaod.setScore(payloadJObj.getString("score"));


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("payload", paylaod);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                /*Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }*/
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}


