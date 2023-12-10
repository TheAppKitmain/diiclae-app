package com.online.davincii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.online.davincii.activities.DashboardScreen;
import com.online.davincii.activities.UserLogin;
import com.online.davincii.models.registger.ImageData;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        context = SplashScreen.this;
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);

        //hideSystemUI();
//        if(!BaseUtil.getSaveDetails(context)) {
//            imageData();
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (BaseUtil.getUserLogIn(SplashScreen.this)) {
                    intent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } else {
                    intent = new Intent(SplashScreen.this, UserLogin.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }

            }
        }, 3500);
    }

//    private void imageData() {
//        if (!BaseUtil.isNetworkAvailable(context)) {
//            BaseUtil.showToast(context, "Check your internet connectivity");
//            return;
//        }
//        this.apiInterface.getImageData().enqueue(new Callback<ImageData>() {
//            @Override
//            public void onResponse(Call<ImageData> call, Response<ImageData> response) {
//                if (response.isSuccessful()) {
//                    BaseUtil.showToast(context, response.body().getMessage());
//                    if (response.body().getError().equals("0")) {
//                        BaseUtil.putImageKey(context, response.body().getS3KEY());
//                        BaseUtil.putImageSec(context, response.body().getSecret());
//                        BaseUtil.putSaveDetails(context, true);
//                    }
//                } else {
//                    BaseUtil.showToast(context, "Server error");
//                }
//            }
//            @Override
//            public void onFailure(Call<ImageData> call, Throwable t) {
//                BaseUtil.showToast(context, "Failed to connect with server");
//            }
//        })
//  }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}