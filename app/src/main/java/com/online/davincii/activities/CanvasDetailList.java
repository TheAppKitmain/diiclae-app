package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.online.davincii.R;
import com.online.davincii.models.CanvasListRequest;
import com.online.davincii.models.CanvasListResponse;
import com.online.davincii.models.SelectedCreation;
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

public class CanvasDetailList extends AppCompatActivity {

    private RecyclerView cRecyclerView;
    private CreationListAdapter creationListAdapter;
    private List<SelectedCreation> mList=new ArrayList<>();
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progressDialog;
    private int canvasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_detail_list);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initViews(CanvasDetailList.this);
    }

    private void initViews(Context context){
        apiInterface= ApiClient.getClient();
        progressDialog=new GlobalProgressDialog(CanvasDetailList.this);
        cRecyclerView=findViewById(R.id.cdl_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        cRecyclerView.setLayoutManager(linearLayoutManager);
        canvasId=getIntent().getIntExtra("canvasId",0);

        findViewById(R.id.cdl_backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CanvasDetailList.super.onBackPressed();
            }
        });
        getCanvasList();
    }

    private void getCanvasList(){
        if (!BaseUtil.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.showProgressBar();

        CanvasListRequest request=new CanvasListRequest(canvasId);

        apiInterface.getCanvasList("Bearer "+BaseUtil.getUserToken(getApplicationContext()),request).enqueue(new Callback<CanvasListResponse>() {
            @Override
            public void onResponse(Call<CanvasListResponse> call, Response<CanvasListResponse> response){
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                         mList.addAll(response.body().getSelectedCreation());
                         creationListAdapter = new CreationListAdapter(mList);
                         cRecyclerView.setAdapter(creationListAdapter);
                    }else{
                        Toast.makeText(CanvasDetailList.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CanvasDetailList.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }
            @Override
            public void onFailure(Call<CanvasListResponse> call, Throwable t){
                progressDialog.hideProgressBar();
                Toast.makeText(CanvasDetailList.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo=============================canvas list adapter===========================

    public class CreationListAdapter extends RecyclerView.Adapter<CreationListAdapter.ViewHolder>{

        private List<SelectedCreation> cList;

        public CreationListAdapter(List<SelectedCreation> cList) {
            this.cList = cList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_canvas_detail,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.canvasName.setText(cList.get(position).getTitle());
            holder.canvasDesc.setText(cList.get(position).getDescription());
            holder.commentCount.setText(String.valueOf(cList.get(position).getComment()));
            holder.likeCount.setText(String.valueOf(cList.get(position).getLikes()));
            holder.viewCount.setText(String.valueOf(cList.get(position).getViews()));

            if (cList.get(position).getCanvastype()==0) {
                holder.sqrLayout.setVisibility(View.VISIBLE);
                holder.landscapeLayout.setVisibility(View.GONE);
                holder.portraitLayout.setVisibility(View.GONE);
                Picasso.get().load(Constant.PROFILE_IMG_BASE + cList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.sqrImage);
            } else if (cList.get(position).getCanvastype()==1) {
                holder.landscapeLayout.setVisibility(View.VISIBLE);
                holder.sqrLayout.setVisibility(View.GONE);
                holder.portraitLayout.setVisibility(View.GONE);
                Picasso.get().load(Constant.PROFILE_IMG_BASE + cList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.landScapeImage);
            } else {
                holder.portraitLayout.setVisibility(View.VISIBLE);
                holder.landscapeLayout.setVisibility(View.GONE);
                holder.sqrLayout.setVisibility(View.GONE);
                Picasso.get().load(Constant.PROFILE_IMG_BASE + cList.get(position).getImageurl()).error(R.drawable.diiclae_colored_icon).into(holder.portraitImage);
            }

            holder.commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(holder.itemView.getContext(),Comments.class);
                    intent.putExtra("canvas_id",cList.get(position).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });

            holder.likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(holder.itemView.getContext(),LikedUser.class);
                    intent.putExtra("canvas_id",cList.get(position).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return cList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView canvasName,canvasDesc,commentCount,likeCount,viewCount;
            private LinearLayout sqrLayout, landscapeLayout,portraitLayout,commentLayout,likeLayout;
            private ImageView sqrImage,landScapeImage,portraitImage;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                canvasName=itemView.findViewById(R.id.cd_canvasName);
                canvasDesc=itemView.findViewById(R.id.cd_canvasDesc);
                commentCount=itemView.findViewById(R.id.cd_commentCount);
                likeCount=itemView.findViewById(R.id.cd_likeCount);
                viewCount=itemView.findViewById(R.id.cd_viewCount);
                sqrLayout=itemView.findViewById(R.id.cd_saquareLay);
                sqrImage=itemView.findViewById(R.id.cd_sqrImage);
                landscapeLayout=itemView.findViewById(R.id.cd_landscapLay);
                landScapeImage=itemView.findViewById(R.id.cd_landScapeImage);
                portraitLayout=itemView.findViewById(R.id.cd_portraitLay);
                portraitImage=itemView.findViewById(R.id.cd_portraitImage);
                commentLayout=itemView.findViewById(R.id.cd_commentLayout);
                likeLayout=itemView.findViewById(R.id.cd_likeLayout);

            }
        }
    }


}