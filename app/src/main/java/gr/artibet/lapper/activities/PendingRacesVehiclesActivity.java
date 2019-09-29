package gr.artibet.lapper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.adapters.PendingRacesVehiclesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.dialogs.SelectVehicleDialog;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingRacesVehiclesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<RaceVehicle> mRaceVehicleList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private int mRaceId;
    private String mRaceTag;
    private Boolean mListModified = false;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private PendingRacesVehiclesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_races_vehicles);

        // Get race id and tag from indent
        Intent intent = getIntent();
        mRaceId = intent.getIntExtra("raceId", -1);
        mRaceTag = intent.getStringExtra("raceTag");

        // Initialize views and layouts
        mProgressBar = findViewById(R.id.progressBar);
        mTvMessage = findViewById(R.id.tvMessage);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new PendingRacesVehiclesAdapter(this, mRaceVehicleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = toolbar.findViewById(R.id.ivBack);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);
        ivTitle.setText(getString(R.string.race_vehicles, mRaceTag));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set vehicle item click listener
        mAdapter.setOnItemClickListener(new PendingRacesVehiclesAdapter.OnItemClickListener() {
            @Override
            public void onDelete(int position) {
                deleteVehicle(position);
            }
        });

        // Fetch data from API and return
        fetchRaceVehicles();

        // Set bottom navigation listener and add action text
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);
        MenuItem addItem = bottomNav.getMenu().findItem(R.id.action_add);
        addItem.setTitle(getString(R.string.add_vehicle));

    }

    // Fetch race vehicles
    private void fetchRaceVehicles() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);


        String token = SharedPrefManager.getInstance(this).getToken();
        Call<List<RaceVehicle>> call = RetrofitClient.getInstance().getApi().getRaceVehiclesForRace(token, mRaceId);
        call.enqueue(new Callback<List<RaceVehicle>>() {
            @Override
            public void onResponse(Call<List<RaceVehicle>> call, Response<List<RaceVehicle>> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(PendingRacesVehiclesActivity.this, response.message());
                }
                else {

                    mRaceVehicleList = response.body();
                    mAdapter.setRaceVehicleList(mRaceVehicleList);

                    if (mRaceVehicleList.size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_vehicles));
                        mTvMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        mTvMessage.setVisibility(View.INVISIBLE);
                    }

                }

                // Hide progress bar
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<RaceVehicle>> call, Throwable t) {
                mTvMessage.setText(getString(R.string.unable_to_connect));
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Override onBackPressed to force MainActivity to refresh


    @Override
    public void onBackPressed() {
        if (mListModified) {
            Intent intent = new Intent(PendingRacesVehiclesActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("fragment", MainActivity.PENDING_RACES);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }

    }

    // Bottom navigation listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.action_add:
                actionAddVehicle();
                break;
        }

        return true;
    }

    // Add new vehicle to the race
    private void actionAddVehicle() {
        SelectVehicleDialog dialog = new SelectVehicleDialog(mRaceId);
        dialog.show(getSupportFragmentManager(), "select vehicle");
        dialog.setSelectVehicleListener(new SelectVehicleDialog.SelectVehicleListener() {
            @Override
            public void onSelectVehicle(Vehicle vehicle, String driver) {

                // Send API request to store selected vehicle into race
                String token = SharedPrefManager.getInstance(PendingRacesVehiclesActivity.this).getToken();
                Call<RaceVehicle> call = RetrofitClient.getInstance().getApi().addVehicleToRace(token, mRaceId, vehicle.getId(), driver);
                call.enqueue(new Callback<RaceVehicle>() {
                    @Override
                    public void onResponse(Call<RaceVehicle> call, Response<RaceVehicle> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(PendingRacesVehiclesActivity.this, response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            RaceVehicle raceVehicle = response.body();
                            mRaceVehicleList.add(raceVehicle);
                            mAdapter.notifyItemInserted(mRaceVehicleList.size()-1);
                            mListModified = true;
                            mTvMessage.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RaceVehicle> call, Throwable t) {
                        Util.errorToast(PendingRacesVehiclesActivity.this, t.getMessage());
                    }
                });
            }
        });
    }

    // Delete vehicle from the race
    private void deleteVehicle(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.delete_vehicle_title), getString(R.string.delete_vehicle_message));
        confirmDialog.show(getSupportFragmentManager(), "delete vehicle");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {
                RaceVehicle rv = mRaceVehicleList.get(position);

                String token = SharedPrefManager.getInstance(PendingRacesVehiclesActivity.this).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().deleteRaceVehicle(token, rv.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(PendingRacesVehiclesActivity.this, response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            mRaceVehicleList.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            mListModified = true;
                            if (mRaceVehicleList.size() == 0) {
                                mTvMessage.setText(getResources().getString(R.string.no_vehicles));
                                mTvMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Util.errorToast(PendingRacesVehiclesActivity.this, t.getMessage());
                    }
                });
            }
        });

    }
}
