package com.online.davincii.adapters;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.online.davincii.activities.Comments;
import com.online.davincii.R;
import com.online.davincii.activities.AddArtPiece;
import com.online.davincii.activities.ArtistDetail;
import com.online.davincii.activities.buyscreens.BuyActivity;
import com.online.davincii.models.FeedData;
import com.online.davincii.models.HomeLikeRequest;
import com.online.davincii.models.HomeLikeResponse;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {
    private List<FeedData> feedList;
    private List<HomeSupportResponse> supportRespon = new ArrayList<>();
    private Boolean isSelected = false;
    private Context context;
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;
    private String from;
    private String change="";

    private AnimatorSet frontAnim, backAnim;
    private boolean isFront =true;


    public FeedAdapter(List<FeedData> feedList, Context context, List<HomeSupportResponse> supportRespon, String from) {
        this.feedList = feedList;
        this.context = context;
        this.supportRespon = supportRespon;
        this.from = from;
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
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.username.setText(feedList.get(position).getUsername());
        holder.artistName.setText(feedList.get(position).getUsername());
        holder.roleName.setText("@"+feedList.get(position).getUsername().replace(" ",""));
        holder.artistType.setText(feedList.get(position).getUsername());
        holder.description.setText(feedList.get(position).getDescription());
        holder.daysAgo.setText(feedList.get(position).getTimeago());
        holder.artistTitle.setText(feedList.get(position).getTitle());
        holder.views.setText("" + feedList.get(position).getViews() + " views");
        holder.backDate.setText(formatdate(feedList.get(position).getCreatedon().substring(0,10)));

        if (feedList.get(position).getLike())
            holder.heartImg.setImageResource(R.drawable.ic_favorite_heart);
        else holder.heartImg.setImageResource(R.drawable.ic_dislike);

        if (feedList.get(position).getSupport())
            holder.support.setText("Supporting");
        else holder.support.setText("Support");

        // Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE+feedList.get(position).getCanvasimage()).into(holder.canvesImage);
        Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getProfileimage()).error(R.drawable.diiclae_colored_icon).into(holder.profileimage);
        if (Integer.parseInt(feedList.get(position).getCanvasId()) == 0) {
            holder.square.setVisibility(View.VISIBLE);
            holder.landscape.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.GONE);
            //Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE+feedList.get(position).getCanvasimage()).into(holder.canvesImage1);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.canvesImage1);
        } else if (Integer.parseInt(feedList.get(position).getCanvasId()) == 1) {
            holder.landscape.setVisibility(View.VISIBLE);
            holder.square.setVisibility(View.GONE);
            holder.portrait.setVisibility(View.GONE);
            //Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE+feedList.get(position).getCanvasimage()).into(holder.canvesImage2);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.canvesImage2);
        } else {
            holder.portrait.setVisibility(View.VISIBLE);
            holder.landscape.setVisibility(View.GONE);
            holder.square.setVisibility(View.GONE);
            //Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE+feedList.get(position).getCanvasimage()).into(holder.canvesImage);
            Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.canvesImage);
        }

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), BuyActivity.class);
                intent.putExtra("buyId", feedList.get(position).getId());
                intent.putExtra("canvasId", Integer.parseInt(feedList.get(position).getCanvasId()));
                intent.putExtra("imgId", Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage());
                holder.itemView.getContext().startActivity(intent);

//                Intent intent = new Intent(holder.itemView.getContext(), AddArtPiece.class);
//                intent.putExtra("buyId", feedList.get(position).getId());
//                intent.putExtra("canvasId", Integer.parseInt(feedList.get(position).getCanvasId()));
//                intent.putExtra("imgId", Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage());
//                holder.itemView.getContext().startActivity(intent);
//                feedList.get(position).setViews(feedList.get(position).getViews() +1);
//                notifyDataSetChanged();
            }
        });

        holder.artistType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ArtistDetail.class);
                intent.putExtra("id", feedList.get(position).getUserid());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ArtistDetail.class);
                intent.putExtra("id", feedList.get(position).getUserid());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ArtistDetail.class);
                intent.putExtra("id", feedList.get(position).getUserid());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.heartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLikeData(feedList.get(position).getId(),holder);
//                if (feedList.get(position).getLike()) {
//                    holder.heartImg.setImageResource(R.drawable.ic_dislike);
//                } else {
//                    holder.heartImg.setImageResource(R.drawable.ic_favorite_heart);
//                }


                Intent intent = new Intent(from);
                intent.putExtra("position", position);
                intent.putExtra("type", "like");
                intent.putExtra("like_id", feedList.get(position).getId());
                LocalBroadcastManager.getInstance(holder.heartImg.getContext()).sendBroadcast(intent);
            }
        });

