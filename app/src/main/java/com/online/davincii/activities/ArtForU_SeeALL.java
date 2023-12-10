package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.adapters.ArtForUSeeAllAdapter;
import com.online.davincii.models.discoverysearch.ArtForYouResponse;
import com.online.davincii.models.discoverysearch.DC_AF_SeeAll_Response;
import com.online.davincii.models.discoverysearch.DC_ArtFu_SeeAllRequest;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtForU_SeeALL extends AppCompatActivity {
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;
    private RecyclerView rv_SeeAll;
    private Context context;
    private int pageNo = 1, totalPage = 0;
    private List<ArtForYouResponse> artForResponses = new ArrayList<>();
    private boolean apiHit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_for_you_all);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        intialize();
    }

    private void intialize() {
        context = ArtForU_SeeALL.this;
        progress = new GlobalProgressDialog(context);
        apiInterface = ApiClient.getClient();
        rv_SeeAll = findViewById(R.id.rv_seeAllOne);

        rv_SeeAll.setHasFixedSize(true);
        rv_SeeAll.setAdapter(new ArtForUSeeAllAdapter(artForResponses, context));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rv_SeeAll.setLayoutManager(gridLayoutManager);
        rv_SeeAll.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastVisibleItemPosition() == artForResponses.size() - 1) {
                        if (!apiHit) {
                            apiHit = true;
                            pageNo++;
                            if (pageNo <= totalPage)
                                getArtForU(pageNo);
                        }
                    }
                }
            }
        });
        getArtForU(pageNo);

        findViewById(R.id.afu_backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArtForU_SeeALL.super.onBackPressed();
            }
        });
    }

    private void getArtForU(int pageNo) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check Your Internet Connentivity");
            return;
        }
        progress.showProgressBar();
        DC_ArtFu_SeeAllRequest request = new DC_ArtFu_SeeAllRequest(pageNo);
        apiInterface.getArtFu("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<DC_AF_SeeAll_Response>() {
            @Override
            public void onResponse(Call<DC_AF_SeeAll_Response> call, Response<DC_AF_SeeAll_Response> response) {
                apiHit = false;
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData().size() > 0) {
                            totalPage = response.body().getNextPage();
                            artForResponses.addAll(response.body().getData());
                            rv_SeeAll.getAdapter().notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<DC_AF_SeeAll_Response> call, Throwable t) {
                apiHit = false;
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}