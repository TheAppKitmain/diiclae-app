package com.online.davincii.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.online.davincii.R;
import com.online.davincii.activities.GiftActivity;
import com.online.davincii.activities.MessageList;
import com.online.davincii.activities.MyCart;
import com.online.davincii.activities.ProfileActivity;
import com.online.davincii.adapters.FeedAdapter;
import com.online.davincii.adapters.ReportOptionAdapter;
import com.online.davincii.adapters.SupporterAdapter;
import com.online.davincii.models.FeedData;
import com.online.davincii.models.FeedRequest;
import com.online.davincii.models.FeedResponse;
import com.online.davincii.models.HomeLikeRequest;
import com.online.davincii.models.HomeLikeResponse;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.SupportersData;
import com.online.davincii.models.discoverydetails.DetailsData;
import com.online.davincii.models.sales_report.ReportListData;
import com.online.davincii.models.sales_report.ReportListResponse;
import com.online.davincii.models.sales_report.ReportRequest;
import com.online.davincii.models.sales_report.ReportResponse;
import com.online.davincii.models.sales_report.SupportersResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private RecyclerView fRecyclerView;
    private List<FeedData> fList = new ArrayList<>();
    private List<HomeLikeResponse> likeResponses = new ArrayList<>();
    private FeedAdapter feedAdapter;
    private View view;
    private Context context;
    private ImageView cartIcon, giftIcon;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;
    private static int pagecount = 1;
    private int lastpage = 0;
    private String id;
    private boolean apiHit = false;
    private boolean likeApi = false;
    private TextView cart_count, msgText;
    LinearLayoutManager linearLayoutManager;
    private List<HomeSupportResponse> supportRespon = new ArrayList<>();
    private List<DetailsData> detailsData = new ArrayList<>();
    private Boolean isSelected = false;
    private List<SupportersData> sList = new ArrayList<>();
    private SupporterAdapter supporterAdapter;
    private RecyclerView supRecyclerView,repRecyclerView;
    private static BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    private BottomSheetDialog bottomSheetDialog;
    private List<ReportListData> rList=new ArrayList<>();
    private ReportOptionAdapter reportOptionAdapter;
    private int canvasId, mPosition;
    private String from="home";
    private ImageView profileImg;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();
        initView();
        setOnclickListener();
        Picasso.get().load(BaseUtil.getUserProfile(requireActivity())).error(R.drawable.ic_user).into(profileImg);
        return view;
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            canvasId=intent.getIntExtra("canvasId",0);
            mPosition = intent.getIntExtra("position", 0);
            if (intent.hasExtra("get_supporters")) {
                bottomSheetDialog = new BottomSheetDialog(getActivity());
                View sheetView = getLayoutInflater().inflate(R.layout.supporter_bottom_sheet, null);
                supRecyclerView = sheetView.findViewById(R.id.sbs_recyclerView);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                getSupporterList(intent.getStringExtra("canvasId"),
                        intent.getStringExtra("canvasImage"),
                        intent.getStringExtra("canvasName"),
                        intent.getStringExtra("artBy"));
            }

            else if (intent.hasExtra("report")){
                bottomSheetDialog = new BottomSheetDialog(getActivity());
                View sheetView = getLayoutInflater().inflate(R.layout.report_dialoge, null);
                repRecyclerView=sheetView.findViewById(R.id.rp_recyclerView);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                getReportOptions();
                bottomSheetDialog.show();
            }

            else if (intent.hasExtra("type")) {
                int likeId = intent.getIntExtra("like_id", 0);
                int position = intent.getIntExtra("position", 0);
//                if(!likeApi) {
//                    likeApi = true;
//                    getLikeData(String.valueOf(likeId));
//                }
            } else {
                String userId = intent.getStringExtra("user_id");
                int position = intent.getIntExtra("position", 0);
                FeedAdapter.FeedHolder feedHolder = (FeedAdapter.FeedHolder) fRecyclerView.findViewHolderForAdapterPosition(position);
//                assert feedHolder != null;
                getSupportData(feedHolder.support, userId);
            }
        }
    };

    public BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("do_report") || intent.hasExtra("report")) {
                addReport(canvasId,intent.getIntExtra("optionId",0));
                bottomSheetDialog.dismiss();
            }
        }
    };

    private void setOnclickListener() {

    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        profileImg=view.findViewById(R.id.db_fm_userprofile);
        cartIcon = view.findViewById(R.id.hm_cartbtn);
        fRecyclerView = view.findViewById(R.id.hm_recycler);
        cart_count = view.findViewById(R.id.fh_count);
        giftIcon = view.findViewById(R.id.hm_gift);
        msgText=view.findViewById(R.id.hm_msg);
        getFeedData(1, true);
        feedAdapter = new FeedAdapter(fList, context, supportRespon, from);
        linearLayoutManager = new LinearLayoutManager(context);
        fRecyclerView.setLayoutManager(linearLayoutManager);
        fRecyclerView.setAdapter(feedAdapter);
        fRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == fList.size() - 1) {
                        if (!apiHit) {
                            apiHit = true;
                            if (pagecount > 0)
                                getFeedData(pagecount, true);
                        }
                    }
                }
            }
        });

        giftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GiftActivity.class));
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCart.class));
            }
        });


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
    }

    public void getSupportData(TextView textView, String id) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        HomeSupportRequest requestSupport = new HomeSupportRequest(id);
        apiInterface.homeSupport("Bearer " + BaseUtil.getUserToken(getContext()), requestSupport).enqueue(new Callback<HomeSupportResponse>() {
            @Override
            public void onResponse(Call<HomeSupportResponse> call, Response<HomeSupportResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        textView.setText(response.body().getMessage());
                        getFeedData(pagecount, true);
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                    feedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HomeSupportResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
            }
        });
    }

    public void getLikeData(String id) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            likeApi = false;
            return;
        }

        HomeLikeRequest request = new HomeLikeRequest(Integer.valueOf(id));
        apiInterface.homeLike("Bearer " + BaseUtil.getUserToken(getActivity().getApplicationContext()), request).enqueue(new Callback<HomeLikeResponse>() {
            @Override
            public void onResponse(Call<HomeLikeResponse> call, Response<HomeLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")){
//                        if (response.body().isLiked)
//                            imgHeart.setImageResource(R.drawable.ic_favorite_heart);
//                        else imgHeart.setImageResource(R.drawable.ic_dislike);
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                      // getFeedData(pagecount, false);
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
//                        if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
//                            imgHeart.setImageResource(R.drawable.ic_favorite_heart);
//                        else imgHeart.setImageResource(R.drawable.ic_dislike);
                    }
                } else {
//                    if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
//                        imgHeart.setImageResource(R.drawable.ic_favorite_heart);
//                    else imgHeart.setImageResource(R.drawable.ic_dislike);
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                likeApi = false;
            }

            @Override
            public void onFailure(Call<HomeLikeResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
//                if (imgHeart.getDrawableState().equals(getResources().getDrawable(R.drawable.ic_dislike)))
//                    imgHeart.setImageResource(R.drawable.ic_favorite_heart);
//                else imgHeart.setImageResource(R.drawable.ic_dislike);
                likeApi = false;
            }
        });
    }

    private void getFeedData(int page_no, boolean isProgress) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        if (isProgress)
            progress.showProgressBar();

        FeedRequest request = new FeedRequest(page_no);
        apiInterface.getFeed("Bearer " + BaseUtil.getUserToken(getActivity().getApplicationContext()), request).enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (fList.size() > 0)
                    fList.clear();
                apiHit = false;
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getNextPage() != 0)
                            pagecount = response.body().getNextPage();
                        fList.addAll(response.body().getData());
                        feedAdapter.notifyDataSetChanged();
                        fRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        fRecyclerView.setVisibility(View.GONE);
                        msgText.setVisibility(View.VISIBLE);
                        //BaseUtil.showToast(context, response.body().getMessage());
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
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                progress.hideProgressBar();
                apiHit = false;
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });
    }

    private void getCartItem() {
        cart_count.setVisibility(View.GONE);
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        apiInterface.myCartItems("Bearer " + BaseUtil.getUserToken(context)).enqueue(new Callback<MyCartResponse>() {
            @Override
            public void onResponse(Call<MyCartResponse> call, Response<MyCartResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        cart_count.setVisibility(View.VISIBLE);
                        cart_count.setText(String.valueOf(response.body().getCartItems().size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<MyCartResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(from));
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("report"));
        getCartItem();
        super.onResume();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getSupporterList(String canvasId, String canvasImage, String canvasName, String artBy) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        apiInterface.getSupportersList("Bearer " + BaseUtil.getUserToken(getActivity())).enqueue(new Callback<SupportersResponse>() {
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
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SupportersResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReportOptions(){
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
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
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReport(int canvasId, int optionId){
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        ReportRequest reportRequest=new ReportRequest(canvasId,optionId);
        apiInterface.addReport("Bearer "+BaseUtil.getUserToken(getActivity()),reportRequest).enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        fList.get(mPosition).setReport(true);
                        feedAdapter.notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }else{
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeFragment(Fragment fragment, String fragmentTag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}