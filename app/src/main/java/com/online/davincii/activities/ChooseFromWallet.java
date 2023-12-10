package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.online.davincii.R;
import com.online.davincii.adapters.AllAdapter;
import com.online.davincii.models.ModelClass;

public class ChooseFromWallet extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_from_wallet);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        ModelClass[] myListData = new ModelClass[] {
                new ModelClass("", R.drawable.img_one),
                new ModelClass("", R.drawable.image_two),
                new ModelClass("", R.drawable.king_one),
        };

        recyclerView=  findViewById(R.id.cfw_recyclerView);
        AllAdapter adapter = new AllAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

        initViews();
        clicks();
    }


    private void clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews(){
        back=findViewById(R.id.cfg_back);
    }
}