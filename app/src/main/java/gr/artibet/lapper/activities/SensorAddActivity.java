package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import gr.artibet.lapper.R;

public class SensorAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_add);

        // Set action bar's title
        getSupportActionBar().setTitle("Add sensor");
    }
}
