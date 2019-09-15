package gr.artibet.lapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleFormActivity extends AppCompatActivity {

    // Form views
    private TextInputEditText etTag;
    private TextInputEditText etOwner;
    private TextInputEditText etDriver;
    private Switch swActive;

    private boolean mCreate = false;
    Vehicle mVehicle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_form);

        // If vehicle extra exist edit, else create new
        Intent intent = getIntent();
        String json = intent.getStringExtra("vehicle");
        if (json != null) {
            mVehicle = new Gson().fromJson(json, Vehicle.class);
            mCreate = false;
        }
        else {
            mVehicle = new Vehicle();
            mCreate = true;
        }


        // Get views
        etTag = findViewById(R.id.vehicle_tag);
        etOwner = findViewById(R.id.vehicle_owner);
        etDriver = findViewById(R.id.vehicle_driver);
        swActive = findViewById(R.id.vehicle_isactive);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivOk = toolbar.findViewById(R.id.ivOk);
        ImageView ivCancel = toolbar.findViewById(R.id.ivCancel);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);

        // Set actionbar and data if editing
        if (mCreate) {
            ivTitle.setText(getString(R.string.add_vehicle));
        }
        else {
            ivTitle.setText(getString(R.string.edit_vehicle));
            etTag.setText(mVehicle.getTag());
            etOwner.setText(mVehicle.getOwner().getUsername());
            etDriver.setText(mVehicle.getDriver());
            swActive.setChecked(mVehicle.isActive());
        }

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVehicle();
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

    // Create vehicle
    private void saveVehicle() {

        // Get field values
        String tag = etTag.getText().toString().trim();
        String owner = etOwner.getText().toString().trim();
        String driver = etDriver.getText().toString().trim();
        Boolean isactive = swActive.isChecked();

        // Tag validation
        if (tag.isEmpty()) {
            etTag.setError(getString(R.string.required_field));
            etTag.requestFocus();
            return;
        }

        // Owner validation
        if (owner.isEmpty()) {
            etOwner.setError(getString(R.string.required_field));
            etOwner.requestFocus();
            return;
        }

        // Update vehicle fields
        mVehicle.setTag(tag);
        mVehicle.setOwner(null);
        mVehicle.setDriver(driver);
        mVehicle.setActive(isactive);

        if (mCreate) {
            createVehicle();
        }
        else {
            updateVehicle();
        }

    }

    // Send post request to create new sensor
    private void createVehicle() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<Vehicle> call = RetrofitClient.getInstance().getApi().createVehicle(token, mVehicle);
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(VehicleFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(VehicleFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.VEHICLES);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Util.errorToast(VehicleFormActivity.this, t.getMessage());
            }
        });

    }

    // Sen put request to update existing sensor
    private void updateVehicle() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<Vehicle> call = RetrofitClient.getInstance().getApi().updateVehicle(token, mVehicle.getId(), mVehicle);
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(VehicleFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(VehicleFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.VEHICLES);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Util.errorToast(VehicleFormActivity.this, t.getMessage());
            }
        });

    }
}
