package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.models.OrderListData;
import com.online.davincii.models.OrderListResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderList extends AppCompatActivity {

    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private RecyclerView recyclerView;
    private ImageView od_backBtn;
    private List<OrderListData> oList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initViews(OrderList.this);
    }

    private void initViews(Context context){
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(OrderList.this);
        recyclerView=findViewById(R.id.ol_recyclerView);
        od_backBtn=findViewById(R.id.od_backBtn);
        getOrderList();

        od_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList.super.onBackPressed();
            }
        });
    }

    private void getOrderList(){
        if (!BaseUtil.isNetworkAvailable(OrderList.this)) {
            BaseUtil.showToast(OrderList.this, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        apiInterface.getOrderList("Bearer "+BaseUtil.getUserToken(getApplicationContext())).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                   if (response.isSuccessful()){
                       if (response.body().getError().equals("0")){
                        oList.addAll(response.body().getOrders());
                        OrderListAdapter orderListAdapter=new OrderListAdapter(oList);
                        recyclerView.setAdapter(orderListAdapter);
                       }else{
                           Toast.makeText(OrderList.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       Toast.makeText(OrderList.this, "Server Error", Toast.LENGTH_SHORT).show();
                   }
                   progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(OrderList.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    //todo======================Order list Adapter================================

    public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{

        private List<OrderListData> mList;

        public OrderListAdapter(List<OrderListData> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_history,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
            holder.orderNumber.setText(String.valueOf(mList.get(position).getOrderid()));
            if (mList.get(position).getTotalAmt()!= null){

                holder.total.setText(BaseUtil.getCurrency(getApplicationContext()) + String.format("%.2f", Double.valueOf(mList.get(position).getTotalAmt())));
            }
            holder.orderDate.setText(mList.get(position).getOrderdate());


            if (mList.get(position).getStatus().equals("0")){
                holder.status.setText("Pending");
            }else if (mList.get(position).getStatus().equals("1")){
                holder.status.setText("Approved");
            }else{
                holder.status.setText("Completed");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(holder.itemView.getContext(),OrderDetail.class);
                    intent.putExtra("order_id",mList.get(position).getOrderid());
                    holder.itemView.getContext().startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView orderNumber,total,orderDate,status;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                orderNumber=itemView.findViewById(R.id.orderNoTextView);
                total=itemView.findViewById(R.id.orderTotalAmount);
                orderDate=itemView.findViewById(R.id.orderDeliveredDate);
                status=itemView.findViewById(R.id.orderStatus);
            }
        }
    }

}