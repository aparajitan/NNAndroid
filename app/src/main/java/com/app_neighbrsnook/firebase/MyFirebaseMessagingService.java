package com.app_neighbrsnook.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;

import androidx.core.app.NotificationCompat;

import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import android.annotation.SuppressLint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";
    private SharedPrefsManager sm;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize SharedPrefsManager here
        sm = new SharedPrefsManager(this);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        if (sm.containsKey("user_id")) {
            String loginToken = sm.getString("firebase_token", "");

            if (loginToken.isEmpty() || !loginToken.equals(token)) {
                sendTokenToServer(token);
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Get notification data
        String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "Notification";
        String message = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "You have a new message.";

        Log.d(TAG, "Notification Title: " + title);
        Log.d(TAG, "Notification Message: " + message);

        // Show notification
        sendNotification(title, message);

        // Check for specific titles and restart MainActivity
        if (title != null && (title.equals("Welcome to Neighbrsnook!") || title.equals("Incomplete Document!"))) {
            restartMainActivity();
        }
    }

    private void restartMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add a unique request code to each PendingIntent
        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(requestCode, notificationBuilder.build());
    }

    private void sendTokenToServer(String token) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("firebase_token", token);

        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<CommonPojoSuccess> call = service.updateTokenApi(hm);

        call.enqueue(new Callback<CommonPojoSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommonPojoSuccess commonPojoSuccess = response.body();
                    if ("success".equals(commonPojoSuccess.getStatus())) {
                        sm.setString("firebase_token", token);
                        Log.d(TAG, "Token updated in MyFirebaseMessagingService");
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                Log.e(TAG, "API Failure in MyFirebaseMessagingService: " + t.getMessage());
            }
        });
    }
}