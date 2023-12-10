package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.online.davincii.R;
import com.online.davincii.adapters.BuyCreationAdapter;
import com.online.davincii.models.AddCartResponse;
import com.online.davincii.models.BuyCreationCanvasSize;
import com.online.davincii.models.BuyCreationData;
import com.online.davincii.models.BuyCreationRequest;
import com.online.davincii.models.BuyCreationResponse;

import com.online.davincii.models.MyCartRequest;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.order.NFTRequest;
import com.online.davincii.models.order.OrderRequest;
import com.online.davincii.models.order.OrderResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArtPiece extends AppCompatActivity implements View.OnClickListener {
    private ImageView shopCart, mailBox, mainImg, mainImg1, mainImg2;
    private TextView username, descprition, vert_Horiz, dgt_downdAll, cancel, addToCart, cart_count;
    private RecyclerView rv_addSizes;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private List<BuyCreationData> dataList = new ArrayList<>();
    private List<BuyCreationCanvasSize> canvasSizeList = new ArrayList<>();
    private BuyCreationAdapter buyAdapter;
    private int buyId, canvasType, sizeId, position;
    private String buyImg;
    private BuyCreationData buyCreationData;
    private Integer selectedSize = -1;
    private String downloadLocation;
    private int isNft = 0;
    private LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_art_piece);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("send_info"));

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initialize();
        setOnClickListener();
    }

    private void setOnClickListener() {
        cancel.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        shopCart.setOnClickListener(this);
        dgt_downdAll.setOnClickListener(this);
        mailBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_mail:
                startActivity(new Intent(context, MessageList.class));
                break;
            case R.id.add_cacel_txt:
                finish();
                break;
            case R.id.add_addToCart_txt:
//                if (addToCart.equals("GO TO CART")){
//                    startActivity(new Intent(AddArtPiece.this,MyCart.class));
//                }
                if (sizeId == 0) {
                    Toast.makeText(context, "Please select size", Toast.LENGTH_SHORT).show();
                } else {
                    addItemInCart();
                }
                break;
            case R.id.add_shpcart:
                startActivity(new Intent(AddArtPiece.this, MyCart.class));
                break;
            case R.id.add_dwnld_txt:
               // downloadNtf();
                if(isNft == 0) {
                    payForNFT();
                }else{
                    downloadNtf();
                }
                break;
        }
    }

    private void downloadNtf() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // this will request for permission when user has not granted permission for the app
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        else{
            //Download Script
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadLocation);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Davincii_" + System.currentTimeMillis());
            downloadManager.enqueue(request);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(main, "Download Art Piece Successfully", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }, 1000);

        }
    }

    private void payForNFT() {
        DropInRequest dropInRequest = new DropInRequest()
                .vaultCard(true)
                .collectDeviceData(true)
                .allowVaultCardOverride(true)
                .vaultManager(true)
                .tokenizationKey(Constant.TOKENIZER_KEY);
        startActivityForResult(dropInRequest.getIntent(this), 101);
    }

    private void initialize() {
        context = AddArtPiece.this;
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        rv_addSizes = findViewById(R.id.add_recyclerView);
        shopCart = findViewById(R.id.add_shpcart);
        mailBox = findViewById(R.id.add_mail);
        mainImg = findViewById(R.id.add_main_image);
        mainImg1 = findViewById(R.id.add_main_image1);
        mainImg2 = findViewById(R.id.add_main_image2);
        username = findViewById(R.id.add_username);
        descprition = findViewById(R.id.add_descpon);
        vert_Horiz = findViewById(R.id.add_Vert_Hor);
        dgt_downdAll = findViewById(R.id.add_dwnld_txt);
        cancel = findViewById(R.id.add_cacel_txt);
        addToCart = findViewById(R.id.add_addToCart_txt);
        cart_count = findViewById(R.id.aap_count);
        main = findViewById(R.id.add_main);

        rv_addSizes = findViewById(R.id.add_recyclerView);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rv_addSizes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //  rv_addSizes.setLayoutManager(linearLayoutManager);
        rv_addSizes.setHasFixedSize(true);

        buyId = getIntent().getIntExtra("buyId", 0);
        canvasType = getIntent().getIntExtra("canvasId", 0);
        buyImg = getIntent().getStringExtra("imgId");

        if (canvasType == 0) {
            findViewById(R.id.add_saquareLay).setVisibility(View.VISIBLE);
            Picasso.get().load(buyImg).resize(900, 900).into(mainImg1);
            //Glide.with(this).load(buyImg).error(R.drawable.diiclae_color_logo).into(mainImg1);
            findViewById(R.id.add_landscapLay).setVisibility(View.GONE);
            findViewById(R.id.add_portraitLay).setVisibility(View.GONE);
        } else if (canvasType == 1) {
            findViewById(R.id.add_saquareLay).setVisibility(View.GONE);
            findViewById(R.id.add_landscapLay).setVisibility(View.VISIBLE);
            Picasso.get().load(buyImg).resize(900, 900).into(mainImg2);
            findViewById(R.id.add_portraitLay).setVisibility(View.GONE);
        } else {
            findViewById(R.id.add_saquareLay).setVisibility(View.GONE);
            findViewById(R.id.add_landscapLay).setVisibility(View.GONE);
            findViewById(R.id.add_portraitLay).setVisibility(View.VISIBLE);
            Picasso.get().load(buyImg).resize(900, 900).into(mainImg);

        }

        callBuyCreationApi();
    }

    private void callBuyCreationApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            return;
        }
        progress.showProgressBar();
        BuyCreationRequest request = new BuyCreationRequest(buyId);
        apiInterface.buyCreation("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<BuyCreationResponse>() {
            @Override
            public void onResponse(Call<BuyCreationResponse> call, Response<BuyCreationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if(response.body().getNft()){
                            isNft = 1;
                        }else{
                            isNft = 0;
                        }
                        downloadLocation = Constant.PROFILE_IMG_BASE + response.body().getData().pictureUrl;
                        username.setText(response.body().getData().getTitle());
                        descprition.setText(response.body().getData().getDescription());
                        vert_Horiz.setText(response.body().getData().canvasType + " pieces");
                        canvasSizeList = response.body().getCanvassizes();
                        buyAdapter = new BuyCreationAdapter(canvasSizeList, getApplicationContext(), selectedSize);
                        rv_addSizes.setAdapter(buyAdapter);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progress.hideProgressBar();
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyCreationResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItemInCart() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        MyCartRequest request = new MyCartRequest(buyId, sizeId);

        apiInterface.addItems("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        BaseUtil.showToast(context, response.body().getMessage());
                        Intent intent = new Intent(AddArtPiece.this, MyCart.class);
                        intent.putExtra("buyId", buyId);
                        intent.putExtra("sizeId", sizeId);
                        startActivity(intent);
//                        addToCart.setText("GO TO CART");
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.hideProgressBar();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Failed to connect with server");
                progress.hideProgressBar();
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sizeId = intent.getIntExtra("sizeId", 0);
            position = intent.getIntExtra("position", 0);
            position = sizeId;
            buyAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void getCartItem() {
        cart_count.setVisibility(View.GONE);
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        apiInterface.myCartItems("Bearer " + BaseUtil.getUserToken(context)).enqueue(new Callback<MyCartResponse>() {
            @Override
            public void onResponse(Call<MyCartResponse> call, Response<MyCartResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        cart_count.setVisibility(View.VISIBLE);
                        cart_count.setText(String.valueOf(response.body().getCartItems().size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<MyCartResponse> call, Throwable t) {


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String nonce = result.getPaymentMethodNonce().getNonce();
                orderApi(nonce);
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    private void orderApi(String nonce) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        progress.showProgressBar();
        NFTRequest request = new NFTRequest(buyId, nonce);
        apiInterface.nftOrder("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        BaseUtil.showToast(context, response.body().getMessage());
                        isNft = 1;
                        downloadNtf();
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });
    }

    @Override
    public void onResume() {
        getCartItem();
        super.onResume();
    }

}