//        holder.support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (feedList.get(position).getSupport()) {
//                    holder.support.setText("Supporting");
//                } else {
//                    holder.support.setText("Support");
//
//                    Intent intent = new Intent(from);
//                    intent.putExtra("position", position);
//                    intent.putExtra("user_id", feedList.get(position).getUserid());
//                    LocalBroadcastManager.getInstance(holder.support.getContext()).sendBroadcast(intent);
//                }
//            }
//        });


        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Comments.class);
                intent.putExtra("canvas_id", feedList.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(from);
                intent.putExtra("get_supporters","");
                intent.putExtra("canvasImage",Constant.PROFILE_IMG_BASE+feedList.get(position).getCanvasimage());
                intent.putExtra("canvasName",feedList.get(position).getTitle());
                intent.putExtra("canvasId",String.valueOf(feedList.get(position).getId()));
                intent.putExtra("artBy",feedList.get(position).getUsername());
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (!feedList.get(position).getReport()){
                    Intent intent = new Intent(from);
                    intent.putExtra("report", "");
                    intent.putExtra("canvasId", feedList.get(position).getId());
                    intent.putExtra("position", position);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                }else{
                    Toast.makeText(context, "This post already reported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportData(holder,String.valueOf(feedList.get(position).getUserid()));
            }
        });

        float scale=holder.itemView.getContext().getResources().getDisplayMetrics().density;
        Animator front_animation = AnimatorInflater.loadAnimator(holder.itemView.getContext(), R.animator.front_animator);
        Animator back_animation = AnimatorInflater.loadAnimator(holder.itemView.getContext(), R.animator.back_animator);

//      holder.cardBackground.cameraDistance = 8000 * scale;
//      holder.imgLayout.cameraDistance = 8000 * scale;

        holder.cardBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.cardBackground, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.imgLayout, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa1.setDuration(1000);
                        oa2.setDuration(1000);
                        oa2.start();
                        holder.apprLayout.setVisibility(View.VISIBLE);
                        if (change=="1"){
                            holder.cardBackground.setVisibility(View.GONE);
                            holder.imgLayout.setVisibility(View.VISIBLE);
                            holder.portrait.setVisibility(View.VISIBLE);
                        }else if (change=="2"){
                            holder.cardBackground.setVisibility(View.GONE);
                            holder.imgLayout.setVisibility(View.VISIBLE);
                            holder.landscape.setVisibility(View.VISIBLE);

                        }else if (change=="3"){
                            holder.cardBackground.setVisibility(View.GONE);
                            holder.imgLayout.setVisibility(View.VISIBLE);
                            holder.square.setVisibility(View.VISIBLE);

                        }
                    }
                });
                oa1.start();
            }
        });

        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apprLayout.setVisibility(View.GONE);
//                int pos;
//                pos= holder.getAdapterPosition();
                change="1";
                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3))).into(holder.backPortraitBackground);
                Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.backPortrait);
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.imgLayout, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.cardBackground, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa1.setDuration(1000);
                        oa2.setDuration(1000);
                        oa2.start();
                        holder.cardBackground.setVisibility(View.VISIBLE);
                        holder.imgLayout.setVisibility(View.GONE);
                        holder.backPortrait.setVisibility(View.VISIBLE);
                    }
                });
                oa1.start();
            }
        });

        holder.landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apprLayout.setVisibility(View.GONE);
                change="2";
//                int pos;
//                pos= holder.getAdapterPosition();
                Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.backLandscape);
                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3))).into(holder.backPortraitBackground);

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.imgLayout, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.cardBackground, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa2.start();
                        oa1.setDuration(1000);
                        oa2.setDuration(1000);
                        holder.cardBackground.setVisibility(View.VISIBLE);
                        holder.imgLayout.setVisibility(View.GONE);
                        holder.backLandscape.setVisibility(View.VISIBLE);
                    }
                });
                oa1.start();

            }
        });

        holder.square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apprLayout.setVisibility(View.GONE);
                change="3";
