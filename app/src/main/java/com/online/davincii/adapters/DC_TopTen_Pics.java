package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.models.discoverysearch.TopTenPieceResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DC_TopTen_Pics extends RecyclerView.Adapter<DC_TopTen_Pics.TopTenHolder> {

    List<TopTenPieceResponse> topTenResponses;

    private Context context;

    public DC_TopTen_Pics(List<TopTenPieceResponse> topTenResponses, Context context) {
        this.topTenResponses = topTenResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public TopTenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopTenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topten, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopTenHolder holder, int position) {
        if (topTenResponses.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + topTenResponses.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.saqImg);
        } else if (topTenResponses.get(position).getCanvastype() == 1) {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + topTenResponses.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.VISIBLE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + topTenResponses.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DiscoveryDetails.class).putExtra("id", topTenResponses.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topTenResponses.size();
    }

    public class TopTenHolder extends RecyclerView.ViewHolder {
        private LinearLayout portLay, saqLay, landLay;
        private ImageView portImg, saqImg, landImg;

        public TopTenHolder(@NonNull View itemView) {
            super(itemView);

            portLay = itemView.findViewById(R.id.tt_add_portLay);
            saqLay = itemView.findViewById(R.id.tt_add_saqLay);
            landLay = itemView.findViewById(R.id.tt_add_landLay);
            portImg = itemView.findViewById(R.id.tt_add_portImg);
            saqImg = itemView.findViewById(R.id.tt_add_saqImg);
            landImg = itemView.findViewById(R.id.tt_add_landImg);

        }
    }
}