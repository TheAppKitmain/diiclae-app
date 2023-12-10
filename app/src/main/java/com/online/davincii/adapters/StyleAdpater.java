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
import com.online.davincii.models.ProfileStyles;
import com.online.davincii.models.UserProfileStyle;

import java.util.List;

public class StyleAdpater extends RecyclerView.Adapter<StyleAdpater.ViewHolder> {

    List<ProfileStyles> stylelist;
    private TextView previousSelected;
    private Integer selectedId;

    public StyleAdpater(List<ProfileStyles> stylelist, Integer selectedId) {
        this.stylelist = stylelist;
        this.selectedId = selectedId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.styleName.setText(stylelist.get(position).getName());

        if (position == selectedId) {
            holder.styleName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.upload_border));
            holder.styleName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        } else {
            holder.styleName.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.back_uploadrow));
            holder.styleName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        holder.styleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("get_id");
                intent.putExtra("styleId", stylelist.get(position).getId());
                intent.putExtra("type", "styleId");
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                selectedId = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stylelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView styleName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            styleName = itemView.findViewById(R.id.upd_rv_adptxt);
        }
    }


}
