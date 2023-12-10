package com.online.davincii.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.davincii.R;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.adapters.CanvasAdapter;
import com.online.davincii.models.ModelClass;

public class CanvasFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_canvas, container, false);
        ModelClass[] myListData = new ModelClass[] {
                new ModelClass("", R.drawable.king_one),
                new ModelClass("", R.drawable.king_one),

        };

        recyclerView=  view.findViewById(R.id.cf_recyclerView);
        CanvasAdapter adapter = new CanvasAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),2));
        recyclerView.setAdapter(adapter);
        return view;
    }
}