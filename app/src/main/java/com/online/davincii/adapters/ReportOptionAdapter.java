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
import com.online.davincii.models.sales_report.ReportListData;

import java.util.List;

public class ReportOptionAdapter extends RecyclerView.Adapter<ReportOptionAdapter.ViewHolder> {

    private List<ReportListData> oList;

    public ReportOptionAdapter(List<ReportListData> oList) {
        this.oList = oList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_report_options, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.options.setText(oList.get(position).getOption());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("report");
                intent.putExtra("do_report", "");
                intent.putExtra("optionId",oList.get(position).getId());
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return oList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            options = itemView.findViewById(R.id.ro_textView);
        }
    }

}
