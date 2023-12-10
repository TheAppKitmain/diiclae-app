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
import com.online.davincii.models.discoverysearch.TopStudio_SeeAllresponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TopStudio_SeeAll extends RecyclerView.Adapter<TopStudio_SeeAll.Studioholder> {
    private List<TopStudio_SeeAllresponse> topStudio_seeAlls;
    private Context context;

    public TopStudio_SeeAll(List<TopStudio_SeeAllresponse> topStudio_seeAlls, Context context) {
        this.topStudio_seeAlls = topStudio_seeAlls;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public Studioholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Studioholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topstudio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Studioholder holder, int position) {
        holder.landLay.setVisibility(View.VISIBLE);
        Picasso.get().load(Constant.STUDIO_IMG_BASE + topStudio_seeAlls.get(position).getImageurl()).into(holder.landImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ArtistDetail.class).putExtra("id", topStudio_seeAlls.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topStudio_seeAlls.size();
    }

    public class Studioholder extends RecyclerView.ViewHolder {
        private LinearLayout landLay;
        private ImageView landImg;

        public Studioholder(@NonNull @NotNull View itemView) {
            super(itemView);
            landImg = itemView.findViewById(R.id.ts_landImg);
            landLay = itemView.findViewById(R.id.ts_landLay);
        }
    }
}
