package com.online.davincii.adapters;

import android.app.Activity;
import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.ActivityChat;
import com.online.davincii.models.SupportersData;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SupporterAdapter extends RecyclerView.Adapter<SupporterAdapter.ViewHolder> {

    private List<SupportersData> sList;
    private String canvasId,canvasImage,canvasName,artBy;

    public SupporterAdapter(List<SupportersData> sList, String canvasId, String canvasImage, String canvasName,String artBy) {
        this.sList = sList;
        this.canvasId = canvasId;
        this.canvasImage = canvasImage;
        this.canvasName = canvasName;
        this.artBy=artBy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_liked_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalBroadcastManager.getInstance(holder.itemView.getContext()).registerReceiver(receiver,new IntentFilter(""));
        holder.userName.setText(sList.get(position).getName());
        Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getImage()).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), ActivityChat.class);
                intent.putExtra("studioImage", Constant.PROFILE_IMG_BASE + sList.get(position).getImage());
                intent.putExtra("studioName",sList.get(position).getName());
                intent.putExtra("studioId", sList.get(position).getUserid());
                intent.putExtra("canvasName",canvasName);
                intent.putExtra("canvasImage",canvasImage);
                intent.putExtra("canvasId",canvasId);
                intent.putExtra("artBy",artBy);
                intent.putExtra("share","");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImage;
        private TextView userName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.rlu_userImage);
            userName=itemView.findViewById(R.id.rlu_userName);
        }
    }

}
