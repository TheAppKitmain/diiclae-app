package com.online.davincii.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.online.davincii.R;
import com.online.davincii.activities.DiscoveryDetails;
import com.online.davincii.models.chat.ChatObject;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.DataHolder> {

    private ArrayList<ChatObject> chatObjectList;
    private Context context;
    // private SessionSecuredPreferences loginPreferences;
    private String userRole;
    private String senderId;
    private String loggedInUserId;
    private String receiverId;
    private List<String> key;
    // private String key;

    public ChatAdapter(Context context, ArrayList<ChatObject> chatObjectList, String senderId, String receiverId, List<String> key) {
        this.chatObjectList = chatObjectList;
        this.context = context;
        this.senderId=senderId;
        this.receiverId=receiverId;
        this.key=key;

//        this.loginPreferences = ApplicationHelper.application().loginPreferences(LOGIN_PREFERENCE);
//        this.userRole = loginPreferences.getString(USER_ROLE, "");
//        this.loggedInUserId = String.valueOf(this.loginPreferences.getInt(USER_ID, 0));
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        ChatObject chatObject = this.chatObjectList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy,hh:mm aa", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(chatObjectList.get(position).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String dt = df.format(date);

        String mDate[]=dt.split(",");

        String t[] = chatObjectList.get(position).getTime().split(",");

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = sdf.format(cal.getTime());

        if (position > 0) {
            String g[] = chatObjectList.get(position - 1).getTime().split(",");
            setView(t[0], g[0], holder.date_main, holder.s_date);
        } else {
            holder.date_main.setVisibility(View.VISIBLE);
            if (t[0].equals(currentDate)) {
                holder.s_date.setText("Today");
            } else {
                if(t[0].equals(yesterday)){
                    holder.s_date.setText("Yesterday");
                }else {
                    holder.s_date.setText(t[0]);
                }
            }
        }

        if (chatObject.getSender().equals(senderId)){
            if(chatObject.getType().equals("text")){
                holder.s_image_layout.setVisibility(View.GONE);
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.s_msg.setText(chatObject.getMessage());
                holder.s_time.setText(mDate[1]);
            }else if (chatObject.getType().equals("image")){
                holder.s_msg.setVisibility(View.GONE);
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.s_image_layout.setVisibility(View.VISIBLE);
                Picasso.get().load(chatObject.getMessage()).into(holder.s_image);
                holder.s_time.setText(mDate[1]);
            }else{
                holder.shareSenderLayout.setVisibility(View.VISIBLE);
                holder.shareSenderMessage.setText(chatObject.getMessage());
                holder.artBySender.setText("Art by: "+chatObject.getArt_by());
                Picasso.get().load(chatObject.getCanvasImage()).into(holder.shareSenderImage);
                holder.shareSenderTime.setText(mDate[1]);
            }
        }else{
            if (chatObject.getType().equals("text")){
                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.r_msg.setText(chatObject.getMessage());
                holder.r_time.setText(mDate[1]);
            }else if (chatObject.getType().equals("image")){
                holder.r_msg.setVisibility(View.GONE);
                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.r_msg.setVisibility(View.GONE);
                holder.r_image_layout.setVisibility(View.VISIBLE);
                Picasso.get().load(chatObject.getMessage()).into(holder.r_image);
                holder.r_time.setText(mDate[1]);
            }
            else{
                holder.shareReceiverLayout.setVisibility(View.VISIBLE);
                holder.shareReceiverMessage.setText(chatObject.getMessage());
                holder.artByReceiver.setText("Art by: "+chatObject.getArt_by());
                Picasso.get().load(chatObject.getCanvasImage()).into(holder.shareReceiverImage);
                holder.shareReceiverTime.setText(mDate[1]);
            }
        }

        holder.s_msg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteMessage(holder.itemView.getContext(), key.get(position), position);
                return false;
            }
        });

        holder.s_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteMessage(holder.itemView.getContext(), key.get(position), position);
                return false;
            }
        });

        holder.s_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(chatObjectList.get(position).getMessage(), holder.itemView.getContext());
            }
        });

        holder.r_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(chatObjectList.get(position).getMessage(), holder.itemView.getContext());
            }
        });

        holder.r_msg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteMessage(holder.itemView.getContext(), key.get(position), position);
                return false;
            }
        });

        holder.shareSenderLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteMessage(holder.itemView.getContext(), key.get(position), position);
                return false;
            }
        });

        holder.shareSenderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), DiscoveryDetails.class);
                intent.putExtra("id",Integer.parseInt(chatObjectList.get(position).getCanvasId()));
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.shareReceiverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), DiscoveryDetails.class);
                intent.putExtra("id",Integer.parseInt(chatObjectList.get(position).getCanvasId()));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    private void showPopup(String message, Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.image_preview_dialog, null);

        ImageView close = dialogView.findViewById(R.id.ipd_close);
        ImageView image = dialogView.findViewById(R.id.ipd_image);
        // ImageView delete = dialogView.findViewById(R.id.ipd_delete);
        ImageView isVideo = dialogView.findViewById(R.id.ipd_video);

        //delete.setVisibility(View.GONE);
        isVideo.setVisibility(View.GONE);

