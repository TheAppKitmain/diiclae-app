package com.online.davincii.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.online.davincii.activities.MessageList;
import com.online.davincii.activities.ProfileActivity;
import com.online.davincii.adapters.UsersAdapter;
import com.online.davincii.models.chat.ChatUser;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class ChatFragment extends Fragment {

    private GlobalProgressDialog progress;
    private ApiClient.APIInterface apiInterface;
    private RecyclerView rvMessage;
    private ImageView profileImg;
    private EditText searchEditText;
    private DatabaseReference reference;
    private List<ChatUser> uList = new ArrayList<>();
    private UsersAdapter adapter;
    private TextView msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        progress = new GlobalProgressDialog(requireContext());
        progress.showProgressBar();
        apiInterface = ApiClient.getClient();
        msg = view.findViewById(R.id.fch_msg);
        profileImg = view.findViewById(R.id.msg_userImg);
        searchEditText = view.findViewById(R.id.msg_search);
        rvMessage = view.findViewById(R.id.msg_rv);

        rvMessage.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

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
        getChatUser(requireContext());

        Picasso.get().load(BaseUtil.getUserProfile(requireActivity())).error(R.drawable.ic_user).into(profileImg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        return view;
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