package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.online.davincii.R;
import com.online.davincii.models.ModelClass;

public class CanvasAdapter extends RecyclerView.Adapter<CanvasAdapter.AllViewHolder> {
    private ModelClass[] model;

    public CanvasAdapter(ModelClass[] model) {
        this.model = model;
    }

    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.row_canvas, parent, false);
        AllViewHolder viewHolder = new AllViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {
        holder.imageView.setImageResource(model[position].getImgId());
    }

    @Override
    public int getItemCount() {
        return model.length;
    }

    public class AllViewHolder extends  RecyclerView.ViewHolder{
        private ImageView imageView;
        public AllViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rc_image);
        }
    }
}
