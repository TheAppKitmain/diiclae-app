package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.online.davincii.R;
import com.online.davincii.models.ModelClass;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DiscoveryCategoryAdapter extends RecyclerView.Adapter<DiscoveryCategoryAdapter.ViewHolder> {
    private ModelClass[] model;

    public DiscoveryCategoryAdapter(ModelClass[] model) {
        this.model = model;
    }


    @NonNull
    @Override
    public DiscoveryCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_discovery_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoveryCategoryAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(model[position].getImgId());
        holder.text.setText(model[position].getDescription());

       // Glide.with(holder.itemView.getContext()).load(R.drawable.transparent_bg).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3))).into(holder.blurImage);
    }

    @Override
    public int getItemCount() {
        return model.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView, blurImage;
        private TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rdc_image);
            blurImage=itemView.findViewById(R.id.rdc_blurImage);
            text=itemView.findViewById(R.id.rdc_name);
        }
    }
}
