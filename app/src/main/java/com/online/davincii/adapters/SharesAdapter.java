package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.ModelClass;

public class SharesAdapter extends RecyclerView.Adapter<SharesAdapter.ViewHolder> {

    private ModelClass[] model;

    public SharesAdapter(ModelClass[] model) {
        this.model = model;
    }

    @NonNull
    @Override
    public SharesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_shares, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(model[position].getImgId());
    }

    @Override
    public int getItemCount() {
        return model.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rs_image);
        }
    }
}
