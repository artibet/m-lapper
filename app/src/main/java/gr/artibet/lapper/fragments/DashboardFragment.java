package gr.artibet.lapper.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.LoginActivity;
import gr.artibet.lapper.adapters.LiveDataAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.LoginUser;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private List<LiveData> mLiveDataList;
    private ProgressBar mProgressBar;
    private TextView mNoData;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private LiveDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mNoData = v.findViewById(R.id.tvNoData);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        // Fetch data from API and return
        fetchLiveData();
        return v;
    }

    // FETCH LIVE DATA
    public void fetchLiveData() {

        Call<List<LiveData>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getLiveData(SharedPrefManager.getInstance(getActivity()).getToken());

        call.enqueue(new Callback<List<LiveData>>() {
            @Override
            public void onResponse(Call<List<LiveData>> call, Response<List<LiveData>> response) {

                if (!response.isSuccessful()) {
                    String errorMessage = response.message();
                    Util.errorToast(getActivity(), errorMessage);
                }
                else {
                    mLiveDataList = response.body();
                    mAdapter = new LiveDataAdapter(getActivity(), mLiveDataList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    if (mLiveDataList.size() == 0) {
                        mNoData.setVisibility(View.VISIBLE);
                    }
                    else {
                        mNoData.setVisibility(View.INVISIBLE);
                    }
                }

                // Hide progress bar
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<LiveData>> call, Throwable t) {
                String errorMessage = "Unable to connect to API Server";
                Util.errorToast(getActivity(), t.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });



    }

}
