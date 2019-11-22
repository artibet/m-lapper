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
import gr.artibet.lapper.adapters.ActiveRacesVehiclesAdapter;
import gr.artibet.lapper.adapters.CheckpointsAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.Checkpoint;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.VehicleData;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckpointsActivity extends AppCompatActivity  {

    private VehicleData mVehicleData = new VehicleData();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private int mRaceId;
    private long mVehicleId;
    private String mRaceTag;
    private String mVehicleTag;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private CheckpointsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoints);

        // Get race and vehicle id and tag
        Intent intent = getIntent();
        mRaceId = intent.getIntExtra("raceId", -1);
        mVehicleId = intent.getLongExtra("vehicleId", -1);
        mRaceTag = intent.getStringExtra("raceTag");
        mVehicleTag = intent.getStringExtra("vehicleTag");

        // Initialize views and layouts
        mProgressBar = findViewById(R.id.progressBar);
        mTvMessage = findViewById(R.id.tvMessage);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new CheckpointsAdapter(this, new ArrayList<Checkpoint>());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Setup action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = toolbar.findViewById(R.id.ivBack);
        TextView ivTitle = toolbar.findViewById(R.id.ivTitle);
        ivTitle.setText(getString(R.string.checkpoints_title, mVehicleTag, mRaceTag));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Fetch data from API and return
        fetchVehicleData();

    }

    // Fetch race vehicles
    private void fetchVehicleData() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);


        String token = SharedPrefManager.getInstance(this).getToken();
        Call<VehicleData> call = RetrofitClient.getInstance().getApi().getVehicleData(token, mRaceId, mVehicleId);
        call.enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(Call<VehicleData> call, Response<VehicleData> response) {

                if (!response.isSuccessful()) {
                    //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                    Util.errorToast(CheckpointsActivity.this, response.message());
                }
                else {

                    mVehicleData = response.body();
                    mAdapter.setCheckpointList(mVehicleData.getCheckpoints());

                    if (mVehicleData.getCheckpoints().size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_checkpoints));
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
            public void onFailure(Call<VehicleData> call, Throwable t) {
                mTvMessage.setText(getString(R.string.unable_to_connect));
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}