//                int pos;
//                pos= holder.getAdapterPosition();
                Picasso.get().load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).resize(500, 500).error(R.drawable.diiclae_colored_icon).into(holder.backSquare);
                Glide.with(holder.itemView.getContext()).load(Constant.PROFILE_IMG_BASE + feedList.get(position).getCanvasimage()).error(R.drawable.diiclae_colored_icon).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3))).into(holder.backPortraitBackground);

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.imgLayout, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.cardBackground, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa2.start();
                        oa1.setDuration(1000);
                        oa2.setDuration(1000);
                        holder.cardBackground.setVisibility(View.VISIBLE);
                        holder.imgLayout.setVisibility(View.GONE);
                        holder.backSquare.setVisibility(View.VISIBLE);
                    }
                });
                oa1.start();

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder {
        public TextView username,backDate,roleName,description, artistType, daysAgo, artistTitle, buyBtn, support, views, artistName;
        public ImageView canvesImage, canvesImage1, canvesImage2, profileimage, heartImg, chat, share, more, backImage;
        public LinearLayout portrait, landscape, square, apprLayout;
        public CardView cardBackground;
        public RelativeLayout imgLayout;
        public ImageView backLandscape, backPortrait, backSquare, backPortraitBackground;

        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            backDate=itemView.findViewById(R.id.backDate);
            artistName= itemView.findViewById(R.id.rh_artistName);
            buyBtn = itemView.findViewById(R.id.hm_buybtn);
            portrait = itemView.findViewById(R.id.rh_portraitLay);
            landscape = itemView.findViewById(R.id.rh_landscapLay);
            square = itemView.findViewById(R.id.rh_saquareLay);
            username = itemView.findViewById(R.id.hm_username);
            roleName=itemView.findViewById(R.id.hm_role_name);
            description = itemView.findViewById(R.id.hm_content);
            artistType = itemView.findViewById(R.id.hm_artist_name);
            daysAgo = itemView.findViewById(R.id.hm_dayscount);
            canvesImage = itemView.findViewById(R.id.hm_main_image);
            canvesImage1 = itemView.findViewById(R.id.hm_main_image1);
            canvesImage2 = itemView.findViewById(R.id.hm_main_image2);
            profileimage = itemView.findViewById(R.id.hm_profile);
            artistTitle = itemView.findViewById(R.id.hm_main_name);
            support = itemView.findViewById(R.id.hm_support);
            heartImg = itemView.findViewById(R.id.hm_heart_icon);
            chat = itemView.findViewById(R.id.hm_chat_icon);
            share = itemView.findViewById(R.id.hm_share_icon);
            views = itemView.findViewById(R.id.hm_views);
            more = itemView.findViewById(R.id.hm_more);
            cardBackground=itemView.findViewById(R.id.rh_backCard);
            imgLayout=itemView.findViewById(R.id.rl_shodw);
            backPortrait=itemView.findViewById(R.id.rh_portraitBack);
            backLandscape=itemView.findViewById(R.id.rh_landscapeBack);
            backSquare=itemView.findViewById(R.id.rh_squareBack);
            apprLayout=itemView.findViewById(R.id.rh_appreciation);
            backPortraitBackground=itemView.findViewById(R.id.rh_portraitBackground);


        }

    }

    public void getSupportData(FeedHolder holder,String id){

        apiInterface=ApiClient.getClient();
        progress=new GlobalProgressDialog(holder.itemView.getContext());

        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        HomeSupportRequest requestSupport = new HomeSupportRequest(id);
        apiInterface.homeSupport("Bearer " + BaseUtil.getUserToken(context), requestSupport).enqueue(new Callback<HomeSupportResponse>() {
            @Override
            public void onResponse(Call<HomeSupportResponse> call, Response<HomeSupportResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        holder.support.setText(response.body().getMessage());
                    } else {
                        Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
               progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<HomeSupportResponse> call, Throwable t) {
               progress.hideProgressBar();
                BaseUtil.showToast(context, t.getMessage());
            }
        });
    }

    public void getLikeData(Integer id,FeedHolder holder) {

        apiInterface=ApiClient.getClient();
        progress=new GlobalProgressDialog(holder.itemView.getContext());

        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");

            return;
        }

        HomeLikeRequest request = new HomeLikeRequest(id);
        apiInterface.homeLike("Bearer " + BaseUtil.getUserToken(holder.itemView.getContext()), request).enqueue(new Callback<HomeLikeResponse>() {
            @Override
            public void onResponse(Call<HomeLikeResponse> call, Response<HomeLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")){
                        if (response.body().isLiked)
                            holder.heartImg.setImageResource(R.drawable.ic_favorite_heart);
                        else holder.heartImg.setImageResource(R.drawable.ic_dislike);
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        // getFeedData(pagecount, false);
                    } else {
                       // BaseUtil.showToast(context, response.body().getMessage());
                        if (holder.heartImg.getDrawableState().equals(holder.itemView.getContext().getDrawable(R.drawable.ic_dislike)))
                            holder.heartImg.setImageResource(R.drawable.ic_favorite_heart);
                        else holder.heartImg.setImageResource(R.drawable.ic_dislike);
                    }
                } else {
                    if (holder.heartImg.getDrawableState().equals(holder.itemView.getContext().getDrawable(R.drawable.ic_dislike)))
                        holder.heartImg.setImageResource(R.drawable.ic_favorite_heart);
                    else holder.heartImg.setImageResource(R.drawable.ic_dislike);
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<HomeLikeResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
//                if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
//                    imgHeart.setImageResource(R.drawable.ic_favorite_heart);
//                else imgHeart.setImageResource(R.drawable.ic_dislike);

            }
        });
        }

    public String formatdate(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat d= new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;
    }



}
