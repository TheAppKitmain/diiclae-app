package com.online.davincii.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.DashboardScreen;
import com.online.davincii.activities.MyCart;
import com.online.davincii.models.addresses.AddressRequest;
import com.online.davincii.models.addresses.AddressResponse;
import com.online.davincii.models.addresses.ListAddress;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.DataHolder> {

    private List<ListAddress> listAddress;
    private int selectPosition;
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;

    public AddressAdapter(List<ListAddress> listAddress, int selectPosition) {
        this.listAddress = listAddress;
        this.selectPosition = selectPosition;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {

        String aAdd =listAddress.get(position).getName()+ "\n"
                + listAddress.get(position).getAddress()
                + " " + listAddress.get(position).getBuildingname()
                + ",\n" + listAddress.get(position).getCity() + ",\n"
                + listAddress.get(position).getZip()
                +" "+ listAddress.get(position).getState() ;
        holder.addCheck.setText(aAdd);

        if(position == selectPosition){
            holder.main.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.edit_text_border));
        }else{
            holder.main.setBackground(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("send_price");
                intent.putExtra("type","");
                intent.putExtra("position", position);
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setIcon(R.drawable.diiclae_color_logo)
                        .setTitle("Davincii")
                        .setCancelable(false)
                        .setMessage("Are you sure to delete address")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteApi(holder.itemView.getContext(), listAddress.get(position).getId(), position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("send_price");
                intent.putExtra("edit","");
                intent.putExtra("position", position);
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
            }
        });

    }

    private void deleteApi(Context context, Integer id, int position) {
        progress = new GlobalProgressDialog(context);
        apiInterface = ApiClient.getClient();

        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        AddressRequest request = new AddressRequest(id);
        apiInterface.deleteAddress("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if(selectPosition == position){
                            selectPosition =0;
                        }
                        Intent intent = new Intent("send_price");
                        intent.putExtra("delete","");
                        intent.putExtra("position", position);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });


    }

    @Override
    public int getItemCount() {
        return listAddress.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private TextView addCheck;
        private ImageView edit, delete;
        private LinearLayout main;

        public DataHolder(@NonNull View itemView) {
            super(itemView);

            addCheck = itemView.findViewById(R.id.ra_address);
            edit = itemView.findViewById(R.id.ra_edit);
            delete = itemView.findViewById(R.id.ra_delete);
            main = itemView.findViewById(R.id.ra_main);

        }
    }
}
