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
import gr.artibet.lapper.models.RaceVehicle;

public class ActiveRacesVehiclesAdapter extends RecyclerView.Adapter<ActiveRacesVehiclesAdapter.ActiveRacesVehicleViewHolder> {

    // Context
    private Context mContext;

    // Vehicle List
    private List<RaceVehicle> mRaceVehicleList;

    // VIEW HOLDER CLASS
    public static class ActiveRacesVehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivStatus;
        public TextView tvTag;
        public TextView tvOwner;
        public TextView tvDriver;


        public ActiveRacesVehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.race_vehicle_item_status);
            tvTag = itemView.findViewById(R.id.race_vehicle_item_tag);
            tvOwner = itemView.findViewById(R.id.race_vehicle_item_owner);
            tvDriver = itemView.findViewById(R.id.race_vehicle_item_driver);
        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Vehicle
    public ActiveRacesVehiclesAdapter(Context context, List<RaceVehicle> raceVehicleList) {
        mContext = context;
        mRaceVehicleList = raceVehicleList;
    }

    @NonNull
    @Override
    public ActiveRacesVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_races_vehicle_item, parent, false);
        ActiveRacesVehicleViewHolder viewHolder = new ActiveRacesVehicleViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveRacesVehicleViewHolder holder, int position) {
        RaceVehicle rv = mRaceVehicleList.get(position);

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

     }

    @Override
    public int getItemCount() {
        return mRaceVehicleList.size();
    }

    public void setRaceVehicleList(List<RaceVehicle> raceVehicleList) {
        mRaceVehicleList = raceVehicleList;
        notifyDataSetChanged();
    }
}
