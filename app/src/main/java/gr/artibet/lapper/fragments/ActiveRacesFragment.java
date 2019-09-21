package gr.artibet.lapper.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.ActiveRacesVehiclesActivity;
import gr.artibet.lapper.activities.PendingRacesVehiclesActivity;
import gr.artibet.lapper.adapters.ActiveRacesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.RaceResponse;
import gr.artibet.lapper.models.RaceState;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveRacesFragment extends Fragment {

    private List<Race> mRaceList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private ActiveRacesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ActiveRacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_active_races, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new ActiveRacesAdapter(getActivity(), mRaceList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set race item click listener
        mAdapter.setOnItemClickListener(new ActiveRacesAdapter.OnItemClickListener() {
            @Override
            public void onDeactivate(int position) {
                deactivateRace(position);
            }

            @Override
            public void onViewVehicles(int position) {
                viewVehicles(position);
            }

            @Override
            public void onStartRace(int position) {
                startRace(position);
            }
        });


        // Fetch data from API and return
        fetchRaces();

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
                .getRaces(SharedPrefManager.getInstance(getActivity()).getToken(), RaceState.STATE_ACTIVE);

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
                        mTvMessage.setText(getResources().getString(R.string.no_active_races));
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


    // Deactivate race
    private void deactivateRace(final int position) {

        final Race race = mRaceList.get(position);

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.deactivate_race_title), getString(R.string.deactivate_race_confirm_message, race.getTag()));
        confirmDialog.show(getActivity().getSupportFragmentManager(), "deactivate race");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {

                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Race> call = RetrofitClient.getInstance().getApi().deactivateRace(token, race.getId());
                call.enqueue(new Callback<Race>() {
                    @Override
                    public void onResponse(Call<Race> call, Response<Race> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            Util.successToast(getActivity(), getString(R.string.race_deactivated));

                            // Send socket message
                            Race deactivatedRace = response.body();
                            Gson gson = new Gson();
                            try {
                                JSONObject jsonObj = new JSONObject(gson.toJson(deactivatedRace));
                                SocketIO.getInstance().getSocket().emit("race_deactivated", jsonObj);
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


    // View Race vehicles
    private void viewVehicles(int position) {
        // Get race ID and tag and open ActiveRacesVehiclesActivity
        Race race = mRaceList.get(position);
        Intent intent = new Intent(getActivity(), ActiveRacesVehiclesActivity.class);
        intent.putExtra("raceId", race.getId());
        intent.putExtra("raceTag", race.getTag());
        startActivity(intent);
    }

    // Start race
    private void startRace(int position) {
        // TODO: implement start race
    }
}

