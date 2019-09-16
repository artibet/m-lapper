package gr.artibet.lapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleFormActivity extends AppCompatActivity {

    // Form views
    private TextInputEditText etTag;
    private Spinner spOwner;
    private TextInputEditText etDriver;
    private Switch swActive;
    private TextView tvDescription;

    private boolean mCreate = false;
    Vehicle mVehicle = null;

    // List of users to select from if admim
    List<User> mUserList = new ArrayList<User>();


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
        etTag = findViewById(R.id.vehicle_form_tag);
        spOwner = findViewById(R.id.vehicle_form_owner);
        etDriver = findViewById(R.id.vehicle_form_driver);
        swActive = findViewById(R.id.vehicle_form_isactive);
        tvDescription = findViewById(R.id.vehicle_form_description);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivOk = toolbar.findViewById(R.id.ivOk);
        ImageView ivCancel = toolbar.findViewById(R.id.ivCancel);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);

        // Set actionbar and data if editing
        if (mCreate) {
            ivTitle.setText(getString(R.string.add_vehicle));

            // Set owner to login user
            User owner = new User();
            LoginUser loginUser = SharedPrefManager.getInstance(this).getLoggedInUser();
            owner.setId(loginUser.getId());
            owner.setEmail(loginUser.getEmail());
            owner.setFirstName(loginUser.getFirstName());
            owner.setLastLogin(loginUser.getLastName());
            owner.setUsername(loginUser.getUsername());
            owner.setSuperUser(loginUser.isSuperuser());
            owner.setStaff(loginUser.isStaff());
            mVehicle.setOwner(owner);
        }
        else {
            ivTitle.setText(getString(R.string.edit_vehicle));
            etTag.setText(mVehicle.getTag());
            etDriver.setText(mVehicle.getDriver());
            swActive.setChecked(mVehicle.isActive());
            tvDescription.setText(mVehicle.getDescription());
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

        // If not superuser remove Owner spinner else fetch users
        fetchUsers();
    }

    // Fetch User list if superuser
    private void fetchUsers() {

        if (!SharedPrefManager.getInstance(this).isAdmin()) {
            LinearLayout ownerLayout = findViewById(R.id.vehicle_owner_layout);
            ((LinearLayout)ownerLayout.getParent()).removeView(ownerLayout);  // hide user spinner
            return;
        }

        // User is admin - fetch user list and customize spinner owner
        Call<List<User>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUsers(SharedPrefManager.getInstance(this).getToken());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (!response.isSuccessful()) {
                    Util.errorToast(VehicleFormActivity.this, response.message());
                }
                else {
                    mUserList = response.body();
                    ArrayAdapter<User> adapter = new ArrayAdapter<User>(VehicleFormActivity.this, android.R.layout.simple_spinner_item, mUserList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spOwner.setAdapter(adapter);

                    // Set selected owner
                    for (int i=0; i<spOwner.getCount(); i++) {
                        User user = (User)spOwner.getItemAtPosition(i);
                        if (user.getId() == mVehicle.getOwner().getId()) {
                            spOwner.setSelection(i);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Util.errorToast(VehicleFormActivity.this, t.getMessage());
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
        String driver = etDriver.getText().toString().trim();
        Boolean isactive = swActive.isChecked();
        String description = tvDescription.getText().toString().trim();

        // Tag validation
        if (tag.isEmpty()) {
            etTag.setError(getString(R.string.required_field));
            etTag.requestFocus();
            return;
        }

        // Update vehicle fields
        mVehicle.setTag(tag);
        mVehicle.setDriver(driver);
        mVehicle.setActive(isactive);
        mVehicle.setDescription(description);

        // If superuser set owner
        if (SharedPrefManager.getInstance(this).isAdmin()) {
            mVehicle.setOwner((User)spOwner.getSelectedItem());
        }

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
        Call<Vehicle> call = RetrofitClient.getInstance().getApi().createVehicle(
                token,
                mVehicle.getTag(),
                mVehicle.getOwner().getId(),
                mVehicle.getDriver(),
                mVehicle.isActive(),
                mVehicle.getDescription()
        );
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
        Call<Vehicle> call = RetrofitClient.getInstance().getApi().updateVehicle(
                token, mVehicle.getId(),
                mVehicle.getTag(),
                mVehicle.getOwner().getId(),
                mVehicle.getDriver(),
                mVehicle.isActive(),
                mVehicle.getDescription()

        );
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
