package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.SoldProductData;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.SoldHolder> {

    private List<SoldProductData> sList;

    public SoldProductAdapter(List<SoldProductData> sList) {
        this.sList = sList;
    }

    @NonNull
    @Override
    public SoldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SoldProductAdapter.SoldHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sold_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SoldHolder holder, int position) {

        holder.title.setText(sList.get(position).getTitle());
        holder.size.setText(sList.get(position).getSize());
        holder.qty.setText("Qty "+String.valueOf(sList.get(position).getQuantity()));

        if (sList.get(position).getCanvasType()==0) {
            holder.square.setVisibility(View.VISIBLE);
            holder.landscape.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getImage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.squareImage);
        } else if (sList.get(position).getCanvasType()==1) {
            holder.landscape.setVisibility(View.VISIBLE);
            holder.square.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getImage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landscapeImage);
        } else {
            holder.portrait.setVisibility(View.VISIBLE);
            holder.landscape.setVisibility(View.GONE);
            holder.square.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getImage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portraitImage);
        }

    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public class SoldHolder extends  RecyclerView.ViewHolder{
        private TextView title, size,qty;
        public LinearLayout portrait, landscape, square;
        public ImageView squareImage, portraitImage, landscapeImage;

        public SoldHolder(@NonNull View itemView) {
            super(itemView);

            qty=itemView.findViewById(R.id.rsp_qty);
            title=itemView.findViewById(R.id.rsp_title);
            size=itemView.findViewById(R.id.rsp_size);
            portrait = itemView.findViewById(R.id.rsp_portraitLay);
            landscape = itemView.findViewById(R.id.rsp_landscapLay);
            square = itemView.findViewById(R.id.rsp_saquareLay);
            squareImage = itemView.findViewById(R.id.rsp_squareImage);
            portraitImage = itemView.findViewById(R.id.rsp_portraitImage);
            landscapeImage = itemView.findViewById(R.id.rsp_landscapeImage);
        }
    }

}
