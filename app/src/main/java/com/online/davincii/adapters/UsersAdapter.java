package com.online.davincii.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.online.davincii.R;
import com.online.davincii.activities.ActivityChat;
import com.online.davincii.activities.ArtistDetail;
import com.online.davincii.models.chat.ChatUser;
import com.online.davincii.utils.BaseUtil;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.DataHolder> {

    List<ChatUser> data;
    String receiverId;
    Query reference;

    public UsersAdapter(List<ChatUser> data, String receiverId) {
        this.data = data;
        this.receiverId=receiverId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, @SuppressLint("RecyclerView") int position) {

        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("messages");
        Query query = reference.orderByChild("is_seen").equalTo("1");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int m_unread = 0;

                for (DataSnapshot ds:snapshot.getChildren()){
                    HashMap<String, String> data1 = (HashMap<String, String>) ds.getValue();
                    String us = data1.get("sender");
//                    if (us.equals(receiverId)){
//                        m_unread++;
//                    }

                    if(data1.get("receiver") != null) {
                        if (data1.get("receiver").equals(BaseUtil.getUserAccountId(holder.itemView.getContext())) && data1.get("sender").equals(data.get(position).getId())) {
                            m_unread++;
                        }
                    }
                }

                if(m_unread>0){
                    holder.unread.setVisibility(View.VISIBLE);
                    holder.unread.setText(String.valueOf(m_unread));
                    holder.msg.setTypeface(holder.msg.getTypeface(), Typeface.BOLD);
                    holder.msg.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.blue_light));
                    holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.blue_light));
                    holder.time.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.blue_light));
                }else {
                    holder.unread.setVisibility(View.GONE);
                    holder.msg.setTypeface(holder.msg.getTypeface(), Typeface.NORMAL);
                    holder.msg.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.light_gray));
                    holder.name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
                    holder.time.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.grey));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String md = data.get(position).getMessage();
        if (md != null) {
            if (md.equals("")) {
                md = "\uD83D\uDCF7  photo";
            } else {
                if (md.length() > 36) {
                    md = md.substring(0, 36) + "...";
                }
            }
        }
        holder.msg.setText(md);

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy,hh:mm aa", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(data.get(position).getLast_update());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());

        if (date !=null){
            String formattedDate  = df.format(date);
            String tm[]=formattedDate.split(",");
            String fTime=tm[1];
            holder.time.setText(fTime);
        }

        Glide.with(holder.itemView.getContext()).load(data.get(position).getImage()).into(holder.image);
        holder.name.setText(data.get(position).getName());

//        if(data.get(position).getUnread()==0){
//            holder.unread.setVisibility(View.GONE);
//        }else {
//            holder.unread.setVisibility(View.VISIBLE);
//            holder.unread.setText(String.valueOf(data.get(position).getUnread()));
//          }

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ActivityChat.class);
                intent.putExtra("studioId", String.valueOf(data.get(position).getId()));
                intent.putExtra("studioName", data.get(position).getName());
                intent.putExtra("studioImage", data.get(position).getImage());
                intent.putExtra("from","userList");
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), ArtistDetail.class);
                intent.putExtra("id",data.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateList(List<ChatUser> list){
        data = list;
        notifyDataSetChanged();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private LinearLayout main;
        private CircleImageView image;
        private TextView name, msg, time, unread;

        public DataHolder(@NonNull View view) {
            super(view);

            image = view.findViewById(R.id.rcu_userImage);
            name = view.findViewById(R.id.rc_userName);
            msg = view.findViewById(R.id.rcu_lastMsg);
            time = view.findViewById(R.id.rcu_lastTime);
            unread = view.findViewById(R.id.rcu_readCount);
            main = view.findViewById(R.id.rcu_main);
        }
    }
}
