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
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.Vehicle;

public class RaceVehiclesAdapter extends RecyclerView.Adapter<RaceVehiclesAdapter.RaceVehicleViewHolder> {

    // Context
    private Context mContext;

    // Vehicle List
    private List<RaceVehicle> mRaceVehicleList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class RaceVehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivStatus;
        public ImageView ivDelete;
        public TextView tvTag;
        public TextView tvOwner;
        public TextView tvDriver;


        public RaceVehicleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.race_vehicle_item_status);
            ivDelete = itemView.findViewById(R.id.race_vehicle_item_delete);
            tvTag = itemView.findViewById(R.id.race_vehicle_item_tag);
            tvOwner = itemView.findViewById(R.id.race_vehicle_item_owner);
            tvDriver = itemView.findViewById(R.id.race_vehicle_item_driver);

            // Delete click listener
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDelete(position);
                        }
                    }
                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Vehicle
    public RaceVehiclesAdapter(Context context, List<RaceVehicle> raceVehicleList) {
        mContext = context;
        mRaceVehicleList = raceVehicleList;
    }

    @NonNull
    @Override
    public RaceVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.race_vehicle_item, parent, false);
        RaceVehicleViewHolder viewHolder = new RaceVehicleViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RaceVehicleViewHolder holder, int position) {
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
