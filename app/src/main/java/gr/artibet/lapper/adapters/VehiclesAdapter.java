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

import org.w3c.dom.Text;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.RaceVehicleState;
import gr.artibet.lapper.models.Sensor;
import gr.artibet.lapper.models.Vehicle;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder> {

    // Context
    private static Context mContext;

    // Vehicle List
    private List<Vehicle> mVehicleList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class VehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMenu;
        public ImageView ivStatus;
        public TextView tvTag;
        public TextView tvOwner;
        public TextView tvDriver;
        public TextView tvUpdatedAt;


        public VehicleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivStatus = itemView.findViewById(R.id.vehicle_item_status);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            tvTag = itemView.findViewById(R.id.vehicle_item_tag);
            tvOwner = itemView.findViewById(R.id.vehicle_item_owner);
            tvDriver = itemView.findViewById(R.id.vehicle_item_driver);
            tvUpdatedAt = itemView.findViewById(R.id.vehicle_item_updatedAt);

            // Crete popup menu
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, ivMenu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                // Edit
                                case R.id.item_edit:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onEdit(position);
                                        }
                                    }
                                    return true;

                                // Delete
                                case R.id.item_delete:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onDelete(position);
                                        }
                                    }
                                    return true;
                            }

                            return false;
                        }
                    });

                    // Show popup
                    popupMenu.inflate(R.menu.vehicles_menu);
                    Util.enablePopupIcons(popupMenu);
                    popupMenu.show();

                }
            });


        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Vehicle
    public VehiclesAdapter(Context context, List<Vehicle> vehicleList) {
        mContext = context;
        mVehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item, parent, false);
        VehicleViewHolder viewHolder = new VehicleViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = mVehicleList.get(position);

        // Tag
        holder.tvTag.setText(vehicle.getTag());

        // Owner
        holder.tvOwner.setText("(" + vehicle.getOwner().getUsername() + ")");

        // Driver
        holder.tvDriver.setText(mContext.getString(R.string.driver)+ ": " + vehicle.getDriver());

        // Updated At
        holder.tvUpdatedAt.setText(mContext.getString(R.string.updated) + ": " + Util.TimestampToDatetime(vehicle.getUpdatedAtTs(), false));

        // status
        if (vehicle.isActive()) {
            holder.ivStatus.setImageResource(R.drawable.ic_done_green);
        }
        else {
            holder.ivStatus.setImageResource(R.drawable.ic_block_red);
        }

     }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }

    public void setmVehicleList(List<Vehicle> vehicleList) {
        mVehicleList = vehicleList;
        notifyDataSetChanged();
    }
}
