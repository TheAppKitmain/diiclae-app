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
import com.online.davincii.models.discoverysearch.ComicArtResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DC_ComicArtAdapter extends RecyclerView.Adapter<DC_ComicArtAdapter.ComicHolder> {
    private List<ComicArtResponse> comicArtResp;
    private Context context;

    public DC_ComicArtAdapter(List<ComicArtResponse> comicArtResp, Context context) {
        this.comicArtResp = comicArtResp;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ComicHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ComicHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_artforyou, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComicHolder holder, int position) {
        if (comicArtResp.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + comicArtResp.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.squareImg);
        } else if (comicArtResp.get(position).getCanvastype() == 1) {
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + comicArtResp.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.portLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + comicArtResp.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portraitImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DiscoveryDetails.class).putExtra("id", comicArtResp.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicArtResp.size();
    }

    public class ComicHolder extends RecyclerView.ViewHolder {
        private LinearLayout saqLay, portLay, landLay;
        private ImageView squareImg, portraitImg, landImg;

        public ComicHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            saqLay = itemView.findViewById(R.id.art_add_saqLay);
            portLay = itemView.findViewById(R.id.art_add_portLay);
            landLay = itemView.findViewById(R.id.art_add_landLay);
            squareImg = itemView.findViewById(R.id.art_add_saqImg);
            portraitImg = itemView.findViewById(R.id.art_add_portImg);
            landImg = itemView.findViewById(R.id.art_add_landImg);
        }
    }
}
