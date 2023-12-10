package com.online.davincii.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.online.davincii.R;
import com.online.davincii.adapters.CategoryAdapter;
import com.online.davincii.adapters.PriceAdapter;
import com.online.davincii.adapters.StyleAdpater;
import com.online.davincii.models.AddCreationRequest;
import com.online.davincii.models.AddCreationResponse;
import com.online.davincii.models.CanvasSizeData;
import com.online.davincii.models.CanvasSizeRequest;
import com.online.davincii.models.CanvasSizeResponse;

import com.online.davincii.models.PriceData;
import com.online.davincii.models.ProfileCategories;
import com.online.davincii.models.ProfileResponse;
import com.online.davincii.models.ProfileStyles;
import com.online.davincii.models.StyleListResponse;
import com.online.davincii.models.UserProfileCategory;
import com.online.davincii.models.UserProfileRespose;
import com.online.davincii.models.UserProfileStyle;
import com.online.davincii.models.sales_report.ReportResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.Constant;
import com.online.davincii.utils.GlobalProgressDialog;
import com.online.davincii.utils.HashTagEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Upload extends AppCompatActivity implements View.OnClickListener {
    private ImageView back, square, portrait, landscape;
    private TextView addPiece, categoryOne, categoryTwo, addHasHTag, Add;
    private EditText titleEditText, statementEditText;
    private RecyclerView rv_CategOne, rv_CategTwo;
    private Context context;
    private GlobalProgressDialog progressDialog;
    private ApiClient.APIInterface apiInterface;
    private List<ProfileCategories> categorylist = new ArrayList<>();
    private List<ProfileStyles> selectedCatgory = new ArrayList<>();
    private int catId, styleId;
    private String canvesId;
    private HashTagEditText hashTagText;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    private String img;
    private Integer catselectedId = -1,position;
    private Integer stySelectedId = -1;
    private CategoryAdapter catAdapter;
    private StyleAdpater styAdapter;
    private PriceAdapter priceAdapter;
    private RecyclerView sizeRecyclerView;
    private List<CanvasSizeData> sList=new ArrayList<>();
    private Long sizeId;
    private String canvasSize;
    private Double price;
    private CheckBox checkBox;
  //  private ImageView editIcon;
    private String edit="0";

    private List<PriceData> priceDataList=new ArrayList<>();

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("type").equals("categoryId")) {
                catId = intent.getIntExtra("catId",0);
                position=intent.getIntExtra("position",0);
                position = catId;
                catAdapter.notifyDataSetChanged();
            } else {
                styleId = intent.getIntExtra("styleId",0);
                position=intent.getIntExtra("position",0);
                position = styleId;
                styAdapter.notifyDataSetChanged();
            }
        }
    };

    public BroadcastReceiver priceReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              int mPos =intent.getIntExtra("position", 0);
              String mPrice = intent.getStringExtra("price");
              if (!mPrice.equals("")){
                  sList.get(mPos).setPrice(Double.valueOf(mPrice));
              }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("get_id"));
        LocalBroadcastManager.getInstance(this).registerReceiver(priceReceiver, new IntentFilter("add_price"));

        canvesId = getIntent().getStringExtra("canvesId");
        context = Upload.this;

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initView();
        onClickListener();
        getSizeList();

        // callAddCreationApi();
    }

    private void initView() {
        credentials = new BasicAWSCredentials(getResources().getString(R.string.aws_kay), getResources().getString(R.string.hidden_kay));
        s3Client = new AmazonS3Client(credentials);
        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(Upload.this);
        back = findViewById(R.id.upd_back);
        square = findViewById(R.id.upd_square);
        portrait = findViewById(R.id.upd_portrait);
        landscape = findViewById(R.id.upd_landscape);
        addPiece = findViewById(R.id.upd_addPiece);
        categoryOne = findViewById(R.id.upd_categ_one);
        categoryTwo = findViewById(R.id.upd_categ_two);
        addHasHTag = findViewById(R.id.upd_addHastag);
        rv_CategOne = findViewById(R.id.upd_rv_categOne);
        rv_CategTwo = findViewById(R.id.upd_rv_categTwo);
        titleEditText = findViewById(R.id.upd_title);
        statementEditText = findViewById(R.id.upd_writeState);
        sizeRecyclerView=findViewById(R.id.upd_rv_sizes);
        //editIcon=findViewById(R.id.upd_edit);
        checkBox=findViewById(R.id.up_checkBox);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        sizeRecyclerView.setLayoutManager(gridLayoutManager);
        sizeRecyclerView.setHasFixedSize(true);

        catAdapter = new CategoryAdapter(categorylist, catselectedId);
        styAdapter = new StyleAdpater(selectedCatgory, stySelectedId);
        rv_CategOne.setAdapter(catAdapter);
        rv_CategTwo.setAdapter(styAdapter);

        img = getIntent().getStringExtra("img");
        hashTagText = findViewById(R.id.upd_EditHashTag);

        if (canvesId.equals("0")) {
            square.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(img).into(square);
        } else if (canvesId.equals("1")) {
            landscape.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(img).into(landscape);
        } else {
            portrait.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(img).into(portrait);
        }

        categ_style();

        FlexboxLayoutManager flexCatOne = new FlexboxLayoutManager(context);
        flexCatOne.setFlexDirection(FlexDirection.ROW);
        flexCatOne.setJustifyContent(JustifyContent.FLEX_START);
//        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_CategOne.setLayoutManager(flexCatOne);

        FlexboxLayoutManager flexCatTwo = new FlexboxLayoutManager(context);
        flexCatTwo.setFlexDirection(FlexDirection.ROW);
        flexCatTwo.setJustifyContent(JustifyContent.FLEX_START);
//        StaggeredGridLayoutManager staggeredGridLayoutManager1=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_CategTwo.setLayoutManager(flexCatTwo);

//        hashTagText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(HashTagEditText.mValuesSpan.size()>=3){
//                    hashTagText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(s.length())});
//                }else{
//                    hashTagText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    private void onClickListener() {
        addPiece.setOnClickListener(this);
        back.setOnClickListener(this);
       // editIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upd_back:
                finish();
                break;
            case R.id.upd_addPiece:
                if (statementEditText.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show();
                }else if (catId==0){
                    Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show();
                }else if (styleId==0){
                    Toast.makeText(context, "Please select style", Toast.LENGTH_SHORT).show();
                } //else if (TextUtils.isEmpty(hashTagText.getText().toString())) {
                   // Toast.makeText(context, "Please add hash tag", Toast.LENGTH_SHORT).show();
                 else {

                    if (checkBox.isChecked()){
                        uploadFile(new File(img));
                    }else{
                        Toast.makeText(Upload.this, "Please accepts terms.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
//            case R.id.upd_edit:
//                edit="1";
//                getSizeList();
//                break;
        }
    }

    private void callAddCreationApi(String img) {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check Your Internet Connectivity");
            return;
        }

        for (int i=0; sList.size() > i; i++){
               price=sList.get(i).getPrice();
               sizeId=sList.get(i).getId();
               canvasSize=sList.get(i).getSize();

             PriceData priceData=new PriceData(canvasSize,price,sizeId);
             priceDataList.add(priceData);
        }

//        progressDialog.showProgressBar();
        AddCreationRequest request = new AddCreationRequest(canvesId,
                img,
                HashTagEditText.mValuesSpan.size()==1?HashTagEditText.mValuesSpan.get(0).getText():"",
//                HashTagEditText.mValuesSpan.size()==2?HashTagEditText.mValuesSpan.get(1).getText():"",
//                HashTagEditText.mValuesSpan.size()==3?HashTagEditText.mValuesSpan.get(2).getText():"",
                String.valueOf(styleId),
                statementEditText.getText().toString(),
                statementEditText.getText().toString(),
                String.valueOf(catId),
                true,
                priceDataList);

        apiInterface.addCreation("Bearer " + BaseUtil.getUserToken(getApplicationContext()), request).enqueue(new Callback<AddCreationResponse>() {
            @Override
            public void onResponse(Call<AddCreationResponse> call, Response<AddCreationResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Upload.this, DashboardScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AddCreationResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void categ_style() {
        if (!BaseUtil.isNetworkAvailable(context)) {
            BaseUtil.showToast(context, "Check your Internet Connection");
        }
        progressDialog.showProgressBar();
        apiInterface.getProfile("Bearer " + BaseUtil.getUserToken(getApplicationContext())).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    //  BaseUtil.showToast(context, response.body().getMessage());
                    if (response.body().getError().equals("0")) {
                        categorylist.addAll(response.body().getCategories());
                        rv_CategOne.getAdapter().notifyDataSetChanged();
                        selectedCatgory.addAll(response.body().getStyles());
                        rv_CategTwo.getAdapter().notifyDataSetChanged();
                        // startActivity(new Intent(context, SignIn.class));
                    }
                } else {
                    BaseUtil.showToast(context, "Server error");
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                BaseUtil.showToast(context, "Failed To Connect With Server ");

            }
        });
    }

    protected void uploadFile(File file) {
        progressDialog.showProgressBar();
        if (file != null) {
            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(getApplicationContext())
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .defaultBucket("davinciii-assets")
                            .s3Client(s3Client)
                            .build();
            TransferObserver uploadObserver = transferUtility.upload("ProfilePic/" + file.getName(), file, CannedAccessControlList.PublicReadWrite);

            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        callAddCreationApi(file.getName());
                    } else if (TransferState.FAILED == state) {
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
                }
            });
        }

    }

    private void getSizeList(){

        CanvasSizeRequest request=new CanvasSizeRequest(Integer.parseInt(canvesId));
        apiInterface.getCanvasSizes("Bearer "+BaseUtil.getUserToken(this),request).enqueue(new Callback<CanvasSizeResponse>() {
            @Override
            public void onResponse(Call<CanvasSizeResponse> call, Response<CanvasSizeResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getError().equals("0")){
                       sList=response.body().getData();
                       priceAdapter=new PriceAdapter(sList,edit);
                       sizeRecyclerView.setAdapter(priceAdapter);

                    }
                }
            }
            @Override
            public void onFailure(Call<CanvasSizeResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(priceReceiver);
        super.onDestroy();
    }


}