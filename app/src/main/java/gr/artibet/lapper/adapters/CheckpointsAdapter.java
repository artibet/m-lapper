package gr.artibet.lapper.adapters;

import android.content.Context;
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
import gr.artibet.lapper.models.Checkpoint;

public class CheckpointsAdapter extends RecyclerView.Adapter<CheckpointsAdapter.CheckpointsViewHolder> {

    // Context
    private static Context mContext;

    // Checkpoint List
    private List<Checkpoint> mCheckpointList;

    // VIEW HOLDER CLASS
    public static class CheckpointsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDt;
        public TextView tvSector;
        public TextView tvSectorInterval;
        public TextView tvLapLabel;
        public TextView tvLap;
        public TextView tvLapIntervalLabel;
        public TextView tvLapInterval;
        public ImageView ivSectorUp;
        public ImageView ivSectorDown;
        public ImageView ivLapUp;
        public ImageView ivLapDown;

        public CheckpointsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDt = itemView.findViewById(R.id.tvDt);
            tvSector = itemView.findViewById(R.id.tvSector);
            tvSectorInterval = itemView.findViewById(R.id.tvSectorInterval);
            tvLapLabel = itemView.findViewById(R.id.tvLapLabel);
            tvLap = itemView.findViewById(R.id.tvLap);
            tvLapIntervalLabel = itemView.findViewById(R.id.tvLapIntervalLabel);
            tvLapInterval = itemView.findViewById(R.id.tvLapInterval);
            ivSectorUp = itemView.findViewById(R.id.ivSectorUp);
            ivSectorDown = itemView.findViewById(R.id.ivSectorDown);
            ivLapUp = itemView.findViewById(R.id.ivLapUp);
            ivLapDown = itemView.findViewById(R.id.ivLapDown);

        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF checkpoints
    public CheckpointsAdapter(Context context, List<Checkpoint> checkpointList) {
        mContext = context;
        mCheckpointList = checkpointList;
    }

    @NonNull
    @Override
    public CheckpointsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoints_item, parent, false);
        CheckpointsViewHolder viewHolder = new CheckpointsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckpointsViewHolder holder, int position) {

        Checkpoint cp = mCheckpointList.get(position);

        // Timestamp
        holder.tvDt.setText(Util.TimestampToTime(cp.getLastTs()));

        // Sector, intervals and arrows
        String sector;
        if (cp.getPrevSensor() == null) {   // Race start
            sector = mContext.getResources().getString(R.string.race_start);
            holder.tvSectorInterval.setVisibility(View.INVISIBLE);
        }
        else {
            sector = cp.getPrevSensor().getTag() + " - " + cp.getLastSensor().getTag();
            if (cp.getPrevInterval() != 0) {
                if (cp.getPrevInterval() > cp.getInterval()) {
                    holder.ivSectorDown.setVisibility(View.VISIBLE);
                }
                else {
                    holder.ivSectorUp.setVisibility(View.VISIBLE);
                }
            }
            holder.tvSectorInterval.setText(cp.getIntervalString());
        }
        holder.tvSector.setText(sector);


        // Lap, lap interval and arrows
        holder.tvLapLabel.setText(mContext.getString(R.string.lap) + ":");
        holder.tvLap.setText(String.valueOf(cp.getLap()));
        if (cp.getLastSensor().isStart() && cp.getLap() > 1) {
            holder.tvLapIntervalLabel.setText(mContext.getString(R.string.lap_interval) + ":");
            holder.tvLapInterval.setText(cp.getLapIntervalString());
            if (cp.getPrevLapInterval() > 0) {
                if (cp.getPrevLapInterval() > cp.getLapInterval()) {
                    holder.ivLapDown.setVisibility(View.VISIBLE);
                }
                else {
                    holder.ivLapUp.setVisibility(View.VISIBLE);
                }
            }
        }
        else {
            holder.tvLapIntervalLabel.setVisibility(View.INVISIBLE);
            holder.tvLapInterval.setVisibility(View.INVISIBLE);
        }

     }

    @Override
    public int getItemCount() {
        return mCheckpointList.size();
    }

    public void setCheckpointList(List<Checkpoint> checkpointList) {
        mCheckpointList = checkpointList;
        notifyDataSetChanged();
    }
}
