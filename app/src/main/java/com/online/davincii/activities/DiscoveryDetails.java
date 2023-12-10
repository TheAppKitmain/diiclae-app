package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.online.davincii.R;
import com.online.davincii.adapters.FeedAdapter;
import com.online.davincii.adapters.ReportOptionAdapter;
import com.online.davincii.adapters.SupporterAdapter;
import com.online.davincii.models.FeedData;
import com.online.davincii.models.HomeLikeRequest;
import com.online.davincii.models.HomeLikeResponse;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.models.SupportersData;
import com.online.davincii.models.discoverydetails.DetailsData;
import com.online.davincii.models.discoverydetails.DiscoveryRequest;
import com.online.davincii.models.discoverydetails.DiscoveryResponses;
import com.online.davincii.models.discoverydetails.SelectedCanvas;
import com.online.davincii.models.sales_report.ReportListData;
import com.online.davincii.models.sales_report.ReportListResponse;
import com.online.davincii.models.sales_report.ReportRequest;
import com.online.davincii.models.sales_report.ReportResponse;
import com.online.davincii.models.sales_report.SupportersResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoveryDetails extends AppCompatActivity {
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private RecyclerView rvDetails;
    private List<DetailsData> detailsData = new ArrayList<>();
    private List<SelectedCanvas> selectedCanvas = new ArrayList<>();
    private List<FeedData> feedList = new ArrayList<>();
    private List<HomeSupportResponse> supportRespon = new ArrayList<>();
    private int pageNo = 1, totalPage = 0;
    private boolean apiHit = false;
    private boolean likeApi = false;
    private Integer id;
    private ImageView backbtn;
    private static BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    private List<SupportersData> sList = new ArrayList<>();
    private SupporterAdapter supporterAdapter;
    private RecyclerView supRecyclerView,repRecyclerView;
    private BottomSheetDialog bottomSheetDialog;
    private int canvasId, mPosition;
    private List<ReportListData> rList=new ArrayList<>();
    private ReportOptionAdapter reportOptionAdapter;
    private String from="discovery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_details);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        rvDetails = findViewById(R.id.dd_rvDetail);
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(this);
        backbtn = findViewById(R.id.dd_backbtn);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(from));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("report"));
        id = getIntent().getIntExtra("id", 0);

        rvDetails.setAdapter(new FeedAdapter(feedList, getApplicationContext(), supportRespon, from));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvDetails.setLayoutManager(layoutManager);

        rvDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == feedList.size() - 1) {
                        if (!apiHit) {
                            apiHit = true;
                            if (pageNo <= totalPage) {
                                getDetailsApi();
                            }
                        }
                    }
                }
            }
        });
        //  rvDetails.setAdapter(new FeedAdapter(detailsData, canvasList));
        //  rvDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getDetailsApi();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("notification")){
                    DiscoveryDetails.super.onBackPressed();
                }else{
                    DiscoveryDetails.super.onBackPressed();
                }

            }
        });
    }

    public BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("do_report") || intent.hasExtra("report")) {
            addReport(canvasId,intent.getIntExtra("optionId",0));
            bottomSheetDialog.dismiss();
        }
        }
    };

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            canvasId=intent.getIntExtra("canvasId",0);
            mPosition = intent.getIntExtra("position", 0);
            if (intent.hasExtra("get_supporters")) {
                bottomSheetDialog = new BottomSheetDialog(DiscoveryDetails.this);
                View sheetView = getLayoutInflater().inflate(R.layout.supporter_bottom_sheet, null);
                supRecyclerView = sheetView.findViewById(R.id.sbs_recyclerView);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                getSupporterList(intent.getStringExtra("canvasId"),
                        intent.getStringExtra("canvasImage"),
                        intent.getStringExtra("canvasName"),
                        intent.getStringExtra("artBy"));
            } else if (intent.hasExtra("report")){
                bottomSheetDialog = new BottomSheetDialog(DiscoveryDetails.this);
                View sheetView = getLayoutInflater().inflate(R.layout.report_dialoge, null);
                repRecyclerView=sheetView.findViewById(R.id.rp_recyclerView);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                getReportOptions();
                bottomSheetDialog.show();
            }
            else
            if (intent.hasExtra("type")) {
                int likeId = intent.getIntExtra("like_id", 0);
                int position = intent.getIntExtra("position", 0);
//                FeedAdapter.FeedHolder feedHolder = (FeedAdapter.FeedHolder) rvDetails.findViewHolderForAdapterPosition(position);
//                assert feedHolder != null;
//                if(!likeApi) {
//                    getLikeData(feedHolder.heartImg, String.valueOf(likeId));
//                }
            } else {
                String userId = intent.getStringExtra("user_id");
                int position = intent.getIntExtra("position", 0);
                FeedAdapter.FeedHolder feedHolder = (FeedAdapter.FeedHolder) rvDetails.findViewHolderForAdapterPosition(position);
                assert feedHolder != null;
                getSupportData(feedHolder.support, userId);
            }
        }
    };


    private void getDetailsApi() {
        if (!BaseUtil.isNetworkAvailable(this)) {
            BaseUtil.showToast(getApplicationContext(), "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();

        DiscoveryRequest request = new DiscoveryRequest(id, pageNo);

        apiInterface.discoveryDetails("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<DiscoveryResponses>() {
            @Override
            public void onResponse(Call<DiscoveryResponses> call, Response<DiscoveryResponses> response) {
                if (response.isSuccessful()) {
                    apiHit = false;
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData().size() > 0) {
                            totalPage = response.body().getNextPage();
//                            detailsData.add(response.body().getSelectedCanvas());
                            feedList.add(response.body().getSelectedCanvas());
                            feedList.addAll(response.body().getData());
                            rvDetails.getAdapter().notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(DiscoveryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DiscoveryDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.hideProgressBar();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<DiscoveryResponses> call, Throwable t) {
                progress.hideProgressBar();
                apiHit = false;
                Toast.makeText(DiscoveryDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getSupportData(TextView textView, String id) {
        if (!BaseUtil.isNetworkAvailable(getApplicationContext())) {
            BaseUtil.showToast(this, "Check your internet connectivity");
            return;
        }
        HomeSupportRequest requestSupport = new HomeSupportRequest(id);
        apiInterface.homeSupport("Bearer " + BaseUtil.getUserToken(getApplicationContext()), requestSupport).enqueue(new Callback<HomeSupportResponse>() {
            @Override
            public void onResponse(Call<HomeSupportResponse> call, Response<HomeSupportResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        textView.setText(response.body().getMessage());

//                        if (isSelected) {
//
//                            isSelected = false;
//                        } else {
//
//                            isSelected = true;
//                        }
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<HomeSupportResponse> call, Throwable t) {
                BaseUtil.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    public void getLikeData(ImageView imgHeart, String id) {
        if (!BaseUtil.isNetworkAvailable(getApplicationContext())) {
            BaseUtil.showToast(getApplicationContext(), "Check your Internet Connectivity");
            return;
        }
        likeApi = true;
        HomeLikeRequest request = new HomeLikeRequest(Integer.valueOf(id));
        apiInterface.homeLike("Bearer " + BaseUtil.getUserToken(getApplicationContext().getApplicationContext()), request).enqueue(new Callback<HomeLikeResponse>() {
            @Override
            public void onResponse(Call<HomeLikeResponse> call, Response<HomeLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().isLiked)
                            imgHeart.setImageResource(R.drawable.ic_favorite_heart);
                        else imgHeart.setImageResource(R.drawable.ic_dislike);
                       // Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
                            imgHeart.setImageResource(R.drawable.ic_favorite_heart);
                        else imgHeart.setImageResource(R.drawable.ic_dislike);
                      //  Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
                        imgHeart.setImageResource(R.drawable.ic_favorite_heart);
                    else imgHeart.setImageResource(R.drawable.ic_dislike);
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
                likeApi = false;
            }

            @Override
            public void onFailure(Call<HomeLikeResponse> call, Throwable t) {
                BaseUtil.showToast(getApplicationContext(), t.getMessage());
                if (imgHeart.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
                    imgHeart.setImageResource(R.drawable.ic_favorite_heart);
                else imgHeart.setImageResource(R.drawable.ic_dislike);

                likeApi = false;
            }
        });
    }

    private void getSupporterList(String canvasId, String canvasImage, String canvasName, String artBy) {
        if (!BaseUtil.isNetworkAvailable(DiscoveryDetails.this)) {
            BaseUtil.showToast(DiscoveryDetails.this, "Check your internet connectivity");
            return;
        }
        apiInterface.getSupportersList("Bearer " + BaseUtil.getUserToken(DiscoveryDetails.this)).enqueue(new Callback<SupportersResponse>() {
            @Override
            public void onResponse(Call<SupportersResponse> call, Response<SupportersResponse> response) {
                sList.clear();
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        sList.addAll(response.body().getSupporters());
                        supporterAdapter = new SupporterAdapter(sList, canvasId, canvasImage, canvasName, artBy);
                        supRecyclerView.setAdapter(supporterAdapter);
                        bottomSheetDialog.show();
                    } else {
                        Toast.makeText(DiscoveryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DiscoveryDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SupportersResponse> call, Throwable t) {
                Toast.makeText(DiscoveryDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getReportOptions(){
        if (!BaseUtil.isNetworkAvailable(DiscoveryDetails.this)) {
            BaseUtil.showToast(DiscoveryDetails.this, "Check your internet connectivity");
            return;
        }
        apiInterface.getReportOptions().enqueue(new Callback<ReportListResponse>() {
            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        rList=response.body().getData();
                        reportOptionAdapter=new ReportOptionAdapter(rList);
                        repRecyclerView.setAdapter(reportOptionAdapter);
                    }else{
                        Toast.makeText(DiscoveryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DiscoveryDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                Toast.makeText(DiscoveryDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReport(int canvasId, int optionId){
        if (!BaseUtil.isNetworkAvailable(DiscoveryDetails.this)) {
            BaseUtil.showToast(DiscoveryDetails.this, "Check your internet connectivity");
            return;
        }
        ReportRequest reportRequest=new ReportRequest(canvasId,optionId);

        apiInterface.addReport("Bearer "+BaseUtil.getUserToken(DiscoveryDetails.this),reportRequest).enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        Toast.makeText(DiscoveryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        feedList.get(mPosition).setReport(true);
                        rvDetails.getAdapter().notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }else{
                        Toast.makeText(DiscoveryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DiscoveryDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }
}