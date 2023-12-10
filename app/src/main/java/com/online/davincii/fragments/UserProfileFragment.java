package com.online.davincii.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.online.davincii.R;
import com.online.davincii.activities.ActivityChat;
import com.online.davincii.activities.DashboardScreen;
import com.online.davincii.activities.EditProfile;
import com.online.davincii.activities.MessageList;
import com.online.davincii.activities.Settings;
import com.online.davincii.adapters.ProfileCategoryAdapter;
import com.online.davincii.adapters.UpTabAdapter;
import com.online.davincii.models.ProfileCategories;
import com.online.davincii.models.ProfileResponse;
import com.online.davincii.models.UserProfileData;
import com.online.davincii.models.UserProfileRespose;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;

import java.sql.Wrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView topHeading, cretcolt, pieces, supportes, supporting, descripton, daviciUrl, contact, edit,atText;
    private ImageView studioImage, profileImg, menu;
    private GlobalProgressDialog progressDialog;
    private View view;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    public UserProfileData profileData;
    public ProfileCategories categories;
    private String userName,userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        context = view.getContext();


        initIalize();
        setOnClickListener();

        return view;
    }

    private void setOnClickListener() {
        view.findViewById(R.id.up_edit).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        callUsPrFrag();
    }


    public void composeEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void initIalize(){
        apiInterface = ApiClient.getClient();
        tabLayout = view.findViewById(R.id.up_tablayout);
        viewPager = view.findViewById(R.id.up_view_pager);
        contact = view.findViewById(R.id.up_contact);
        topHeading = view.findViewById(R.id.up_tophdg);
        progressDialog = new GlobalProgressDialog(context);
        cretcolt = view.findViewById(R.id.up_creatcolect_txt);
        pieces = view.findViewById(R.id.up_pieces_txt);
        supportes = view.findViewById(R.id.up_suportes_txt);
        supporting = view.findViewById(R.id.up_supting_txt);
        descripton = view.findViewById(R.id.up_descpton_txt);
        daviciUrl = view.findViewById(R.id.up_davnci_url);
        edit = view.findViewById(R.id.up_edit);
        studioImage = view.findViewById(R.id.up_std_img);
        profileImg = view.findViewById(R.id.up_profile_img);
        menu = view.findViewById(R.id.up_menu);
        atText=view.findViewById(R.id.up_at_text);

        tabLayout.addTab(tabLayout.newTab().setText("Creation"));
        tabLayout.addTab(tabLayout.newTab().setText("Collection"));


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, menu, R.style.CustomPopUpStyle);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.mnu_settings) {
                            startActivity(new Intent(getActivity(), Settings.class));
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });

        callUsPrFrag();

        contact.setOnClickListener(v ->
        {

            startActivity(new Intent(context, MessageList.class));
        //    startActivity(new Intent(new Intent(getActivity(), ActivityChat.class)));
//            BottomSheetDialog contactbtmsheet = new BottomSheetDialog(getActivity());
//            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.contactbottomsheet, null);
//            contactbtmsheet.setContentView(sheetView);
//            ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            contactbtmsheet.show();
//
//            TextView call = sheetView.findViewById(R.id.contact_callno);
//            TextView email = sheetView.findViewById(R.id.contact_email);
//
//            call.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String mobileNumber = "0123456789";
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
//                    intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
//                    startActivity(intent);
//
//                }
//            });
//
//            email.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    composeEmail("naveensmartitventures@gmail.com");
//                }
//            });
        });

        final UpTabAdapter tabAdapter = new UpTabAdapter(view.getContext(), getChildFragmentManager(), tabLayout.getTabCount(), false, "");
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(view.getContext());
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    tabTextView.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.glacial_regular));
                text.setTextColor(getResources().getColor(R.color.white));
            }



            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.glacial_regular));
                text.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.up_edit:

                startActivity(new Intent(getActivity(), EditProfile.class).putExtra("data", profileData));
//                Intent intent=new Intent(getActivity(), EditProfile.class);
//                startActivity(intent);
                break;

        }
    }

    private void callUsPrFrag() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            return;
        }
        progressDialog.showProgressBar();

        apiInterface.getProfile("Bearer " + BaseUtil.getUserToken(getActivity())).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData() != null) {
                            BaseUtil.putUserProfile(getContext(),response.body().getData().getProfilePicture());
                            userId=response.body().getData().getUserid();
                            userName = response.body().getData().getUsername();
                            topHeading.setText(response.body().getData().getUsername());
                            cretcolt.setText(response.body().getData().getFirstName() + " " + response.body().getData().getLastName());
                            descripton.setText(response.body().getData().getBio());
                            pieces.setText(String.valueOf(response.body().getPeices()));
                            supportes.setText(String.valueOf(response.body().getSupported()));
                            supporting.setText(String.valueOf(response.body().getSupporting()));
                            daviciUrl.setText(String.valueOf(response.body().getData().getLink()));
                            profileData = response.body().getData();
                            atText.setText(response.body().getData().getEmail());
//                            ((DashboardScreen) getActivity()).setProfilePic(Constant.PROFILE_IMG_BASE + response.body().getData().profilePicture);
                            Glide.with(getActivity()).load(Constant.PROFILE_IMG_BASE + response.body().getData().getProfilePicture()).into(profileImg);
                            Glide.with(getActivity()).load(Constant.STUDIO_IMG_BASE + response.body().getData().getStudioPicture()).into(studioImage);
                        }
                    }else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}