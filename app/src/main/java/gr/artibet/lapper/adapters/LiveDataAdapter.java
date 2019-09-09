package gr.artibet.lapper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.models.LiveData;
import gr.artibet.lapper.models.RaceVehicleState;

public class LiveDataAdapter extends RecyclerView.Adapter<LiveDataAdapter.LiveDataViewHolder> {

    // Context
    private Context mContext;

    // LiveData List
    private List<LiveData> mLiveDataList;

    // VIEW HOLDER CLASS
    public static class LiveDataViewHolder extends RecyclerView.ViewHolder {

        public TextView mLastDt;
        public TextView mVehicleTag;
        public TextView mRaceTag;
        public TextView mSector;
        public TextView mSectorInterval;
        public TextView mLapLabel;
        public TextView mLap;
        public TextView mLapIntervalLabel;
        public TextView mLapInterval;
        public ImageView mFinishFlag;

        public LiveDataViewHolder(@NonNull View itemView) {
            super(itemView);

            mLastDt = itemView.findViewById(R.id.tvLastDt);
            mVehicleTag = itemView.findViewById(R.id.tvVehicleTag);
            mRaceTag = itemView.findViewById(R.id.tvRaceTag);
            mSector = itemView.findViewById(R.id.tvSector);
            mSectorInterval = itemView.findViewById(R.id.tvSectorInterval);
            mLapLabel = itemView.findViewById(R.id.tvLapLabel);
            mLap = itemView.findViewById(R.id.tvLap);
            mLapIntervalLabel = itemView.findViewById(R.id.tvLapIntervalLabel);
            mLapInterval = itemView.findViewById(R.id.tvLapInterval);
            mFinishFlag = itemView.findViewById(R.id.ivFinishFlag);
        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF LiveData
    public LiveDataAdapter(Context context, List<LiveData> liveDataList) {
        mContext = context;
        mLiveDataList = liveDataList;
    }

    @NonNull
    @Override
    public LiveDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_data_item, parent, false);
        LiveDataViewHolder viewHolder = new LiveDataViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveDataViewHolder holder, int position) {
        LiveData liveData = mLiveDataList.get(position);

        holder.mLastDt.setText(Util.TimestampToTime(liveData.getLastTs()));
        holder.mVehicleTag.setText(liveData.getVehicle().getTag());
        holder.mRaceTag.setText("(" + liveData.getRace().getTag() + ")");

        // Sector
        String sector;
        if (liveData.getPrevSensor() == null) {
            sector = mContext.getResources().getString(R.string.race_start);
            holder.mSectorInterval.setVisibility(View.INVISIBLE);
        }
        else {
            sector = liveData.getPrevSensor().getTag() + " - " + liveData.getLastSensor().getTag();
        }
        holder.mSector.setText(sector);

        // Sector interval
        holder.mSectorInterval.setText(liveData.getIntervalString());
        if (liveData.getPrevInterval() != 0) {
            if (liveData.getPrevInterval() > liveData.getInterval()) {
                holder.mSectorInterval.setTextColor(Color.rgb(0, 128,0));
            }
            else {
                holder.mSectorInterval.setTextColor(Color.RED);
            }
        }

        // Lap
        holder.mLapLabel.setText(mContext.getString(R.string.lap) + ":");
        holder.mLap.setText(String.valueOf(liveData.getLap()) + "/" + String.valueOf(liveData.getRace().getLaps()));

        // Lap interval - when passing start sensor
        if (liveData.getLastSensor().isStart() && liveData.getLap() > 1) {
            holder.mLapIntervalLabel.setText(mContext.getString(R.string.lap_interval) + ":");
            holder.mLapInterval.setText(liveData.getLapIntervalString());
            if (liveData.getPrevLapInterval() > 0) {
                if (liveData.getPrevLapInterval() > liveData.getLapInterval()) {
                    holder.mLapInterval.setTextColor(Color.rgb(0, 128,0));
                }
                else {
                        holder.mLapInterval.setTextColor(Color.RED);
                }
            }
        }
        else {
            holder.mLapIntervalLabel.setVisibility(View.INVISIBLE);
            holder.mLapInterval.setVisibility(View.INVISIBLE);
        }

        // Finish flag visibility
        if (liveData.getRvState().getId() == RaceVehicleState.STATE_FINISHED) {
            holder.mFinishFlag.setVisibility(View.VISIBLE);
        }
        else {
            holder.mFinishFlag.setVisibility(View.INVISIBLE);
        }

     }

    @Override
    public int getItemCount() {
        return mLiveDataList.size();
    }

    public void setLiveDataList(List<LiveData> liveDataList) {
        mLiveDataList = liveDataList;
        notifyDataSetChanged();
    }
}
