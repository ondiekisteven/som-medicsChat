package cf.somwaki.medicalchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class RemediesList extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies_list);
        listView = (ListView) findViewById(R.id.remediesListView);
        showRemedies();

    }

    private void showRemedies() {
        ArrayList<RemedyItem> items = new ArrayList<RemedyItem>(Arrays.asList(RemediesItemsList.items));
        Log.d("REMEDIES LIST", items.toString());
        listView.setAdapter(new RemedyListAdapter(getApplicationContext(), items));
    }

    private class RemedyListAdapter extends ArrayAdapter<RemedyItem> {

        List<RemedyItem> objects;
        public RemedyListAdapter(@NonNull Context context, @NonNull List<RemedyItem> objects) {
            super(context, R.layout.remedy_item_template, objects);
            this.objects = objects;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.remedy_item_template, parent, false);

            TextView title = (TextView) view.findViewById(R.id.remedyListTitle);
            TextView number = (TextView) view.findViewById(R.id.remedyListNumber);
            TextView body = (TextView) view.findViewById(R.id.remedyListBody);

            title.setText(objects.get(position).getTitle());
            number.setText(objects.get(position).getId());
            body.setText(objects.get(position).getBody());
            return view;
        }
    }

}