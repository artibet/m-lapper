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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.adapters.RaceVehiclesAdapter;
import gr.artibet.lapper.adapters.VehiclesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RaceVehiclesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<RaceVehicle> mRaceVehicleList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private int mRaceId;
    private String mRaceTag;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private RaceVehiclesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_vehicles);

        // Get race id and tag from indent
        Intent intent = getIntent();
        mRaceId = intent.getIntExtra("raceId", -1);
        mRaceTag = intent.getStringExtra("raceTag");

        // Initialize views and layouts
        mProgressBar = findViewById(R.id.progressBar);
        mTvMessage = findViewById(R.id.tvMessage);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new RaceVehiclesAdapter(this, mRaceVehicleList);
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
        mAdapter.setOnItemClickListener(new RaceVehiclesAdapter.OnItemClickListener() {
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
                    Util.errorToast(RaceVehiclesActivity.this, response.message());
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
        // TODO: Create add vehicle to race form
        //Intent intent = new Intent(getActivity(), VehicleFormActivity.class);
        //startActivity(intent);
    }

    // Delete vehicle from the race
    private void deleteVehicle(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.delete_vehicle_title), getString(R.string.delete_vehicle_message));
        confirmDialog.show(getSupportFragmentManager(), "delete vehicle");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {
                RaceVehicle rv = mRaceVehicleList.get(position);

                String token = SharedPrefManager.getInstance(RaceVehiclesActivity.this).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().deleteRaceVehicle(token, rv.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(RaceVehiclesActivity.this, response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            mRaceVehicleList.remove(position);
                            mAdapter.notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Util.errorToast(RaceVehiclesActivity.this, t.getMessage());
                    }
                });
            }
        });

    }
}
