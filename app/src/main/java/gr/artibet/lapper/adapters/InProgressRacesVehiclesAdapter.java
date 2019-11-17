package gr.artibet.lapper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.RaceVehicleState;

public class InProgressRacesVehiclesAdapter extends RecyclerView.Adapter<InProgressRacesVehiclesAdapter.RaceVehicleViewHolder> {

    // Context
    private Context mContext;

    // Vehicle List
    private List<RaceVehicle> mRaceVehicleList;

    // Race laps
    private int mRaceLaps;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onCancel(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class RaceVehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivStatus;
        public ImageView ivCancel;
        public TextView tvAa;
        public TextView tvTag;
        public TextView tvOwner;
        public TextView tvDriver;
        public TextView tvSector;
        public TextView tvSectorInterval;
        public TextView tvBestSectorIntervalLabel;
        public TextView tvBestSectorInterval;
        public TextView tvLapLabel;
        public TextView tvLap;
        public TextView tvLapIntervalLabel;
        public TextView tvLapInterval;
        public TextView tvBestLapIntervalLabel;
        public TextView tvBestLapInterval;


        public RaceVehicleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.race_vehicle_item_status);
            ivCancel = itemView.findViewById(R.id.race_vehicle_item_delete);
            tvAa = itemView.findViewById(R.id.tvAa);
            tvTag = itemView.findViewById(R.id.race_vehicle_item_tag);
            tvOwner = itemView.findViewById(R.id.race_vehicle_item_owner);
            tvDriver = itemView.findViewById(R.id.race_vehicle_item_driver);
            tvSector = itemView.findViewById(R.id.tvSector);
            tvSectorInterval = itemView.findViewById(R.id.tvSectorInterval);
            tvBestSectorIntervalLabel = itemView.findViewById(R.id.tvBestSectorIntervalLabel);
            tvBestSectorInterval = itemView.findViewById(R.id.tvBestSectorInterval);
            tvLapLabel = itemView.findViewById(R.id.tvLapLabel);
            tvLap = itemView.findViewById(R.id.tvLap);
            tvLapIntervalLabel = itemView.findViewById(R.id.tvLapIntervalLabel);
            tvLapInterval = itemView.findViewById(R.id.tvLapInterval);
            tvBestLapIntervalLabel = itemView.findViewById(R.id.tvBestLapIntervalLabel);
            tvBestLapInterval = itemView.findViewById(R.id.tvBestLapInterval);

            // Cancel vehicle click listener
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCancel(position);
                        }
                    }
                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Vehicle and race laps
    public InProgressRacesVehiclesAdapter(Context context, List<RaceVehicle> raceVehicleList, int laps) {
        mContext = context;
        mRaceVehicleList = raceVehicleList;
        mRaceLaps = laps;
        sortVehicleList();
    }

    @NonNull
    @Override
    public RaceVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inprogress_races_vehicle_item, parent, false);
        RaceVehicleViewHolder viewHolder = new RaceVehicleViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RaceVehicleViewHolder holder, int position) {
        RaceVehicle rv = mRaceVehicleList.get(position);

        // AA
        String aa = Integer.toString(position + 1) + ".";
        holder.tvAa.setText(aa);

        // Tag
        holder.tvTag.setText(rv.getVehicle().getTag());

        // Owner
        holder.tvOwner.setText("(" + rv.getVehicle().getOwner().getUsername() + ")");

        // Driver
        holder.tvDriver.setText(mContext.getString(R.string.driver)+ ": " + rv.getVehicle().getDriver());

        // status
        if (rv.getVehicle().isActive()) {
            holder.ivStatus.setImageResource(R.drawable.ic_done_green);
        }
        else {
            holder.ivStatus.setImageResource(R.drawable.ic_block_red);
        }

        // Sector
        String sector;
        if (rv.getPrevSensor() == null) {
            sector = mContext.getResources().getString(R.string.race_start);
            holder.tvSectorInterval.setVisibility(View.INVISIBLE);
        }
        else {
            sector = rv.getPrevSensor().getTag() + " - " + rv.getLastSensor().getTag() + ":";
        }
        holder.tvSector.setText(sector);

        // Sector interval
        holder.tvSectorInterval.setText(rv.getIntervalString());

        // Best sector interval
        holder.tvBestSectorIntervalLabel.setText(mContext.getResources().getString(R.string.best_time) + ":");
        holder.tvBestSectorInterval.setText(rv.getBestIntervalString());

        // Lap
        holder.tvLapLabel.setText(mContext.getString(R.string.lap) + ":");
        holder.tvLap.setText(String.valueOf(rv.getLap()) + "/" + mRaceLaps);

        // Lap interval - when passing start sensor
        holder.tvLapIntervalLabel.setText(mContext.getString(R.string.lap_interval) + ":");
        holder.tvLapInterval.setText(rv.getLapIntervalString());
        holder.tvBestLapIntervalLabel.setText(mContext.getString(R.string.best_lap_interval) + ":");
        holder.tvBestLapInterval.setText(rv.getBestLapIntervalString());

        // Set status icon and remove delete button if finished or canceled
        String uri = null;
        switch (rv.getState().getId()) {
            case RaceVehicleState.STATE_RUNNING:
                uri = "@drawable/ic_flash_green";
            break;
            case RaceVehicleState.STATE_CANCELED:
                uri = "@drawable/ic_block_red";
                holder.ivCancel.setVisibility(View.INVISIBLE);
                break;
            case RaceVehicleState.STATE_FINISHED:
                uri = "@drawable/ic_flag_blue";
                holder.ivCancel.setVisibility(View.INVISIBLE);
                break;
        }
        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        Drawable res = mContext.getResources().getDrawable(imageResource);
        holder.ivStatus.setImageDrawable(res);


     }

    @Override
    public int getItemCount() {
        return mRaceVehicleList.size();
    }

    public void setRaceVehicleList(List<RaceVehicle> raceVehicleList) {
        mRaceVehicleList = raceVehicleList;
        sortVehicleList();
        notifyDataSetChanged();
    }


    // Sort Vehicle List
    public void sortVehicleList() {
        Collections.sort(mRaceVehicleList, new Comparator<RaceVehicle>() {
            @Override
            public int compare(RaceVehicle lhs, RaceVehicle rhs) {
                int lhsState = lhs.getState().getId();
                int rhsState = rhs.getState().getId();
                if (lhsState == RaceVehicleState.STATE_FINISHED && rhsState != RaceVehicleState.STATE_FINISHED) return -1;
                if (lhsState != RaceVehicleState.STATE_FINISHED && rhsState == RaceVehicleState.STATE_FINISHED) return 1;
                if (lhsState == RaceVehicleState.STATE_CANCELED && rhsState != RaceVehicleState.STATE_CANCELED) return 1;
                if (lhsState != RaceVehicleState.STATE_CANCELED && rhsState == RaceVehicleState.STATE_CANCELED) return -1;
                if (lhs.getLap() > rhs.getLap()) return -1;
                if (lhs.getLap() < rhs.getLap()) return 1;
                if (lhs.getLastSensor().getAa() > rhs.getLastSensor().getAa()) return -1;
                if (lhs.getLastSensor().getAa() < rhs.getLastSensor().getAa()) return 1;
                return (int)(lhs.getLastTs() - rhs.getLastTs());
            }
        });
    }
}
