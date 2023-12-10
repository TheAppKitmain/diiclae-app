package com.online.davincii.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.online.davincii.R;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.models.ModelClass;

public class AllFragment extends Fragment {

    RecyclerView recyclerView;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_all, container, false);

       ModelClass[] myListData = new ModelClass[] {
               new ModelClass("", R.drawable.img_one),
               new ModelClass("", R.drawable.image_two),
       };

       recyclerView=  view.findViewById(R.id.af_recyclerView);
       AllAdapter adapter = new AllAdapter(myListData);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),2));
       recyclerView.setAdapter(adapter);

       return view;
    }
}