package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.online.davincii.R;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.adapters.GiftUsersAdapter;
import com.online.davincii.models.ModelClass;

public class GiftActivity extends AppCompatActivity {

    private LinearLayout fromActivityBtn, pickEmail;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView userRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
        clicks();
    }

    private void clicks(){
        fromActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GiftActivity.this, ChooseFromWallet.class));
            }
        });

        pickEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(GiftActivity.this);
                View sheetView = getLayoutInflater().inflate(R.layout.gift_user_sheet, null);
                userRecycler=sheetView.findViewById(R.id.gus_recyclerView);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                bottomSheetDialog.show();
                getUsers();
            }
        });
    }

    private void initView(){
        fromActivityBtn = findViewById(R.id.ga_fromActivity);
        pickEmail=findViewById(R.id.ga_pickEmail);
        //userRecycler=findViewById(R.id.ga_pickEmail);
    }

    private void getUsers(){
        ModelClass[] myListData = new ModelClass[] {
                new ModelClass("User One", R.drawable.ic_user),
                new ModelClass("User Two", R.drawable.ic_user),
                new ModelClass("User Three", R.drawable.ic_user),
        };

        GiftUsersAdapter adapter = new GiftUsersAdapter(myListData);
        userRecycler.setHasFixedSize(true);
        userRecycler.setLayoutManager(new LinearLayoutManager(this));
        userRecycler.setAdapter(adapter);
    }


}