package com.online.davincii.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.online.davincii.R;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.GlobalProgressDialog;

public class Community extends AppCompatActivity implements View.OnClickListener {
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private ImageView backbtn;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
        setOnclickListener();
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(this);
        backbtn = findViewById(R.id.cm_backbtn);
        search = findViewById(R.id.cm_search);

    }

    private void setOnclickListener() {
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cm_backbtn:
                Community.super.onBackPressed();
                break;
        }
    }
}