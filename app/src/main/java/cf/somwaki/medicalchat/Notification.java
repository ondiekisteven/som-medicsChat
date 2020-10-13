package cf.somwaki.medicalchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Notification extends AppCompatActivity {
    MyDatabase database;
    ListView listView;
    List<NotificationItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        database = new MyDatabase(this);
        listView = findViewById(R.id.notifications_list);
        showNotifications();
        Collections.reverse(list);
        listView.setAdapter(new NotificationListAdapter(this, list));
    }

    void showNotifications(){
        Cursor cursor = database.getNotifications();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new NotificationItem(cursor.getString(1), cursor.getString(2)));
            }
        }else{
            Toast.makeText(this, "You have No Notifications", Toast.LENGTH_SHORT).show();
        }
    }

    public static class NotificationListAdapter extends ArrayAdapter<NotificationItem>{

        List<NotificationItem> objects;

        public NotificationListAdapter(@NonNull Context context, @NonNull List<NotificationItem> objects) {
            super(context, R.layout.notification_template, objects);
            this.objects = objects;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.notification_template, parent, false);

            TextView title, body;
            title = view.findViewById(R.id.notification_title);
            body = view.findViewById((R.id.notification_body));

            title.setText(objects.get(position).getTitle());
            body.setText(objects.get(position).getBody());

            return view;
        }
    }

}
