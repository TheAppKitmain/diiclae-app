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
import com.online.davincii.models.discoverysearch.CulturedArtResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DC_CulturedArtAdapter extends RecyclerView.Adapter<DC_CulturedArtAdapter.CulturedHolder> {
    private List<CulturedArtResponse> culturedArt;
    private Context context;

    public DC_CulturedArtAdapter(List<CulturedArtResponse> culturedArt, Context context) {
        this.culturedArt = culturedArt;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CulturedHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CulturedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_artforyou, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CulturedHolder holder, int position) {
        if (culturedArt.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + culturedArt.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.squareImg);
        } else if (culturedArt.get(position).getCanvastype() == 1) {
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + culturedArt.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.portLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + culturedArt.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portraitImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DiscoveryDetails.class).putExtra("id", culturedArt.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return culturedArt.size();
    }

    public class CulturedHolder extends RecyclerView.ViewHolder {
        private LinearLayout saqLay, portLay, landLay;
        private ImageView squareImg, portraitImg, landImg;


        public CulturedHolder(@NonNull @NotNull View itemView) {
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
