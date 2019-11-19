package gr.artibet.lapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.adapters.CompletedRacesVehiclesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedRacesVehiclesActivity extends AppCompatActivity {

    private List<RaceVehicle> mRaceVehicleList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private int mRaceId;
    private String mRaceTag;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private CompletedRacesVehiclesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_races_vehicles);

        // Get race id and tag from indent
        Intent intent = getIntent();
        mRaceId = intent.getIntExtra("raceId", -1);
        mRaceTag = intent.getStringExtra("raceTag");

        // Initialize views and layouts
        mProgressBar = findViewById(R.id.progressBar);
        mTvMessage = findViewById(R.id.tvMessage);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new CompletedRacesVehiclesAdapter(this, mRaceVehicleList);
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
        mAdapter.setOnItemClickListener(new CompletedRacesVehiclesAdapter.OnItemClickListener() {
            @Override
            public void onViewCheckpoints(int position) {
                viewCheckpoints(position);
            }
        });


        // Fetch data from API and return
        fetchRaceVehicles();

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
                    Util.errorToast(CompletedRacesVehiclesActivity.this, response.message());
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    // View vehicle checkpoints
    private void viewCheckpoints(final int position) {

        // TODO: Show vehicle checkpoints

    }

}
