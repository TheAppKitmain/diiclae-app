package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.online.davincii.R;
import com.online.davincii.activities.uploadscreens.UploadOne;
import com.online.davincii.fragments.ChatFragment;
import com.online.davincii.fragments.DiscoverFragment;
import com.online.davincii.fragments.HomeFragment;
import com.online.davincii.fragments.SalesReportFragment;
import com.online.davincii.fragments.UploadFragment;
import com.online.davincii.fragments.UserProfileFragment;
import com.online.davincii.fragments.WalletFragment;
import com.online.davincii.models.UpdateTokenData;
import com.online.davincii.models.UpdateTokenRequest;
import com.online.davincii.models.UpdateTokenResponse;
import com.online.davincii.models.comments.CommentLikeResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageView home, discover, upload, salesReport, userProfile,chatImg;
    private Context context;
    private Fragment fragment;
    private ApiClient.APIInterface apiInterface;
    private String device_id,fcmTokan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseUtil.putSenderAccount(getApplicationContext(),"");
        setContentView(R.layout.activity_dashboard_screen);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        context = DashboardScreen.this;
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        initilize();
        setOnClickListener();
    }

    private void setOnClickListener() {
        apiInterface = ApiClient.getClient();
        home.setOnClickListener(this);
        discover.setOnClickListener(this);
        upload.setOnClickListener(this);
        salesReport.setOnClickListener(this);
        userProfile.setOnClickListener(this);
        chatImg.setOnClickListener(this);
    }

    private void initilize() {
        home = findViewById(R.id.db_home);
        discover = findViewById(R.id.db_discover);
        upload = findViewById(R.id.db_upload);
        salesReport = findViewById(R.id.db_sales_report);
        userProfile = findViewById(R.id.db_userprofile);
        chatImg=findViewById(R.id.db_chat);
        changeFragment(new HomeFragment(), "home");
        home.setImageResource(R.drawable.home_logo_color);
        discover.setImageResource(R.drawable.ic_search_new);
        upload.setImageResource(R.drawable.ic_upload_new);
        salesReport.setImageResource(R.drawable.ic_sales_report);
        userProfile.setImageResource(R.drawable.ic_wallet_new);
        generateFirebaseToken();
       // Picasso.get().load(BaseUtil.getUserProfile(getApplicationContext())).error(R.drawable.ic_user).into(userProfile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_home:
                fragment = getSupportFragmentManager().findFragmentByTag("home");
                if (fragment != null && fragment.isVisible()) {

                } else {
                    changeFragment(new HomeFragment(), "home");
                    home.setImageResource(R.drawable.home_logo_color);
                    discover.setImageResource(R.drawable.ic_search_new);
                    upload.setImageResource(R.drawable.ic_upload_new);
                    salesReport.setImageResource(R.drawable.ic_sales_report);
                    userProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    discover.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    chatImg.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    salesReport.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                }
                break;
            case R.id.db_chat:
                //startActivity(new Intent(this, MessageList.class));
                fragment = getSupportFragmentManager().findFragmentByTag("chat");
                if (fragment != null && fragment.isVisible()) {

                } else {
                    changeFragment(new ChatFragment(), "chat");
                    home.setImageResource(R.drawable.home_logo);
                    discover.setImageResource(R.drawable.ic_search_new);
                    upload.setImageResource(R.drawable.ic_upload_new);
                    salesReport.setImageResource(R.drawable.ic_sales_report);
                    userProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    discover.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    chatImg.setImageTintList(ContextCompat.getColorStateList(this, R.color.tab_selected));
                    salesReport.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));

                }
                break;
            case R.id.db_discover:
                fragment = getSupportFragmentManager().findFragmentByTag("discover");
                if (fragment != null && fragment.isVisible()) {

                } else {
                    changeFragment(new DiscoverFragment(), "discover");
                    home.setImageResource(R.drawable.home_logo);
                    discover.setImageResource(R.drawable.ic_search_new);
                    upload.setImageResource(R.drawable.ic_upload_new);
                    salesReport.setImageResource(R.drawable.ic_sales_report);
                    userProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    discover.setImageTintList(ContextCompat.getColorStateList(this, R.color.tab_selected));
                    chatImg.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    salesReport.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                }
                break;
            case R.id.db_upload:
                startActivity(new Intent(this, UploadOne.class));

