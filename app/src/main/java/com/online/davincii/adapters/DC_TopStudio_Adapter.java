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
import com.online.davincii.activities.ArtistDetail;
import com.online.davincii.models.discoverysearch.TopStudioResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DC_TopStudio_Adapter extends RecyclerView.Adapter<DC_TopStudio_Adapter.TopStudiioHolder> {
    List<TopStudioResponse> topStudioRespon;
    private Context context;

    public DC_TopStudio_Adapter(List<TopStudioResponse> topStudioRespon, Context context) {
        this.topStudioRespon = topStudioRespon;
        this.context = context;
    }

    @NonNull
    @Override
    public TopStudiioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopStudiioHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topstudio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopStudiioHolder holder, int position) {
//        if (topStudioRespon.get(position).getStudioPicture().equals("0")) {
        holder.landLay.setVisibility(View.VISIBLE);
        Picasso.get().load(Constant.STUDIO_IMG_BASE + topStudioRespon.get(position).getImageurl()).into(holder.landImg);
//        } else if (topStudioRespon.get(position).getStudioPicture().equals("1")) {
//            holder.landLay.setVisibility(View.VISIBLE);
//            Picasso.get().load(Constant.STUDIO_IMG_BASE + topStudioRespon.get(position).getStudioPicture()).into(holder.landImg);
//        } else {
//            holder.portLay.setVisibility(View.VISIBLE);
//            Picasso.get().load(Constant.STUDIO_IMG_BASE + topStudioRespon.get(position).getStudioPicture()).into(holder.portImg);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ArtistDetail.class).putExtra("id", topStudioRespon.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topStudioRespon.size();
    }

    public class TopStudiioHolder extends RecyclerView.ViewHolder {
        private LinearLayout landLay, saqLay, portLay;
        private ImageView landImg, saqImg, portImg;

        public TopStudiioHolder(@NonNull View itemView) {
            super(itemView);

            landLay = itemView.findViewById(R.id.ts_landLay);
            saqLay = itemView.findViewById(R.id.ts_saqLay);
            portLay = itemView.findViewById(R.id.ts_portLay);
            landImg = itemView.findViewById(R.id.ts_landImg);
            saqImg = itemView.findViewById(R.id.ts_saqImg);
            portImg = itemView.findViewById(R.id.ts_portImg);

        }
    }
}
