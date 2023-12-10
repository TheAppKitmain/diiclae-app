package com.online.davincii.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;

import de.hdodenhof.circleimageview.CircleImageView;
public class Seles_ReportAdapter extends RecyclerView.Adapter<Seles_ReportAdapter.SalesHolder> {
    public Seles_ReportAdapter() {
    }
    @NonNull
    @Override
    public SalesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sales_report , parent ,false));
    }
    @Override
    public void onBindViewHolder(@NonNull SalesHolder holder, int position) {
    }
    @Override
    public int getItemCount() {
        return 10;
    }
    public class SalesHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImg;
        private TextView userName, description, hours;
        public SalesHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
