package cf.somwaki.medicalchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatsList extends AppCompatActivity {

    private static final int RC_SIGN_IN = 234;
    ListView chatsList;
    FirebaseAuth mAuth;
    private FirebaseListAdapter<ChatList_Item> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        chatsList = (ListView) findViewById(R.id.chatslist_ListView);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            create_sign_in_intent();
        }
        if (mAuth.getCurrentUser() != null) {
            displayChats();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            adapter.startListening();
        } catch (Exception e) {
//            Snackbar.make(input, "Error loading messages.", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            Log.d("CHAT", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() == null) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            adapter.stopListening();
        } catch (Exception e) {
            Log.d("CHAT", Objects.requireNonNull(e.getMessage()));
//            Snackbar.make(input, "Error loading messages", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChatsList.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
                        }
                    });
            finish();
        }
        return true;
    }

    void create_sign_in_intent(){
        // Creating sign in intent using google
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN
        );
    }

    private void displayChats() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("messages");
        FirebaseListOptions<ChatList_Item> options = new FirebaseListOptions.Builder<ChatList_Item>()
                .setQuery(query, ChatList_Item.class)
                .setLayout(R.layout.chat_list_item)
                .build();
        adapter = new FirebaseListAdapter<ChatList_Item>(options) {
            @Override
            protected void populateView(View v, ChatList_Item model, int position) {
                TextView name = (TextView) v.findViewById(R.id.chat_name);
                name.setText(model.getName());
            }
        };
        chatsList.setAdapter(adapter);
    }
}
