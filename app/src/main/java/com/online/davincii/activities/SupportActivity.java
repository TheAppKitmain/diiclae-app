package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.davincii.R;
import com.online.davincii.models.SupportResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        initView();
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(this);
        backbtn = findViewById(R.id.sp_back);
        getSupportDesc();
        setOnclickListener();
    }

    private void setOnclickListener() {
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_back:
                SupportActivity.super.onBackPressed();
                break;
        }
    }

    private void getSupportDesc() {
        if (!BaseUtil.isNetworkAvailable(this)) {
            BaseUtil.showToast(this, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        apiInterface.getSupport().enqueue(new Callback<SupportResponse>() {
            @Override
            public void onResponse(Call<SupportResponse> call, Response<SupportResponse> response) {
                if (response.isSuccessful()) {
                  //  BaseUtil.showToast(SupportActivity.this, response.body().getError());
                    if (response.body().getError().equals("0")) {
                        if (!TextUtils.isEmpty(response.body().getData())) {
                            ((TextView) findViewById(R.id.sp_textone)).setText(Html.fromHtml(response.body().getData()));
                        }
                    }
                } else {
                    BaseUtil.showToast(SupportActivity.this, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<SupportResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(SupportActivity.this, "Failed to connect with server");

            }
        });
    }
}