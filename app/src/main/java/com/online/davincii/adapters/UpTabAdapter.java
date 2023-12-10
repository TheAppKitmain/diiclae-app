package com.online.davincii.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.online.davincii.fragments.UpCollectionFragment;
import com.online.davincii.fragments.UpCreationFragment;

public class UpTabAdapter extends FragmentPagerAdapter {
    private Context tabcontext;
    private int totalTabs;
    private boolean isOther=false;
    private String businessId;

//    public UpTabAdapter(@NonNull Context context, FragmentManager fm, int totalTabs) {
//        super(fm, totalTabs);
//        tabcontext = context;
//        this.totalTabs = totalTabs;
//    }

    public UpTabAdapter(@NonNull Context context, FragmentManager fm, int totalTabs,boolean isOther,String businessId) {
        super(fm, totalTabs);
        tabcontext = context;
        this.totalTabs = totalTabs;
        this.isOther=isOther;
        this.businessId=businessId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UpCreationFragment creationFragment = new UpCreationFragment(isOther,businessId);
                return creationFragment;

            case 1:
                UpCollectionFragment collectionFragment = new UpCollectionFragment(isOther, businessId);
                return collectionFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
