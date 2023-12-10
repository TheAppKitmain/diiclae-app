package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.adapters.SoldProductAdapter;
import com.online.davincii.models.SoldProductData;
import com.online.davincii.models.SoldProductResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoldProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiClient.APIInterface apiInterface;
    private SoldProductAdapter soldProductAdapter;
    private List<SoldProductData> dataList=new ArrayList<>();
    private GlobalProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_product);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        apiInterface = ApiClient.getClient();
        recyclerView=findViewById(R.id.sp_recycler);
        progress=new GlobalProgressDialog(SoldProductActivity.this);

        findViewById(R.id.sp_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("notification")){
                    SoldProductActivity.super.onBackPressed();
                }else{
                    SoldProductActivity.super.onBackPressed();
                }

            }
        });
        getSoldItemList();
    }

    private void getSoldItemList(){
        if (!BaseUtil.isNetworkAvailable(this)){
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
            return;
        }
         progress.showProgressBar();

        apiInterface.getSoldItem("Bearer "+BaseUtil.getUserToken(SoldProductActivity.this)).enqueue(new Callback<SoldProductResponse>() {
            @Override
            public void onResponse(Call<SoldProductResponse> call, Response<SoldProductResponse> response) {
               if (response.isSuccessful()){
                   if (response.body().getError().equals("0")){
                         dataList.addAll(response.body().getData());
                         soldProductAdapter=new SoldProductAdapter(dataList);
                         recyclerView.setAdapter(soldProductAdapter);
                   }else{
                       Toast.makeText(SoldProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
               progress.hideProgressBar();

            }

            @Override
            public void onFailure(Call<SoldProductResponse> call, Throwable t) {
                     progress.hideProgressBar();
                Toast.makeText(SoldProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}