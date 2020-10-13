package cf.somwaki.medicalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Appointments extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText patientEmail, purpose, selectedDate, selectedTime;
    String TAG = "APPOINTMENTS";
    Calendar calendar = Calendar.getInstance();
    Button createAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        patientEmail = (EditText) findViewById(R.id.patientEmail);
        purpose = (EditText) findViewById(R.id.purpose);
        selectedDate = (EditText) findViewById(R.id.select_date_edittext);
        selectedTime = (EditText) findViewById(R.id.select_time_edittext);
        createAppointment = (Button) findViewById(R.id.create_appointments_button);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, Appointments.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, Appointments.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        // DATE AND TIME PICKERS...
        selectedTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timePickerDialog.show();
                }
            }
        });
        selectedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show();
                }
            }
        });


        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Appointments.this, "Add Some Action", Toast.LENGTH_SHORT).show();
                createAppointment(calendar.getTimeInMillis());
            }
        });

    }

    private void createAppointment(long timeInMillis) {

        String date = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(calendar.get(Calendar.MONTH)) + "/" + Integer.toString(calendar.get(Calendar.YEAR));
        String time = Integer.toString(calendar.get(Calendar.HOUR)) +":"+Integer.toString(calendar.get(Calendar.MINUTE));

        // GET VALUES
        String pps;
        if (purpose.getText().toString().isEmpty()) {
            purpose.setError("Purpose cannot be empty");
            return;
        } else {
            pps = purpose.getText().toString();
        }

        // CREATE INTENT
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("purpose", pps);
        intent.putExtra("date", date);
        intent.putExtra("time", time);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.RTC_WAKEUP, pendingIntent);

        Toast.makeText(this, "Appointment is set", Toast.LENGTH_SHORT).show();

        patientEmail.setText("");
        purpose.setText("");
        selectedTime.setText("");
        selectedDate.setText("");

        finish();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String message = "Date set on: " + Integer.toString(dayOfMonth) + "/" +Integer.toString(month) + "/" + Integer.toString(year);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
        selectedDate.setText(message);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        String message = "Time set at: " + Integer.toString(hourOfDay) + ":" + Integer.toString(minute);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
        selectedTime.setText(message);
    }
}
