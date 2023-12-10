package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.adapters.OrderDataAdapter;
import com.online.davincii.models.OrderCart;
import com.online.davincii.models.OrderDetailRequest;
import com.online.davincii.models.OrderDetailResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView total,subTotal,deliverCharge,orderNumber;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private OrderDataAdapter orderDataAdapter;
    private List<OrderCart> mList = new ArrayList<>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        initViews(OrderDetail.this);
    }

    private void initViews(Context context){
        id = getIntent().getIntExtra("order_id", 0);
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(OrderDetail.this);
        recyclerView=findViewById(R.id.orderDetail_recyclerView);
        total=findViewById(R.id.grand_Total_price_TextView);
        subTotal=findViewById(R.id.order_Subtotal_Price_textView);
        deliverCharge=findViewById(R.id.deliveryCharge_textView);
        orderNumber=findViewById(R.id.orderNo_textView);

        findViewById(R.id.odd_backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OrderDetail.super.onBackPressed();
            }
        });
        getOrderDetail();
    }

    private void getOrderDetail(){
        if (!BaseUtil.isNetworkAvailable(OrderDetail.this)) {
            BaseUtil.showToast(OrderDetail.this, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();

        OrderDetailRequest request = new OrderDetailRequest(id);
        apiInterface.orderDetail("Bearer " +BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        orderNumber.setText("Order Number "+String.valueOf(response.body().getData().getId()));

                        deliverCharge.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getDeliveryCharge())));
                        subTotal.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getSubTotal())));
                        total.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getTotal())));

                        mList = response.body().getCart();
                        orderDataAdapter = new OrderDataAdapter(mList);
                        recyclerView.setAdapter(orderDataAdapter);
                    } else {
                        Toast.makeText(OrderDetail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetail.this,"Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(OrderDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}