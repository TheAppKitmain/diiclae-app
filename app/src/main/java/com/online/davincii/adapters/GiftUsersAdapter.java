package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.ModelClass;

public class GiftUsersAdapter extends RecyclerView.Adapter<GiftUsersAdapter.UserHolder> {

    private ModelClass[] model;

    public GiftUsersAdapter(ModelClass[] model) {
        this.model = model;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_gift_user, parent, false);
        UserHolder viewHolder = new UserHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GiftUsersAdapter.UserHolder holder, int position) {
        holder.imageView.setImageResource(model[position].getImgId());
        holder.name.setText(model[position].getDescription());
    }

    @Override
    public int getItemCount() {
        return model.length;
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rfu_userImage);
            name=itemView.findViewById(R.id.rfu_userName);
        }
    }
}