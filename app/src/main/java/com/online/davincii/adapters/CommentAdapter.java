package com.online.davincii.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.activities.Comments;
import com.online.davincii.models.comments.CommentLikeRequest;
import com.online.davincii.models.comments.CommentLikeResponse;
import com.online.davincii.models.comments.CommentList;
import com.online.davincii.models.comments.CommentMakeResponse;
import com.online.davincii.models.comments.ReplyCommentRequest;
import com.online.davincii.models.comments.ReplyCommentResponse;
import com.online.davincii.models.comments.ReplyData;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<CommentList> cList;
    private List<ReplyData> replyLists;
    private Context context;
    private GlobalProgressDialog progress;
    private ReplyAdapter replyAdapter;

    public CommentAdapter(List<CommentList> cList, Context context) {
        this.cList = cList;
        this.context = context;
        this.replyLists = replyLists;
    }

    @NonNull
    @NotNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentHolder holder, int position) {

        if (cList.get(position).getLikeStatus()) {
            holder.likeImg.setImageResource(R.drawable.ic_favorite_heart);
        } else {
            holder.likeImg.setImageResource(R.drawable.ic_dislike);
        }

        holder.userName.setText(cList.get(position).getName());
        holder.message.setText(cList.get(position).getMessage());
        holder.likeTxt.setText(cList.get(position).getLikeCount() + " Likes");
        holder.hours.setText(cList.get(position).getTimeago());

        if (!TextUtils.isEmpty(cList.get(position).getImage())) {
            Picasso.get().load(Constant.PROFILE_IMG_BASE + cList.get(position).getImage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.userImg);
        }

        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLikeApi(holder.likeImg, cList.get(position).getId(), cList.get(position).getLikeCount(), holder.likeTxt);
                if (cList.get(position).getLikeStatus()) {
                    holder.likeImg.setImageResource(R.drawable.ic_dislike);
                } else {
                    holder.likeImg.setImageResource(R.drawable.ic_favorite_heart);
                }
            }
        });

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("Data_List_two");
                intent.putExtra("type", "reply");
                intent.putExtra("comment_id", String.valueOf(cList.get(position).getId()));
                intent.putExtra("user_name", cList.get(position).getName());
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
            }
        });

        holder.rvReplyDown.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        replyAdapter = new ReplyAdapter(cList.get(position).getReply(), context);
        holder.rvReplyDown.setAdapter(replyAdapter);
        replyAdapter.notifyDataSetChanged();

    }

    private void getLikeApi(ImageView likeImg, Integer id, String likeCount, TextView likeTxt) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        CommentLikeRequest request = new CommentLikeRequest(id);
        ApiClient.getClient().commentLike("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<CommentLikeResponse>() {
            @Override
            public void onResponse(Call<CommentLikeResponse> call, Response<CommentLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getLiked()) {
                            likeImg.setImageResource(R.drawable.ic_favorite_heart);
                            likeTxt.setText((Integer.valueOf(likeCount) + 1) + " Likes");
                        } else {
                            likeTxt.setText((Integer.valueOf(likeCount) - 1) + " Likes");
                            likeImg.setImageResource(R.drawable.ic_dislike);
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       // notifyDataSetChanged();
                    } else {
                        if (likeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike))) {
                            likeImg.setImageResource(R.drawable.ic_favorite_heart);
                        } else {
                            likeImg.setImageResource(R.drawable.ic_dislike);
                        }
                        BaseUtil.showToast(context, response.body().getMessage());
                    }

                } else {
                    if (likeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike)))
                        likeImg.setImageResource(R.drawable.ic_favorite_heart);
                    else likeImg.setImageResource(R.drawable.ic_dislike);
                    BaseUtil.showToast(context, "Server Error");
                }
            }

            @Override
            public void onFailure(Call<CommentLikeResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
                if (likeImg.getDrawableState().equals(context.getDrawable(R.drawable.ic_dislike)))
                    likeImg.setImageResource(R.drawable.ic_favorite_heart);
                else likeImg.setImageResource(R.drawable.ic_dislike);
            }
        });
    }

    @Override
    public int getItemCount(){
        return cList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private CircleImageView userImg;
        private ImageView likeImg;
        private TextView userName, message, likeTxt, reply, hours;
        private RecyclerView rvReplyDown;

        public CommentHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.ct_userImg);
            likeImg = itemView.findViewById(R.id.ct_likeImg);
            userName = itemView.findViewById(R.id.ct_userName);
            message = itemView.findViewById(R.id.ct_message);
            likeTxt = itemView.findViewById(R.id.ct_likeTxt);
            reply = itemView.findViewById(R.id.ct_reply);
            hours = itemView.findViewById(R.id.ct_hours);
            rvReplyDown = itemView.findViewById(R.id.ct_rv_replydown);

        }
    }
}
