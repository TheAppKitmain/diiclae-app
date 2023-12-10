package com.online.davincii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;
import com.online.davincii.R;
import com.online.davincii.adapters.SU_CategoriesAdapter;
import com.online.davincii.adapters.SU_StyleAdapter;
import com.online.davincii.models.CategoriesListResponse;
import com.online.davincii.models.CategorylistResponse;
import com.online.davincii.models.RegisterRequest;
import com.online.davincii.models.registger.RegisterResponse;
import com.online.davincii.models.StyleListResponse;
import com.online.davincii.models.registger.UserProfile;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText firstName, lastName, email, userName, password, doB;
    private LinearLayout signup;
    private ImageView profileImage, studioImage, showPassword;
    private Context context;
    private TextView login, terms;
    CountryCodePicker country;
    private RecyclerView rvCatagory, rvStyle;
    private String fcmToken;
    private String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private List<CategorylistResponse> categorylist = new ArrayList<>();
    private List<CategorylistResponse> selectedCatgory = new ArrayList<>();
    private List<StyleListResponse> styleList = new ArrayList<>();
    private List<StyleListResponse> selectedStyle = new ArrayList<>();
    private Boolean isProfile = true;
    private File profileFile = null;
    private File studioFile = null;
    private Boolean isCountry = false;
    private List<Integer> cate = new ArrayList<>();
    private List<Integer> style = new ArrayList<>();
    private Calendar myCalender;
    private DatePickerDialog.OnDateSetListener date;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    protected static int count = 0;
    private List<File> imageFiles = new ArrayList<>();
    private String device_id;



    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("position", 0);
            String type = intent.getStringExtra("type");
            String isAdd = intent.getStringExtra("is_add");
            if (type.equals("0")) {
                if (isAdd.equals("1")) {
                    selectedCatgory.add(categorylist.get(pos));
                } else {
                    selectedCatgory.remove(categorylist.get(pos));
                }
            } else {
                if (isAdd.equals("1")) {
                    selectedStyle.add(styleList.get(pos));
                } else {
                    selectedStyle.remove(styleList.get(pos));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        device_id = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        credentials = new BasicAWSCredentials(getResources().getString(R.string.aws_kay), getResources().getString(R.string.hidden_kay));
        s3Client = new AmazonS3Client(credentials);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("list_data"));
        context = UserSignUp.this;
        initView();
        setOnClickListener();
        setUpCalender();
        generateFirebaseToken();
    }

    private boolean isValid() {
        if (firstName.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter Your First Name");
            return false;
        } else if (lastName.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter Your Last Name");
            return false;
        } else if (email.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter Your Email");
            return false;
        } else if (!email.getText().toString().trim().matches(emailValidation)) {
            BaseUtil.showToast(context, "Enter Your Valid Email");
            return false;
        } else if (userName.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter Your User Name");
            return false;
        } else if (password.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Enter your Password");
            return false;
        } else if (password.getText().toString().length() < 6) {
            BaseUtil.showToast(context, "Password length must be 6");
            return false;
//        } else if (!password.getText().toString().trim().matches(passRegex)) {
//            BaseUtil.showToast(context, "Password should contain at least one uppercase letter, one lowercase latter and one number");
//            return false;
        } else if (doB.getText().toString().isEmpty()) {
            BaseUtil.showToast(context, "Select Your Date Of Birth");
            return false;
        } else if (!isCountry) {
            BaseUtil.showToast(context, "Select Your Country");
            return false;
        } else if (profileFile == null) {
            BaseUtil.showToast(context, "Select Your Profile Picture");
            return false;
        } else if (studioFile == null) {
            BaseUtil.showToast(context, "Select Your Studio Picture");
            return false;
        } else if (selectedCatgory.size() == 0) {
            BaseUtil.showToast(context, "Select At least one category");
            return false;
        } else if (selectedStyle.size() == 0) {
            BaseUtil.showToast(context, "Select At least one style");
            return false;
        }
        return true;
    }

    private void setOnClickListener() {
        profileImage.setOnClickListener(this);
        studioImage.setOnClickListener(this);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        findViewById(R.id.su_dob).setOnClickListener(this);
    }

    private void initView() {
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        firstName = findViewById(R.id.su_firstname);
        lastName = findViewById(R.id.su_lastname);
        email = findViewById(R.id.su_email);
        userName = findViewById(R.id.su_username);
        password = findViewById(R.id.su_password);
        doB = findViewById(R.id.su_dob);
        country = findViewById(R.id.su_country);
        profileImage = findViewById(R.id.su_profile_img);
        studioImage = findViewById(R.id.su_studio_img);
        signup = findViewById(R.id.su_signup);
        login = findViewById(R.id.su_privacy);
        rvCatagory = findViewById(R.id.su_rvcatagories);
        rvStyle = findViewById(R.id.su_rvstyle);
        rvCatagory.setAdapter(new SU_CategoriesAdapter(categorylist));
        rvStyle.setAdapter(new SU_StyleAdapter(styleList));
        terms= findViewById(R.id.su_terms);

        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        terms.setPaintFlags(terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        categ_style();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.BASELINE);
        rvCatagory.setLayoutManager(layoutManager);
        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(context);
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager1.setFlexWrap(FlexWrap.WRAP);
        layoutManager1.setAlignItems(AlignItems.BASELINE);
        rvStyle.setLayoutManager(layoutManager1);
        country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                isCountry = true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.su_profile_img:
                isProfile = true;
                openGallery();
                break;
            case R.id.su_studio_img:
                isProfile = false;
                openGallery();
                break;
            case R.id.su_privacy:
               // startActivity(new Intent(context, UserLogin.class));
                break;
            case R.id.su_dob:
                openCalenderPick();
                break;
            case R.id.su_signup:
                if (isValid()) {
                    if (profileFile != null && studioFile != null) {
                        imageFiles.clear();
                        imageFiles.add(profileFile);
                        imageFiles.add(studioFile);
                        uploadFile(imageFiles);
                    } else {
                        Toast.makeText(this, "please choose image ", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void setUpCalender() {
        myCalender = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, monthOfYear);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                doB.setText(sdf.format(myCalender.getTime()));
            }
        };
        myCalender.set(1990, 00, 01);
    }

    private void openCalenderPick(){
        DatePickerDialog m_date = new DatePickerDialog(context, R.style.AppDefaultTheme, date, myCalender
                .get(Calendar.YEAR), myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH));
        m_date.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Objects.requireNonNull(m_date.getWindow()).getAttributes().windowAnimations = R.style.ScaleFromCenter;
        m_date.show();
    }

    private void signUpApi() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connection");
        }
        if (!progress.isShowing())
            progress.showProgressBar();

        for (int i = 0; i < selectedCatgory.size(); i++) {
            cate.add(selectedCatgory.get(i).getId());
        }
        for (int i = 0; i < selectedStyle.size(); i++) {
            style.add(selectedStyle.get(i).getId());
        }

        RegisterRequest request = new RegisterRequest(
                firstName.getText().toString().trim(),
                lastName.getText().toString().trim(),
                email.getText().toString().trim(),
                userName.getText().toString().trim(),
                doB.getText().toString().trim(),
                country.getSelectedCountryName().toString(),
                profileFile.getName(),
                studioFile.getName(),
                password.getText().toString().trim(),
                cate,
                style,
                fcmToken,
                "android",
                device_id);

        apiInterface.userRegister(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()){
                    BaseUtil.showToast(context, response.body().getMessage());
                    if (response.body().getError().equals("0")) {
                        UserSignUp.this.finishAffinity();
                        BaseUtil.putUserLogIn(UserSignUp.this, true);
                        BaseUtil.putUserToken(UserSignUp.this, response.body().getToken());
                        BaseUtil.putCurrency(UserSignUp.this, response.body().getCurrency());
                        BaseUtil.putUserName(UserSignUp.this,response.body().getUserProfile().getUsername());
                        BaseUtil.putUserAccount(UserSignUp.this,response.body().getUserProfile().getUserid());
                        BaseUtil.putUserProfile(UserSignUp.this,response.body().getUserProfile().getProfilePicture());
                        saveToken(response.body().getUserProfile().getUserid());
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Failed To Connect With Server ");
                progress.hideProgressBar();
            }
        });
    }

    private void categ_style() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connection");
        }
        progress.showProgressBar();
        apiInterface.getCategories().enqueue(new Callback<CategoriesListResponse>() {
            @Override
            public void onResponse(Call<CategoriesListResponse> call, Response<CategoriesListResponse> response) {
                if (response.isSuccessful()) {
                    //  BaseUtil.showToast(context, response.body().getMessage());
                    if (response.body().getError().equals("0")) {
                        categorylist.addAll(response.body().categorylistResponse);
                        rvCatagory.getAdapter().notifyDataSetChanged();
                        styleList.addAll(response.body().styleListResponse);
                        rvStyle.getAdapter().notifyDataSetChanged();
                        // startActivity(new Intent(context, SignIn.class));
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progress.hideProgressBar();
            }
            @Override
            public void onFailure(Call<CategoriesListResponse> call, Throwable t) {
                BaseUtil.showToast(context, "Failed To Connect With Server ");
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
        Pix.start(UserSignUp.this, options);
    }

    private void uploadStudioProfile(TransferUtility transferUtility, File file) {
        TransferObserver studioObserver = transferUtility.upload("StudioPic/" + file.getName(), file, CannedAccessControlList.PublicReadWrite);

        studioObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    signUpApi();
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

    protected void uploadFile(List<File> fileList) {
        progress.showProgressBar();
        if (fileList != null) {

            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(getApplicationContext())
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .defaultBucket("davinciii-assets")
                            .s3Client(s3Client)
                            .build();
            TransferObserver uploadObserver = transferUtility.upload("ProfilePic/" + fileList.get(0).getName(), fileList.get(0), CannedAccessControlList.PublicReadWrite);

            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        if (count < fileList.size()) {
                            uploadStudioProfile(transferUtility, fileList.get(1));
                        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //profileUpdate = true;
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(context).getContentResolver(), resultUri);
                    if (isProfile) {
                        Glide.with(context).load(bmp).into(profileImage);
                        profileFile = persistImage(bmp);
                    } else {
                        Glide.with(context).load(bmp).into(studioImage);
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
            for (int i = 0; i < resultArray.size(); i++){
                String path = resultArray.get(i);
                File file = new File(path);
                Uri imageUri = Uri.fromFile(new File(file.getAbsolutePath()));

                if (isProfile){
                    CropImage.activity(imageUri)
                            .setAspectRatio(1, 1)
                            .start(UserSignUp.this);
                }else{
                    CropImage.activity(imageUri)
                            .setAspectRatio(3, 2)
                            .start(UserSignUp.this);
                }
            }
        }
    }

    private File persistImage(Bitmap bitmap){
        File filesDir = context.getApplicationContext().getFilesDir();
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
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    // TODO save firebase token to firebase db
    private void saveToken(String payload) {
        DatabaseReference c_reference;
        c_reference = FirebaseDatabase.getInstance().getReference("firebase_token").child(String.valueOf(payload));
        HashMap<String, String> data2 = new HashMap<>();
        data2.put("token", fcmToken);
        data2.put("type","A");
        startActivity(new Intent(UserSignUp.this, DashboardScreen.class));
        finish();
        c_reference.setValue(data2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    private void generateFirebaseToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        fcmToken = task.getResult();
                    }
                });
    }
}


