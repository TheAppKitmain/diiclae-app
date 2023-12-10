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
import com.online.davincii.models.discoverysearch.DC_TS_SeeAllResponse;
import com.online.davincii.models.discoverysearch.DC_TopStu_SeeAllResquest;
import com.online.davincii.models.discoverysearch.TopStudio_SeeAllresponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopStudio_SeeAll extends AppCompatActivity {
    private RecyclerView rv_topStudio;
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apInterface;
    private Context context;
    private List<TopStudio_SeeAllresponse> topStudioList = new ArrayList<>();
    private int pageNo = 1, totalPage = 0;
    private boolean apiHit = false;
    private RecyclerView rv_topSudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_studio_see_all);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        init();
    }

    private void init() {
        context = TopStudio_SeeAll.this;
        apInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        rv_topStudio = findViewById(R.id.rv_topStudio);
        rv_topStudio.setHasFixedSize(true);
        rv_topStudio.setAdapter(new com.online.davincii.adapters.TopStudio_SeeAll(topStudioList, context));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rv_topStudio.setLayoutManager(gridLayoutManager);
        rv_topStudio.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastVisibleItemPosition() == topStudioList.size() - 1) {
                        if (!apiHit) {
                            apiHit = true;
                            pageNo++;
                            if (pageNo <= totalPage)
                                getTopStudio(pageNo);
                        }
                    }
                }
            }
        });
        getTopStudio(pageNo);

        findViewById(R.id.tss_backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopStudio_SeeAll.super.onBackPressed();
            }
        });

    }

    private void getTopStudio(int pageNo) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check  your Internet Connectivity");
            return;
        }
        progress.showProgressBar();
        DC_TopStu_SeeAllResquest request = new DC_TopStu_SeeAllResquest(pageNo);
        apInterface.getTopStudio("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<DC_TS_SeeAllResponse>() {
            @Override
            public void onResponse(Call<DC_TS_SeeAllResponse> call, Response<DC_TS_SeeAllResponse> response) {
                apiHit = false;
                if (response.isSuccessful()) {

                    if (response.body().getError().equals("0")) {
                        if (response.body().getData().size() > 0) {
                            totalPage = response.body().getNextPage();
                            topStudioList.addAll(response.body().getData());
                            rv_topStudio.getAdapter().notifyDataSetChanged();

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
            public void onFailure(Call<DC_TS_SeeAllResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}