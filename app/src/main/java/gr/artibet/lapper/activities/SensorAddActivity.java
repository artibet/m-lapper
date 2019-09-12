package gr.artibet.lapper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);
        ivTitle.setText(getString(R.string.add_sensor));

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
            mAa.setError(getString(R.string.required_field));
            mAa.requestFocus();
            return;
        }

        // Tag validation
        if (tag.isEmpty()) {
            mTag.setError(getString(R.string.required_field));
            mTag.requestFocus();
            return;
        }

        // Threshold validation
        if (threshold.isEmpty()) {
            mThreshold.setError(getString(R.string.required_field));
            mThreshold.requestFocus();
            return;
        }

        // Send post request to add new sensor
        Sensor sensor = new Sensor();
        sensor.setAa(Long.parseLong(aa));
        sensor.setTag(tag);
        sensor.setThreshold(Integer.parseInt(threshold));
        sensor.setActive(isactive);
        sensor.setStart(isstart);

        String token = SharedPrefManager.getInstance(this).getToken();
        Call<Sensor> call = RetrofitClient.getInstance().getApi().createSensor(token, sensor);
        call.enqueue(new Callback<Sensor>() {
            @Override
            public void onResponse(Call<Sensor> call, Response<Sensor> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorAddActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(SensorAddActivity.this, response.message());
                }
                else {
                    Util.successToast(SensorAddActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(SensorAddActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.SENSORS);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Sensor> call, Throwable t) {
                Util.errorToast(SensorAddActivity.this, getString(R.string.sensor_create_failed));
            }
        });

    }
}
