package com.online.davincii.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.ArtistDetail;
import com.online.davincii.models.discoverysearch.FeaturedArtistResponse;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DCS_FeaturedArtistAdapter extends RecyclerView.Adapter<DCS_FeaturedArtistAdapter.ArtistHolder> {

    private List<FeaturedArtistResponse> featdArtist;

    public DCS_FeaturedArtistAdapter(List<FeaturedArtistResponse> featdArtist) {
        this.featdArtist = featdArtist;
    }

    @NonNull
    @NotNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ArtistHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_featuredartist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ArtistHolder holder, int position) {
        Picasso.get().load(Constant.PROFILE_IMG_BASE + featdArtist.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.fa_CirlceImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ArtistDetail.class).putExtra("id", featdArtist.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return featdArtist.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {
        private CircleImageView fa_CirlceImg;
        private TextView names;

        public ArtistHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            names=itemView.findViewById(R.id.dcs_names);
            fa_CirlceImg = itemView.findViewById(R.id.dcs_cirleImg);
        }
    }
}