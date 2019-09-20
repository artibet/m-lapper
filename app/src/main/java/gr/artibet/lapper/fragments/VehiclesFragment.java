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

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.SensorFormActivity;
import gr.artibet.lapper.activities.VehicleFormActivity;
import gr.artibet.lapper.adapters.SensorsAdapter;
import gr.artibet.lapper.adapters.VehiclesAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehiclesFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<Vehicle> mVehicleList = new ArrayList<Vehicle>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private VehiclesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public VehiclesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vehicles, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new VehiclesAdapter(getActivity(), mVehicleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set vehicle item click listener
        mAdapter.setOnItemClickListener(new VehiclesAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                editVehicle(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteVehicle(position);
            }
        });

        // Fetch data from API and return
        // TODO: Create an abstrace fragment and add fetch method. Remove fetch from here and call explicitly from parent activity
        fetchVehicles();

        // Set bottom navigation listener and add action text
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);
        MenuItem addItem = bottomNav.getMenu().findItem(R.id.action_add);
        addItem.setTitle(getString(R.string.add_vehicle));

        // Return view
        return v;
    }

    private void fetchVehicles() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<List<Vehicle>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getVehicles(SharedPrefManager.getInstance(getActivity()).getToken());

        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {

                if (!response.isSuccessful()) {
                    mTvMessage.setText(response.message());
                    mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    mVehicleList = response.body();
                    mAdapter.setmVehicleList(mVehicleList);

                    if (mVehicleList.size() == 0) {
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
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
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
                actionAddVehicle();
                break;
        }

        return true;
    }

    // Add new vehicle
    private void actionAddVehicle() {
        Intent intent = new Intent(getActivity(), VehicleFormActivity.class);
        startActivity(intent);
    }

    // Edit sensor
    private void editVehicle(int position) {
        Vehicle vehicle = mVehicleList.get(position);

        // Open vehicle form
        String json = new Gson().toJson(vehicle);
        Intent intent = new Intent(getActivity(), VehicleFormActivity.class);
        intent.putExtra("vehicle", json);
        startActivity(intent);
    }

    // Delete vehicle
    private void deleteVehicle(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.delete_vehicle_title), getString(R.string.delete_vehicle_message));
        confirmDialog.show(getActivity().getSupportFragmentManager(), "delete vehicle");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {
                Vehicle vehicle = mVehicleList.get(position);

                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().deleteVehicle(token, vehicle.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            mVehicleList.remove(position);
                            mAdapter.notifyItemRemoved(position);
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
}
