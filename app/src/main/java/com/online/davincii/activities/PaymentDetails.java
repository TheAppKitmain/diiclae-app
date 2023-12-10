package com.online.davincii.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.online.davincii.R;
public class PaymentDetails extends AppCompatActivity {

    private LinearLayout payPal;
    private String price;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        initViews(PaymentDetails.this);
    }

    private void initViews(Context context){
         payPal=findViewById(R.id.pd_payPalLayout);
         back=findViewById(R.id.pd_back);

         price=getIntent().getStringExtra("amount");
         payPal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(PaymentDetails.this, Payment.class).putExtra("amount",price));
             }
         });

         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 PaymentDetails.super.onBackPressed();
             }
         });

    }
}