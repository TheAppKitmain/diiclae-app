package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.models.discoverysearch.ArtForYouResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DC_ArtForYou_Adapter extends RecyclerView.Adapter<DC_ArtForYou_Adapter.ArtForHolder> {

    List<ArtForYouResponse> artForResponses;

    private Context context;

    public DC_ArtForYou_Adapter(List<ArtForYouResponse> artForResponses, Context context) {
        this.artForResponses = artForResponses;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ArtForHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtForHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explore, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtForHolder holder, int position) {

        if (artForResponses.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + artForResponses.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.squareImg);
        } else if (artForResponses.get(position).getCanvastype() == 1) {
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + artForResponses.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.portLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + artForResponses.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.portraitImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, DiscoveryDetails.class).putExtra("id", artForResponses.get(position).getId())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return artForResponses.size();
    }

    public class ArtForHolder extends RecyclerView.ViewHolder {
        private LinearLayout saqLay, portLay, landLay, raMain;
        private ImageView squareImg, portraitImg, landImg;

        public ArtForHolder(@NonNull View itemView) {
            super(itemView);

            raMain=itemView.findViewById(R.id.ra_main);
            saqLay = itemView.findViewById(R.id.re_add_saqLay);
            portLay = itemView.findViewById(R.id.re_add_portLay);
            landLay = itemView.findViewById(R.id.re_add_landLay);
            squareImg = itemView.findViewById(R.id.re_add_saqImg);
            portraitImg = itemView.findViewById(R.id.re_add_portImg);
            landImg = itemView.findViewById(R.id.re_add_landImg);

        }
    }
}
