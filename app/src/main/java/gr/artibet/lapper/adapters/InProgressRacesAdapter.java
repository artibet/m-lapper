package gr.artibet.lapper.adapters;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.models.Race;

public class InProgressRacesAdapter extends RecyclerView.Adapter<InProgressRacesAdapter.InProgressRacesViewHolder> {

    // Context
    private static Context mContext;

    // Vehicle List
    private List<Race> mRaceList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onCancel(int position);
        void onViewVehicles(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class InProgressRacesViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPublic;
        public ImageView ivMenu;

        public TextView tvTag;
        public TextView tvCreator;
        public TextView tvStart;
        public TextView tvStartMethod;
        public TextView tvMode;
        public TextView tvVehiclesNo;
        public TextView tvLaps;
        public TextView tvInProgress;
        public TextView tvCanceled;
        public TextView tvFinished;
        public Chronometer crDuration;


        public InProgressRacesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPublic = itemView.findViewById(R.id.inprogress_races_item_ivPublic);
            ivMenu = itemView.findViewById(R.id.ivMenu);

            tvTag = itemView.findViewById(R.id.inprogress_races_item_tag);
            tvCreator = itemView.findViewById(R.id.inprogress_races_item_creator);
            tvStart = itemView.findViewById(R.id.inprogress_races_item_start);
            tvStartMethod = itemView.findViewById(R.id.inprogress_races_item_startMethod);
            tvMode = itemView.findViewById(R.id.inprogress_races_item_mode);
            tvVehiclesNo = itemView.findViewById(R.id.inprogress_races_item_vehiclesNo);
            tvLaps = itemView.findViewById(R.id.inprogress_races_item_laps);
            tvInProgress = itemView.findViewById(R.id.tv_inprogress);
            tvCanceled = itemView.findViewById(R.id.tv_canceled);
            tvFinished = itemView.findViewById(R.id.tv_finished);
            crDuration = itemView.findViewById(R.id.inprogress_races_item_duration);

            // Crete popup menu
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, ivMenu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                // Cancel race
                                case R.id.item_cancel_race:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onCancel(position);
                                        }
                                    }
                                    return true;

                                // View vehicles
                                case R.id.item_vehicles:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onViewVehicles(position);
                                        }
                                    }
                                    return true;

                            }

                            return false;
                        }
                    });
                    popup.inflate(R.menu.inprogress_races_menu);
                    Util.enablePopupIcons(popup);
                    popup.show();
                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF inprogress races
    public InProgressRacesAdapter(Context context, List<Race> raceList) {
        mContext = context;
        mRaceList = raceList;
    }

    @NonNull
    @Override
    public InProgressRacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inprogress_races_item, parent, false);
        InProgressRacesViewHolder viewHolder = new InProgressRacesViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InProgressRacesViewHolder holder, int position) {
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

        // Start datetime and method
        holder.tvStart.setText(mContext.getString(R.string.start) + ": " + Util.timestampToDatetime(race.getStartTs(), true));
        holder.tvStartMethod.setText(mContext.getString(R.string.start_method) + ": " + race.getStartMethod().getDescription());

        // Mode
        holder.tvMode.setText(mContext.getString(R.string.activator) + ": " + race.getMode().getDescription());

        // Number of vehicles
        holder.tvVehiclesNo.setText(mContext.getString(R.string.vehicles) + ": " + String.valueOf(race.getTotalVehicles()));

        // Laps
        holder.tvLaps.setText(mContext.getString(R.string.laps) + ": " + String.valueOf(race.getLaps()));

        // In progress vehicles
        holder.tvInProgress.setText(String.valueOf(race.getRunningVehicles()));

        // Canceled vehicles
        holder.tvCanceled.setText(String.valueOf(race.getCanceledVehicles()));

        // Finished vehicles
        holder.tvFinished.setText(String.valueOf(race.getFinishedVehicles()));

        // Start duration cronometer
        long start_ts_mills = (long)(race.getStartTs() * 1000);
        long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        holder.crDuration.setBase(start_ts_mills - elapsedRealtimeOffset);
        holder.crDuration.setFormat("%s");
        holder.crDuration.start();


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
