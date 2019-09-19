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
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.Vehicle;

public class PendingRacesAdapter extends RecyclerView.Adapter<PendingRacesAdapter.PendingRacesViewHolder> {

    // Context
    private Context mContext;

    // Vehicle List
    private List<Race> mRaceList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteItem(int position);
        void onActivateItem(int position);
        void onItemVehicles(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class PendingRacesViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPublic;
        public ImageView ivVehicles;
        public ImageView ivActivate;
        public ImageView ivDelete;

        public TextView tvTag;
        public TextView tvCreator;
        public TextView tvStartMethod;
        public TextView tvMode;
        public TextView tvVehiclesNo;
        public TextView tvLaps;
        public TextView tvUpdatedAt;


        public PendingRacesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPublic = itemView.findViewById(R.id.pending_races_item_ivPublic);
            ivVehicles = itemView.findViewById(R.id.pending_races_item_ivVehicles);
            ivActivate = itemView.findViewById(R.id.pending_races_item_ivActivate);
            ivDelete = itemView.findViewById(R.id.pending_races_item_ivDelete);

            tvTag = itemView.findViewById(R.id.pending_races_item_tag);
            tvCreator = itemView.findViewById(R.id.pending_races_item_creator);
            tvStartMethod = itemView.findViewById(R.id.pending_races_item_startMethod);
            tvMode = itemView.findViewById(R.id.pending_races_item_mode);
            tvVehiclesNo = itemView.findViewById(R.id.pending_races_item_vehiclesNo);
            tvLaps = itemView.findViewById(R.id.pending_races_item_laps);
            tvUpdatedAt = itemView.findViewById(R.id.pending_race_item_updatedAt);

            // Item click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // Delete click listener
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteItem(position);
                        }
                    }
                }
            });

            // Activate click listener
            ivActivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onActivateItem(position);
                        }
                    }
                }
            });

            // Vehicles click listener
            ivVehicles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemVehicles(position);
                        }
                    }
                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF pending races
    public PendingRacesAdapter(Context context, List<Race> raceList) {
        mContext = context;
        mRaceList = raceList;
    }

    @NonNull
    @Override
    public PendingRacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_races_item, parent, false);
        PendingRacesViewHolder viewHolder = new PendingRacesViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRacesViewHolder holder, int position) {
        Race race = mRaceList.get(position);

        // Public
        if (race.isPublic()) {
            holder.ivPublic.setVisibility(View.VISIBLE);
        }
        else {
            holder.ivPublic.setVisibility(View.INVISIBLE);
        }

        // Tag
        holder.tvTag.setText(race.getTag());

        // creator
        holder.tvCreator.setText("(" + race.getCreator().getUsername() + ")");

        // Start Method
        holder.tvStartMethod.setText(mContext.getString(R.string.start_method) + ": " + race.getStartMethod().getDescription());

        // Mode
        holder.tvMode.setText(mContext.getString(R.string.activator) + ": " + race.getMode().getDescription());

        // Number of vehicles
        holder.tvVehiclesNo.setText(mContext.getString(R.string.vehicles) + ": " + String.valueOf(race.getTotalVehicles()));

        // Laps
        holder.tvLaps.setText(mContext.getString(R.string.laps) + ": " + String.valueOf(race.getLaps()));

        // Updated At
        holder.tvUpdatedAt.setText(mContext.getString(R.string.updated) + ": " + Util.TimestampToDatetime(race.getUpdatedAtTs()));

     }

    @Override
    public int getItemCount() {
        return mRaceList.size();
    }

    public void setRaceList(List<Race> raceList) {
        mRaceList = raceList;
        notifyDataSetChanged();
    }
}
