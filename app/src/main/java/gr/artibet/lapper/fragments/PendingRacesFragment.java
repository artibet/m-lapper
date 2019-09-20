package gr.artibet.lapper.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import gr.artibet.lapper.activities.RaceFormActivity;
import gr.artibet.lapper.activities.RaceVehiclesActivity;
import gr.artibet.lapper.activities.SensorFormActivity;
import gr.artibet.lapper.adapters.PendingRacesAdapter;
import gr.artibet.lapper.adapters.SensorsAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.RaceResponse;
import gr.artibet.lapper.models.RaceState;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingRacesFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<Race> mRaceList = new ArrayList<Race>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private PendingRacesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PendingRacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_races, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new PendingRacesAdapter(getActivity(), mRaceList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set race item click listener
        mAdapter.setOnItemClickListener(new PendingRacesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editRace(position);
            }

            @Override
            public void onDeleteItem(int position) {
                deleteRace(position);
            }

            @Override
            public void onActivateItem(int position) {
                activateRace(position);
            }

            @Override
            public void onItemVehicles(int position) {
                raceVehicles(position);
            }
        });


        // Fetch data from API and return
        // TODO: Create an abstrace fragment and add fetch method. Remove fetch from here and call explicitly from parent activity
        fetchRaces();

        // Set bottom navigation listener and add action menu text
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);
        MenuItem addItem = bottomNav.getMenu().findItem(R.id.action_add);
        addItem.setTitle(getString(R.string.add_race));

        // Return view
        return v;
    }

    public void fetchRaces() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<RaceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRaces(SharedPrefManager.getInstance(getActivity()).getToken(), RaceState.STATE_PENDING);

        call.enqueue(new Callback<RaceResponse>() {
            @Override
            public void onResponse(Call<RaceResponse> call, Response<RaceResponse> response) {

                if (!response.isSuccessful()) {
                    mTvMessage.setText(response.message());
                    mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    mRaceList = (response.body()).getResults();
                    mAdapter.setRaceList(mRaceList);

                    if (mRaceList.size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_pending_races));
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
            public void onFailure(Call<RaceResponse> call, Throwable t) {
                mTvMessage.setText(t.getMessage());
                //mTvMessage.setText(getString(R.string.unable_to_connect));
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
                actionAddRace();
                break;
        }

        return true;
    }

    // Add new sensor
    private void actionAddRace() {
        Intent intent = new Intent(getActivity(), RaceFormActivity.class);
        startActivity(intent);
    }

    // Edit sensor
    private void editRace(int position) {
        Race race = mRaceList.get(position);

        // Open sensor form
        String json = new Gson().toJson(race);
        Intent intent = new Intent(getActivity(), RaceFormActivity.class);
        intent.putExtra("race", json);
        startActivity(intent);
    }

    // Delete sensor
    private void deleteRace(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.delete_race_title), getString(R.string.delete_race_message));
        confirmDialog.show(getActivity().getSupportFragmentManager(), "delete race");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {

                Race race = mRaceList.get(position);

                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().deleteRace(token, race.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            mRaceList.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            if (mRaceList.size() == 0) {
                                mTvMessage.setText(getResources().getString(R.string.no_pending_races));
                                mTvMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Util.errorToast(getActivity(), t.getMessage());
                    }
                });

            }
        });

    }

    // Activate Race
    private void activateRace(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog("Ενεργοποίηση αγώνα", "Να ενεργοποιηθεί ο επιλεγμένος αγώνας?");
        confirmDialog.show(getActivity().getSupportFragmentManager(), "activate race");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {

                Race race = mRaceList.get(position);

                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Race> call = RetrofitClient.getInstance().getApi().activateRace(token, race.getId());
                call.enqueue(new Callback<Race>() {
                    @Override
                    public void onResponse(Call<Race> call, Response<Race> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            Util.successToast(getActivity(), getString(R.string.race_activated));

                            // Send socket message
                            Race activatedRace = response.body();
                            Gson gson = new Gson();
                            try {
                                JSONObject jsonObj = new JSONObject(gson.toJson(activatedRace));
                                SocketIO.getInstance().getSocket().emit("race_activated", jsonObj);
                                mRaceList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Race> call, Throwable t) {
                        Util.errorToast(getActivity(), t.getMessage());
                    }
                });
            }
        });
    }

    // Race Vehicles
    private void raceVehicles(int position) {
        // Get race ID and tag and open RaceVehiclesActivity
        Race race = mRaceList.get(position);
        Intent intent = new Intent(getActivity(), RaceVehiclesActivity.class);
        intent.putExtra("raceId", race.getId());
        intent.putExtra("raceTag", race.getTag());
        startActivity(intent);
    }
}

