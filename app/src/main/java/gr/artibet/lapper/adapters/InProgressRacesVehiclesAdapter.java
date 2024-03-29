package gr.artibet.lapper.adapters;

import android.content.Context;
import android.graphics.Color;
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

public class InProgressRacesVehiclesAdapter extends RecyclerView.Adapter<InProgressRacesVehiclesAdapter.RaceVehicleViewHolder> {

    // Context
    private static Context mContext;

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
        public ImageView ivMenu;
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

        public RaceVehicleViewHolder(@NonNull View itemView, final OnItemClickListener listener, final List<RaceVehicle> rvList) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.race_vehicle_item_status);
            ivMenu = itemView.findViewById(R.id.ivMenu);
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
                                case R.id.item_cancel_vehicle:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onCancel(position);
                                        }
                                    }
                                    return true;
                            }

                            return false;
                        }
                    });

                    // Show popup and disable cancelation item if state not is running
                    popupMenu.inflate(R.menu.inprogress_races_vehicles_menu);
                    Util.enablePopupIcons(popupMenu);
                    popupMenu.show();
                    MenuItem menuItemCancel = popupMenu.getMenu().findItem(R.id.item_cancel_vehicle);
                    int position = getAdapterPosition();
                    RaceVehicle rv = rvList.get(position);
                    if (rv.getState().getId() != RaceVehicleState.STATE_RUNNING) {
                        menuItemCancel.setEnabled(false);
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
        Util.sortVehicleList(mRaceVehicleList);
    }

    @NonNull
    @Override
    public RaceVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inprogress_races_vehicle_item, parent, false);
        RaceVehicleViewHolder viewHolder = new RaceVehicleViewHolder(v, mItemListener, mRaceVehicleList);
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
        holder.tvDriver.setText(mContext.getString(R.string.driver)+ ": " + rv.getDriver());

        // Sector
        String sector;
        if (rv.getPrevSensor() == null) {
            sector = mContext.getResources().getString(R.string.race_start);
            holder.tvSectorInterval.setVisibility(View.INVISIBLE);
        }
        else {
            sector = rv.getPrevSensor().getTag() + " - " + rv.getLastSensor().getTag() + ":";
            holder.tvSectorInterval.setVisibility(View.VISIBLE);
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
        Util.sortVehicleList(mRaceVehicleList);
        notifyDataSetChanged();
    }
}
