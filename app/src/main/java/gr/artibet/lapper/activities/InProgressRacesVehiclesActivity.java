package gr.artibet.lapper.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.adapters.InProgressRacesVehiclesAdapter;
import gr.artibet.lapper.adapters.PendingRacesVehiclesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.dialogs.SelectVehicleDialog;
import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InProgressRacesVehiclesActivity extends AppCompatActivity {

    private List<RaceVehicle> mRaceVehicleList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private int mRaceId;
    private int mRaceLaps;
    private String mRaceTag;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private InProgressRacesVehiclesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprogress_races_vehicles);

        // Get race id and tag from indent
        Intent intent = getIntent();
        mRaceId = intent.getIntExtra("raceId", -1);
        mRaceTag = intent.getStringExtra("raceTag");
        mRaceLaps = intent.getIntExtra("laps", -1);

        // Initialize views and layouts
        mProgressBar = findViewById(R.id.progressBar);
        mTvMessage = findViewById(R.id.tvMessage);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new InProgressRacesVehiclesAdapter(this, mRaceVehicleList, mRaceLaps);
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
        mAdapter.setOnItemClickListener(new InProgressRacesVehiclesAdapter.OnItemClickListener() {
            @Override
            public void onCancel(int position) {

            }
        });

        // Fetch data from API and return
        fetchRaceVehicles();

        // Subscribe on "checkpoint" socket message
        SocketIO.getInstance().getSocket().on("checkpoint", onCheckPoint);

    }

    // Unsubscribe from socket events on destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketIO.getInstance().getSocket().off("checkpoint", onCheckPoint);
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
                    Util.errorToast(InProgressRacesVehiclesActivity.this, response.message());
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


    // Delete vehicle from the race
    private void cancelVehicle(final int position) {

        // TODO: Implement vehicle cancelation
    }

    // On checkpoint socket message
    private Emitter.Listener onCheckPoint = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    LiveData ld = gson.fromJson(args[0].toString(), LiveData.class);

                    // find rv for ld
                    int pos = -1;
                    for (int i=0; i < mRaceVehicleList.size(); i++) {
                        if (mRaceVehicleList.get(i).getId() == ld.getRvId()) {
                            pos = i;
                            break;
                        }
                    }

                    // If rv exists into list
                    if (pos > -1) {
                        RaceVehicle rv = mRaceVehicleList.get(pos);

                        rv.setState(ld.getRvState());
                        rv.setPrevSensor(ld.getPrevSensor());
                        rv.setLastSensor(ld.getLastSensor());
                        rv.setPrevTs(ld.getPrevTs());
                        rv.setLastTs(ld.getLastTs());
                        rv.setIntervalString(ld.getIntervalString());
                        rv.setBestIntervalString(ld.getBestIntervalString());
                        rv.setLap(ld.getLap());
                        rv.setLapIntervalString(ld.getLapIntervalString());
                        rv.setBestLapIntervalString(ld.getBestLapIntervalString());

                        // Notify item changed and resort list
                        mAdapter.notifyItemChanged(pos);
                        mAdapter.sortVehicleList();

                        // If item has changed position, notify
                        for (int i=0; i < mRaceVehicleList.size(); i++) {
                            RaceVehicle rv2 = mRaceVehicleList.get(i);
                            if (rv2.getId() == rv.getId()) {
                                if (pos != i) {
                                    mAdapter.notifyItemMoved(pos, i);
                                    mAdapter.notifyItemChanged(i);
                                    mAdapter.notifyItemChanged(pos);
                                    break;
                                }
                            }
                        }


                        // Set list again for sorting
                        //mAdapter.setRaceVehicleList(mRaceVehicleList);

                        // Find rv into sorted list for animation
                        // TODO: animate changed item
                        /*
                        for (int i=0; i < mRaceVehicleList.size(); i++) {
                            rv = mRaceVehicleList.get(i);
                            if (rv.getId() == ld.getRvId()) {
                                mAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                        */
                    }
                }
            });
        }
    };
}