//                fragment = getSupportFragmentManager().findFragmentByTag("upload");
//                if (fragment != null && fragment.isVisible()) {
//                } else {
//                    changeFragment(new UploadFragment(), "upload");
//                    home.setImageResource(R.drawable.home_logo);
//                    discover.setImageResource(R.drawable.ic_search_new);
//                    upload.setImageResource(R.drawable.ic_upload_new);
//                    salesReport.setImageResource(R.drawable.ic_sales_report);
//                }
                break;
            case R.id.db_sales_report:
                fragment = getSupportFragmentManager().findFragmentByTag("sales");
                if (fragment != null && fragment.isVisible()) {

                } else {
                    changeFragment(new SalesReportFragment(), "sales");
                    home.setImageResource(R.drawable.home_logo);
                    discover.setImageResource(R.drawable.ic_search_new);
                    upload.setImageResource(R.drawable.ic_upload_new);
                    salesReport.setImageResource(R.drawable.ic_sales_report);
                    userProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    discover.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    chatImg.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    salesReport.setImageTintList(ContextCompat.getColorStateList(this, R.color.tab_selected));
                }
                break;
            case R.id.db_userprofile:
                fragment = getSupportFragmentManager().findFragmentByTag("profile");
                if (fragment != null && fragment.isVisible()) {

                } else {
                    //changeFragment(new UserProfileFragment(), "profile");
                    changeFragment(new WalletFragment(), "profile");
                    home.setImageResource(R.drawable.home_logo);
                    discover.setImageResource(R.drawable.ic_search_new);
                    upload.setImageResource(R.drawable.ic_upload_new);
                    salesReport.setImageResource(R.drawable.ic_sales_report);
                    userProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.tab_selected));
                    discover.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    chatImg.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                    salesReport.setImageTintList(ContextCompat.getColorStateList(this, R.color.icon_color));
                }
        }
    }

    public void setProfilePic(String imgUrl) {
        Picasso.get().load(BaseUtil.getUserProfile(getApplicationContext())).error(R.drawable.ic_user).into(userProfile);
    }

    private void changeFragment(Fragment fragment, String fragmentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        fragment = getSupportFragmentManager().findFragmentByTag("home");
        if (fragment != null && fragment.isVisible()) {
            new AlertDialog.Builder(DashboardScreen.this)
                    .setIcon(R.drawable.diiclae_color_logo)
                    .setTitle("Davincii")
                    .setCancelable(false)
                    .setMessage("Are you sure to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

//        private void setFragment(Fragment fragment) {
//        FragmentTransaction fragHome = getSupportFragmentManager().beginTransaction();
//        fragHome.replace(R.id.flFragment, fragment).commit();
//        FragmentTransaction fragDiscover = getSupportFragmentManager().beginTransaction();
//        fragDiscover.replace(R.id.flFragment, fragment).commit();
//        FragmentTransaction fragUpload = getSupportFragmentManager().beginTransaction();
//        fragUpload.replace(R.id.flFragment, fragment).commit();
//        FragmentTransaction fragSalesRport = getSupportFragmentManager().beginTransaction();
//        fragSalesRport.replace(R.id.flFragment, fragment).commit();
//        FragmentTransaction fragUserProfile = getSupportFragmentManager().beginTransaction();
//        fragUserProfile.replace(R.id.flFragment, fragment).commit();
//    }

    private void addDeviceData(){
        if (!BaseUtil.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
            return;
        }
        UpdateTokenRequest updateTokenRequest=new UpdateTokenRequest(fcmTokan,"android",device_id);
        apiInterface.updateToken("Bearer "+BaseUtil.getUserToken(getApplicationContext()),updateTokenRequest).enqueue(new Callback<UpdateTokenResponse>() {
            @Override
            public void onResponse(Call<UpdateTokenResponse> call, Response<UpdateTokenResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals(0)){

                    }else{

                    }
                }else{

                    }
            }
            @Override
            public void onFailure(Call<UpdateTokenResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateFirebaseToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        fcmTokan = task.getResult();
                        addDeviceData();
                    }
                });
    }

}