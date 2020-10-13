package cf.somwaki.medicalchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Chat extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    FirebaseAuth mAuth;
    FloatingActionButton fab;
    EditText input;
    private FirebaseListAdapter<ChatMessage> adapter;
    ListView listOfMessages;
    public static final String botEmail = "infermedica-bot@somwaki.cf";
    private JSONObject response;
    String TAG = "CHAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        input = (EditText) findViewById(R.id.input);
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
                        Toast.makeText(Chat.this, "Got token", Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Chat.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
                        }
                    });
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() == null) {
            finish();
        }
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

    private void displayChatMessages() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("group");
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


    private void postMessage(String message) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        myRef.push().setValue(new ChatMessage(message, FirebaseAuth.getInstance().getCurrentUser().getEmail()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("group");
        myRef.push().setValue(new ChatMessage(message, FirebaseAuth.getInstance().getCurrentUser().getEmail()));


//        if (message.startsWith("#")) {
//            myRef.push().setValue(new ChatMessage("How do you feel today?", botEmail));
//        }else if (adapter.getItem(adapter.getCount() - 2).getMessageText().startsWith("#")) {
//            try {
//                sendParseRequest(message);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Chat.this, "PARSE REQUEST ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            try {
//                JSONObject finalResp = appendChoice(message);
//                sendDiagnosisRequest(finalResp);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Chat.this, "MAKING CHOICE ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }

        input.setText("");

    }


    private void sendDiagnosisRequest(JSONObject json) throws JSONException {
        JSONObject jsonObjectResponse = null;

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        okhttp3.RequestBody body = RequestBody.create(JSON, json.toString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://chuka-medics.herokuapp.com")
                .post(body)
                .build();
        okhttp3.Response response = null;
        try {
            Log.d("DIAGNOSIS REQUEST", "Executing request + " + json.toString());
            Toast.makeText(this, "DIAGNOSIS REQUEST SENDING REQUEST TO SERVER : " + json.toString(), Toast.LENGTH_SHORT).show();
            response = client.newCall(request).execute();

            assert response.body() != null;
            String networkResponse = response.body().string();
            if (!networkResponse.isEmpty()) {
                Log.d("DIAGNOSIS REQUEST", networkResponse);
                jsonObjectResponse = parseJSONStringToJSONObject(networkResponse);
                this.response = jsonObjectResponse;
                showNextQuestion(jsonObjectResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "DIAGNOSIS ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private JSONObject appendChoice(String choice) throws JSONException {
        JSONObject resp = this.response;

        if (choice.equals("Yes")) {
            resp.put("choice", "1");
        } else if (choice.equals("No")) {
            resp.put("choice", "2");
        }else{
            resp.put("choice", "3");
        }

        return resp;
    }


    private void sendParseRequest(String text) throws JSONException {

        String TAG = "PARSE REQUEST";
        Log.d(TAG, "PREPARING TO SEND /parse REQUEST");
        JSONObject jsonObjectResponse = null;
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("text", text);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.infermedica.com/v2/parse")
                .post(body)
                .addHeader("App-Key", "e5992ccd24bec107b2252f318e18ebcb")
                .addHeader("App-Id", "78979690")
                .addHeader("Content-Type", "application/json")
                .build();
        String message = "SENDING REQUEST TO /parse ...";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);

        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();

            String networkResponse = response.body().string();
            if (!networkResponse.isEmpty()) {
                Log.d(TAG, networkResponse);

                jsonObjectResponse = parseJSONStringToJSONObject(networkResponse);

                JSONArray mentions = jsonObjectResponse.getJSONArray("mentions");
                List<JSONObject> detectedSymptoms =  new ArrayList<>();
                for (int i = 0; i < mentions.length(); i++) {
                    JSONObject symptom = (JSONObject) mentions.get(i);
                    if (symptom.getString("type").equals("symptom")) {
                        String symptom_id = symptom.getString("id");
                        String choice_id = symptom.getString("choice_id");

                        JSONObject newSymp = new JSONObject();
                        newSymp.put("choice_id", choice_id);
                        newSymp.put("id", symptom_id);
                        if (i==0){
                            newSymp.put("initial", "true");
                        }
                        detectedSymptoms.add(newSymp);
                    }
                }
                // TODO: BUILD INITIAL JSON TO SEND TO API
                int age = 25;
                String gender = "male";

                JSONObject finalRequestBody = new JSONObject();
                finalRequestBody.put("age", age);
                finalRequestBody.put("sex", gender);
                finalRequestBody.put("symptoms", detectedSymptoms);
                Toast.makeText(this, "FINAL REQUEST: " + finalRequestBody, Toast.LENGTH_SHORT).show();
                Log.d("FINAL REQUEST: ", finalRequestBody.toString());

                sendDiagnosisRequest(finalRequestBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Response error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void showNextQuestion(JSONObject jsonObjectResponse) throws JSONException {

        JSONObject nextQuestion = (JSONObject)jsonObjectResponse.get("next_question");
        String nextQuestionText = nextQuestion.getString("text");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.push().setValue(new ChatMessage(nextQuestionText, botEmail));

    }


    static JSONObject parseJSONStringToJSONObject(String networkResponse) {
        JSONObject response = null;
        try {
            response = new JSONObject(networkResponse);
            Log.d("ParseJsonStringToObject", response.toString());
        } catch (JSONException e) {
            try{
                response = new JSONObject();
                response.put("result", "failed");
                response.put("data", networkResponse);
                response.put("error", e.getMessage());
                Log.d("ParseJsonStringToObject", response.toString());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }


    public void saveToken(String s, String email) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        // Building request body and adding token and email
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("new_token", s);
            requestBody.put("email", email);

            okhttp3.RequestBody body = RequestBody.create(JSON, requestBody.toString());
            Log.d("CHAT", body.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://zamo-messaging.herokuapp.com/save-token")
                    .post(body)
                    .build();

            okhttp3.Response response = null;

            response = client.newCall(request).execute();

            assert response.body() != null;
            String stringResponse = response.body().toString();

            if (!stringResponse.isEmpty()) {
                // got successful response from server...
                //TODO: check response to see token was saved successfully
                Toast.makeText(this, "TOKEN: " + stringResponse, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SAVING TOKEN-->" + stringResponse);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }

    }

}

