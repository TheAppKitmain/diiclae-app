package com.online.davincii.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.models.comments.Reply;
import com.online.davincii.models.comments.ReplyData;
import com.online.davincii.models.comments.ReplyLikeRequest;
import com.online.davincii.models.comments.ReplyLikeResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder> {
    private List<Reply> replyListsData;
    private Context context;
    private GlobalProgressDialog progress;

    public ReplyAdapter(List<Reply> replyListsData, Context context) {
        this.replyListsData = replyListsData;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ReplyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ReplyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReplyHolder holder, int position) {


        if (replyListsData.get(position).getLikeStatus())
            holder.replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);
        else holder.replyLikeImg.setImageResource(R.drawable.ic_dislike);

        holder.replyUserName.setText(replyListsData.get(position).getName());
        holder.replyMessage.setText(replyListsData.get(position).getMessage());
        holder.replyLikeTxt.setText(replyListsData.get(position).getLikeCount() + " Likes");
        holder.replyHours.setText(replyListsData.get(position).getTimeago());

        if (!TextUtils.isEmpty(replyListsData.get(position).getImage())) {
            Picasso.get().load(Constant.PROFILE_IMG_BASE + replyListsData.get(position).getImage()).resize(500, 500).error(R.drawable.ic_user).into(holder.replyUserImg);
        }

        holder.replyLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (replyListsData.get(position).getLikeStatus()) {
                    holder.replyLikeImg.setImageResource(R.drawable.ic_dislike);
                } else {
                    holder.replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);

                }
                getReplyLikeImgApi(holder.replyLikeImg, replyListsData.get(position).getId());

            }
        });

    }

    private void getReplyLikeImgApi(ImageView replyLikeImg, Integer id) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        ReplyLikeRequest request = new ReplyLikeRequest(id);
        ApiClient.getClient().replyLike("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<ReplyLikeResponse>() {
            @Override
            public void onResponse(Call<ReplyLikeResponse> call, Response<ReplyLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getLiked())
                            replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);
                        else
                            replyLikeImg.setImageResource(R.drawable.ic_dislike);
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //notifyDataSetChanged();
                    } else {
                        if (replyLikeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike)))
                            replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);
                        else replyLikeImg.setImageResource(R.drawable.ic_dislike);
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    if (replyLikeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike)))
                        replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);
                    else replyLikeImg.setImageResource(R.drawable.ic_dislike);
                    BaseUtil.showToast(context, "Server Error");
                }
            }

            @Override
            public void onFailure(Call<ReplyLikeResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
                if (replyLikeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike)))
                    replyLikeImg.setImageResource(R.drawable.ic_favorite_heart);
                else replyLikeImg.setImageResource(R.drawable.ic_dislike);
            }
        });

    }

    @Override
    public int getItemCount() {
        return replyListsData.size();
    }

    public class ReplyHolder extends RecyclerView.ViewHolder {
        private CircleImageView replyUserImg;
        private ImageView replyLikeImg;
        private TextView replyUserName, replyMessage, replyLikeTxt, replyHours;

        public ReplyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            replyUserImg = itemView.findViewById(R.id.ct_replyuserImg);
            replyLikeImg = itemView.findViewById(R.id.ct_replylikeImg);
            replyUserName = itemView.findViewById(R.id.ct_replyuserName);
            replyMessage = itemView.findViewById(R.id.ct_replymessage);
            replyLikeTxt = itemView.findViewById(R.id.ct_replylikeTxt);
            replyHours = itemView.findViewById(R.id.ct_replyhours);
        }
    }
}
