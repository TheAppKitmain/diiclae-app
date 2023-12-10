package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.online.davincii.models.comments.CommentList;
import com.online.davincii.R;
import com.online.davincii.adapters.CommentAdapter;
import com.online.davincii.models.comments.AllCommentResponse;
import com.online.davincii.models.comments.AllCommentsRequest;
import com.online.davincii.models.comments.CommentMakeRequest;
import com.online.davincii.models.comments.CommentMakeResponse;
import com.online.davincii.models.comments.ReplyCommentRequest;
import com.online.davincii.models.comments.ReplyCommentResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Comments extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private RecyclerView rvComment, rvReplyDown;
    private ImageView backBtn, sendImg;
    private CircleImageView userImg;
    private EditText comment;
    private List<CommentList> commentLists = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private Integer totalPage = 0,pageno = 1,canvasid;
    private boolean apiHit = false;
    private String commentId,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        intialize();
        setOnClickListener();
    }

    private void intialize() {
        context = Comments.this;
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        backBtn = findViewById(R.id.cmt_backBtn);
        sendImg = findViewById(R.id.cmt_sendImg);
        userImg = findViewById(R.id.cmt_userImg);
        comment = findViewById(R.id.cmt_comment);
        rvComment = findViewById(R.id.cmt_rv);
//        rvReplyDown = findViewById(R.id.ct_rv_replydown);

        LocalBroadcastManager.getInstance(context).registerReceiver(mBroadcast, new IntentFilter("Data_List_two"));

        canvasid = getIntent().getIntExtra("canvas_id", 0);

        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvComment.setLayoutManager(linearLayout);

        Picasso.get().load(BaseUtil.getUserProfile(this)).error(R.drawable.ic_user).into(userImg);

        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (linearLayout != null && linearLayout.findLastVisibleItemPosition() == commentLists.size() - 1) {
                        if (!apiHit) {
                            apiHit = true;
                            pageno++;
                            if (pageno <= totalPage) {
                                allCommentApi(canvasid, pageno);
                            }
                        }
                    }
                }
            }
        });
        allCommentApi(canvasid, 1);

    }

    public BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            type=intent.getStringExtra("type");
            commentId=intent.getStringExtra("comment_id");
            if (intent.getStringExtra("type").equals("reply")) {
                comment.setText(intent.getStringExtra("user_name"));
            }
        }
    };

    private void setOnClickListener() {
        backBtn.setOnClickListener(this);
        sendImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmt_backBtn:
                finish();
                break;
            case R.id.cmt_sendImg:
                if (type!=null){
                if (type.equals("reply")){
                    if(!comment.getText().toString().isEmpty()){
                        replyOnComment(Integer.parseInt(commentId), comment.getText().toString());
                    }
                }
                }else{
                    makeCommentApi(canvasid, comment.getText().toString().trim());
                }
                break;
        }
    }

    private void allCommentApi(Integer id, Integer pageno) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connection");
            return;
        }
        progress.showProgressBar();
        AllCommentsRequest request = new AllCommentsRequest(id, pageno);
        apiInterface.allComment("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<AllCommentResponse>() {
            @Override
            public void onResponse(Call<AllCommentResponse> call, Response<AllCommentResponse> response) {
                apiHit = false;
                // replyList.clear();
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData().size() > 0) {
                            totalPage = response.body().getNextPage();
                            if(pageno>1) {
                                commentLists.addAll(response.body().getData());
                            }else{
                                commentLists = response.body().getData();
                            }
                            commentAdapter=new CommentAdapter(commentLists,getApplicationContext());
                            rvComment.setAdapter(commentAdapter);
//                            rvReplyDown.getAdapter().notifyDataSetChanged();
                        }
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server Error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AllCommentResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, t.getMessage());
            }
        });
    }

    private void makeCommentApi(Integer id, String message) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();
        CommentMakeRequest request = new CommentMakeRequest(id, message);
        apiInterface.commentMake("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<CommentMakeResponse>() {
            @Override
            public void onResponse(Call<CommentMakeResponse> call, Response<CommentMakeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        allCommentApi(canvasid, 1);
                        comment.setText("");
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server Error");
                }

                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<CommentMakeResponse> call, Throwable t) {
                progress.hideProgressBar();
                BaseUtil.showToast(context, t.getMessage());
            }
        });
    }

    private void replyOnComment(Integer id, String message) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        ReplyCommentRequest request = new ReplyCommentRequest(id, message);
        ApiClient.getClient().replyComment("Bearer " + BaseUtil.getUserToken(context), request).enqueue(new Callback<ReplyCommentResponse>() {
            @Override
            public void onResponse(Call<ReplyCommentResponse> call, Response<ReplyCommentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData().size() > 0) {
                            comment.setText("");
                        }
                        allCommentApi(canvasid, 1);
                    } else {
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                } else {
                    BaseUtil.showToast(context, "Server Error");
                }
            }
            @Override
            public void onFailure(Call<ReplyCommentResponse> call, Throwable t) {
                BaseUtil.showToast(context, t.getMessage());
            }
        });
    }
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mBroadcast);
        super.onDestroy();
    }
}