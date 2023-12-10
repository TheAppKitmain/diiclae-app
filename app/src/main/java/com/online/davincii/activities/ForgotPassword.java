package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.online.davincii.R;
import com.online.davincii.models.ForgotRequest;
import com.online.davincii.models.ForgotResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private TextView sumitbtn;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        context = ForgotPassword.this;
        initView();
        setOnclickListener();

    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        email = findViewById(R.id.fp_email);
        sumitbtn = findViewById(R.id.fp_subimt);
    }

    private void setOnclickListener() {
        sumitbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fp_subimt:
                if (valid()) {
                    callForgotPasswordApi();
                }
                break;
        }
    }

    private void callForgotPasswordApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        ForgotRequest request = new ForgotRequest(email.getText().toString().trim());
        apiInterface.forgetPassword(request).enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                if (response.isSuccessful()) {
                    BaseUtil.showToast(context, response.body().getMessage());
                    if (response.body().getError().equals("0")) {
                        Intent intent=new Intent(context,VerificationCode.class);
                        intent.putExtra("email",email.getText().toString().trim());
                        startActivity(intent);
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Failed to connect with server");
                progress.hideProgressBar();
            }
        });
    }

    private boolean valid() {
        String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter your email");
            return false;
        } else if (!email.getText().toString().trim().matches(emailValidation)) {
            BaseUtil.showToast(context, "Enter Valid email");
            return false;
        }
        return true;
    }
}