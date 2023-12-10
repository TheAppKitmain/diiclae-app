package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.online.davincii.R;
import com.online.davincii.adapters.UpTabAdapter;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.models.StudioprofileCreator;
import com.online.davincii.models.StudioprofileRequest;
import com.online.davincii.models.StudioprofileResponse;
import com.online.davincii.models.UserProfileData;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistDetail extends AppCompatActivity implements View.OnClickListener {
    private TextView topHeading, cretcolt, pieces, supportes, supporting, descripton, daviciUrl, contact,contactEmail, message, support, viewPagerMessage;
    private ImageView studioImage, profileImg, settingbtn, backbtn;
    private GlobalProgressDialog progress;
    private View view;
    private ApiClient.APIInterface apiInterface;
    public  UserProfileData profileData;
    private String name;
    private RecyclerView recCreator;
    private String id, canvasId,studioId,stdProfileImage,studioName;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomSheetDialog bottomSheetDialog;
    private List<StudioprofileCreator> mList = new ArrayList<>();
    private String userEmail;
    private LinearLayout apprLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        context = ArtistDetail.this;

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        id = getIntent().getStringExtra("id");
        initView();
        setOnclickListener();
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(this);
        apiInterface = ApiClient.getClient();
        contact = findViewById(R.id.ad_contact);
        topHeading = findViewById(R.id.ad_tophdg);
        cretcolt = findViewById(R.id.ad_creatcolect_txt);
        pieces = findViewById(R.id.ad_pieces_txt);
        tabLayout = findViewById(R.id.ad_tablayout);
        viewPager = findViewById(R.id.ad_view_pager);
        supportes = findViewById(R.id.ad_suportes_txt);
        supporting = findViewById(R.id.ad_supting_txt);
        descripton = findViewById(R.id.ad_descpton_txt);
        daviciUrl = findViewById(R.id.ad_davnci_url);
        message = findViewById(R.id.ad_message);
        viewPagerMessage = findViewById(R.id.ad_view_pagermessage);
        studioImage = findViewById(R.id.ad_std_img);
        backbtn = findViewById(R.id.ad_back);
        profileImg = findViewById(R.id.ad_profile_img);
        support = findViewById(R.id.ad_support);
        apprLayout=findViewById(R.id.ad_apprLayout);
        callUsPrFrag();

        tabLayout.addTab(tabLayout.newTab().setText("Creation"));
        tabLayout.addTab(tabLayout.newTab().setText("Collection"));

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportData(id);
            }
        });
        viewPagerSetup();
    }

    private void viewPagerSetup() {
        final UpTabAdapter tabAdapter = new UpTabAdapter(ArtistDetail.this, getSupportFragmentManager(), tabLayout.getTabCount(), true, id);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(ArtistDetail.this);
                tab.setCustomView(tabTextView);
                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.setText(tab.getText());
                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    tabTextView.setTextColor(getResources().getColor(R.color.white));
                }else{
                    tabTextView.setTextColor(getResources().getColor(R.color.white));
//                    tabTextView.setTypeface(null, Typeface.BOLD);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(ResourcesCompat.getFont(ArtistDetail.this, R.font.glacial_regular));
                text.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(ResourcesCompat.getFont(ArtistDetail.this, R.font.glacial_regular));
                text.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void changeViewColor(boolean isSelected) {
        if (isSelected) {
            support.setTextColor(getResources().getColor(R.color.white));
          //  support.setBackgroundResource(R.drawable.red_corner_border);
//            isSelected = false;
        } else {
            support.setTextColor(getResources().getColor(R.color.white));
         //   support.setBackgroundResource(R.drawable.back_card_payment);
//            isSelected = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ad_back:
                ArtistDetail.super.onBackPressed();
                break;
            case R.id.ad_contact:
                bottomSheetDialog = new BottomSheetDialog(ArtistDetail.this);
                View sheetView = getLayoutInflater().inflate(R.layout.contactbottomsheet, null);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                bottomSheetDialog.show();

                TextView call = sheetView.findViewById(R.id.contact_callno);
                contactEmail = sheetView.findViewById(R.id.contact_email);
                contactEmail.setText(userEmail);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = "0123456789";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                        intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                        startActivity(intent);
                    }
                });

                contactEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        composeEmail(userEmail);
                    }
                });
                break;
            case R.id.ad_message:
                Intent intent=new Intent(context, ActivityChat.class);
                intent.putExtra("studioId",studioId);
                intent.putExtra("studioImage",stdProfileImage);
                intent.putExtra("studioName",studioName);
                startActivity(intent);
                break;

            case R.id.ad_davnci_url:
                  startActivity(new Intent(this, OpenBioLink.class).putExtra("link", daviciUrl.getText().toString()));
                break;
            case R.id.ad_apprLayout:
                startActivity(new Intent(ArtistDetail.this, AppriciationActivity.class)
                        .putExtra("name", studioName));
        }
    }

    public void composeEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void setOnclickListener(){
        backbtn.setOnClickListener(this);
        contact.setOnClickListener(this);
        message.setOnClickListener(this);
        daviciUrl.setOnClickListener(this);
    }

    public void getSupportData(String id){
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        HomeSupportRequest requestSupport = new HomeSupportRequest(id);
        apiInterface.homeSupport("Bearer " + BaseUtil.getUserToken(getApplicationContext()), requestSupport).enqueue(new Callback<HomeSupportResponse>() {
            @Override
            public void onResponse(Call<HomeSupportResponse> call, Response<HomeSupportResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        support.setText(response.body().getMessage());
                        if (response.body().getMessage().equalsIgnoreCase("support")) {
                            changeViewColor(false);
                        } else {
                            changeViewColor(true);
                        }
                        // Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callUsPrFrag(){
        if (!BaseUtil.isNetworkAvailable(this)) {
            BaseUtil.showToast(this, "Check your Internet Connectivity");
            return;
        }
        progress.showProgressBar();
        StudioprofileRequest studioprofileRequest = new StudioprofileRequest();
        studioprofileRequest.setId(id);
        apiInterface.studioProfile("Bearer " + BaseUtil.getUserToken(getApplicationContext()), studioprofileRequest).enqueue(new Callback<StudioprofileResponse>() {
            @Override
            public void onResponse(Call<StudioprofileResponse> call, Response<StudioprofileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData() != null) {
                            if (response.body().getCreator().size() > 0) {
                                mList.addAll(response.body().getCreator());
//                                recCreator.getAdapter().notifyDataSetChanged();
                                viewPagerMessage.setVisibility(View.GONE);
                            } else {
                                viewPagerMessage.setVisibility(View.VISIBLE);
                            }
                            studioName=response.body().getData().getUsername();
                            studioId=response.body().getData().getUserid();
                            stdProfileImage=Constant.PROFILE_IMG_BASE + response.body().getData().getProfilePicture();
                            cretcolt.setText(response.body().getData().getFirstName() + " " + response.body().getData().getLastName());
                            descripton.setText(response.body().getData().getBio());
                            pieces.setText(String.valueOf(response.body().getPieces()));
                            supportes.setText(response.body().getSupported());
                            topHeading.setText(response.body().getData().getUsername());
                            supporting.setText(response.body().getSupporting());
                            userEmail=response.body().getData().getEmail();
                            if (response.body().isSupport()) {
                                support.setText("Supporting");
                                changeViewColor(true);
                            } else {
                                support.setText("Support");
                                changeViewColor(false);
                            }

                            daviciUrl.setText(response.body().getData().getLink());
                            Glide.with(ArtistDetail.this).load(Constant.STUDIO_IMG_BASE + response.body().getData().getStudioPicture()).into(studioImage);
                            Glide.with(ArtistDetail.this).load(Constant.PROFILE_IMG_BASE + response.body().getData().getProfilePicture()).into(profileImg);
                        }
                    } else {
                        Toast.makeText(ArtistDetail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ArtistDetail.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<StudioprofileResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(ArtistDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}