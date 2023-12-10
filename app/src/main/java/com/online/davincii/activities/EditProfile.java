package com.online.davincii.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.online.davincii.R;
import com.online.davincii.adapters.ProfileCategoryAdapter;
import com.online.davincii.adapters.ProfileStyleAdapter;
import com.online.davincii.models.CategorylistResponse;
import com.online.davincii.models.EditProfileRequest;
import com.online.davincii.models.EditProfileResponse;
import com.online.davincii.models.ProfileCategories;
import com.online.davincii.models.ProfileResponse;
import com.online.davincii.models.ProfileStyles;
import com.online.davincii.models.StyleListResponse;
import com.online.davincii.models.UserProfileData;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText firstname, lastname, username, bio, link;
    private TextView headername, savebtn;
    private Context context;
    private ImageView imagemain, profileimage, settingbtn, editbtnone, editbtntwo, backbtn;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private Boolean isProfile = true;
    private File profileFile = null;
    private File studioFile = null;
    protected static int count = 0;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    public UserProfileData profileData;
    private boolean isProfilePicChanged = false;
    private boolean isStudioPicChanged = false;
    private TransferUtility transferUtility;
    private String user;
    private RecyclerView catRecycler, styleRecycler;
    private List<ProfileCategories> categorylist = new ArrayList<>();
    private List<ProfileStyles> styleList = new ArrayList<>();
    private List<ProfileCategories> selectedCatgory = new ArrayList<>();
    private List<ProfileStyles> selectedStyle = new ArrayList<>();
    private GlobalProgressDialog progressDialog;
    private List<Integer> cate = new ArrayList<>();
    private List<Integer> style = new ArrayList<>();

    private List<File> imageFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        credentials = new BasicAWSCredentials(getResources().getString(R.string.aws_kay), getResources().getString(R.string.hidden_kay));
        s3Client = new AmazonS3Client(credentials);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("cat_list"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2, new IntentFilter("style_list"));

        if (getIntent().hasExtra("data")) {
            profileData = (UserProfileData) getIntent().getSerializableExtra("data");
            // headername.setText(profileData.getUsername());
        }
        context = EditProfile.this;
        initView();
        callUsPrFrag();
        setOnclickListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ep_backbtn:
                EditProfile.super.onBackPressed();
                break;
            case R.id.ep_savebtn:
                if (isValidForm()) {
                    if ((profileFile == null) && (studioFile == null)) {
                        updateUser();
                    } else {
                        if (profileFile != null) {
                            uploadFile(profileFile);
                        } else if (studioFile != null) {
                            uploadStudioProfile(studioFile);
                        }
                    }
                }
                break;
            case R.id.ep_setting:
                PopupMenu popup = new PopupMenu(context, settingbtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.mnu_settings) {
                            startActivity(new Intent(EditProfile.this, Settings.class));
                        }
                        return true;
                    }
                });

                popup.show();
        }
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        firstname = findViewById(R.id.ep_firstname);
        lastname = findViewById(R.id.ep_lastname);
        username = findViewById(R.id.ep_username);
        bio = findViewById(R.id.ep_bio);
        link = findViewById(R.id.ep_link);
        headername = findViewById(R.id.ep_headername);
        savebtn = findViewById(R.id.ep_savebtn);
        imagemain = findViewById(R.id.ep_imageView2);
        profileimage = findViewById(R.id.ep_profile_image);
        settingbtn = findViewById(R.id.ep_setting);
        editbtnone = findViewById(R.id.ep_editbtn_one);
        editbtntwo = findViewById(R.id.ep_editbtn_two);
        backbtn = findViewById(R.id.ep_backbtn);
        headername.setText(profileData.getUsername());
        catRecycler = findViewById(R.id.ep_catRecycler);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.BASELINE);
        catRecycler.setLayoutManager(layoutManager);

        styleRecycler = findViewById(R.id.ep_styleRecycler);
        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(context);
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager1.setFlexWrap(FlexWrap.WRAP);
        layoutManager1.setAlignItems(AlignItems.BASELINE);
        styleRecycler.setLayoutManager(layoutManager1);

        progressDialog = new GlobalProgressDialog(context);

        if (profileData != null) {
            Glide.with(EditProfile.this).load(Constant.PROFILE_IMG_BASE + profileData.getProfilePicture()).into(profileimage);
            Glide.with(EditProfile.this).load(Constant.STUDIO_IMG_BASE + profileData.getStudioPicture()).into(imagemain);
            firstname.setText(profileData.getFirstName());
            lastname.setText(profileData.getLastName());
            bio.setText(profileData.getBio());
            username.setText(profileData.getUsername());
            link.setText(profileData.getLink());
        }

        transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .defaultBucket("davinciii-assets")
                        .s3Client(s3Client)
                        .build();
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("position", 0);
            String isAdd = intent.getStringExtra("is_add");
                if (isAdd.equals("1")) {
                    selectedCatgory.add(categorylist.get(pos));
                } else {
                    selectedCatgory.remove(categorylist.get(pos));
                }
        }
    };

    public BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("position", 0);
            String isStyle=intent.getStringExtra("is_style");
            if (isStyle.equals("1")) {
                selectedStyle.add(styleList.get(pos));
            } else {
                selectedStyle.remove(styleList.get(pos));
            }
        }
    };


    private void setOnclickListener() {
        backbtn.setOnClickListener(this);
        settingbtn.setOnClickListener(this);
        savebtn.setOnClickListener(this);
    }


    private boolean isValidForm() {

        if (firstname.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter first Name");
            return false;
        } else if (lastname.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter last name");
            return false;
        } else if (username.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter user name");
            return false;
        } else if (bio.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter bio");
            return false;
        } else if (link.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter link");
            return false;
        }
        return true;
    }

    private void updateUser() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your internet connectivity");
            return;
        }
        progress.showProgressBar();

        for (int i = 0; i < selectedCatgory.size(); i++) {
            cate.add(selectedCatgory.get(i).getId());
        }
        for (int i = 0; i < selectedStyle.size(); i++) {
            style.add(selectedStyle.get(i).getId());
        }

        EditProfileRequest request = new EditProfileRequest();
        request.setFirstname(firstname.getText().toString());
        request.setLastname(lastname.getText().toString());
        request.setUsername(username.getText().toString());
        request.setBio( bio.getText().toString());
        request.setLink(link.getText().toString());
        if (profileFile != null)
            request.setProfilepic(profileFile.getName());
        if (studioFile != null)
            request.setStudiopic(studioFile.getName());
        request.setCategory(cate);
        request.setStyles(style);


        apiInterface.editProfile("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        finish();
                    }
                    BaseUtil.showToast(context, response.body().getMessage());
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Server error");
                progress.hideProgressBar();
            }
        });
    }

    private void openGallery() {
        Options options = Options.init()
                .setRequestCode(101)
                .setCount(1)
                .setFrontfacing(false)
                .setExcludeVideos(true)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);
        Pix.start(EditProfile.this, options);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //   profileUpdate = true;
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(context).getContentResolver(), resultUri);
                    if (isProfile) {
                        Glide.with(context).load(bmp).into(profileimage);
                        profileFile = persistImage(bmp);
                    } else {
                        Glide.with(context).load(bmp).into(imagemain);
                        studioFile = persistImage(bmp);
                    }
                    //   mediaFile = persistImage(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (data != null && resultCode == RESULT_OK && requestCode == 101) {
            ArrayList<String> resultArray = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert resultArray != null;
            for (int i = 0; i < resultArray.size(); i++) {
                String path = resultArray.get(i);
                File file = new File(path);
                Uri imageUri = Uri.fromFile(new File(file.getAbsolutePath()));
                if (isProfile){
                    CropImage.activity(imageUri)
                            .setAspectRatio(1, 1)
                            .start(EditProfile.this);
                }else{
                    CropImage.activity(imageUri)
                            .setAspectRatio(3, 2)
                            .start(EditProfile.this);
                }

            }
        }
    }

    private void uploadStudioProfile(File file) {
        TransferObserver studioObserver = transferUtility.upload("StudioPic/" + file.getName(), file, CannedAccessControlList.PublicReadWrite);
        studioObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    updateUser();
                } else if (TransferState.FAILED == state) {
                    progress.hideProgressBar();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }
            @Override
            public void onError(int id, Exception ex) {
            }
        });
    }

    protected void uploadFile(File profileFile) {
        progress.showProgressBar();
        if (profileFile != null) {
            TransferObserver uploadObserver = transferUtility.upload("ProfilePic/" + profileFile.getName(), profileFile, CannedAccessControlList.PublicReadWrite);
            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        if (studioFile != null)
                            uploadStudioProfile(studioFile);
                        else
                            updateUser();
                    } else if (TransferState.FAILED == state) {
                        progress.hideProgressBar();
                    }
                }
                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
// tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
                }
                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                    progress.hideProgressBar();
                }
            });
        }
    }

    private void callUsPrFrag() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connectivity");
            return;
        }
        progressDialog.showProgressBar();
        apiInterface.getProfile("Bearer " + BaseUtil.getUserToken(getApplicationContext())).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getError().equals("0")) {
                        if (response.body().getData() != null) {
                            firstname.setText(response.body().getData().getFirstName());
                            lastname.setText(response.body().getData().getLastName());
                            bio.setText(response.body().getData().getBio());
                            link.setText(String.valueOf(response.body().getData().getLink()));
                            profileData = response.body().getData();
                            Glide.with(EditProfile.this).load(Constant.PROFILE_IMG_BASE + response.body().getData().profilePicture).into(profileimage);
                            Glide.with(EditProfile.this).load(Constant.STUDIO_IMG_BASE + response.body().getData().getStudioPicture()).into(imagemain);

                            categorylist.addAll(response.body().getCategories());
                            ProfileCategoryAdapter adapter=new ProfileCategoryAdapter(categorylist);
                            catRecycler.setAdapter(adapter);

                            styleList.addAll(response.body().getStyles());
                            ProfileStyleAdapter adapter1=new ProfileStyleAdapter(styleList);
                            styleRecycler.setAdapter(adapter1);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private File persistImage(Bitmap bitmap) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, System.currentTimeMillis() + ".jpg");
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }

    public void imgBtnClick(View view) {
        isProfile = true;
        openGallery();
    }

    public void studioimgBtnClick(View view) {
        isProfile = false;
        openGallery();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver2);
        super.onDestroy();
    }

}