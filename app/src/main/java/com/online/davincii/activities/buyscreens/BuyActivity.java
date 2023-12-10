package com.online.davincii.activities.buyscreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.online.davincii.R;
import com.online.davincii.activities.AddArtPiece;
import com.online.davincii.utils.Constant;

public class BuyActivity extends AppCompatActivity {

    private ImageView backImg, files;
    private TextView acquire, buyShare;
    private BottomSheetDialog bottomSheetDialog, bottomSheetDialog1;
    private TextView cancelAcquire, cancelBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
    }

    private void initView(){
        backImg=findViewById(R.id.ba_back);
        files=findViewById(R.id.ab_file);
        acquire=findViewById(R.id.ba_acquire);
        buyShare=findViewById(R.id.ba_buyShare);


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyActivity.this, ArtDetail.class));
            }
        });

        acquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(BuyActivity.this);
                View sheetView = getLayoutInflater().inflate(R.layout.acuire_sheet, null);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                bottomSheetDialog.show();

                cancelAcquire=sheetView.findViewById(R.id.as_cancel);
                cancelAcquire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                TextView next=sheetView.findViewById(R.id.as_next);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BuyActivity.this, AddArtPiece.class);
                        intent.putExtra("buyId", getIntent().getIntExtra("buyId",0));
                        intent.putExtra("canvasId", getIntent().getIntExtra("canvasId", 0));
                        intent.putExtra("imgId", getIntent().getStringExtra("imgId"));
                        startActivity(intent);
                    }
                });


            }
        });

        buyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1 = new BottomSheetDialog(BuyActivity.this);
                View sheetView = getLayoutInflater().inflate(R.layout.share_sheet, null);
                bottomSheetDialog1.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                bottomSheetDialog1.show();

                cancelBuy=sheetView.findViewById(R.id.ss_cancel);
                cancelBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog1.dismiss();
                    }
                });
            }
        });
    }



}