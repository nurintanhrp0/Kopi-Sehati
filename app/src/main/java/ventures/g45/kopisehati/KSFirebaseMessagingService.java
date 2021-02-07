package ventures.g45.kopisehati;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class KSFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences sharedpreferences;
    private static final String TAG = KSFirebaseMessagingService.class.getSimpleName();
    String token;
    SharedPreferences.Editor editor;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        sharedpreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedpreferences.edit();
        token = sharedpreferences.getString("token", "");
        if (token.isEmpty()) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("token", s);
            editor.apply();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleNotification(String message) {
        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            // Intent pushNotification = new Intent(NotifConfig.PUSH_NOTIFICATION);
            //pushNotification.putExtra("message", message);
            //LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

            // If the app is in background, firebase itself handles the notification
            try {
                JSONObject json = new JSONObject(message);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            Log.e(TAG, "Disini: " + message);
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.d(TAG, "Di dalam handleDataMessage");
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String isi = data.getString("message");
            String tipe = data.getString("tipe");
            Integer id = data.getInt("id");


            if (tipe.equals("produk")) {
                //String hasil = data.getString("hasil");
                Intent resultIntent = new Intent(getApplicationContext(), Promo.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("isi", isi);
                resultIntent.putExtra("id", id);
                startActivity(resultIntent);
            } else if (tipe.equals("voucher")){
                // String hasil = data.getString("hasil");
                Intent resultIntent = new Intent(getApplicationContext(), Detail_inbox.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                editor.putString("isi", isi);
                editor.putInt("id", id);
                editor.apply();
                startActivity(resultIntent);

            } else {
                Intent resultIntent = new Intent(getApplicationContext(), Inbox.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(resultIntent);
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
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        /*notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);*/
    }
}
