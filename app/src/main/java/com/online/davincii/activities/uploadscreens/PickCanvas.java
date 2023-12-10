package com.online.davincii.activities.uploadscreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.davincii.R;
import com.online.davincii.activities.New_Canvas;

public class PickCanvas extends AppCompatActivity implements View.OnClickListener {
    private TextView txtnewcanvs, txtseltcanvs, txtnext;
    private ImageView canvsone_img, canvstwo_img, canvsthree_img;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_canvas);

        context = PickCanvas.this;

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initialize();
        setOnClickListener();
    }

    private void initialize() {
        txtnewcanvs = findViewById(R.id.upd_frg_newcanvs_txt);
        txtseltcanvs = findViewById(R.id.upd_frg_sltcanvs_txt);
        txtnext = findViewById(R.id.upd_frg_nxt);
        canvsone_img = findViewById(R.id.upd_frg_canvsone);
        canvstwo_img = findViewById(R.id.upd_frg_canvsotwo);
        canvsthree_img = findViewById(R.id.upd_frg_canvsthree);
    }

    private void setOnClickListener() {
        canvsone_img.setOnClickListener(this);
        canvstwo_img.setOnClickListener(this);
        canvsthree_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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