package cf.somwaki.medicalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClinicChooser extends AppCompatActivity {

    TextView bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_chooser);

        bmi = (TextView) findViewById(R.id.clinicService_bmiCalc);
        process();
    }

    private void process() {
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBMI = new Intent(getApplicationContext(), BMI_Calculator.class);
                startActivity(openBMI);
            }
        });
    }


}
