package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.online.davincii.R;
import com.online.davincii.models.LoginRequest;
import com.online.davincii.models.LoginResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private TextView forgotPassword, signUpLink;
    private LinearLayout signIn;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        context = UserLogin.this;
        initView();
        setOnclickListener();
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        email = findViewById(R.id.si_email);
        password = findViewById(R.id.si_password);
        forgotPassword = findViewById(R.id.si_forgot_password);
        signUpLink = findViewById(R.id.si_signup);
        signIn = findViewById(R.id.si_signin);
        generateFirebaseToken();
    }

    private void setOnclickListener() {
        forgotPassword.setOnClickListener(this);
        signUpLink.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.si_forgot_password:
                startActivity(new Intent(context, ForgotPassword.class));
                break;
            case R.id.si_signup:
                startActivity(new Intent(context, UserSignUp.class));
                break;
            case R.id.si_signin:
                if (email.getText().toString().isEmpty()) {
                    BaseUtil.showToast(context, "Enter your email");
                } else if (!email.getText().toString().matches(emailValidation)) {
                    BaseUtil.showToast(context, "Enter Valid email");
                } else if (password.getText().toString().isEmpty()) {
                    BaseUtil.showToast(context, "Enter your password");
                } else {
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
        LoginRequest request = new LoginRequest(email.getText().toString().trim(), password.getText().toString());
        apiInterface.userLogin(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    BaseUtil.showToast(context, response.body().getMessage());
                    if (response.body().getError().equals("0")) {
                        UserLogin.this.finishAffinity();
                        BaseUtil.putUserLogIn(UserLogin.this, true);
                        BaseUtil.putUserToken(UserLogin.this, response.body().getData());
                        BaseUtil.putCurrency(UserLogin.this, response.body().getCurrency());
                        BaseUtil.putUserAccount(UserLogin.this, response.body().getUserId());
                        BaseUtil.putUserName(getApplicationContext(),response.body().getUserName());
                        BaseUtil.putUserProfile(UserLogin.this, response.body().profile_picture);
                        saveToken(response.body().getUserId());
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Failed to connect with server");
                progress.hideProgressBar();
            }
        });
    }

    // TODO save firebase token to firebase db
    private void saveToken(String payload) {
        DatabaseReference c_reference;
        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(String.valueOf(payload));
        HashMap<String, String> data2 = new HashMap<>();
        data2.put("token", fcmToken);
        data2.put("type","A");
        startActivity(new Intent(UserLogin.this, DashboardScreen.class));
        finish();
        c_reference.setValue(data2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
                        fcmToken = task.getResult();
                    }
                });
    }
}