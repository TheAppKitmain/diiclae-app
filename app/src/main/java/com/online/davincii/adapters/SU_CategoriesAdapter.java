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
import com.online.davincii.models.CategorylistResponse;

import java.util.List;


public class SU_CategoriesAdapter extends RecyclerView.Adapter<SU_CategoriesAdapter.CategoriesViewHolder> {

    List<CategorylistResponse> catelist;

    public SU_CategoriesAdapter(List<CategorylistResponse> cateList) {
        this.catelist = cateList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CategoriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_categories_style, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        CategorylistResponse catListData = catelist.get(position);
        holder.ct_rv_txt.setText(catListData.getName());

        holder.ct_rv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.ct_rv_txt.getBackground().getConstantState().equals(holder.itemView.getContext().getResources().getDrawable(R.drawable.border).getConstantState())) {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));

                    Intent intent = new Intent("list_data");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "0");
                    intent.putExtra("is_add", "1");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                } else {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));

                    Intent intent = new Intent("list_data");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "0");
                    intent.putExtra("is_add", "0");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catelist.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView ct_rv_txt;
        public LinearLayout ct_rv_rl;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ct_rv_txt = itemView.findViewById(R.id.su_rvtxt);
            ct_rv_rl = itemView.findViewById(R.id.su_rv_linearlayout);
        }
    }
}
