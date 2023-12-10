package com.online.davincii.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.AddArtPiece;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.models.ProfileCollections;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UpCollectionAdapter extends RecyclerView.Adapter<UpCollectionAdapter.CollectionHolder> {

    private List<ProfileCollections> pList;
    private boolean isOther;

    public UpCollectionAdapter(List<ProfileCollections> pList, boolean isOther) {
        this.pList = pList;
        this.isOther = isOther;
    }

    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upcollection_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionHolder holder, @SuppressLint("RecyclerView") int position) {

        if (pList.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + pList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.saqImg);
        } else if (pList.get(position).getCanvastype() == 1) {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + pList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.VISIBLE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + pList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOther) {
                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DiscoveryDetails.class).putExtra("id", pList.get(position).getId()));

//                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AddArtPiece.class)
//                            .putExtra("buyId", pList.get(position).getId())
//                            .putExtra("canvasId", pList.get(position).getCanvastype())
//                            .putExtra("imgId", Constant.PROFILE_IMG_BASE + pList.get(position).getImageurl()));
                }else{
                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DiscoveryDetails.class).putExtra("id", pList.get(position).getId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    public class CollectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout portLay, saqLay, landLay;
        private ImageView portImg, saqImg, landImg, close, delete;

        public CollectionHolder(@NonNull View itemView) {
            super(itemView);
            portLay = itemView.findViewById(R.id.cl_portLay);
            saqLay = itemView.findViewById(R.id.cl_saqLay);
            landLay = itemView.findViewById(R.id.cl_landLay);
            portImg = itemView.findViewById(R.id.cl_portImg);
            saqImg = itemView.findViewById(R.id.cl_saqImg);
            landImg = itemView.findViewById(R.id.cl_landImg);
//            close = itemView.findViewById(R.id.up_rv_close);
//            delete = itemView.findViewById(R.id.up_rv_delete);

        }
    }
}
