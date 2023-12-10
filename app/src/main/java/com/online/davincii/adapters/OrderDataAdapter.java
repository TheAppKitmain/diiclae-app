package com.online.davincii.adapters;

import android.content.Intent;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.online.davincii.R;
import com.online.davincii.activities.OrderDetail;
import com.online.davincii.models.OrderCart;
import com.online.davincii.models.OrderDetailResponse;
import com.online.davincii.models.order.OrderData;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;

import java.util.List;

public class OrderDataAdapter extends RecyclerView.Adapter<OrderDataAdapter.ViesHolder> {

    private List<OrderCart> cList;

    public OrderDataAdapter(List<OrderCart> cList) {
        this.cList = cList;
    }

    @NonNull
    @Override
    public ViesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViesHolder holder, int position) {

        // for (int i=0;cList.get(position).getCart().size()>0; i++) {

        holder.artistName.setText(cList.get(position).getTitle());
        holder.price.setText(BaseUtil.getCurrency(holder.itemView.getContext()) + String.format("%.2f", cList.get(position).getPrice()));
        holder.size.setText(cList.get(position).getSize());
        holder.qty.setText(String.valueOf(cList.get(position).getQuantity()) + " Qty");

        int type = Integer.parseInt(cList.get(position).getCanvasType());
        if (type == 0) {
            holder.landscapeLayout.setVisibility(View.GONE);
            holder.portraitLayout.setVisibility(View.GONE);
            holder.squareLayout.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.squareImg);
        } else if (type == 1) {
            holder.portraitLayout.setVisibility(View.GONE);
            holder.squareLayout.setVisibility(View.GONE);
            holder.landscapeLayout.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.landscapeImg);
        } else {
            holder.squareLayout.setVisibility(View.GONE);
            holder.landscapeLayout.setVisibility(View.GONE);
            holder.portraitLayout.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.portraitImg);
        }

//            int type = cList.get(position).getCanvasId();
//            if (type == 0) {
//                holder.landscapeLayout.setVisibility(View.GONE);
//                holder.portraitLayout.setVisibility(View.GONE);
//                holder.squareLayout.setVisibility(View.VISIBLE);
//                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.squareImg);
//            } else if (type == 1) {
//                holder.portraitLayout.setVisibility(View.GONE);
//                holder.squareLayout.setVisibility(View.GONE);
//                holder.landscapeLayout.setVisibility(View.VISIBLE);
//                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.landscapeImg);
//            } else {
//                holder.squareLayout.setVisibility(View.GONE);
//                holder.landscapeLayout.setVisibility(View.GONE);
//                holder.portraitLayout.setVisibility(View.VISIBLE);
//                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).into(holder.portraitImg);
//            }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), OrderDetail.class);

            }
        });
        //    }

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class ViesHolder extends RecyclerView.ViewHolder {

        private TextView artistName, size, price, qty;
        private LinearLayout portraitLayout, landscapeLayout, squareLayout;
        private ImageView squareImg, landscapeImg, portraitImg;

        public ViesHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.rod_artistName);
            size = itemView.findViewById(R.id.rod_size);
            price = itemView.findViewById(R.id.rod_price);
            qty = itemView.findViewById(R.id.rod_qty);
            squareLayout = itemView.findViewById(R.id.rod_saquareLay);
            portraitLayout = itemView.findViewById(R.id.rod_portraitLay);
            landscapeLayout = itemView.findViewById(R.id.rod_landscapLay);
            squareImg = itemView.findViewById(R.id.rod_squareImage);
            landscapeImg = itemView.findViewById(R.id.rod_landscapeImage);
            portraitImg = itemView.findViewById(R.id.rod_portraitImage);

        }
    }
}
