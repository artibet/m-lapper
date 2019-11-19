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
import gr.artibet.lapper.models.Sensor;

public class SensorsAdapter extends RecyclerView.Adapter<SensorsAdapter.SensorViewHolder> {

    // Context
    private static Context mContext;

    // Sensor List
    private List<Sensor> mSensorList;

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
    public static class SensorViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMenu;
        public TextView mSensorAa;
        public TextView mSensorTag;
        public TextView mSensorThreshold;
        public TextView mSensorUpdatedAt;
        public ImageView mSensorStatus;
        public ImageView mSensorStarter;


        public SensorViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivMenu = itemView.findViewById(R.id.ivMenu);
            mSensorAa = itemView.findViewById(R.id.sensor_item_aa);
            mSensorTag = itemView.findViewById(R.id.sensor_item_tag);
            mSensorThreshold = itemView.findViewById(R.id.sensor_item_threshold);
            mSensorUpdatedAt = itemView.findViewById(R.id.sensor_item_updatedAt);
            mSensorStatus = itemView.findViewById(R.id.sensor_item_status);
            mSensorStarter = itemView.findViewById(R.id.sensor_item_isStart);

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
                    popupMenu.inflate(R.menu.sensors_menu);
                    Util.enablePopupIcons(popupMenu);
                    popupMenu.show();

                }
            });

        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Sensor
    public SensorsAdapter(Context context, List<Sensor> sensorList) {
        mContext = context;
        mSensorList = sensorList;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_item, parent, false);
        SensorViewHolder viewHolder = new SensorViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = mSensorList.get(position);

        // a/a and tag
        holder.mSensorAa.setText(String.valueOf(sensor.getAa()));
        holder.mSensorTag.setText(sensor.getTag());

        // Threshold
        holder.mSensorThreshold.setText(mContext.getString(R.string.threshold)+ ": " + String.valueOf(sensor.getThreshold()) + " sec");

        // UpdatedAt
        holder.mSensorUpdatedAt.setText(mContext.getString(R.string.updated)+ ": " + Util.TimestampToDatetime(sensor.getUpdatedAtTs(), false));

        // status
        if (sensor.isActive()) {
            holder.mSensorStatus.setImageResource(R.drawable.ic_done_green);
        }
        else {
            holder.mSensorStatus.setImageResource(R.drawable.ic_block_red);
        }

        // is starter?
        if (sensor.isStart()) {
            holder.mSensorStarter.setVisibility(View.VISIBLE);
        }
        else {
            holder.mSensorStarter.setVisibility(View.INVISIBLE);
        }




     }

    @Override
    public int getItemCount() {
        return mSensorList.size();
    }

    public void setSensorList(List<Sensor> sensorList) {
        mSensorList = sensorList;
        notifyDataSetChanged();
    }
}
