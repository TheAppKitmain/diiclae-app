package com.online.davincii.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.online.davincii.R;
import com.online.davincii.activities.MyCart;
import com.online.davincii.activities.ProfileActivity;
import com.online.davincii.adapters.FeedAdapter;
import com.online.davincii.adapters.SalesReportAdapter;
import com.online.davincii.adapters.Seles_ReportAdapter;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.models.sales_report.SalesReportData;
import com.online.davincii.models.sales_report.SalesReportRequest;
import com.online.davincii.models.sales_report.SalesReportResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReportFragment extends Fragment {

  private Context context;
  private View view;
    private GlobalProgressDialog progress;
  private RecyclerView sRecyclerView;
  private SalesReportAdapter salesReportAdapter;
  private List<SalesReportData> reporlist=new ArrayList<>();
  private ApiClient.APIInterface apiInterface;
    private ImageView profileImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sales_report, container, false);
        context = view.getContext();
        initView();
        return view;
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                String userId = intent.getStringExtra("user_id");
                int position = intent.getIntExtra("position", 0);
                getSupportData(userId, position);

        }
    };

    private void getSupportData(String userId, int position) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        HomeSupportRequest requestSupport = new HomeSupportRequest(userId);
        apiInterface.homeSupport("Bearer " + BaseUtil.getUserToken(getContext()), requestSupport).enqueue(new Callback<HomeSupportResponse>() {
            @Override
            public void onResponse(Call<HomeSupportResponse> call, Response<HomeSupportResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if(response.body().getMessage().toLowerCase().equals("support")){
                            reporlist.get(position).setSupport(false);
                        }else{
                            reporlist.get(position).setSupport(true);
                        }
                     salesReportAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<HomeSupportResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
                progress.hideProgressBar();
            }
        });
    }

    private void initView() {

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("sales_report"));
        apiInterface=ApiClient.getClient();
        profileImg= view.findViewById(R.id.sr_userprofile);
        progress = new GlobalProgressDialog(context);
        sRecyclerView=view.findViewById(R.id.sr_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        sRecyclerView.setLayoutManager(linearLayoutManager);
        getReportList();

        Picasso.get().load(BaseUtil.getUserProfile(requireActivity())).error(R.drawable.ic_user).into(profileImg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
    }

    private void getReportList(){
        if (!BaseUtil.isNetworkAvailable(getActivity())){
            Toast.makeText(context, "Please check your internet", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.showProgressBar();
        SalesReportRequest reportRequest=new SalesReportRequest(1);
        apiInterface.getSalesReport("Bearer "+BaseUtil.getUserToken(getActivity()),reportRequest).enqueue(new Callback<SalesReportResponse>() {
            @Override
            public void onResponse(Call<SalesReportResponse> call, Response<SalesReportResponse> response) {
                if (response.isSuccessful()){
                    if(response.body().getError().equals("0")){
                     reporlist.addAll(response.body().getData());
                     salesReportAdapter=new SalesReportAdapter(reporlist);
                     sRecyclerView.setAdapter(salesReportAdapter);
                    }else{
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<SalesReportResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progress.hideProgressBar();
            }
        });
    }
}