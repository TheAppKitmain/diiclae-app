package com.online.davincii.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.davincii.R;
import com.online.davincii.activities.New_Canvas;

public class UploadFragment extends Fragment implements View.OnClickListener {
    private TextView txtnewcanvs, txtseltcanvs, txtnext;
    private ImageView canvsone_img, canvstwo_img, canvsthree_img;
    View view;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        context = view.getContext();
        initialize();
        setOnClickListener();
        return view;
    }

    private void setOnClickListener() {
        canvsone_img.setOnClickListener(this);
        canvstwo_img.setOnClickListener(this);
        canvsthree_img.setOnClickListener(this);
    }

    private void initialize() {
        txtnewcanvs = view.findViewById(R.id.upd_frg_newcanvs_txt);
        txtseltcanvs = view.findViewById(R.id.upd_frg_sltcanvs_txt);
        txtnext = view.findViewById(R.id.upd_frg_nxt);
        canvsone_img = view.findViewById(R.id.upd_frg_canvsone);
        canvstwo_img = view.findViewById(R.id.upd_frg_canvsotwo);
        canvsthree_img = view.findViewById(R.id.upd_frg_canvsthree);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upd_frg_canvsone:
                Intent intent_one = new Intent(context, New_Canvas.class);
                intent_one.putExtra("img", 0);
                startActivity(intent_one);
                break;
            case R.id.upd_frg_canvsotwo:
                Intent intent_two = new Intent(context, New_Canvas.class);
                intent_two.putExtra("img", 1);
                startActivity(intent_two);
                break;
            case R.id.upd_frg_canvsthree:
                Intent intent_three = new Intent(context, New_Canvas.class);
                intent_three.putExtra("img", 2);
                startActivity(intent_three);
                break;
        }
    }
}