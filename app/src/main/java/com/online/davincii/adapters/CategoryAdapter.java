package com.online.davincii.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.ProfileCategories;
import com.online.davincii.models.UserProfileCategory;

import java.util.List;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<ProfileCategories> catelist;
    private TextView previousSelected;
    private Integer selectedId;

    public CategoryAdapter(List<ProfileCategories> cateList,Integer selectedId) {
        this.catelist = cateList;
        this.selectedId=selectedId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.catName.setText(catelist.get(position).getName());

        if (position == selectedId) {
            holder.catName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.upload_border));
            holder.catName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        } else {
            holder.catName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.back_uploadrow));
            holder.catName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
                holder.catName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("get_id");
                        intent.putExtra("catId",catelist.get(position).getId());
                        intent.putExtra("type", "categoryId");
                        intent.putExtra("position",position);
                        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                        selectedId = position;
                        notifyDataSetChanged();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return catelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView catName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.upd_rv_adptxt);
        }
    }
}
