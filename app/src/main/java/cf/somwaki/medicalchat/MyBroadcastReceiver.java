package cf.somwaki.medicalchat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras = intent.getExtras();
        String title, body;
        if (extras != null) {
            if (extras.containsKey("purpose")) {
                title = extras.getString("purpose");
                assert title != null;
                title = title.toUpperCase() + " REMINDER";
                String date = extras.getString("date");
                String with = extras.getString("with");
                String time = extras.getString("time");
                body = "You have an appointment with " + with  + " on " +  date + " at " + time ;

            } else if (extras.containsKey("title")) {
                title = extras.getString("title");
                body = extras.getString("body");
            }
            else{
                title = "title";
                body = "body";
            }
        }
        else{
            return;
        }

        Uri alarm_tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarm_tone);
        ringtone.play();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // intent to open notifications when notification is clicked from system tray
        Intent openNotificationsActivity = new Intent(context, Notification.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openNotificationsActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(0, builder.build());
        addNotificationToDatabase(body, title, context);
    }

    private void addNotificationToDatabase(String body, String title, Context context){

        MyDatabase database = new MyDatabase(context);

        if (database.saveToDatabase(new NotificationItem(title, body))) {
            Toast.makeText(context, "You have a new notification", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error loading your notification", Toast.LENGTH_SHORT).show();
        }

    }
}
