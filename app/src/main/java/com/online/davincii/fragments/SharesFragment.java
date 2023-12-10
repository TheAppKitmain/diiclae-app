package com.online.davincii.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.davincii.R;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.adapters.SharesAdapter;
import com.online.davincii.models.ModelClass;


public class SharesFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shares, container, false);

        ModelClass[] myListData = new ModelClass[] {
                new ModelClass("", R.drawable.bunny_one),
                new ModelClass("", R.drawable.bunny_two),

        };

        recyclerView=  view.findViewById(R.id.sf_recyclerView);
        SharesAdapter adapter = new SharesAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}