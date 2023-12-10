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
import com.online.davincii.models.ResetPasswordRequest;
import com.online.davincii.models.ResetPasswordResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText password, confirmpassword;
    private TextView submitbtn;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        context = ResetPassword.this;
        initView();
        setOnclickListener();
    }
    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        confirmpassword = findViewById(R.id.up_confirmpassword);
        password = findViewById(R.id.up_password);
        submitbtn = findViewById(R.id.up_submit);
    }
    private void setOnclickListener() {

        submitbtn.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_submit:
                if (valid()) {
                    callSignInApi();
                }
                break;
        }
    }


    private void callSignInApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        ResetPasswordRequest request = new ResetPasswordRequest(getIntent().getStringExtra("userId"), password.getText().toString());
        apiInterface.updatePassword(request).enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        startActivity(new Intent(context, UserLogin.class));
                        finishAffinity();
                    }
                    BaseUtil.showToast(context, response.body().getMessage());
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Server error");
                progress.hideProgressBar();
            }
        });
    }
    private boolean valid() {
        if (password.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter your password");
            return false;
        } else if (password.getText().toString().length() < 6) {
            BaseUtil.showToast(context, "Password length must be 6");
            return false;
        } else if (confirmpassword.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Re enter your password");
            return false;
        } else if (confirmpassword.getText().toString().length() < 6) {
            BaseUtil.showToast(context, "Password length must be 6");
            return false;
//        } else if (!password.getText().toString().trim().matches(passRegex)) {
//            BaseUtil.showToast(context, "Password should contain at least one uppercase letter, one lowercase latter and one number");
//            return false;
        } else if (!confirmpassword.getText().toString().equals(password.getText().toString())) {
            BaseUtil.showToast(context, "Confirm password doesn't match");
            return false;
        }
        return true;
    }
}
