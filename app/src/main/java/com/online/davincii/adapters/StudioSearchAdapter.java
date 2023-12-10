package com.online.davincii.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.ArtistDetail;
import com.online.davincii.models.StudioSearch;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StudioSearchAdapter extends RecyclerView.Adapter<StudioSearchAdapter.SearchHolder> {
    private List<StudioSearch> studioSearches;

    public StudioSearchAdapter(List<StudioSearch> studioSearches) {
        this.studioSearches = studioSearches;
    }

    @NonNull
    @NotNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topstudio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchHolder holder, int position) {
        Picasso.get().load(Constant.STUDIO_IMG_BASE + studioSearches.get(position).getImageurl()).into(holder.landImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ArtistDetail.class).putExtra("id", studioSearches.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return studioSearches.size();
    }

    public static class SearchHolder extends RecyclerView.ViewHolder {
        private ImageView landImg;

        public SearchHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            landImg = itemView.findViewById(R.id.ts_landImg);
        }
    }
}