//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        image.setLayoutParams(layoutParams);
        Glide.with(context).load(message).into(image);

        AlertDialog sDialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();
        Objects.requireNonNull(sDialog.getWindow()).getAttributes().windowAnimations = R.style.ScaleFromCenter;
        sDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
    }

    private void deleteMessage(Context context, String mKey, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.drawable.diiclae_color_logo);
        dialog.setTitle("Davincii");
        dialog.setCancelable(false);
        dialog.setMessage("You want to delete message?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
//                Query query=reference.orderByChild("c_remove").equalTo("1");
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.child("sender").getValue().equals(senderId)){
//                                HashMap<String, Object> data2 = new HashMap<>();
//                                data2.put("c_remove", "0");
//                                reference.child(key).updateChildren(data2);
//                                chatObjectList.remove(position);
//                                notifyDataSetChanged();
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });\
//                if (chatObjectList.get(position).getSender().equals(receiverId)) {
                HashMap<String, Object> data2 = new HashMap<>();
                data2.put("c_remove", "0");
                reference.child(mKey).updateChildren(data2);
                chatObjectList.remove(position);
                key.remove(position);
                notifyDataSetChanged();
//                }
            }
        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog sDialog = dialog.create();
        Objects.requireNonNull(sDialog.getWindow()).getAttributes().windowAnimations = R.style.ScaleFromCenter;
        sDialog.show();

    }

    private void setView(String s, String g, LinearLayout date_main, TextView s_date) {
        if (g.equals(s)) {
            date_main.setVisibility(View.GONE);
        } else {
            date_main.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDate = sdf.format(new Date());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String yesterday = sdf.format(cal.getTime());
            if (s.equals(currentDate)) {
                s_date.setText("Today");
            } else if (s.equals(yesterday)) {
                s_date.setText("Yesterday");
            } else {
                s_date.setText(s);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatObjectList.size();
    }

    public static class DataHolder extends RecyclerView.ViewHolder {
        private ImageView r_image,s_image,shareSenderImage,shareReceiverImage;
        private CardView r_image_layout, s_image_layout;
        private LinearLayout senderLayout, receiverLayout, date_main,shareSenderLayout,shareReceiverLayout;
        private CircleImageView one_image, two_image;
        private TextView r_msg, s_msg, r_time, s_time, s_date,shareSenderMessage,shareSenderTime
                ,shareReceiverMessage,shareReceiverTime,artBySender,artByReceiver;

        public DataHolder(@NonNull View view){
            super(view);

            receiverLayout = view.findViewById(R.id.rm_receiverLayout);
            senderLayout = view.findViewById(R.id.rm_senderLayout);
            r_image_layout = view.findViewById(R.id.rc_receiverCard);
            s_image_layout = view.findViewById(R.id.rm_senderCard);
            r_msg = view.findViewById(R.id.rm_receiverMsg);
            s_msg = view.findViewById(R.id.rm_senderMsg);
            r_time = view.findViewById(R.id.rm_receiverTime);
            s_time = view.findViewById(R.id.rm_senderTime);
            r_image = view.findViewById(R.id.rm_receiverImage);
            s_image = view.findViewById(R.id.rm_senderImage);
            s_date = view.findViewById(R.id.rc_date);
            date_main = view.findViewById(R.id.rc_main_date);
            artByReceiver=view.findViewById(R.id.rm_shareReceiverArtBy);
            artBySender=view.findViewById(R.id.rm_senderShareArtBy);
            shareSenderLayout=view.findViewById(R.id.rm_senderShareLayout);
            shareSenderImage=view.findViewById(R.id.rm_senderShareImage);
            shareSenderMessage=view.findViewById(R.id.rm_senderShareMsg);
            shareSenderTime=view.findViewById(R.id.rm_senderShareTime);
            shareReceiverLayout=view.findViewById(R.id.rm_shareReceiverLayout);
            shareReceiverImage=view.findViewById(R.id.rm_shareReceiverImage);
            shareReceiverMessage=view.findViewById(R.id.rm_shareReceiverMsg);
            shareReceiverTime=view.findViewById(R.id.rm_shareReceiverTime);

        }
    }
}
