package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.online.davincii.R;
import com.online.davincii.models.MyCartData;
import com.online.davincii.models.MyCartDeleteRequest;
import com.online.davincii.models.MyCartDeleteResponse;
import com.online.davincii.models.MyCartQuantityRequest;
import com.online.davincii.models.MyCartQuantityResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartHolder> {
    private List<MyCartData> mList;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;

    public MyCartAdapter(List<MyCartData> mList) {
        this.mList = mList;
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
    public MyCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartHolder holder, int position) {

        holder.artistName.setText(mList.get(position).getTitle());
        holder.sizeText.setText(mList.get(position).getCanvasSize());
        holder.priceText.setText(BaseUtil.getCurrency(holder.itemView.getContext()) + String.format("%.2f", Double.valueOf(mList.get(position).getTotalprice())));
        holder.numberText.setText(String.valueOf(mList.get(position).getQuantity()));
        int type = mList.get(position).getCanvasId();

        if (type == 0) {
            holder.landscape.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.GONE);
            holder.square.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + mList.get(position).getImage()).into(holder.squareImg);
        } else if (type == 1) {
            holder.portrait.setVisibility(View.GONE);
            holder.square.setVisibility(View.GONE);
            holder.landscape.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + mList.get(position).getImage()).into(holder.landscapeImg);
        } else {
            holder.square.setVisibility(View.GONE);
            holder.landscape.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + mList.get(position).getImage()).into(holder.portraitImg);
        }

        holder.iconPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.iconMinus.setEnabled(true);
                addItem(mList.get(position).getQuantity() + 1, holder.itemView.getContext(), mList.get(position).getCartid(), position);
            }
        });

        holder.iconMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.get(position).getQuantity() == 1) {
                    deleteItem(holder.itemView.getContext(), mList.get(position).getCartid(), position);
                } else {
                    addItem(mList.get(position).getQuantity() - 1, holder.itemView.getContext(), mList.get(position).getCartid(), position);
                }
            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder.itemView.getContext(), mList.get(position).getCartid(), position);
            }
        });
    }

    private void deleteItem(Context context, Integer cartId, int pos) {
        progress = new GlobalProgressDialog(context);
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        MyCartDeleteRequest request = new MyCartDeleteRequest(cartId);
        ApiClient.getClient().myCartDelete("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<MyCartDeleteResponse>() {
            @Override
            public void onResponse(Call<MyCartDeleteResponse> call, Response<MyCartDeleteResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        Intent intent = new Intent("send_price");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        BaseUtil.showToast(context, response.body().getMessage());
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<MyCartDeleteResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
                progress.hideProgressBar();
            }
        });
    }

    private void addItem(int qty, Context context, Integer cartid, int position) {
        progress = new GlobalProgressDialog(context);
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        MyCartQuantityRequest request = new MyCartQuantityRequest(qty, cartid);
        ApiClient.getClient().myCartQuantity("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<MyCartQuantityResponse>() {
            @Override
            public void onResponse(Call<MyCartQuantityResponse> call, Response<MyCartQuantityResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        Intent intent = new Intent("send_price");
                        //  intent.putExtra("totalPrice",String.valueOf(mList.get(position).getTotalprice()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        //  mList.get(position).setQuantity(qty);
                        //  notifyDataSetChanged();
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.hideProgressBar();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<MyCartQuantityResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
                progress.hideProgressBar();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyCartHolder extends RecyclerView.ViewHolder {
        private LinearLayout portrait, landscape, square;
        private ImageView portraitImg, squareImg, landscapeImg, iconMinus, iconPlus, deletebtn;
        private TextView artistName, sizeText, priceText, numberText;

        public MyCartHolder(@NonNull View itemView) {
            super(itemView);
            portrait = itemView.findViewById(R.id.rh_portraitLay);
            landscape = itemView.findViewById(R.id.rh_landscapLay);
            square = itemView.findViewById(R.id.rh_saquareLay);
            portraitImg = itemView.findViewById(R.id.rmc_portraitImage);
            squareImg = itemView.findViewById(R.id.rmc_squareImage);
            landscapeImg = itemView.findViewById(R.id.rmc_landscapeImage);
            iconMinus = itemView.findViewById(R.id.mc_minus);
            iconPlus = itemView.findViewById(R.id.mc_plus);
            artistName = itemView.findViewById(R.id.mc_artistname);
            sizeText = itemView.findViewById(R.id.mc_size);
            priceText = itemView.findViewById(R.id.mc_price);
            numberText = itemView.findViewById(R.id.mc_number);
            deletebtn = itemView.findViewById(R.id.mc_delete);
        }
    }
}
