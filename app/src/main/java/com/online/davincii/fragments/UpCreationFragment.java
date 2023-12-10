package com.online.davincii.fragments;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.adapters.UpCreationAdapter;
import com.online.davincii.models.FeedRequest;
import com.online.davincii.models.ProfileCreator;
import com.online.davincii.models.UserProfileRespose;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UpCreationFragment extends Fragment {
    private RecyclerView rv_creation;
    private View view;
    GlobalProgressDialog progressDialog;
    private ApiClient.APIInterface apiInterface = ApiClient.getClient();
    private List<ProfileCreator> pList = new ArrayList<>();
    private TextView message;
    private boolean isOther = false;
    private  String businessId;

    public UpCreationFragment(boolean isOther,String businessId) {
        this.isOther = isOther;
        this.businessId=businessId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_up_creation, container, false);
        progressDialog = new GlobalProgressDialog(getActivity());
        rv_creation = view.findViewById(R.id.rv_upcreation);
        rv_creation.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        rv_creation.setLayoutManager(gridLayoutManager);
        message = view.findViewById(R.id.fc_message);
        callImage();
        return view;
    }

    private void callImage(){
        if (!BaseUtil.isNetworkAvailable(view.getContext())) {
            BaseUtil.showToast(view.getContext(), "Check your Internet Connectivity");
            return;
        }
        progressDialog.showProgressBar();
        Call<UserProfileRespose> call = null;
        if (!isOther)
            call = apiInterface.getUserProfile("Bearer " + BaseUtil.getUserToken(getActivity()));
        else {
            FeedRequest feedRequest = new FeedRequest();
            feedRequest.setId(businessId);
            call = apiInterface.getStudioProfile("Bearer " + BaseUtil.getUserToken(getActivity()), feedRequest);
        }
        call.enqueue(new Callback<UserProfileRespose>(){
            @Override
            public void onResponse(Call<UserProfileRespose> call, Response<UserProfileRespose> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")) {
                        if (response.body().getCreator().size() > 0) {
                            pList.addAll(response.body().getCreator());
                            UpCreationAdapter upadapter = new UpCreationAdapter(pList, isOther);
                            rv_creation.setAdapter(upadapter);
                            message.setVisibility(View.GONE);
                        } else {
                            message.setVisibility(View.VISIBLE);
                        }
                    } else {
                        message.setVisibility(View.VISIBLE);
                    }
                } else {
                    message.setVisibility(View.VISIBLE);
                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }
            @Override
            public void onFailure(Call<UserProfileRespose> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}