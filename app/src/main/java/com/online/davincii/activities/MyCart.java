package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.braintreepayments.api.dropin.DropInActivity;
//import com.braintreepayments.api.dropin.DropInRequest;
//import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.online.davincii.R;
import com.online.davincii.adapters.AddressAdapter;
import com.online.davincii.adapters.MyCartAdapter;
import com.online.davincii.models.MyCartData;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.addresses.AddressRequest;
import com.online.davincii.models.addresses.AddressResponse;
import com.online.davincii.models.addresses.ListAddress;
import com.online.davincii.models.order.OrderRequest;
import com.online.davincii.models.order.OrderResponse;
import com.online.davincii.models.registger.RegisterResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCart extends AppCompatActivity implements View.OnClickListener {
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private RecyclerView fRecyclerView;
    private View view;
    private Context context;
    private TextView totalprice, placeorderbtn, subTotal, deliverCharges, deliveryAddress;
    private ImageView backbtn, noCartImage, editAddress;
    private List<MyCartData> myCartData = new ArrayList<>();
    private MyCartAdapter myCartAdapter;
    private int sizeId, creationId;
    private LinearLayout priceLayout;
    private String price;
    private List<ListAddress> listAddress = new ArrayList<>();
    private int selectPosition = 0;
    private int lastSelectPos = 0;
    private AddressAdapter adapter;
    private AlertDialog addDialog;
    private RecyclerView addRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        context = MyCart.this;
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
        setOnclickListener();
    }

    private void setOnclickListener() {
        placeorderbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        editAddress.setOnClickListener(this);
    }

    private void initView() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver, new IntentFilter("send_price"));
        sizeId = getIntent().getIntExtra("sizeId", 0);
        creationId = getIntent().getIntExtra("buyId", 0);
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(MyCart.this);
        myCartAdapter = new MyCartAdapter(myCartData);
        fRecyclerView = findViewById(R.id.rec_mycart);
        placeorderbtn = findViewById(R.id.mc_placeorderbtn);
        totalprice = findViewById(R.id.my_totalprice);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        fRecyclerView.setLayoutManager(linearLayoutManager);
        fRecyclerView.setHasFixedSize(true);
        fRecyclerView.setAdapter(myCartAdapter);
        backbtn = findViewById(R.id.my_backbtn);
        noCartImage = findViewById(R.id.mc_noCartImage);
        priceLayout = findViewById(R.id.mc_priceLayout);
        subTotal = findViewById(R.id.mc_SubtotalTextView);
        deliverCharges = findViewById(R.id.mc_deliverCharge);
        deliveryAddress = findViewById(R.id.amc_address);
        editAddress = findViewById(R.id.amc_edit_address);
        getCartItem();
        getAddressApi();
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("type")) {
                selectPosition = intent.getIntExtra("position", 0);
                adapter = new AddressAdapter(listAddress, selectPosition);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                addRv.setLayoutManager(manager);
                addRv.setAdapter(adapter);
            } else if (intent.hasExtra("delete")) {
                int position = intent.getIntExtra("position", 0);
                if (selectPosition == position) {
                    selectPosition = 0;
                }
                listAddress.remove(position);

                adapter = new AddressAdapter(listAddress, selectPosition);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                addRv.setLayoutManager(manager);
                addRv.setAdapter(adapter);
                if (listAddress.size() == 0) {
                    openAddAddressPopup(true, 0);
                    deliveryAddress.setText("");
                }
            } else if (intent.hasExtra("edit")) {
                int position = intent.getIntExtra("position", 0);
                openAddAddressPopup(false, position);
            } else {
                getCartItem();
            }
        }
    };

    private void getAddressApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        apiInterface.getAddress("Bearer " + BaseUtil.getUserToken(context)).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        listAddress = response.body().getAddress();
                        if (adapter != null) {
                            adapter = new AddressAdapter(listAddress, selectPosition);
                            LinearLayoutManager manager = new LinearLayoutManager(context);
                            addRv.setLayoutManager(manager);
                            addRv.setAdapter(adapter);
                        }
                        if (response.body().getAddress().size() > 0) {
                            ListAddress mAddess = response.body().getAddress().get(0);
                            String aAdd = mAddess.getName() + " " + mAddess.getAddress() + " " + mAddess.getBuildingname() + ", " + mAddess.getCity() + ", " + mAddess.getZip() + " " + mAddess.getState();
                            deliveryAddress.setText(aAdd);

                        } else {
                            openAddAddressPopup(true, 0);
                        }
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
            }
        });
    }

    private void openAddAddressPopup(boolean isNew, Integer position) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.add_new_address_dialog, null);
        AlertDialog sDialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();
        EditText name = dialogView.findViewById(R.id.ana_full_name);
        EditText phone = dialogView.findViewById(R.id.ana_number);
        EditText addressOne = dialogView.findViewById(R.id.ana_address_one);
        EditText addressTwo = dialogView.findViewById(R.id.ana_address_two);
        EditText city = dialogView.findViewById(R.id.ana_city);
        EditText state = dialogView.findViewById(R.id.ana_state);
        EditText zip = dialogView.findViewById(R.id.ana_zipcode);
        // EditText country = dialogView.findViewById(R.id.ana_country);
        ImageView close = dialogView.findViewById(R.id.ana_back);
        TextView save = dialogView.findViewById(R.id.ana_save);

        if (!isNew) {
            ListAddress mData = listAddress.get(position);
            name.setText(mData.getName());
            phone.setText(mData.getPhoneNumber());
            addressOne.setText(mData.getAddress());
            addressTwo.setText(mData.getBuildingname());
            city.setText(mData.getCity());
            state.setText(mData.getState());
            zip.setText(mData.getZip());
        }

        sDialog.show();
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    BaseUtil.showToast(context, "Enter full name");
                } else if (phone.getText().toString().trim().isEmpty()) {
                    BaseUtil.showToast(context, "Enter mobile number");
                } else if (phone.getText().toString().trim().length() < 10) {
                    BaseUtil.showToast(context, "Enter valid mobile number");
                } else if (addressOne.getText().toString().trim().isEmpty()) {
                    BaseUtil.showToast(context, "Enter address line one");
                } else if (city.getText().toString().trim().isEmpty()) {
                    BaseUtil.showToast(context, "Enter city");
                } else if (state.getText().toString().trim().isEmpty()) {
                    BaseUtil.showToast(context, "Enter state/province/region");
                } else if (zip.getText().toString().trim().isEmpty()) {
                    BaseUtil.showToast(context, "Enter zip code");
                } else {
                    if (isNew) {
                        callAddAddressApi(
                                name.getText().toString().trim(),
                                phone.getText().toString().trim(),
                                addressOne.getText().toString().trim(),
                                addressTwo.getText().toString().trim(),
                                city.getText().toString().trim(),
                                state.getText().toString().trim(),
                                zip.getText().toString().trim(),
                                sDialog
                        );
                    } else {
                        editAddressApi(
                                isNew,
                                listAddress.get(position).getId(),
                                name.getText().toString().trim(),
                                phone.getText().toString().trim(),
                                addressOne.getText().toString().trim(),
                                addressTwo.getText().toString().trim(),
                                city.getText().toString().trim(),
                                state.getText().toString().trim(),
                                zip.getText().toString().trim(),
                                sDialog
                        );
                    }
                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
    }

    private void editAddressApi(boolean isNew, Integer id, String name, String phone, String addOne, String addTwo, String city, String state, String zip, AlertDialog sDialog) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        AddressRequest request = new AddressRequest(
                id,
                name,
                addOne,
                phone,
                addTwo,
                state,
                city,
                zip
        );

        progress.showProgressBar();
        apiInterface.editAddress("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        sDialog.dismiss();
                        getAddressApi();
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });
    }

    private void callAddAddressApi(String name, String phone, String addOne, String addTwo, String city, String state, String zip, AlertDialog sDialog) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        AddressRequest request = new AddressRequest(
                name,
                addOne,
                phone,
                addTwo,
                state,
                city,
                zip
        );

        progress.showProgressBar();
        apiInterface.addAddress("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        sDialog.dismiss();
                        getAddressApi();
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });

    }


    private void addressPopup() {

        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.popup_address_list, null);
        addDialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();
        TextView con = dialogView.findViewById(R.id.pal_continue);
        TextView addNewAddress = dialogView.findViewById(R.id.pal_add_new);
        ImageView close = dialogView.findViewById(R.id.pal_close);
        addRv = dialogView.findViewById(R.id.pal_address_rv);

        adapter = new AddressAdapter(listAddress, lastSelectPos);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        addRv.setLayoutManager(manager);
        addRv.setAdapter(adapter);

        addDialog.show();
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAddressPopup(true, 0);
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listAddress.size() > 0) {
                    lastSelectPos = selectPosition;
                    ListAddress mAddess = listAddress.get(lastSelectPos);
                    String aAdd = mAddess.getName() + " " + mAddess.getAddress() + " " + mAddess.getBuildingname() + ", " + mAddess.getCity() + ", " + mAddess.getZip() + " " + mAddess.getState();
                    deliveryAddress.setText(aAdd);
                    addDialog.dismiss();
                } else {
                    addDialog.dismiss();
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
    }

    private void getCartItem() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        if (myCartData.size() > 0) {
            myCartData.clear();
            myCartAdapter.notifyDataSetChanged();
        }

        apiInterface.myCartItems("Bearer " + BaseUtil.getUserToken(MyCart.this)).enqueue(new Callback<MyCartResponse>() {
            @Override
            public void onResponse(Call<MyCartResponse> call, Response<MyCartResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        subTotal.setText(BaseUtil.getCurrency(context) + String.format("%.2f", Double.valueOf(response.body().getSubTotal())));
                        deliverCharges.setText(BaseUtil.getCurrency(context) + String.format("%.2f", Double.valueOf(response.body().getDeliveryCharge())));
                        totalprice.setText(BaseUtil.getCurrency(context) + String.format("%.2f", response.body().getTotalPrice()));
                        if (response.body().getCartItems().size() > 0) {
                            myCartData.addAll(response.body().getCartItems());
                            myCartAdapter.notifyDataSetChanged();
                            priceLayout.setVisibility(View.VISIBLE);
                        } else {
                            priceLayout.setVisibility(View.GONE);
                            fRecyclerView.setVisibility(View.GONE);
                            noCartImage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {

                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<MyCartResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, "Failed to connect with server");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mc_placeorderbtn:
                if (listAddress.size() > 0) {
                    DropInRequest dropInRequest = new DropInRequest()
                            .vaultCard(true)
                            .collectDeviceData(true)
                            .vaultManager(true)

                            .tokenizationKey(Constant.TOKENIZER_KEY);
                    startActivityForResult(dropInRequest.getIntent(this), 101);
                } else {
                    openAddAddressPopup(true, 0);
                }
                break;
            case R.id.my_backbtn:
                finish();
                break;
            case R.id.amc_edit_address:
                addressPopup();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String nonce = result.getPaymentMethodNonce().getNonce();
                orderApi(nonce, listAddress.get(lastSelectPos).getId());
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    private void orderApi(String nonce, int add_id) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }

        progress.showProgressBar();
        OrderRequest request = new OrderRequest(add_id, nonce);
        apiInterface.placeOrder("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        BaseUtil.showToast(context, response.body().getMessage());
                        startActivity(new Intent(context, OrderConfirmation.class).putExtra("order_id", response.body().getData().getId()));
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
}
