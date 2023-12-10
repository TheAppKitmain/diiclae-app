package com.online.davincii.adapters;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.CanvasSizeData;

import org.w3c.dom.Text;

import java.nio.file.LinkPermission;
import java.util.List;

import retrofit2.http.POST;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private List<CanvasSizeData> sList;
    private String edit;

    public PriceAdapter(List<CanvasSizeData> sList,String edit) {
        this.sList = sList;
        this.edit=edit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_price, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sizes.setText(sList.get(position).getSize());
        holder.addPrice.setText(String.valueOf(("$"+sList.get(position).getPrice())));

        holder.addPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()!=0){
                    Intent intent=new Intent("add_price");
                    intent.putExtra("position", position);
//                    intent.putExtra("sizeId", String.valueOf(sList.get(position).getId()));
//                    intent.putExtra("size", sList.get(position).getSize());
                    if (holder.addPrice.getText().toString().contains("$")){
                        intent.putExtra("price", holder.addPrice.getText().toString().substring(1));
                    }else{
                        intent.putExtra("price", holder.addPrice.getText().toString());
                    }

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
        private TextView sizes;
        private EditText addPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
              sizes=itemView.findViewById(R.id.rp_size);
              addPrice=itemView.findViewById(R.id.rp_addPrice);
        }
    }
}
