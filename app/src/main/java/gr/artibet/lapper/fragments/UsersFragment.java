package gr.artibet.lapper.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.SensorFormActivity;
import gr.artibet.lapper.activities.UserFormActivity;
import gr.artibet.lapper.adapters.SensorsAdapter;
import gr.artibet.lapper.adapters.UsersAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.dialogs.ConfirmDialog;
import gr.artibet.lapper.dialogs.ResetPasswordDialog;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<User> mUserList = new ArrayList<User>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new UsersAdapter(getActivity(), mUserList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Set user item click listener
        mAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                editUser(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteUser(position);
            }

            @Override
            public void onResetPassword(int position) {
                resetPassword(position);
            }
        });

        // Fetch data from API and return
        fetchUsers();

        // Set bottom navigation listener and add text
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);
        MenuItem addItem = bottomNav.getMenu().findItem(R.id.action_add);
        addItem.setTitle(getString(R.string.add_user));

        // Return view
        return v;
    }


    private void fetchUsers() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<List<User>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUsers(SharedPrefManager.getInstance(getActivity()).getToken());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (!response.isSuccessful()) {
                    mTvMessage.setText(response.message());
                    mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    mUserList = response.body();
                    mAdapter.setUserList(mUserList);

                    if (mUserList.size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_users));
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
            public void onFailure(Call<List<User>> call, Throwable t) {
                mTvMessage.setText(getString(R.string.unable_to_connect));
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.action_add:
                actionAddUser();
                break;
        }

        return true;
    }

    // Add new sensor
    private void actionAddUser() {
        Intent intent = new Intent(getActivity(), UserFormActivity.class);
        startActivity(intent);
    }

    // Edit sensor
    private void editUser(int position) {
        User user = mUserList.get(position);

        // Open sensor form
        String json = new Gson().toJson(user);
        Intent intent = new Intent(getActivity(), UserFormActivity.class);
        intent.putExtra("user", json);
        startActivity(intent);
    }

    // Delete sensor
    private void deleteUser(final int position) {

        ConfirmDialog confirmDialog = new ConfirmDialog(getString(R.string.delete_user_title), getString(R.string.delete_user_message));
        confirmDialog.show(getActivity().getSupportFragmentManager(), "delete user");
        confirmDialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {
                User user = mUserList.get(position);

                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().deleteUser(token, user.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            //Util.successToast(getActivity(), "Delete successfully");
                            mUserList.remove(position);
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

    // Set password
    private void resetPassword(final int potition) {

        User user = mUserList.get(potition);
        final String username = user.getUsername();

        ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog(username);
        resetPasswordDialog.show(getActivity().getSupportFragmentManager(), "reset password");
        resetPasswordDialog.setResetPasswordListener(new ResetPasswordDialog.ResetPasswordListener() {
            @Override
            public void onResetPassword(String password) {

                // If password is empty display toast message
                if (password.isEmpty()) {
                    Util.successToast(getActivity(), getString(R.string.no_empty_password));
                    return;
                }

                // Send api request to reset password
                String token = SharedPrefManager.getInstance(getActivity()).getToken();
                Call<Void> call = RetrofitClient.getInstance().getApi().resetPassword(token, username, password);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            //Util.errorToast(SensorFormActivity.this, getString(R.string.sensor_create_failed));
                            Util.errorToast(getActivity(), response.message());
                        }
                        else {
                            Util.successToast(getActivity(), getString(R.string.password_reset_success));
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
