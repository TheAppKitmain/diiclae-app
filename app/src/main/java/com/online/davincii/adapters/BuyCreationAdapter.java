package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.BuyCreationCanvasSize;
import com.online.davincii.utils.BaseUtil;


import java.util.List;

public class BuyCreationAdapter extends RecyclerView.Adapter<BuyCreationAdapter.CreationHolder> {

    private TextView previousSelected, priceSelected;
    List<BuyCreationCanvasSize> creationSizes;
    private Context context;
    private Integer selectedId;

    public BuyCreationAdapter(List<BuyCreationCanvasSize> creationSizes, Context context, Integer selectedId) {
        this.creationSizes = creationSizes;
        this.context = context;
        this.selectedId = selectedId;
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
    public CreationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_size, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreationHolder holder, int position) {

        holder.canvasSize.setText(creationSizes.get(position).getSize());
        holder.canvasPrice.setText(BaseUtil.getCurrency(holder.itemView.getContext()) + String.format("%.2f", Double.valueOf(creationSizes.get(position).getPrice())));

        if (position == selectedId) {
            holder.canvasSize.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.blue_button));
            holder.canvasSize.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.canvasPrice.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        } else {
            holder.canvasSize.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.gray_btn));
            holder.canvasSize.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.canvasPrice.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        holder.canvasSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("send_info");
                intent.putExtra("sizeId", creationSizes.get(position).getId());
                intent.putExtra("position", position);
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                selectedId = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return creationSizes.size();
    }

    public class CreationHolder extends RecyclerView.ViewHolder {
        private TextView canvasSize, canvasPrice;
        private LinearLayout linearLayout;

        public CreationHolder(@NonNull View itemView) {
            super(itemView);

            canvasSize = itemView.findViewById(R.id.add_rv_sizes);
            canvasPrice = itemView.findViewById(R.id.add_rv_prices);
            linearLayout = itemView.findViewById(R.id.add_rv_linearlayout);
        }
    }
}
