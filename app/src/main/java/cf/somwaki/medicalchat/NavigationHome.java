package cf.somwaki.medicalchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NavigationHome extends AppCompatActivity {
    String TAG = "NAVIGATION HOME";
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    public static String TOKEN;
    CardView chat_cardView, notificationCardView, appointmentsCardView, eClinicCardView, pharmacyCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        chat_cardView = (CardView)findViewById(R.id.chat_cardView);
        notificationCardView = (CardView) findViewById(R.id.notifiation_cardView);
        appointmentsCardView = (CardView) findViewById(R.id.appointmentsCard);
        eClinicCardView = (CardView) findViewById(R.id.eClinicCard);
        pharmacyCardView = (CardView) findViewById(R.id.pharmacyCard);

        mAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(NavigationHome.this, "Could not get token", Toast.LENGTH_SHORT).show();
                            Log.w("NAVIGATION", task.getException());
                            return;
                        }
                        TOKEN = Objects.requireNonNull(task.getResult()).getToken();

                        Log.d("DEVICE TOKEN", TOKEN);
                    }
                });

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationHome.this, Chat.class);
//                SNACKBAR REMOVED HERE
//                Snackbar.make(view, "Opening Chat", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(intent);
            }
        });
        handleHomeMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            }else{
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(NavigationHome.this, "You have been logged out", Toast.LENGTH_LONG).show();
                }
            });
        }else if (item.getItemId() == R.id.action_settings){
            Toast.makeText(this, "SETTINGS ACTIVITY", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.help) {
            Toast.makeText(this, "HELP ACTIVITY", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void handleHomeMenu() {
        chat_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(getApplicationContext(), Chat.class);
                startActivity(chatIntent);
            }
        });
        notificationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNotificationIntent = new Intent(getApplicationContext(), Notification.class);
                startActivity(openNotificationIntent);
            }
        });
        appointmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAppointmentIntent = new Intent(getApplicationContext(), Appointments.class);
                startActivity(openAppointmentIntent);
            }
        });
        eClinicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openClinicChooser = new Intent(getApplicationContext(), ClinicChooser.class);
                startActivity(openClinicChooser);
            }
        });

    }

}
