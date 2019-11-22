package gr.artibet.lapper.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.RaceVehicleState;

public class CompletedRacesVehiclesAdapter extends RecyclerView.Adapter<CompletedRacesVehiclesAdapter.RaceVehicleViewHolder> {

    // Context
    private static Context mContext;

    // Vehicle List
    private List<RaceVehicle> mRaceVehicleList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onViewCheckpoints(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class RaceVehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivStatus;
        public ImageView ivMenu;
        public TextView tvAa;
        public TextView tvTag;
        public TextView tvOwner;
        public TextView tvDriverLabel;
        public TextView tvDriver;
        public TextView tvDurationLabel;
        public TextView tvDuration;

        public RaceVehicleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.race_vehicle_item_status);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvAa = itemView.findViewById(R.id.tvAa);
            tvTag = itemView.findViewById(R.id.race_vehicle_item_tag);
            tvOwner = itemView.findViewById(R.id.race_vehicle_item_owner);
            tvDriverLabel = itemView.findViewById(R.id.tvDriverLabel);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDurationLabel = itemView.findViewById(R.id.tvDurationLabel);
            tvDuration = itemView.findViewById(R.id.tvDuration);

            // Crete popup menu
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, ivMenu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                // Cancel vehicle
                                case R.id.item_checkpoints:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onViewCheckpoints(position);
                                        }
                                    }
                                    return true;
                            }

                            return false;
                        }
                    });

                    // Show popup
                    popupMenu.inflate(R.menu.completed_races_vehicles_menu);
                    Util.enablePopupIcons(popupMenu);
                    popupMenu.show();
                }
            });



        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Vehicle and race laps
    public CompletedRacesVehiclesAdapter(Context context, List<RaceVehicle> raceVehicleList) {
        mContext = context;
        mRaceVehicleList = raceVehicleList;
        Util.sortCompletedVehicleList(mRaceVehicleList);
    }

    @NonNull
    @Override
    public RaceVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_races_vehicle_item, parent, false);
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
        holder.tvDriverLabel.setText(mContext.getString(R.string.driver)+ ":");
        holder.tvDriver.setText(rv.getDriver());

        // Duration
        holder.tvDurationLabel.setText(mContext.getString(R.string.duration)+ ":");
        holder.tvDuration.setText(rv.getDuration());

        // Set status icon and disable cancelation menu item
        String uri = null;
        switch (rv.getState().getId()) {
            case RaceVehicleState.STATE_RUNNING:
                uri = "@drawable/ic_flash_green";
            break;
            case RaceVehicleState.STATE_CANCELED:
                uri = "@drawable/ic_block_red";
                break;
            case RaceVehicleState.STATE_FINISHED:
                uri = "@drawable/ic_flag_blue";
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
        Util.sortCompletedVehicleList(mRaceVehicleList);
        notifyDataSetChanged();
    }

}
