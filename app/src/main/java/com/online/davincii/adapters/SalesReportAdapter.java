package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.online.davincii.R;
import com.online.davincii.activities.CanvasDetailList;
import com.online.davincii.models.sales_report.SalesReportData;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.SalesHolder> {
    private List<SalesReportData> sList;
    public SalesReportAdapter(List<SalesReportData> sList) {
        this.sList = sList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public SalesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sales_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesHolder holder, int position) {
        holder.userName.setText(sList.get(position).getByName());
        holder.desc.setText(sList.get(position).getDescription());

        Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getByImage()).resize(500, 500).error(R.drawable.ic_user).into(holder.userProfile);

        if (sList.get(position).getCanvasTypeId().equals("0")) {
            holder.sqrLayout.setVisibility(View.VISIBLE);
            holder.landScapeLayout.setVisibility(View.GONE);
            holder.portraitLayout.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).into(holder.sqrImage);
        } else if (sList.get(position).getCanvasTypeId().equals("1")) {
            holder.landScapeLayout.setVisibility(View.VISIBLE);
            holder.sqrLayout.setVisibility(View.GONE);
            holder.portraitLayout.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).into(holder.landScapeImage);
        } else {
            holder.portraitLayout.setVisibility(View.VISIBLE);
            holder.landScapeLayout.setVisibility(View.GONE);
            holder.sqrLayout.setVisibility(View.GONE);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + sList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).into(holder.portraitImage);
        }

        if (sList.get(position).getType().equals("support")) {
            holder.portraitLayout.setVisibility(View.GONE);
            holder.landScapeLayout.setVisibility(View.GONE);
            holder.sqrLayout.setVisibility(View.GONE);

            if (sList.get(position).getSupport()) {
                holder.support.setVisibility(View.VISIBLE);
                holder.supporting.setVisibility(View.GONE);
            } else {
                holder.supporting.setVisibility(View.VISIBLE);
                holder.support.setVisibility(View.GONE);
            }
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(!sList.get(position).getType().equals("support")) {
                    Intent intent = new Intent(holder.itemView.getContext(), CanvasDetailList.class);
                    intent.putExtra("canvasId", sList.get(position).getCanvasId());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

        holder.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("sales_report");
                intent.putExtra("position", position);
                intent.putExtra("user_id", sList.get(position).getUserid());
                LocalBroadcastManager.getInstance(holder.support.getContext()).sendBroadcast(intent);
            }
        });

        holder.supporting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("sales_report");
                intent.putExtra("position", position);
                intent.putExtra("user_id", sList.get(position).getUserid());
                LocalBroadcastManager.getInstance(holder.support.getContext()).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public class SalesHolder extends RecyclerView.ViewHolder {
        private ImageView userProfile, portraitImage, sqrImage, landScapeImage;
        private TextView userName, desc, support, supporting;
        private LinearLayout sqrLayout, portraitLayout, landScapeLayout,mainLayout;

        public SalesHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.sr_userImage);
            userName = itemView.findViewById(R.id.sr_username);
            desc = itemView.findViewById(R.id.sr_desc);
            support = itemView.findViewById(R.id.sr_support);
            supporting = itemView.findViewById(R.id.sr_supporting);
            sqrLayout = itemView.findViewById(R.id.sr_saquareLay);
            portraitLayout = itemView.findViewById(R.id.sr_portraitLay);
            landScapeLayout = itemView.findViewById(R.id.sr_landscapLay);
            portraitImage = itemView.findViewById(R.id.sr_portrait_image);
            sqrImage = itemView.findViewById(R.id.sr_square_image);
            landScapeImage = itemView.findViewById(R.id.sr_landscape_image);
            mainLayout=itemView.findViewById(R.id.sr_mainLayout);

        }
    }
}
