package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.cognitoidentity.model.AmbiguousRoleResolutionType;
import com.online.davincii.R;
import com.online.davincii.adapters.OrderDataAdapter;
import com.online.davincii.models.OrderCart;
import com.online.davincii.models.OrderDetailData;
import com.online.davincii.models.OrderDetailRequest;
import com.online.davincii.models.OrderDetailResponse;
import com.online.davincii.models.order.OrderResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmation extends AppCompatActivity {

    private int id;
    private TextView date, expectedDate, userName, address, phone, subTotal, deliveryCharge, total,orderId;
    private RecyclerView recyclerView;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private OrderDataAdapter orderDataAdapter;
    private List<OrderCart> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        initViews(OrderConfirmation.this);

    }

    private void initViews(Context context) {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(OrderConfirmation.this);
        id = getIntent().getIntExtra("order_id", 0);
        date = findViewById(R.id.oc_date);
        expectedDate = findViewById(R.id.oc_expectedDate);
        userName = findViewById(R.id.oc_username);
        address = findViewById(R.id.oc_address);
        subTotal = findViewById(R.id.oc_subTotal);
        phone = findViewById(R.id.oc_phone);
        deliveryCharge = findViewById(R.id.oc_deliveryCharge);
        total = findViewById(R.id.oc_total);
        orderId=findViewById(R.id.oc_orderId);
        recyclerView = findViewById(R.id.oc_recyclerView);

        findViewById(R.id.oc_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(context, DashboardScreen.class));
            }
        });

        getOrderData();
    }

    private void getOrderData() {
        if (!BaseUtil.isNetworkAvailable(OrderConfirmation.this)) {
            BaseUtil.showToast(OrderConfirmation.this, "Check your internet connectivity");
            return;
        }

        OrderDetailRequest request = new OrderDetailRequest(id);
        apiInterface.orderDetail("Bearer " +BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        orderId.setText("Order Id: " + String.valueOf(response.body().getData().getId()));
                        userName.setText(response.body().getAddress().getName());
                        address.setText(response.body().getAddress().getAddress() + "\n" + response.body().getAddress().getBuildingname()
                                + response.body().getAddress().getCity() + "\n" + response.body().getAddress().getState()
                                + "\n" + response.body().getAddress().getZip());
                        phone.setText("Mobile: "+ response.body().getAddress().getPhoneNumber());

                        deliveryCharge.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getDeliveryCharge())));
                        subTotal.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getSubTotal())));
                        total.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(response.body().getData().getTotal())));

                        date.setText(response.body().getCart().get(0).getOrderdate());
                        expectedDate.setText("Expected by: " + response.body().getCart().get(0).getDeliverydate());

                        mList = response.body().getCart();
                        orderDataAdapter = new OrderDataAdapter(mList);
                        recyclerView.setAdapter(orderDataAdapter);
                    } else {
                        Toast.makeText(OrderConfirmation.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderConfirmation.this,"Server Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(OrderConfirmation.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(OrderConfirmation.this, DashboardScreen.class));
    }
}