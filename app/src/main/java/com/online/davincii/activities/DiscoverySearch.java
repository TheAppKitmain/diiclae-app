package com.online.davincii.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;
import com.online.davincii.adapters.DiscoverySearchPieces;
import com.online.davincii.adapters.StudioSearchAdapter;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.PieceSearch;
import com.online.davincii.models.SearchRequest;
import com.online.davincii.models.SearchResponse;
import com.online.davincii.models.StudioSearch;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverySearch extends AppCompatActivity {
    private EditText serchView;
    private ImageView cartImg, backBtn;
    private RecyclerView rvArtist, rvArtPieces;
    private Context context;
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;
    private List<PieceSearch> pieceSearches = new ArrayList<>();
    private List<StudioSearch> studioSearches = new ArrayList<>();
    private TextView cart_count, topStudio, artForU, topStudioRecomd, artForURecomd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discoverysearch);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        inti();
    }

    private void inti() {
        context = DiscoverySearch.this;
        progress = new GlobalProgressDialog(context);
        apiInterface = ApiClient.getClient();
        serchView = findViewById(R.id.dc_searchView);
        rvArtist = findViewById(R.id.dc_rv_artist);
        rvArtist.setHasFixedSize(true);
        rvArtPieces = findViewById(R.id.dc_rv_artPieces);
        cart_count = findViewById(R.id.fds_count);
        topStudioRecomd = findViewById(R.id.dc_topstudiotxtrecomd);
        artForURecomd = findViewById(R.id.dc_artfurecomd);
        topStudio = findViewById(R.id.dc_topstudiotxt);
        artForU = findViewById(R.id.dc_artfutxt);
        backBtn = findViewById(R.id.dc_backbtn);

        StudioSearchAdapter searchAdapter = new StudioSearchAdapter(studioSearches);
        rvArtist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvArtist.setAdapter(searchAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverySearch.super.onBackPressed();
            }
        });


        cart_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MyCart.class));
            }
        });

        serchView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //Toast.makeText(context, "Searching", Toast.LENGTH_SHORT).show();
                    if (serchView.getText().toString().isEmpty()) {
                        BaseUtil.showToast(context, "Please write some text in searchview");
                    } else
                        callSearchApi(serchView.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void callSearchApi(String search) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        SearchRequest request = new SearchRequest(search);
        apiInterface.callSearch("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {

                        if (response.body().getRecommended().equalsIgnoreCase("1")) {

                            topStudioRecomd.setVisibility(View.VISIBLE);
                            artForURecomd.setVisibility(View.VISIBLE);
                            artForU.setVisibility(View.GONE);
                            topStudio.setVisibility(View.GONE);
                        } else {
                            topStudioRecomd.setVisibility(View.GONE);
                            artForURecomd.setVisibility(View.GONE);

                        }

                        if (response.body().getStudios().size() > 0) {
                            studioSearches.clear();
                            studioSearches.addAll(response.body().getStudios());
                            if (response.body().getRecommended().equalsIgnoreCase("1")) {
                                topStudio.setVisibility(View.GONE);
                            } else {
                                topStudio.setVisibility(View.VISIBLE);
                            }

                            rvArtist.getAdapter().notifyDataSetChanged();
                        } else {
                            topStudio.setVisibility(View.GONE);
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (response.body().getPieces().size() > 0) {
                            pieceSearches.clear();
                            pieceSearches.addAll(response.body().getPieces());
                            if (response.body().getRecommended().equalsIgnoreCase("1")) {
                                artForU.setVisibility(View.GONE);
                            } else {
                                artForU.setVisibility(View.VISIBLE);
                            }

                            DiscoverySearchPieces lovedAdapter = new DiscoverySearchPieces(pieceSearches);
                            rvArtPieces.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rvArtPieces.setHasFixedSize(true);
                            rvArtPieces.setAdapter(lovedAdapter);
                            rvArtPieces.getAdapter().notifyDataSetChanged();
                        } else {
                            artForU.setVisibility(View.GONE);
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onResume() {
        getCartItem();
        super.onResume();
    }
}