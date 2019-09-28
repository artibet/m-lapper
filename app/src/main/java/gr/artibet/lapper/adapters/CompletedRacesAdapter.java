package gr.artibet.lapper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.models.Race;

public class CompletedRacesAdapter extends RecyclerView.Adapter<CompletedRacesAdapter.CompletedRacesViewHolder> {

    // Context
    private static Context mContext;

    // Vehicle List
    private List<Race> mRaceList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onDeleteRace(int position);
        void onViewVehicles(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class CompletedRacesViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPublic;
        public ImageView ivMenu;

        public TextView tvTag;
        public TextView tvCreator;
        public TextView tvStartMethod;
        public TextView tvMode;
        public TextView tvVehiclesNo;
        public TextView tvLaps;
        public TextView tvUpdatedAt;


        public CompletedRacesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPublic = itemView.findViewById(R.id.completed_races_item_ivPublic);
            ivMenu = itemView.findViewById(R.id.ivMenu);

            tvTag = itemView.findViewById(R.id.completed_races_item_tag);
            tvCreator = itemView.findViewById(R.id.completed_races_item_creator);
            tvStartMethod = itemView.findViewById(R.id.completed_races_item_startMethod);
            tvMode = itemView.findViewById(R.id.completed_races_item_mode);
            tvVehiclesNo = itemView.findViewById(R.id.completed_races_item_vehiclesNo);
            tvLaps = itemView.findViewById(R.id.completed_races_item_laps);
            tvUpdatedAt = itemView.findViewById(R.id.completed_race_item_updatedAt);


            // Crete popup menu
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, ivMenu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                // Delete
                                case R.id.item_delete:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onDeleteRace(position);
                                        }
                                    }
                                    return true;

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
                    popup.inflate(R.menu.completed_races_menu);
                    Util.enablePopupIcons(popup);
                    popup.show();

                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF pending races and the context
    public CompletedRacesAdapter(Context context, List<Race> raceList) {
        mContext = context;
        mRaceList = raceList;
    }

    @NonNull
    @Override
    public CompletedRacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_races_item, parent, false);
        CompletedRacesViewHolder viewHolder = new CompletedRacesViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedRacesViewHolder holder, int position) {
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
