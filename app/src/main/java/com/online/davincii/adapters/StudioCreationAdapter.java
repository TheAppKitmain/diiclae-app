package com.online.davincii.adapters;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.AddArtPiece;
import com.online.davincii.models.StudioprofileCreator;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;
public class StudioCreationAdapter extends RecyclerView.Adapter<StudioCreationAdapter.CreationHolder> {
    private List<StudioprofileCreator> mList;
    private Context context;
    public StudioCreationAdapter(List<StudioprofileCreator> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }
    @NonNull
    @Override
    public CreationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upcreation_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreationHolder holder, int position) {
        if (mList.get(position).getCanvastype() == 0) {
            holder.saqLay.setVisibility(View.VISIBLE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.saqImg);
        } else if (mList.get(position).getCanvastype() == 1) {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.VISIBLE);
            holder.portLay.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.landImg);
        } else {
            holder.saqLay.setVisibility(View.GONE);
            holder.landLay.setVisibility(View.GONE);
            holder.portLay.setVisibility(View.VISIBLE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.portImg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AddArtPiece.class)
//                        .putExtra("buyId", mList.get(position).getId())
//                        .putExtra("canvasId", mList.get(position).getCanvastype())
//                        .putExtra("imgId", Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()));

                final Dialog mDialog = new Dialog(v.getContext());
                mDialog.setContentView(R.layout.row_deletecreation);
                mDialog.setCancelable(false);
                ImageView rl_saqImg = mDialog.findViewById(R.id.rl_saqImg);
                ImageView rl_landImg = mDialog.findViewById(R.id.rl_landImg);
                ImageView rl_portImg = mDialog.findViewById(R.id.rl_portImg);

                LinearLayout saqLay = mDialog.findViewById(R.id.rl_saqLay);
                LinearLayout landLay = mDialog.findViewById(R.id.rl_landLay);
                LinearLayout portLay = mDialog.findViewById(R.id.rl_portLay);

                ImageView close = mDialog.findViewById(R.id.up_rv_close);
                ImageView delete = mDialog.findViewById(R.id.up_rv_delete);
                delete.setVisibility(View.GONE);

                //       Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.app_icon_square).into(rl_landImg);

                if (mList.get(position).getCanvastype() == 0) {
                    saqLay.setVisibility(View.VISIBLE);
                    landLay.setVisibility(View.GONE);
                    portLay.setVisibility(View.GONE);
                    Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_saqImg);
                } else if (mList.get(position).getCanvastype() == 1) {
                    saqLay.setVisibility(View.GONE);
                    landLay.setVisibility(View.VISIBLE);
                    portLay.setVisibility(View.GONE);
                    Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_landImg);
                } else {
                    saqLay.setVisibility(View.GONE);
                    landLay.setVisibility(View.GONE);
                    portLay.setVisibility(View.VISIBLE);
                    Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_portImg);
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class CreationHolder extends RecyclerView.ViewHolder {
        private LinearLayout portLay, saqLay, landLay;
        private ImageView portImg, saqImg, landImg;
        public CreationHolder(@NonNull View itemView) {
            super(itemView);
            portLay = itemView.findViewById(R.id.rl_portLay);
            saqLay = itemView.findViewById(R.id.rl_saqLay);
            landLay = itemView.findViewById(R.id.rl_landLay);
            portImg = itemView.findViewById(R.id.rl_portImg);
            saqImg = itemView.findViewById(R.id.rl_saqImg);
            landImg = itemView.findViewById(R.id.rl_landImg);
        }
    }
}
