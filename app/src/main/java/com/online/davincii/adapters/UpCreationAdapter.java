package com.online.davincii.adapters;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.AddArtPiece;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.models.DeleteCreationRequest;
import com.online.davincii.models.DeleteCreationResponse;
import com.online.davincii.models.ProfileCreator;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpCreationAdapter extends RecyclerView.Adapter<UpCreationAdapter.CreationHolder> {
    private List<ProfileCreator> mList;
    private List<DeleteCreationResponse> deleteCreation;
    private GlobalProgressDialog progress;
    Integer ids;
    private boolean isOther;

    public UpCreationAdapter(List<ProfileCreator> mList, boolean isOther) {
        this.mList = mList;
        this.isOther = isOther;
    }

    @NonNull
    @Override
    public CreationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upcreation_adapter, parent, false));
    }

    @SuppressLint("RecyclerView")
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

                if (isOther) {

//                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), AddArtPiece.class)
//                            .putExtra("buyId", mList.get(position).getId())
//                            .putExtra("canvasId", mList.get(position).getCanvastype())
//                            .putExtra("imgId", Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()));
                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DiscoveryDetails.class).putExtra("id", mList.get(position).getId()));

                } else {

                    holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DiscoveryDetails.class).putExtra("id", mList.get(position).getId()));

//                    final Dialog mDialog = new Dialog(v.getContext());
//                    mDialog.setContentView(R.layout.row_deletecreation);
//                    mDialog.setCancelable(false);
//                    ImageView rl_saqImg = mDialog.findViewById(R.id.rl_saqImg);
//                    ImageView rl_landImg = mDialog.findViewById(R.id.rl_landImg);
//                    ImageView rl_portImg = mDialog.findViewById(R.id.rl_portImg);
//
//                    LinearLayout saqLay = mDialog.findViewById(R.id.rl_saqLay);
//                    LinearLayout landLay = mDialog.findViewById(R.id.rl_landLay);
//                    LinearLayout portLay = mDialog.findViewById(R.id.rl_portLay);
//
//                    ImageView close = mDialog.findViewById(R.id.up_rv_close);
//                    ImageView delete = mDialog.findViewById(R.id.up_rv_delete);
//
//
//                    if (isOther) {
//                        delete.setVisibility(View.GONE);
//                    } else {
//                        delete.setVisibility(View.VISIBLE);
//                    }
//
//                    //       Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.app_icon_square).into(rl_landImg);
//
//                    if (mList.get(position).getCanvastype() == 0) {
//                        saqLay.setVisibility(View.VISIBLE);
//                        landLay.setVisibility(View.GONE);
//                        portLay.setVisibility(View.GONE);
//                        Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_saqImg);
//                    } else if (mList.get(position).getCanvastype() == 1) {
//                        saqLay.setVisibility(View.GONE);
//                        landLay.setVisibility(View.VISIBLE);
//                        portLay.setVisibility(View.GONE);
//                        Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_landImg);
//                    } else {
//                        saqLay.setVisibility(View.GONE);
//                        landLay.setVisibility(View.GONE);
//                        portLay.setVisibility(View.VISIBLE);
//                        Picasso.get().load(Constant.PROFILE_IMG_BASE + mList.get(position).getImageurl()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(rl_portImg);
//                    }
//
//                    close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mDialog.dismiss();
//                        }
//                    });
//
//                    delete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
//                            builder1.setMessage("Oh no! You're deleting... Are you sure");
//                            builder1.setCancelable(false);
//
//                            builder1.setPositiveButton(
//                                    "Yes, Delete",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            getDeleteCreation(mList.get(position).getId(), v.getContext(), position);
//                                            mDialog.dismiss();
//                                        }
//                                    });
//
//                            builder1.setNegativeButton(
//                                    "Naah Just Kidding",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            mDialog.dismiss();
//                                        }
//                                    });
//
//                            AlertDialog alert11 = builder1.create();
//                            alert11.show();
//
//                        }
//                    });
//                    mDialog.show();
                }
            }
        });
    }

    private void getDeleteCreation(Integer ids, Context context, int pos) {
        progress = new GlobalProgressDialog(context);
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            return;
        }
        progress.showProgressBar();
        DeleteCreationRequest request = new DeleteCreationRequest(ids);
        ApiClient.getClient().deleteCreation("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<DeleteCreationResponse>() {
            @Override
            public void onResponse(Call<DeleteCreationResponse> call, Response<DeleteCreationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        mList.remove(pos);
                        notifyDataSetChanged();
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server Error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<DeleteCreationResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CreationHolder extends RecyclerView.ViewHolder {

        private LinearLayout portLay, saqLay, landLay;
        private ImageView portImg, saqImg, landImg, close, delete;

        public CreationHolder(@NonNull View itemView) {
            super(itemView);

            portLay = itemView.findViewById(R.id.rl_portLay);
            saqLay = itemView.findViewById(R.id.rl_saqLay);
            landLay = itemView.findViewById(R.id.rl_landLay);
            portImg = itemView.findViewById(R.id.rl_portImg);
            saqImg = itemView.findViewById(R.id.rl_saqImg);
            landImg = itemView.findViewById(R.id.rl_landImg);
            close = itemView.findViewById(R.id.up_rv_close);
            delete = itemView.findViewById(R.id.up_rv_delete);


        }
    }
}
