package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.online.davincii.R;
import com.online.davincii.adapters.UsersAdapter;
import com.online.davincii.models.chat.ChatUser;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MessageList extends AppCompatActivity {
    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;
    private RecyclerView rvMessage;
    private ImageView backBtn;
    private EditText searchEditText;
    private DatabaseReference reference;
    private List<ChatUser> uList = new ArrayList<>();
    private UsersAdapter adapter;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        progress = new GlobalProgressDialog(this);
        progress.showProgressBar();
        apiInterface = ApiClient.getClient();
        msg = findViewById(R.id.fch_msg);
        backBtn = findViewById(R.id.msg_backbtn);
        searchEditText = findViewById(R.id.msg_search);
        rvMessage = findViewById(R.id.msg_rv);

        rvMessage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageList.super.onBackPressed();

            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        getChatUser(getApplicationContext());
    }

    private void filter(String text) {
        List<ChatUser> temp = new ArrayList();
        for (ChatUser d : uList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
       if (uList.size()>0)
        adapter.updateList(temp);
    }

    private void getChatUser(Context context) {

        reference = FirebaseDatabase.getInstance().getReference("usersTable").child(String.valueOf(BaseUtil.getUserAccountId(context)));
        Query query = reference.orderByChild("time_stamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uList.clear();
                boolean found = false;
                for (DataSnapshot ds : snapshot.getChildren()){
                    found = true;
                    HashMap<String, String> data = (HashMap<String, String>) ds.getValue();
                    ChatUser us = new ChatUser(data.get("id"), data.get("name"), data.get("image"), data.get("message"), data.get("last_update"));
                    uList.add(us);
                    adapter = new UsersAdapter(uList,data.get("id"));
                    rvMessage.setAdapter(adapter);
                }
                if (found){
                    msg.setVisibility(View.GONE);
                } else {
                    msg.setVisibility(View.VISIBLE);
                }
                Collections.reverse(uList);
                if (uList.size()>0) {
                    adapter.notifyDataSetChanged();
                }
                progress.hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}