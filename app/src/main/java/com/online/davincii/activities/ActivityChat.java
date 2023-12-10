package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.online.davincii.R;
import com.online.davincii.adapters.ChatAdapter;
import com.online.davincii.models.chat.ChatObject;
import com.online.davincii.models.chat.DataModel;
import com.online.davincii.models.chat.NotificationModel;
import com.online.davincii.models.chat.RootModel;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChat extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private ImageView sendBtn, userImage, addIcon, emoji, back;
    private EditText enterMsg;
    private String userRole;
    private String senderId, receiverId, studioImg, studioName;
    private boolean is_create = false;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatObject> chatObjectList = new ArrayList<>();
    private RecyclerView chRecyclerView;
    private boolean isActive = false;
    private BottomSheetDialog bottomSheetDialog;
    private DatabaseReference reference, msgRef;
    private EmojiPopup emojiPopup;
    private ViewGroup rootView;
    private Context context;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    private String FILE_PATH;
    private String token = "", key;
    private String type = "";
    private ApiClient.APIInterface apiInterface;
    List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initViews(ActivityChat.this);
        context = ActivityChat.this;
        credentials = new BasicAWSCredentials(getResources().getString(R.string.aws_kay), getResources().getString(R.string.hidden_kay));
        s3Client = new AmazonS3Client(credentials);
    }

    private void initViews(Context context) {
        apiInterface = ApiClient.getClient();
        this.isActive = true;
        userImage = findViewById(R.id.ac_user_image);
        userName = findViewById(R.id.ac_user_name);
        sendBtn = findViewById(R.id.ac_send_chat);
        enterMsg = findViewById(R.id.ac_enterMessage);

        addIcon = findViewById(R.id.ac_add);
        rootView = findViewById(R.id.linearLayout);
        emoji = findViewById(R.id.ac_emoji);
        back = findViewById(R.id.ac_back);
        senderId = BaseUtil.getUserAccountId(getApplicationContext());

        EmojiManager.install(new GoogleEmojiProvider());
        this.setUpEmojiKeyboard();

        receiverId = getIntent().getStringExtra("studioId");
        studioImg = getIntent().getStringExtra("studioImage");
        studioName = getIntent().getStringExtra("studioName");

        if (getIntent().hasExtra("share")) {
            shareCanvas(getIntent().getStringExtra("canvasName"),
                    getIntent().getStringExtra("canvasImage"),
                    getIntent().getStringExtra("canvasId"),
                    getIntent().getStringExtra("artBy"));
        }

        reference = FirebaseDatabase.getInstance().getReference("messages");
        chRecyclerView = findViewById(R.id.ac_recyclerChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chRecyclerView.setLayoutManager(linearLayoutManager);
        chRecyclerView.setHasFixedSize(true);
        chRecyclerView.scrollToPosition(chatObjectList.size() - 1);

        //     getUserChat();
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
            }
        });

        readMessage();

        if (studioImg != null) {
            if (studioImg.equals("")) {
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
            } else {
                Picasso.get().load(studioImg).into(userImage);
            }
        }
        if (getIntent().getStringExtra("from") != null) {
            if (getIntent().getStringExtra("from").equals("userList")) {
                if (studioImg.equals("")) {
                    if (studioImg.equals("")) {
                        userImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                    } else {
                        Picasso.get().load(studioImg).into(userImage);
                    }
                }
                userName.setText(studioName);
            }
        }
        userName.setText(studioName);

        if (getIntent().hasExtra("notification")) {
            studioImg = getIntent().getStringExtra("image");
            studioName = getIntent().getStringExtra("name");
            userName.setText(studioName);
            receiverId = getIntent().getStringExtra("id");
            Picasso.get().load(studioImg).into(userImage);
        }
        setOnClickLister();
    }

    private void setOnClickLister() {
        sendBtn.setOnClickListener(this);
        addIcon.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_send_chat:
                String message = enterMsg.getText().toString().trim();
                if (!message.isEmpty()) {
                    enterMsg.setText("");
                    if (!is_create) {
                        addUserInFirebase(message);
                    }
                    this.lastUpdate(message, senderId, receiverId);
                    this.sendChat(message);
                } else {
                    Toast.makeText(this, "Type a message", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ac_add:
                bottomSheetDialog = new BottomSheetDialog(ActivityChat.this);
                View sheetView = getLayoutInflater().inflate(R.layout.add_image_bottom, null);
                bottomSheetDialog.setContentView(sheetView);
                ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                bottomSheetDialog.show();

                LinearLayout imageBtn = sheetView.findViewById(R.id.aib_image);
                imageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Options options = Options.init()
                                .setRequestCode(101)
                                .setCount(1)
                                .setFrontfacing(false)
                                .setExcludeVideos(true)
                                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);
                        Pix.start(ActivityChat.this, options);
                        bottomSheetDialog.dismiss();
                    }
                });
                break;
            case R.id.ac_back:
                if (getIntent().hasExtra("notification")) {
                    super.onBackPressed();
                }
                super.onBackPressed();
                break;
        }
    }

    private void setUpEmojiKeyboard() {
        this.emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiPopupShownListener(() -> emoji.setImageResource(R.drawable.ic_baseline_keyboad))
                .setOnEmojiPopupDismissListener(() -> emoji.setImageResource(R.drawable.ic_insert_emoticon))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .build(enterMsg);
    }

    private void sendChat(String message) {
        //reference = FirebaseDatabase.getInstance().getReference("messages").child(senderId + "_" + receiverId);
//        if (msgRef.equals(FirebaseDatabase.getInstance().getReference("messages").child(senderId + "_" + receiverId))) {
//            msgRef = FirebaseDatabase.getInstance().getReference("messages").child(senderId + "_" + receiverId);
//        } else if (msgRef.equals(FirebaseDatabase.getInstance().getReference("messages").child(receiverId + "_" + senderId))) {
//            msgRef = FirebaseDatabase.getInstance().getReference("messages").child(receiverId + "_" + senderId);
//        }
////        else {
////            msgRef = FirebaseDatabase.getInstance().getReference("messages").child(senderId + "_" + receiverId);
////        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy,hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateAndTime = sdf.format(new Date());
        HashMap<String, String> data2 = new HashMap<>();

        data2.put("sender", senderId);
        data2.put("receiver", receiverId);
        data2.put("message", message);
        data2.put("time", currentDateAndTime);
        data2.put("is_seen", "1");
        data2.put("s_remove", "1");
        data2.put("c_remove", "1");
        data2.put("type", "text");

        reference.push().setValue(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(ChatActivity.this, "Send chat", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(ChatActivity.this, "Unable to send chat.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference c_reference;
        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(receiverId);
        c_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
                    token = data.get("token");
                    type = data.get("type");
                    sendmNotification(message, senderId, type, BaseUtil.getUserName(context));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void lastUpdate(String message, String C_ID, String B_ID) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy,hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String m_time = sdf.format(new Date());

        Long tsLong = System.currentTimeMillis() / 1000;
        String time_stamp = tsLong.toString();

        DatabaseReference businessReference, customerReference;

        customerReference = FirebaseDatabase.getInstance().getReference("usersTable").child(C_ID).child(B_ID);
        customerReference.child("last_update").setValue(m_time);
        customerReference.child("message").setValue(message);
        customerReference.child("time_stamp").setValue(time_stamp);

        businessReference = FirebaseDatabase.getInstance().getReference("usersTable").child(B_ID).child(C_ID);
        businessReference.child("last_update").setValue(m_time);
        businessReference.child("message").setValue(message);
        businessReference.child("time_stamp").setValue(time_stamp);
    }

//    private void sendNotification(String token, String type, String msg, String userId, String userName, String userImage) {
//        // RootModel rootModel = new RootModel(token, new DataModel("New Message", userId, msg, userName, userImage));
//        if (type.equals("iOS")) {
//            RootModel rootModel = new RootModel(token, "high", new DataModel("New Message", userId, msg, userName, userImage), new NotificationModel("New Message", msg));
//            apiInterface.sendNotification(rootModel).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
//                    //Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                    //Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            RootModel rootModel = new RootModel(token, "high", new DataModel("New Message", userId, msg, userName, userImage));
//            apiInterface.sendNotification(rootModel).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
//                    //Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                    //Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

//    private void getUserChat() {
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(senderId + "_" + receiverId);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
//        Query query = reference.orderByChild("c_remove").equalTo("1");
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
//                assert data != null;
//                ChatObject us = new ChatObject(data.get("message"), data.get("sender"), data.get("receiver"), data.get("time"), snapshot.getKey(), data.get("type"),
//                        data.get("canvasImage"), data.get("canvasId"),data.get("art_by"));
//                chatObjectList.add(us);
//                chatAdapter.notifyDataSetChanged();
//                chRecyclerView.scrollToPosition(chatObjectList.size() - 1);
//                if (isActive) {
//                    if (Objects.requireNonNull(data.get("sender")).equals("0")) {
//                        HashMap<String, Object> data2 = new HashMap<>();
//                        data2.put("is_seen", "0");
//                        reference.child(Objects.requireNonNull(snapshot.getKey())).updateChildren(data2);
//                    } else {
//                        if (Objects.requireNonNull(data.get("sender")).equals("1")) {
//                            HashMap<String, Object> data2 = new HashMap<>();
//                            data2.put("is_seen", "0");
//                            reference.child(Objects.requireNonNull(snapshot.getKey())).updateChildren(data2);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void readMessage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        Query query = reference.orderByChild("c_remove").equalTo("1");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatObjectList.size() > 0) {
                    chatObjectList.clear();
                }
                if (keys.size() > 0){
                    keys.clear();
                }

                HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
//                assert data != null;
                //  ChatObject chatObject = new ChatObject(data.get("message"), data.get("sender"), data.get("receiver"), data.get("time"), snapshot.getKey(), data.get("type"));
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatObject chatObject = ds.getValue(ChatObject.class);
                    if (chatObject.getReceiver().equals(senderId) && chatObject.getSender().equals(receiverId) ||
                            chatObject.getReceiver().equals(receiverId) && chatObject.getSender().equals(senderId)) {
                        chatObjectList.add(chatObject);
                    }
                    if (isActive) {
                        if (chatObject.getReceiver().equals(senderId)) {
                            HashMap<String, Object> data2 = new HashMap<>();
                            data2.put("is_seen", "0");
                            reference.child(ds.getKey()).updateChildren(data2);
                        }
                    }
                    keys.add(ds.getKey());
                    // key=ds.getKey();
                }
                chatAdapter = new ChatAdapter(ActivityChat.this, chatObjectList, senderId, receiverId, keys);
                chRecyclerView.setAdapter(chatAdapter);
                chRecyclerView.scrollToPosition(chatObjectList.size() - 1);
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addUserInFirebase(String msg) {
        // if (getIntent().hasExtra("chatStart")) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy,hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String m_time = sdf.format(new Date());

        Long tsLong = System.currentTimeMillis() / 1000;
        String time_stamp = tsLong.toString();

        DatabaseReference reference, customerReference;
        customerReference = FirebaseDatabase.getInstance().getReference("usersTable").child(senderId).child(receiverId);

        HashMap<String, String> data2 = new HashMap<>();
        data2.put("id", receiverId);
        data2.put("name", studioName);
        data2.put("image", studioImg);
        data2.put("last_update", m_time);
        data2.put("message", msg);
        data2.put("time_stamp", time_stamp);

        customerReference.setValue(data2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                is_create = true;
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("usersTable").child(receiverId).child(senderId);
        HashMap<String, String> data = new HashMap<>();
        data.put("id", senderId);
        data.put("name", BaseUtil.getUserName(getApplicationContext()));
        data.put("image", BaseUtil.getUserProfile(getApplicationContext()));
        data.put("last_update", m_time);
        data.put("message", msg);
        data.put("time_stamp", time_stamp);
        reference.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                is_create = true;
            }
        });
    }

    //  }
    protected void uploadFile(File file) {
        if (file != null) {
            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(context)
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .defaultBucket("davinciii-assets")
                            .s3Client(s3Client)
                            .build();
            FILE_PATH = "chat_/" + BaseUtil.getUserAccountId(context) + "-" + BaseUtil.getUserName(context).replace(" ", "").toLowerCase().trim() + "/" + System.currentTimeMillis() + file.getName();
            TransferObserver uploadObserver = transferUtility.upload(FILE_PATH, file, CannedAccessControlList.PublicReadWrite);
            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        lastUpdate(Constant.PHOTO_IMG_BASE + FILE_PATH, senderId, receiverId);
                        sendImgMessage();
                    } else if (TransferState.FAILED == state) {
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private void sendImgMessage() {
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("messages");

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy,hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateAndTime = sdf.format(new Date());

        HashMap<String, String> data2 = new HashMap<>();
        data2.put("sender", senderId);
        data2.put("receiver", receiverId);
        data2.put("message", Constant.PHOTO_IMG_BASE + FILE_PATH);
        data2.put("time", currentDateAndTime);
        data2.put("is_seen", "1");
        data2.put("s_remove", "1");
        data2.put("c_remove", "1");
        data2.put("type", "image");


        reference.push().setValue(data2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

        DatabaseReference c_reference;
        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(senderId);
        c_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
                    token = data.get("token");
                    type = data.get("type");
                    if (type == null) {
                        type = "A";
                    }
                    sendmNotification("\uD83D\uDCF7 photo", senderId, type, BaseUtil.getUserName(context));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void shareCanvas(String message, String canvasImage, String canvasId, String artBy) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy,hh:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateAndTime = sdf.format(new Date());
        HashMap<String, String> data2 = new HashMap<>();

        data2.put("sender", senderId);
        data2.put("receiver", receiverId);
        data2.put("message", message);
        data2.put("time", currentDateAndTime);
        data2.put("is_seen", "1");
        data2.put("b_remove", "1");
        data2.put("c_remove", "1");
        data2.put("type", "share");
        data2.put("canvasImage", canvasImage);
        data2.put("canvasId", canvasId);
        data2.put("art_by", artBy);
        reference = FirebaseDatabase.getInstance().getReference("messages");
        reference.push().setValue(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(ChatActivity.this, "Send chat", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(ChatActivity.this, "Unable to send chat.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference c_reference;
        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(receiverId);
        c_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
                    token = data.get("token");
                    type = data.get("type");
                    sendmNotification(message, senderId, type, BaseUtil.getUserName(context));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendmNotification(String msg, String c_id, String type, String name) {
        if (type.equals("I")) {
            RootModel rootModel = new RootModel(token, "high", new NotificationModel("New Message", msg, c_id, "default", BaseUtil.getUserProfile(getApplicationContext()), name));
            apiInterface.sendNotification(rootModel).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Toast.makeText(ChatMessage.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            RootModel rootModel = new RootModel(token, "high", new DataModel("New Message", c_id, msg, BaseUtil.getUserProfile(getApplicationContext()), name));
            apiInterface.sendNotification(rootModel).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Toast.makeText(ChatMessage.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == 101) {
            ArrayList<String> resultArray = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert resultArray != null;
            String path = resultArray.get(0);
            File file = new File(path);
            Uri imageUri = Uri.fromFile(new File(file.getAbsolutePath()));
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(this).getContentResolver(), imageUri);

                ExifInterface ei = new ExifInterface(path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bmp, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bmp, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bmp, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bmp;
                }

                //progress.showProgressBar();
                uploadFile(persistImage(rotatedBitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private File persistImage(Bitmap bitmap) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, System.currentTimeMillis() + ".jpg");
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    @Override
    protected void onStop() {
        isActive = false;
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        isActive = true;
        //this.updateCount();
        BaseUtil.putSenderAccount(getApplicationContext(), receiverId);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseUtil.putSenderAccount(getApplicationContext(), "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseUtil.putSenderAccount(getApplicationContext(), "");

    }

}