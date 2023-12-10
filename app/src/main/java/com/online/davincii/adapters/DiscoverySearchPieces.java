package com.online.davincii.adapters;

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
import com.online.davincii.models.PieceSearch;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscoverySearchPieces extends RecyclerView.Adapter<DiscoverySearchPieces.PiecesHolder> {
    List<PieceSearch> piecesList;

    public DiscoverySearchPieces(List<PieceSearch> piecesList) {
        this.piecesList = piecesList;
    }

    @NonNull
    @NotNull
    @Override
    public PiecesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PiecesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_artforyou, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PiecesHolder holder, int position) {

        if (piecesList.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + piecesList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.squareImg);
        } else if (piecesList.get(position).getCanvastype() == 1) {
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + piecesList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.portLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.saqLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + piecesList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.portraitImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DiscoveryDetails.class).putExtra("id", piecesList.get(position).getId())
                );
            }
        });
    }


    @Override
    public int getItemCount() {
        return piecesList.size();
    }

    public class PiecesHolder extends RecyclerView.ViewHolder {
        private LinearLayout saqLay, portLay, landLay;
        private ImageView squareImg, portraitImg, landImg;

        public PiecesHolder(@NonNull @NotNull View itemView) {
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
