package com.online.davincii.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.online.davincii.R;
import com.online.davincii.activities.MyCart;
import com.online.davincii.activities.ProfileActivity;
import com.online.davincii.utils.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WalletFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView profileImg, cartIcon, gift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.wl_tablayout);
        viewPager = view.findViewById(R.id.wl_view_pager);
        profileImg=view.findViewById(R.id.wf_userprofile);
        cartIcon = view.findViewById(R.id.wf_cart);
        gift = view.findViewById(R.id.wf_gift);

        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("COAs"));
        tabLayout.addTab(tabLayout.newTab().setText("Shares"));
        tabLayout.addTab(tabLayout.newTab().setText("NFTs"));
        tabLayout.addTab(tabLayout.newTab().setText("Canvas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MainAdapter adapter = new MainAdapter(requireContext(), getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Picasso.get().load(BaseUtil.getUserProfile(requireActivity())).error(R.drawable.ic_user).into(profileImg);


        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCart.class));
            }
        });


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });



    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        // Initialize main adapter
        MainAdapter adapter=new MainAdapter(getActivity().getApplicationContext(), getFragmentManager(),4);
        viewPager.setAdapter(adapter);
    }


    private class MainAdapter extends FragmentPagerAdapter {
        private Context myContext;
        int totalTabs;

        public MainAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    AllFragment homeFragment = new AllFragment();
                    return homeFragment;
                case 1:
                    CertificatesFragment sportFragment = new CertificatesFragment();
                    return sportFragment;
                case 2:
                    SharesFragment sharesFragment = new SharesFragment();
                    return sharesFragment;
                case 3:
                    NftFragment nftFragment  = new NftFragment();
                    return nftFragment;
                case 4:
                    CanvasFragment canvasFragment = new CanvasFragment();
                    return canvasFragment;
                default:
                    return null;
            }
        }
        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }




}