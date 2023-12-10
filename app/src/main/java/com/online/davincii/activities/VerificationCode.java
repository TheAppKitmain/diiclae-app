package com.online.davincii.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.online.davincii.R;
import com.online.davincii.models.VerificationcodeRequest;
import com.online.davincii.models.VerificationcodeResponse;
import com.online.davincii.networking.ApiClient;
import com.online.davincii.utils.BaseUtil;
import com.online.davincii.utils.GlobalProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ClipDescription.MIMETYPE_TEXT_HTML;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class VerificationCode extends AppCompatActivity implements View.OnClickListener {

    private EditText digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix;
    private TextView submit;
    private Context context;
    private ApiClient.APIInterface apiInterface;
    private GlobalProgressDialog progress;
    private ClipboardManager clipboardManager;
    private String clipBoardText, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        context = VerificationCode.this;
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vc_submit:
                String otp = digitOne.getText().toString().trim()
                        + digitTwo.getText().toString().trim()
                        + digitThree.getText().toString().trim()
                        + digitFour.getText().toString() .trim()
                        + digitFive.getText().toString().trim()
                        + digitSix.getText().toString().trim();
                if (otp.isEmpty()) {
                    BaseUtil.showToast(context, "Enter OTP to verify");
                } else if (otp.length() < 6) {
                    BaseUtil.showToast(context, "Enter valid OTP");
                } else{
                    verifyOTP(email,otp);
                }
                break;
        }
    }

    private void verifyOTP(String email,String otp) {
        if (!BaseUtil.isNetworkAvailable(VerificationCode.this)){
            Toast.makeText(context, "Not connected! Please check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.showProgressBar();
        VerificationcodeRequest request= new VerificationcodeRequest(email,otp);
        apiInterface.verificationCode(request).enqueue(new Callback<VerificationcodeResponse>() {
            @Override
            public void onResponse(Call<VerificationcodeResponse> call, Response<VerificationcodeResponse> response) {
                if (response.isSuccessful()){
                    if(response.body().getError().equals("0")) {
                        BaseUtil.showToast(context, response.body().getMessage());
                        startActivity(new Intent(VerificationCode.this,ResetPassword.class).putExtra("userId",response.body().getData()));
                    }
                    else{
                        BaseUtil.showToast(context, response.body().getMessage());
                    }
                }else {
                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                }
                progress.hideProgressBar();
            }

            @Override
            public void onFailure(Call<VerificationcodeResponse> call, Throwable t) {
                progress.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView() {
        email =getIntent().getStringExtra("email");
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        apiInterface = ApiClient.getClient();
        progress = new GlobalProgressDialog(context);
        digitOne = findViewById(R.id.vc_digitone);
        digitTwo = findViewById(R.id.vc_digittwo);
        digitThree = findViewById(R.id.vc_digitthree);
        digitFour = findViewById(R.id.vc_digitfour);
        digitFive = findViewById(R.id.vc_digitfive);
        digitSix = findViewById(R.id.vc_digitsix);
        submit = findViewById(R.id.vc_submit);

        digitOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitTwo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitThree.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitFour.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitFive.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitSix.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pasteOtp();
                return true;
            }
        });

        digitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    digitTwo.requestFocus();
                } else if (digitTwo.length() == 0) {
                    digitOne.requestFocus();
                }
            }
        });

        digitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    digitThree.requestFocus();
                }
                if(s.length()==0){
                    digitOne.requestFocus();
                    digitOne.setSelection(1);
                }
            }
        });

        digitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    digitFour.requestFocus();
                }
                if(s.length()==0){
                    digitTwo.requestFocus();
                    digitTwo.setSelection(1);
                }
            }
        });

        digitFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    digitFive.requestFocus();
                }
                if(s.length()==0){
                    digitThree.requestFocus();
                    digitThree.setSelection(1);
                }
            }
        });

        digitFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    digitSix.requestFocus();
                }
                if(s.length()==0){
                    digitFour.requestFocus();
                    digitFour.setSelection(1);
                }
            }
        });

        digitSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0){
                    digitFive.requestFocus();
                    digitFive.setSelection(1);
                }
            }
        });
    }

    private void pasteOtp() {
        if (clipboardManager.hasPrimaryClip() && (clipboardManager.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN) || clipboardManager.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_HTML))) {
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            clipBoardText = getOnlyNumbers(item.getText().toString());;
        }
        if (clipBoardText.length()<6){
            BaseUtil.showToast(getApplicationContext(),"Please enter valid otp");
        }else{
            digitOne.setText(clipBoardText.substring(0));
            digitTwo.setText(clipBoardText.substring(1));
            digitThree.setText(clipBoardText.substring(2));
            digitFour.setText(clipBoardText.substring(3));
            digitFive.setText(clipBoardText.substring(4));
            digitSix.setText(clipBoardText.substring(5));
            digitSix.requestFocus();
            digitSix.setSelection(1);
        }
    }

    public String getOnlyNumbers(String str) {
        str = str.replaceAll("[^\\d.]", "");
        return str;
    }
}