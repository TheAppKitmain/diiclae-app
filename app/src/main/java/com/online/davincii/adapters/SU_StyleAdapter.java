package com.online.davincii.adapters;

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
import com.online.davincii.models.StyleListResponse;

import java.util.List;

public class SU_StyleAdapter extends RecyclerView.Adapter<SU_StyleAdapter.StyleViewHolder> {

    List<StyleListResponse> styleList;

    public SU_StyleAdapter(List<StyleListResponse> styleList) {
        this.styleList = styleList;
    }

    @NonNull
    @Override
    public StyleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater styleinflater = LayoutInflater.from(parent.getContext());
//        View styleItem = styleinflater.inflate(R.layout.recycler_categories_style,parent,false);
//        StyleViewHolder styleHolder = new StyleViewHolder(styleItem);
        return new StyleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_categories_style, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull StyleViewHolder holder, int position) {
        StyleListResponse stlListData = styleList.get(position);
        holder.sl_rv_txt.setText(stlListData.getName());

        holder.sl_rv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.sl_rv_txt.getBackground().getConstantState().equals(holder.itemView.getContext().getResources().getDrawable(R.drawable.border).getConstantState())) {
                    holder.sl_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
                    holder.sl_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));

                    Intent intent = new Intent("list_data");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "1");
                    intent.putExtra("is_add", "1");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                } else {
                    holder.sl_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
                    holder.sl_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));

                    Intent intent = new Intent("list_data");
                    intent.putExtra("psoition", position);
                    intent.putExtra("type", "1");
                    intent.putExtra("is_add", "0");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    public class StyleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sl_rv_ll;
        TextView sl_rv_txt;

        public StyleViewHolder(@NonNull View itemView) {
            super(itemView);
            sl_rv_ll = itemView.findViewById(R.id.su_rv_linearlayout);
            sl_rv_txt = itemView.findViewById(R.id.su_rvtxt);
        }
    }
}
