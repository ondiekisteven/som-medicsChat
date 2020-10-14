package cf.somwaki.medicalchat;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatIndividual extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth mAuth;
    FloatingActionButton fab;
    EditText input;
    private FirebaseListAdapter<ChatMessage> adapter;
    ListView listOfMessages;
    String TAG = "INDIVIDUAL CHAT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_individual);

        listOfMessages = (ListView) findViewById(R.id.list_of_messages_individual);
        fab = (FloatingActionButton) findViewById(R.id.fab_individual);
        input = (EditText) findViewById(R.id.input_individual);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Please log in first", Toast.LENGTH_LONG).show();
            create_sign_in_intent();
        }
        if (mAuth.getCurrentUser() != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        Toast.makeText(ChatIndividual.this, "Got token", Toast.LENGTH_SHORT).show();
//                        saveToken(token, mAuth.getCurrentUser().getEmail());
                    }
                }
            });
            displayChatMessages();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = input.getText().toString();
                    postMessage(message);
                }
            });
            listenEvents();
        }
    }

    private void listenEvents() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatsRef = rootRef.child("messages");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(ChatIndividual.this, "Data changed: " + snapshot.toString(), Toast.LENGTH_SHORT).show();
                Log.d("CHAT INDIVIDUAL", snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatsRef.addValueEventListener(valueEventListener);
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
            Snackbar.make(input, "Error loading messages", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            adapter.startListening();
        } catch (Exception e) {
            Snackbar.make(input, "Error loading messages.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("CHAT", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void postMessage(String message) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));
        myRef.push().setValue(new ChatMessage(message, mAuth.getCurrentUser().getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChatIndividual.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
        input.setText("");
    }



    private void displayChatMessages() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("messages")
                .child(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.my_message)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                Log.d("LIST ADAPTER", model.getMessageText());
                TextView text = (TextView) v.findViewById(R.id.message_text);
                TextView sender = (TextView) v.findViewById(R.id.message_user);
                TextView time = (TextView) v.findViewById(R.id.message_time);

                sender.setText(model.getMessageUser());
                time.setText(DateFormat.format("HH:mm",
                        model.getMessageTime()));
                text.setText(model.getMessageText());

                if (model.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    v.setBackgroundResource(R.drawable.my_message);
                    text.setTextColor(0xffffffff);
                    sender.setTextColor(0xffffffff);
                    time.setTextColor(0xffffffff);
                }else{
                    v.setBackgroundResource(R.drawable.their_message);
                    text.setTextColor(Color.BLACK);
                    sender.setTextColor(Color.BLACK);
                    time.setTextColor(Color.BLACK);
                }
            }
        };
        listOfMessages.setAdapter(adapter);
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
                            Toast.makeText(ChatIndividual.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
                        }
                    });
            finish();
        }
        return true;
    }

    public void create_sign_in_intent(){
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
}
