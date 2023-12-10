package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.online.davincii.R;
import com.online.davincii.models.LogOutResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private ImageView backBtn;
    private LinearLayout needHelp, findFriends, ordersList,soldItem;
    private TextView link, logOut,deleteAccount;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private SwitchCompat changeTheme;
    private int pick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_settings);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
        setOnCLickListener();
        findViewById(R.id.st_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        backBtn = findViewById(R.id.st_back);
        needHelp = findViewById(R.id.st_needhelp);
        findFriends = findViewById(R.id.st_findfriends);
        link = findViewById(R.id.st_link);
        logOut = findViewById(R.id.st_logout);
        ordersList=findViewById(R.id.st_orderList);
        soldItem=findViewById(R.id.st_sold);
        deleteAccount=findViewById(R.id.st_delete_account);
        changeTheme= findViewById(R.id.st_theme_btn);

    }

    private void setOnCLickListener() {
        backBtn.setOnClickListener(this);
        logOut.setOnClickListener(this);
        needHelp.setOnClickListener(this);
        findFriends.setOnClickListener(this);
        ordersList.setOnClickListener(this);
        soldItem.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);
        changeTheme.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.st_logout:
                logOutPopup();
                break;
            case R.id.st_needhelp:
                startActivity(new Intent(getApplicationContext(), SupportActivity.class));
                break;
            case R.id.st_findfriends:
                startActivity(new Intent(getApplicationContext(), Community.class));
                break;
            case R.id.st_orderList:
                startActivity(new Intent(getApplicationContext(),OrderList.class));
                break;
            case R.id.st_sold:
                startActivity(new Intent(getApplicationContext(),SoldProductActivity.class));
                break;
            case R.id.st_delete_account:
                deletePopup();
                break;
            case R.id.st_theme_btn:
                if (changeTheme.isChecked()){
                   // new Theme().changeTheme(Settings.this, pick);
                }
                break;

        }
    }

    private void deletePopup() {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.delete_dialog, null);
        AlertDialog sDialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();
        TextView cancel = dialogView.findViewById(R.id.da_nobtn);
        TextView yes = dialogView.findViewById(R.id.da_yesbtn);
        sDialog.show();
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sDialog.dismiss();
                deleteAccount();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });

    }

    private void logOutPopup() {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog_logout, null);
        AlertDialog sDialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();
        TextView cancel = dialogView.findViewById(R.id.di_nobtn);
        TextView yes = dialogView.findViewById(R.id.di_yesbtn);
        sDialog.show();
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sDialog.dismiss();
                logoutApi();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });

    }

    private void deleteAccount(){
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        apiInterface.deleteAccount("Bearer " + BaseUtil.getUserToken(getApplicationContext())).enqueue(new Callback<LogOutResponse>() {
            @Override
            public void onResponse(Call<LogOutResponse> call, Response<LogOutResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        DatabaseReference c_reference;
                        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(BaseUtil.getUserAccountId(getApplicationContext()));
                        c_reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid){
                                BaseUtil.signOut(context);
                                finishAffinity();
                                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<LogOutResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });

    }


    private void logoutApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        apiInterface.logOut("Bearer " + BaseUtil.getUserToken(getApplicationContext())).enqueue(new Callback<LogOutResponse>() {
            @Override
            public void onResponse(Call<LogOutResponse> call, Response<LogOutResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        DatabaseReference c_reference;
                        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(BaseUtil.getUserAccountId(getApplicationContext()));
                        c_reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid){
                                BaseUtil.signOut(context);
                                finishAffinity();
                                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<LogOutResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });
    }
}