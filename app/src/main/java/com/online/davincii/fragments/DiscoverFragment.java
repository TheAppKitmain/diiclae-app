package com.online.davincii.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.online.davincii.R;
import com.online.davincii.activities.ArtForU_SeeALL;
import com.online.davincii.activities.DiscoverySearch;
import com.online.davincii.activities.MyCart;
import com.online.davincii.activities.ProfileActivity;
import com.online.davincii.activities.TopStudio_SeeAll;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.adapters.DCS_FeaturedArtistAdapter;
import com.online.davincii.adapters.DC_ArtForYou_Adapter;
import com.online.davincii.adapters.DC_ComicArtAdapter;
import com.online.davincii.adapters.DC_CulturedArtAdapter;
import com.online.davincii.adapters.DC_RecentlyLovedAdapter;
import com.online.davincii.adapters.DC_TopStudio_Adapter;
import com.online.davincii.adapters.DC_TopTen_Pics;
import com.online.davincii.adapters.DiscoveryCategoryAdapter;
import com.online.davincii.models.ModelClass;
import com.online.davincii.models.discoverysearch.ArtForYouResponse;
import com.online.davincii.models.discoverysearch.ComicArtResponse;
import com.online.davincii.models.discoverysearch.CulturedArtResponse;
import com.online.davincii.models.discoverysearch.DiscoveryResponse;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.discoverysearch.FeaturedArtistResponse;
import com.online.davincii.models.discoverysearch.RecentlyLovedResponse;
import com.online.davincii.models.discoverysearch.TopStudioResponse;
import com.online.davincii.models.discoverysearch.TopTenPieceResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment implements View.OnClickListener {
    private ImageView cartImg, profileImg;
    private TextView seeAll_one, seeAll_two, searchView;
    private RecyclerView rv_ArtForu, rv_TopStudio, rv_TopTenPc, rv_FeatArtist, rv_RecentLv, rv_ComicArt, rv_CultrdArt, rv_category;
    private Context context;
    private View view;
    private GlobalProgressDialog progressDialog;
    private ApiClient.APIInterface apiInterface;
    private List<ArtForYouResponse> artForList = new ArrayList<>();
    private List<TopStudioResponse> topStudioList = new ArrayList<>();
    private List<TopTenPieceResponse> topTenList = new ArrayList<>();
    private List<FeaturedArtistResponse> featArtisRespon = new ArrayList<>();
    private List<RecentlyLovedResponse> lovedRespons = new ArrayList<>();
    private List<ComicArtResponse> comicArtRespon = new ArrayList<>();
    private List<CulturedArtResponse> culturedRespon = new ArrayList<>();

    private TextView cart_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        context = view.getContext();

        intialize();
        setOnClickListener();
        return view;

    }

    private void intialize() {
        progressDialog = new GlobalProgressDialog(context);
        apiInterface = ApiClient.getClient();
        searchView = view.findViewById(R.id.dc_fg_search);
        cartImg = view.findViewById(R.id.dc_fg_cart_Img);
        seeAll_one = view.findViewById(R.id.dc_fg_seeAll_txtone);
        seeAll_two = view.findViewById(R.id.dc_fgseeAll_txtwo);
        rv_ArtForu = view.findViewById(R.id.dc_fg_rv_artforu);
        rv_TopStudio = view.findViewById(R.id.dc_fg_rv_topstudios);
        rv_TopTenPc = view.findViewById(R.id.dc_fg_rv_toptenPc);
        cart_count = view.findViewById(R.id.fd_count);
        rv_FeatArtist = view.findViewById(R.id.dc_fg_rv_featArtist);
        rv_RecentLv = view.findViewById(R.id.dc_fg_rv_rectLoved);
        rv_ComicArt = view.findViewById(R.id.dc_fg_rv_comicArt);
        rv_CultrdArt = view.findViewById(R.id.dc_fg_rv_cultArt);
        rv_category=view.findViewById(R.id.dc_ct_recycler);
        profileImg=view.findViewById(R.id.db_userprofile);
        Picasso.get().load(BaseUtil.getUserProfile(requireActivity())).error(R.drawable.ic_user).into(profileImg);

        categoryList();
        callDiscoverApi();
    }

    private void setOnClickListener() {
        seeAll_one.setOnClickListener(this);
        seeAll_two.setOnClickListener(this);
        cartImg.setOnClickListener(this);
        searchView.setOnClickListener(this);
        profileImg.setOnClickListener(this);
    }

    private void callDiscoverApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            return;
        }
        progressDialog.showProgressBar();

        apiInterface.getdiscovery("Bearer " + BaseUtil.getUserToken(getContext())).enqueue(new Callback<DiscoveryResponse>() {
            @Override
            public void onResponse(Call<DiscoveryResponse> call, Response<DiscoveryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {

                        if (response.body().getArtForYou().size() > 0) {
                            artForList.addAll(response.body().getArtForYou());
                            DC_ArtForYou_Adapter studio_adapter = new DC_ArtForYou_Adapter(artForList, context);
                            rv_ArtForu.setLayoutManager(new GridLayoutManager(context,2));
                            rv_ArtForu.setHasFixedSize(true);
                            rv_ArtForu.setAdapter(studio_adapter);
                        }

                        if (response.body().getTopTenPieces().size() > 0) {
                            if (topTenList.size() > 0) {
                                topTenList.clear();
                            }

                            topTenList.addAll(response.body().getTopTenPieces());
                            DC_TopTen_Pics topTen_pics = new DC_TopTen_Pics(topTenList, context);
                            rv_TopTenPc.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_TopTenPc.setHasFixedSize(true);
                            rv_TopTenPc.setAdapter(topTen_pics);
                        }
                        if (response.body().getTopStudios().size() > 0) {
                            topStudioList.addAll(response.body().getTopStudios());
                            DC_TopStudio_Adapter topStudio_adapter = new DC_TopStudio_Adapter(topStudioList, context);
                            rv_TopStudio.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_TopStudio.setHasFixedSize(true);
                            rv_TopStudio.setAdapter(topStudio_adapter);
                        }

                        if (response.body().getFeaturedArtist().size() > 0) {
                            featArtisRespon.addAll(response.body().getFeaturedArtist());
                            DCS_FeaturedArtistAdapter artistAdapter = new DCS_FeaturedArtistAdapter(featArtisRespon);
                            rv_FeatArtist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_FeatArtist.setHasFixedSize(true);
                            rv_FeatArtist.setAdapter(artistAdapter);
                        }

                        if (response.body().getRecentlyLoved().size() > 0) {
                            lovedRespons.addAll(response.body().getRecentlyLoved());
                            DC_RecentlyLovedAdapter lovedAdapter = new DC_RecentlyLovedAdapter(lovedRespons, context);
                            rv_RecentLv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_RecentLv.setHasFixedSize(true);
                            rv_RecentLv.setAdapter(lovedAdapter);
                        }

                        if (response.body().getComicArt().size() > 0) {
                            comicArtRespon.addAll(response.body().getComicArt());
                            DC_ComicArtAdapter comicArtAdapt = new DC_ComicArtAdapter(comicArtRespon, context);
                            rv_ComicArt.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_ComicArt.setHasFixedSize(true);
                            rv_ComicArt.setAdapter(comicArtAdapt);
                        }

                        if (response.body().getCulturedart().size() > 0) {
                            culturedRespon.addAll(response.body().getCulturedart());
                            DC_CulturedArtAdapter culturdAdapter = new DC_CulturedArtAdapter(culturedRespon, context);
                            rv_CultrdArt.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            rv_CultrdArt.setHasFixedSize(true);
                            rv_CultrdArt.setAdapter(culturdAdapter);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<DiscoveryResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dc_fg_seeAll_txtone:
                Intent intent = new Intent(context, ArtForU_SeeALL.class);
                startActivity(intent);
                break;

            case R.id.dc_fgseeAll_txtwo:
                Intent seeIntent = new Intent(context, TopStudio_SeeAll.class);
                startActivity(seeIntent);
                break;
            case R.id.dc_fg_search:
                startActivity(new Intent(getActivity(), DiscoverySearch.class));
                break;

            case R.id.dc_fg_cart_Img:
                startActivity(new Intent(getActivity(), MyCart.class));
                break;

            case R.id.db_userprofile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;

        }
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

    private void categoryList()
    {
        ModelClass[] myListData = new ModelClass[] {
                new ModelClass("Painting", R.drawable.cat_painting),
                new ModelClass("Photography", R.drawable.cat_photography),
                new ModelClass("Digital", R.drawable.cat_digital),
                new ModelClass("Sculpture", R.drawable.cat_sculpture),
                new ModelClass("Mixed Media", R.drawable.mixed_media),
                new ModelClass("A.I.", R.drawable.cat_ai),
                new ModelClass("3D", R.drawable.threedee),
                new ModelClass("Prints", R.drawable.prints),
        };
        DiscoveryCategoryAdapter adapter = new DiscoveryCategoryAdapter(myListData);
        rv_category.setHasFixedSize(true);
        rv_category.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        getCartItem();
        super.onResume();
    }
}