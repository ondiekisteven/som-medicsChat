package cf.somwaki.medicalchat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MyService extends FirebaseMessagingService {

    private static final String TAG = "MyService";
    MyDatabase database;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        database = new MyDatabase(getApplicationContext());
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            database.saveToDatabase(new NotificationItem(remoteMessage.getData().get("title"), remoteMessage.getData().get("body")));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            scheduleNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /*
    * Function for scheduling an notification. When called, it creates a pending intent
    * which opens the Broadcast receiver after a specified time.
    * */
    private void scheduleNotification(String title, String body) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);

        // create a pending intent that will schedule the notification for a specific time
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        assert alarmManager != null;
        // TODO: UPDATE timeInMillis with variable
        alarmManager.setRepeating(AlarmManager.RTC, 20000, AlarmManager.RTC_WAKEUP, pendingIntent);
    }

}
