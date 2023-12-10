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
import com.online.davincii.models.ProfileStyles;

import java.util.List;

public class ProfileStyleAdapter extends RecyclerView.Adapter<ProfileStyleAdapter.ViewHolder> {

    private List<ProfileStyles> sList;

    public ProfileStyleAdapter(List<ProfileStyles> sList) {
        this.sList = sList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileStyleAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_categories_style, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (sList.get(position).getCheck()){
            holder.ct_rv_txt.setText(sList.get(position).getName());
            holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
            holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            Intent intent = new Intent("style_list");
            intent.putExtra("position", position);
            intent.putExtra("type", "0");
            intent.putExtra("is_style", "1");
            LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);


        }else{
            holder.ct_rv_txt.setText(sList.get(position).getName());
            holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
            holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));

        }

        holder.ct_rv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.ct_rv_txt.getBackground().getConstantState().equals(holder.itemView.getContext().getResources().getDrawable(R.drawable.border).getConstantState())) {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border_clicked));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));

                    Intent intent = new Intent("style_list");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "0");
                    intent.putExtra("is_style", "1");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                } else {
                    holder.ct_rv_txt.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.border));
                    holder.ct_rv_txt.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
                    Intent intent = new Intent("style_list");
                    intent.putExtra("position", position);
                    intent.putExtra("type", "0");
                    intent.putExtra("is_style", "0");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                }
            }
        });
}

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ct_rv_txt;
        private LinearLayout ct_rv_rl;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ct_rv_txt = itemView.findViewById(R.id.su_rvtxt);
            ct_rv_rl = itemView.findViewById(R.id.su_rv_linearlayout);
        }
    }
}
