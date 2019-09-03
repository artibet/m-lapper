package gr.artibet.lapper.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.LoginActivity;
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

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fetchLiveData();
        return v;
    }

    // FETCH LIVE DATA
    public void fetchLiveData() {

        Call<List<LiveData>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getLiveData();

        call.enqueue(new Callback<List<LiveData>>() {
            @Override
            public void onResponse(Call<List<LiveData>> call, Response<List<LiveData>> response) {

                if (!response.isSuccessful()) {
                    String errorMessage = response.message();
                    Util.errorToast(getActivity(), errorMessage);
                    return;
                }
                else {
                    mLiveDataList = response.body();
                    Util.successToast(getActivity(), "oleeeee");
                }
            }

            @Override
            public void onFailure(Call<List<LiveData>> call, Throwable t) {
                String errorMessage = "Unable to connect to API Server";
                Util.errorToast(getActivity(), t.getMessage());
            }
        });



    }

}
