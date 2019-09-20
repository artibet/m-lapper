package gr.artibet.lapper.fragments;


import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.adapters.LiveDataAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveDataFragment extends Fragment {

    private List<LiveData> mLiveDataList = new ArrayList<LiveData>();
    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    // Recycler view members
    private RecyclerView mRecyclerView;
    private LiveDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrameLayout mMainLayoutView;


    public LiveDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_live_data, container, false);

        // Initialize views and layouts
        mProgressBar = v.findViewById(R.id.progressBar);
        mTvMessage = v.findViewById(R.id.tvMessage);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mMainLayoutView = v.findViewById(R.id.mainLayoutView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new LiveDataAdapter(getActivity(), mLiveDataList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // Fetch data from API and return
        // TODO: Create an abstrace fragment and add fetch method. Remove fetch from here and call explicitly from parent activity
        fetchLiveData();

        // Subscribe on "checkpoint" socket message
        SocketIO.getInstance().getSocket().on("checkpoint", onCheckPoint);


        // Return view
        return v;
    }

    // Unsubscribe from socket events on destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketIO.getInstance().getSocket().off("checkpoint", onCheckPoint);
    }

    // FETCH LIVE DATA
    public void fetchLiveData() {

        // Show progress bar and hide message text
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);

        Call<List<LiveData>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getLiveData(SharedPrefManager.getInstance(getActivity()).getToken());

        call.enqueue(new Callback<List<LiveData>>() {
            @Override
            public void onResponse(Call<List<LiveData>> call, Response<List<LiveData>> response) {

                if (!response.isSuccessful()) {
                    mTvMessage.setText(response.message());
                    mTvMessage.setVisibility(View.VISIBLE);
                    //Util.errorToast(getActivity(), response.message);
                }
                else {
                    mLiveDataList = response.body();
                    mAdapter.setLiveDataList(mLiveDataList);

                    if (mLiveDataList.size() == 0) {
                        mTvMessage.setText(getResources().getString(R.string.no_race_in_progress));
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
            public void onFailure(Call<List<LiveData>> call, Throwable t) {
                mTvMessage.setText(getString(R.string.unable_to_connect));
                mTvMessage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // On checkpoint socket message
    private Emitter.Listener onCheckPoint = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    LiveData ld = gson.fromJson(args[0].toString(), LiveData.class);

                    // Show data only if connected user is superuser, or race is public
                    // or race is private but connected user has vehicle into it.
                    long connectedUserΙd = SharedPrefManager.getInstance(getActivity()).getLoggedInUser().getId();
                    if (SharedPrefManager.getInstance(getActivity()).isAdmin() || ld.getRace().isPublic() || ld.getRace().hasVehicle(connectedUserΙd)) {
                        mTvMessage.setVisibility(View.INVISIBLE);
                        mLayoutManager.scrollToPosition(0);
                        mLiveDataList.add(0, ld);
                        mAdapter.notifyItemInserted(0);

                        // If this is the first item don't play sound (played start race notification)
                        if (mLiveDataList.size() > 1) {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
                            r.play();
                        }
                    }
                }
            });
        }
    };

}
