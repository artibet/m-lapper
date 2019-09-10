package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;

public class SensorAddActivity extends AppCompatActivity {

    // Form views
    private TextInputEditText mAa;
    private TextInputEditText mTag;
    private CheckBox mIsStart;
    private TextInputEditText mThreshold;
    private Switch mIsActive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_form);

        // Get views
        mAa = findViewById(R.id.sensor_aa);
        mTag = findViewById(R.id.sensor_tag);
        mIsStart = findViewById(R.id.sensor_isstart);
        mThreshold = findViewById(R.id.sensor_threshold);
        mIsActive = findViewById(R.id.sensor_isactive);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivOk = toolbar.findViewById(R.id.ivOk);
        ImageView ivCancel = toolbar.findViewById(R.id.ivCancel);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSensor();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    // Go back
    private void cancel() {
        onBackPressed();
    }

    // Create new Sensor
    private void saveSensor() {

        // Get field values
        String aa = mAa.getText().toString().trim();
        String tag = mTag.getText().toString().trim();
        String threshold = mThreshold.getText().toString().trim();
        Boolean isstart = mIsStart.isChecked();
        Boolean isactive = mIsActive.isChecked();

        // A/A validation
        if (aa.isEmpty()) {
            mAa.setError("xxxx");
            mAa.requestFocus();
            return;
        }

        // Tag validation
        if (tag.isEmpty()) {
            mTag.setError("xxxx");
            mTag.requestFocus();
            return;
        }

        // Threshold validation
        if (threshold.isEmpty()) {
            mThreshold.setError("xxxx");
            mThreshold.requestFocus();
            return;
        }

        
    }
}
