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
import gr.artibet.lapper.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    // Context
    private static Context mContext;

    // User List
    private List<User> mUserList;

    // item click listener member
    private OnItemClickListener mItemListener;


    // Item click interface
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onResetPassword(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }


    // VIEW HOLDER CLASS
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView mUsername;
        public TextView mRole;
        public TextView mFullName;
        public TextView mDateJoined;
        public ImageView mUserStatus;
        public ImageView ivMenu;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.user_item_username);
            mRole = itemView.findViewById(R.id.user_item_role);
            mFullName = itemView.findViewById(R.id.user_item_fullname);
            mDateJoined = itemView.findViewById(R.id.user_item_dateJoined);
            mUserStatus = itemView.findViewById(R.id.user_item_status);
            ivMenu = itemView.findViewById(R.id.ivMenu);

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

            // Crete popup menu
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, ivMenu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                // Edit
                                case R.id.item_edit:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onItemClick(position);
                                        }
                                    }
                                    return true;

                                // Reset passwored
                                case R.id.item_resetPassword:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onResetPassword(position);
                                        }
                                    }
                                    return true;

                                // Delete
                                case R.id.item_delete:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onDeleteClick(position);
                                        }
                                    }
                                    return true;

                            }

                            return false;
                        }
                    });
                    popup.inflate(R.menu.users_menu);
                    Util.enablePopupIcons(popup);
                    popup.show();

                }
            });



        }
    }

    // CONSTRUCTOR WITH AN ARRAY LIST OF Users
    public UsersAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(v, mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUserList.get(position);

        // status
        if (user.isActive()) {
            holder.mUserStatus.setImageResource(R.drawable.ic_done_green);
        }
        else {
            holder.mUserStatus.setImageResource(R.drawable.ic_block_red);
        }

        // Username
        holder.mUsername.setText(user.getUsername());

        // Role
        String role = "";
        if (user.isSuperUser()) {
            role = mContext.getString(R.string.administrator);
        }
        else {
            role = mContext.getString(R.string.normal_user);
        }
        holder.mRole.setText("(" + role + ")");

        // Fullname
        holder.mFullName.setText(user.getFullName());

        // DateJoined
        holder.mDateJoined.setText(mContext.getString(R.string.date_joined)+ ": " + Util.timestampToDatetime(user.getDateJoinedTs(), false));

     }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setUserList(List<User> userList) {
        mUserList = userList;
        notifyDataSetChanged();
    }
}
