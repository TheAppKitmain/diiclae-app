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
import com.online.davincii.models.ProfileCategories;

import java.util.List;

public class ProfileCategoryAdapter extends RecyclerView.Adapter<ProfileCategoryAdapter.ViewHolder> {

    private List<ProfileCategories> cList;
    public ProfileCategoryAdapter(List<ProfileCategories> cList) {
        this.cList = cList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_categories_style, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

          if (cList.get(position).getCheck()){
              holder.ct_rv_txt.setText(cList.get(position).getName());
              holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
              holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));

              Intent intent = new Intent("cat_list");
              intent.putExtra("position", position);
              intent.putExtra("type", "0");
              intent.putExtra("is_add", "1");
              LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

          }else{
              holder.ct_rv_txt.setText(cList.get(position).getName());
              holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
              holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
          }

        holder.ct_rv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.ct_rv_txt.getBackground().getConstantState().equals(holder.itemView.getContext().getResources().getDrawable(R.drawable.border).getConstantState())) {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));

                    Intent intent = new Intent("cat_list");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "0");
                    intent.putExtra("is_add", "1");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                } else {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));

                    Intent intent = new Intent("cat_list");
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
        return cList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ct_rv_txt;
        private  LinearLayout ct_rv_rl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ct_rv_txt = itemView.findViewById(R.id.su_rvtxt);
            ct_rv_rl = itemView.findViewById(R.id.su_rv_linearlayout);
        }
    }
}
