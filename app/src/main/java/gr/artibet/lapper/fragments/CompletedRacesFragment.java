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
import gr.artibet.lapper.activities.PendingRacesVehiclesActivity;
import gr.artibet.lapper.adapters.CompletedRacesAdapter;
import gr.artibet.lapper.adapters.PendingRacesAdapter;
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
public class CompletedRacesFragment extends Fragment {

    private List<Race> mRaceList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private CompletedRacesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Pagination related members
    private int mPage = 1;
    private int mPageSize = 10;
    private boolean mIsLoading = true;
    private int mPastVisibleItems = 0;
    private int mVisibleItemsCount = 0;
    private int mTotalItemsCount = 0;
    private int mPrevTotalItemsCount = 0;
    private int mViewThreshold = 10;


    public CompletedRacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_completed_races, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new CompletedRacesAdapter(getActivity(), mRaceList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set race item click listener
        mAdapter.setOnItemClickListener(new CompletedRacesAdapter.OnItemClickListener() {
            @Override
            public void onDeleteRace(int position) {
                deleteRace(position);
            }

            @Override
            public void onViewVehicles(int position) {
                raceVehicles(position);
            }
        });

        // Set scroll listener for recycler view
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mVisibleItemsCount = mLayoutManager.getChildCount();
                mTotalItemsCount = mLayoutManager.getItemCount();
                mPastVisibleItems = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

                // If scroll up
                if (dy > 0) {

                    if (mIsLoading) {
                        if (mTotalItemsCount > mPrevTotalItemsCount) {
                            mIsLoading = false;
                            mPrevTotalItemsCount = mTotalItemsCount;
                        }
                    }

                    if (!mIsLoading && (mTotalItemsCount - mVisibleItemsCount <= mPastVisibleItems + mViewThreshold)) {
                        mPage += 1;
                        paginate();
                        mIsLoading = true;
                    }
                }

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
                .getPagedRaces(SharedPrefManager.getInstance(getActivity()).getToken(), RaceState.STATE_COMPLETED, mPage, mPageSize);

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
                        mTvMessage.setText(getResources().getString(R.string.no_completed_races));
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
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    // Delete race
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
                                mTvMessage.setText(getResources().getString(R.string.no_completed_races));
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

    // Race Vehicles
    private void raceVehicles(int position) {

        // TODO: Show completed race vehicles
        /*
        // Get race ID and tag and open PendingRacesVehiclesActivity
        Race race = mRaceList.get(position);
        Intent intent = new Intent(getActivity(), PendingRacesVehiclesActivity.class);
        intent.putExtra("raceId", race.getId());
        intent.putExtra("raceTag", race.getTag());
        startActivity(intent);
        */

    }


    // Perform pagination
    private void paginate() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<RaceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPagedRaces(SharedPrefManager.getInstance(getActivity()).getToken(), RaceState.STATE_COMPLETED, mPage, mPageSize);

        call.enqueue(new Callback<RaceResponse>() {
            @Override
            public void onResponse(Call<RaceResponse> call, Response<RaceResponse> response) {

                if (!response.isSuccessful()) {
                    //mTvMessage.setText(response.message());
                    //mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    if (response.body().getCount() > 0) {
                        // Add new races to the list
                        List<Race> races = response.body().getResults();
                        for (Race race : races) {
                            mRaceList.add(race);
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                }

                // Hide progress bar
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<RaceResponse> call, Throwable t) {
                mTvMessage.setText(t.getMessage());
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}

