package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.online.davincii.R;
import com.online.davincii.models.CanvasListRequest;
import com.online.davincii.models.LikeUserResponse;
import com.online.davincii.models.LikedUserData;
import com.online.davincii.models.comments.CommentLikeRequest;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class LikedUser extends AppCompatActivity {

    private RecyclerView uRecyclerView;
    private LikedUserAdapter likedUserAdapter;
    private List<LikedUserData> mList;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progressDialog;
    private int canvasid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_user);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        initViews(LikedUser.this);
    }

    private void initViews(Context context){
        apiInterface= ApiClient.getClient();
        progressDialog=new GlobalProgressDialog(LikedUser.this);
        uRecyclerView=findViewById(R.id.lu_recyclerView);
        mList=new ArrayList<>();
        canvasid = getIntent().getIntExtra("canvas_id", 0);

        findViewById(R.id.lu_backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikedUser.super.onBackPressed();
            }
        });
        getLikedUser();
    }

    private void getLikedUser(){
        if (!BaseUtil.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.showProgressBar();
        CanvasListRequest request=new CanvasListRequest(canvasid);
        apiInterface.getLikedUser("Bearer "+BaseUtil.getUserToken(getApplicationContext()),request).enqueue(new Callback<LikeUserResponse>() {
            @Override
            public void onResponse(Call<LikeUserResponse> call, Response<LikeUserResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                     mList.addAll(response.body().getData());
                     likedUserAdapter=new LikedUserAdapter(mList);
                     uRecyclerView.setAdapter(likedUserAdapter);
                    } else {
                        Toast.makeText(LikedUser.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LikedUser.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }
            @Override
            public void onFailure(Call<LikeUserResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(LikedUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo==========================Liked user adapter=======================

    public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.ViewHolder>{

        private List<LikedUserData> uList;

        public LikedUserAdapter(List<LikedUserData> uList) {
            this.uList = uList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_liked_user,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.userName.setText(uList.get(position).getUsername());
            Picasso.get().load(Constant.PROFILE_IMG_BASE+uList.get(position).getImageurl()).error(R.drawable.ic_user).into(holder.userImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(holder.itemView.getContext(),ArtistDetail.class);
                    intent.putExtra("id",uList.get(position).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return uList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView userName;
            private ImageView userImage;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userImage=itemView.findViewById(R.id.rlu_userImage);
                userName=itemView.findViewById(R.id.rlu_userName);
            }
        }
    }
}