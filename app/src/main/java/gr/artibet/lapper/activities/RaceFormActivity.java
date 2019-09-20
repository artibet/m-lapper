package gr.artibet.lapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.RaceMode;
import gr.artibet.lapper.models.RaceStartMethod;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RaceFormActivity extends AppCompatActivity {

    // Form views
    private TextInputEditText etTag;
    private TextInputEditText etLaps;
    private Spinner spStartMethod;
    private Spinner spMode;
    private Switch swIsPublic;
    private TextView tvDescription;

    private boolean mCreate = false;
    Race mRace = null;

    // List of Start Methods and modes to select from
    List<RaceStartMethod> mStartMethodList;
    List<RaceMode> mRaceModeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_form);

        // If race extra exist edit, else create new
        Intent intent = getIntent();
        String json = intent.getStringExtra("race");
        if (json != null) {
            mRace = new Gson().fromJson(json, Race.class);
            mCreate = false;
        }
        else {
            mRace = new Race();
            mCreate = true;
        }

        // Get views
        etTag = findViewById(R.id.race_form_tag);
        etLaps = findViewById(R.id.race_form_laps);
        spStartMethod = findViewById(R.id.race_form_startMethod);
        spMode = findViewById(R.id.race_form_mode);
        swIsPublic = findViewById(R.id.race_form_ispublic);
        tvDescription = findViewById(R.id.race_form_description);

        // Initialize mode and startMethod lists
        mRaceModeList = RaceMode.getList();
        mStartMethodList = RaceStartMethod.getList();
        ArrayAdapter<RaceMode> modeAdapter = new ArrayAdapter<RaceMode>(RaceFormActivity.this, android.R.layout.simple_spinner_item, mRaceModeList);
        ArrayAdapter<RaceStartMethod> startMethodAdapter = new ArrayAdapter<RaceStartMethod>(RaceFormActivity.this, android.R.layout.simple_spinner_item, mStartMethodList);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode.setAdapter(modeAdapter);
        spStartMethod.setAdapter(startMethodAdapter);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivOk = toolbar.findViewById(R.id.ivOk);
        ImageView ivCancel = toolbar.findViewById(R.id.ivCancel);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);

        // Set actionbar and data if editing
        if (mCreate) {
            ivTitle.setText(getString(R.string.add_race));
        }
        else {
            ivTitle.setText(getString(R.string.edit_race));
            etTag.setText(mRace.getTag());
            etLaps.setText(String.valueOf(mRace.getLaps()));
            swIsPublic.setChecked(mRace.isPublic());
            tvDescription.setText(mRace.getDescription());

            // Set selected start method
            for (int i=0; i<spStartMethod.getCount(); i++) {
                RaceStartMethod startMethod = (RaceStartMethod) spStartMethod.getItemAtPosition(i);
                if (startMethod.getId() == mRace.getStartMethod().getId()) {
                    spStartMethod.setSelection(i);
                    break;
                }
            }

            // Set selected mode
            for (int i=0; i<spMode.getCount(); i++) {
                RaceMode raceMode = (RaceMode) spMode.getItemAtPosition(i);
                if (raceMode.getId() == mRace.getMode().getId()) {
                    spMode.setSelection(i);
                    break;
                }
            }
        }

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRace();
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
    private void saveRace() {

        // Get field values
        String tag = etTag.getText().toString().trim();
        String lapsString = etLaps.getText().toString().trim();
        Boolean isPublic = swIsPublic.isChecked();
        String description = tvDescription.getText().toString().trim();

        // Tag validation
        if (tag.isEmpty()) {
            etTag.setError(getString(R.string.required_field));
            etTag.requestFocus();
            return;
        }

        // Laps validation
        if (lapsString.isEmpty()) {
            etLaps.setError(getString(R.string.required_field));
            etLaps.requestFocus();
            return;
        }

        // Update race fields
        mRace.setTag(tag);
        mRace.setLaps(Integer.parseInt(lapsString));
        mRace.setStartMethod((RaceStartMethod)spStartMethod.getSelectedItem());
        mRace.setMode((RaceMode)spMode.getSelectedItem());
        mRace.setPublic(isPublic);
        mRace.setDescription(description);

        // Send API
        if (mCreate) {
            createRace();
        }
        else {
            updateRace();
        }

    }

    // Send post request to create new race
    private void createRace() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<Race> call = RetrofitClient.getInstance().getApi().createRace(
                token,
                mRace.getTag(),
                mRace.getLaps(),
                mRace.getStartMethod().getId(),
                mRace.getMode().getId(),
                mRace.isPublic(),
                mRace.getDescription()
        );
        call.enqueue(new Callback<Race>() {
            @Override
            public void onResponse(Call<Race> call, Response<Race> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(RaceFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to pendingRaces
                    Intent intent = new Intent(RaceFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.PENDING_RACES);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Race> call, Throwable t) {
                Util.errorToast(RaceFormActivity.this, t.getMessage());
            }
        });

    }

    // Sen put request to update existing race
    private void updateRace() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Call<Race> call = RetrofitClient.getInstance().getApi().updateRace(
                token,
                mRace.getId(),
                mRace.getTag(),
                mRace.getLaps(),
                mRace.getStartMethod().getId(),
                mRace.getMode().getId(),
                mRace.isPublic(),
                mRace.getDescription()

        );
        call.enqueue(new Callback<Race>() {
            @Override
            public void onResponse(Call<Race> call, Response<Race> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(RaceFormActivity.this, response.message());
                }
                else {
                    //Util.successToast(SensorFormActivity.this, getString(R.string.sensor_create_success));

                    // Open MainActivity and set fragment to sensors
                    Intent intent = new Intent(RaceFormActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("fragment", MainActivity.PENDING_RACES);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Race> call, Throwable t) {
                Util.errorToast(RaceFormActivity.this, t.getMessage());
            }
        });

    }
}
