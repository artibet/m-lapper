package gr.artibet.lapper.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.LoginActivity;
import gr.artibet.lapper.activities.MainActivity;
import gr.artibet.lapper.activities.SensorAddActivity;
import gr.artibet.lapper.adapters.SensorAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorsFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<Sensor> mSensorList = new ArrayList<Sensor>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private SensorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SensorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensors, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new SensorAdapter(getActivity(), mSensorList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set sensor item click listener
        mAdapter.setOnItemClickListener(new SensorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Sensor sensor = mSensorList.get(position);

                // Open sensor form
                String json = new Gson().toJson(sensor);
                Intent intent = new Intent(getActivity(), SensorAddActivity.class);
                intent.putExtra("sensor", json);
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //intent.setFlags(FUL);
                startActivity(intent);
                //Util.successToast(getActivity(), "Open form");
            }
        });

        // Fetch data from API and return
        fetchSensors();

        // Set bottom navigation listener
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Return view
        return v;
    }

    private void fetchSensors() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<List<Sensor>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getSensors(SharedPrefManager.getInstance(getActivity()).getToken());

        call.enqueue(new Callback<List<Sensor>>() {
            @Override
            public void onResponse(Call<List<Sensor>> call, Response<List<Sensor>> response) {

                if (!response.isSuccessful()) {
                    mTvMessage.setText(response.message());
                    mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    mSensorList = response.body();
                    mAdapter.setSensorList(mSensorList);

                    if (mSensorList.size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_sensors));
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
            public void onFailure(Call<List<Sensor>> call, Throwable t) {
                mTvMessage.setText(getString(R.string.unable_to_connect));
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Bottom navigationview listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.action_add:
                actionAddSensor();
                break;
        }

        return true;
    }

    // Add new sensor
    private void actionAddSensor() {
        Intent intent = new Intent(getActivity(), SensorAddActivity.class);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //intent.setFlags(FUL);
        startActivity(intent);
    }
}
