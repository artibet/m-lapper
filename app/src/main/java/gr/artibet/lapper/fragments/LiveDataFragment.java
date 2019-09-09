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
import gr.artibet.lapper.Util;
import gr.artibet.lapper.adapters.LiveDataAdapter;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.Race;
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
        fetchLiveData();

        // Subscribe on "checkpoint" socket message
        SocketIO.getInstance().getSocket().on("checkpoint", onCheckPoint);
        SocketIO.getInstance().getSocket().on("race_started", onRaceStart);
        SocketIO.getInstance().getSocket().on("race_activated", onRaceActivated);
        SocketIO.getInstance().getSocket().on("race_deactivated", onRaceDeactivated);

        // Return view
        return v;
    }

    // Unsubscribe from socket events on destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketIO.getInstance().getSocket().off("checkpoint", onCheckPoint);
        SocketIO.getInstance().getSocket().off("race_started", onRaceStart);
        SocketIO.getInstance().getSocket().off("race_activated", onRaceActivated);
        SocketIO.getInstance().getSocket().off("race_deactivated", onRaceDeactivated);
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
                    LiveData ld = gson.fromJson((String)args[0].toString(), LiveData.class);

                    // Show data only if connected user is superuser, or race is public
                    // or race is private but connected user has vehicle into it.
                    long connectedUserΙd = SharedPrefManager.getInstance(getActivity()).getLoggedInUser().getId();
                    if (SharedPrefManager.getInstance(getActivity()).isAdmin() || ld.getRace().isPublic() || ld.getRace().hasVehicle(connectedUserΙd)) {
                        mTvMessage.setVisibility(View.INVISIBLE);
                        mLayoutManager.scrollToPosition(0);
                        mLiveDataList.add(0, ld);
                        mAdapter.notifyItemInserted(0);
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
                        r.play();
                    }
                }
            });
        }
    };

    // On race start socket message
    private Emitter.Listener onRaceStart = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    Race race = gson.fromJson((String)args[0].toString(), Race.class);

                    // If connected user has rights to the race
                    long connectedUserΙd = SharedPrefManager.getInstance(getActivity()).getLoggedInUser().getId();
                    if (SharedPrefManager.getInstance(getActivity()).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                        Util.successToast(getActivity(), getActivity().getString(R.string.the_race) + " " + race.getTag() + " " + getActivity().getString(R.string.has_been_started));
                    }

                }
            });
        }
    };

    // On race activated socket message
    private Emitter.Listener onRaceActivated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    Race race = gson.fromJson((String)args[0].toString(), Race.class);

                    // If connected user has rights to the race
                    long connectedUserΙd = SharedPrefManager.getInstance(getActivity()).getLoggedInUser().getId();
                    if (SharedPrefManager.getInstance(getActivity()).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                        Util.successToast(getActivity(), "Ο αγώνας " + race.getTag() + " ενεργοποιήθηκε");
                    }
                }
            });
        }
    };

    // On race deactivated socket message
    private Emitter.Listener onRaceDeactivated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    Race race = gson.fromJson((String)args[0].toString(), Race.class);

                    // If connected user has rights to the race
                    long connectedUserΙd = SharedPrefManager.getInstance(getActivity()).getLoggedInUser().getId();
                    if (SharedPrefManager.getInstance(getActivity()).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                        Util.successToast(getActivity(), "Ο αγώνας " + race.getTag() + " απενεργοποιήθηκε");
                    }
                }
            });
        }
    };

}
