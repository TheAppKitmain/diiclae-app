package com.online.davincii.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.online.davincii.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class New_Canvas extends AppCompatActivity implements View.OnClickListener {
    private ImageView backBtn, showImage_One, showImage_Two, showImage_Three;
    private TextView next;
    private Context context;
    private CardView uploadImg;
    private int value;
    private Uri imguri, resultUri;
    private File file;
    private String path, imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_canvas);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        initialize();
        setOnCLickListener();
    }

    private void initialize() {
        context = New_Canvas.this;
        showImage_One = findViewById(R.id.nc_show_img_one);
        showImage_Two = findViewById(R.id.nc_show_img_two);
        showImage_Three = findViewById(R.id.nc_show_img_three);
        backBtn = findViewById(R.id.nc_back);
        uploadImg = findViewById(R.id.nc_upload_img);
        next = findViewById(R.id.nc_next);

        if (getIntent().hasExtra("img")) {
            value = getIntent().getIntExtra("img", 0);
            switch (value) {
                case 0:
                    // showImage_One.setImageResource(R.drawable.canvas_one);
                    findViewById(R.id.relativeOne).setVisibility(View.VISIBLE);
                    findViewById(R.id.relativeTwo).setVisibility(View.GONE);
                    findViewById(R.id.relativeThree).setVisibility(View.GONE);
                   // showImage_One.setBackground(New_Canvas.this.getResources().getDrawable(R.drawable.canvus_border));
                    break;
                case 1:
                    // showImage_Two.setImageResource(R.drawable.canvas_two);
                    findViewById(R.id.relativeOne).setVisibility(View.GONE);
                    findViewById(R.id.relativeTwo).setVisibility(View.VISIBLE);
                    findViewById(R.id.relativeThree).setVisibility(View.GONE);
                   // showImage_Two.setBackground(New_Canvas.this.getResources().getDrawable(R.drawable.canvus_border));
                    break;
                case 2:
                    //  showImage_Three.setImageResource(R.drawable.canvas_three);
                    findViewById(R.id.relativeOne).setVisibility(View.GONE);
                    findViewById(R.id.relativeTwo).setVisibility(View.GONE);
                    findViewById(R.id.relativeThree).setVisibility(View.VISIBLE);
                   // showImage_Three.setBackground(New_Canvas.this.getResources().getDrawable(R.drawable.canvus_border));
                    break;
            }
        }
    }

    private void setOnCLickListener() {
        backBtn.setOnClickListener(this);
        uploadImg.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nc_upload_img:
                openGallery();
                break;
            case R.id.nc_back:
                finish();
                break;
            case R.id.nc_next:
                if (imagePath == null) {
                    Toast.makeText(context, "Please select image first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, Upload.class);
                    intent.putExtra("canvesId", String.valueOf(value));
                    intent.putExtra("img", imagePath);
                    startActivity(intent);
                }
                break;
        }
    }

    private void openGallery() {
        Options options = Options.init()
                .setRequestCode(101)
                .setCount(1)
                .setFrontfacing(false)
                .setExcludeVideos(true)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);
        Pix.start(New_Canvas.this, options);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imagePath = resultUri.getPath();
                try {
                    Bitmap bmt = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(context).getContentResolver(), resultUri);
                    Glide.with(context).load(bmt).into(showImage_One);
                    Glide.with(context).load(bmt).into(showImage_Two);
                    Glide.with(context).load(bmt).into(showImage_Three);
                    // showImage = persistImg(bmt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            ArrayList<String> resultArray = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert resultArray != null;
            for (int i = 0; i < resultArray.size(); i++) {
                path = resultArray.get(i);
                file = new File(path);
                imguri = Uri.fromFile(new File(file.getAbsolutePath()));

                if (value == 0) {
                    CropImage.activity(imguri).setAspectRatio(1, 1)
                            .start(New_Canvas.this);
                } else if (value == 1) {
                    CropImage.activity(imguri).setAspectRatio(4, 3)
                            .start(New_Canvas.this);
                } else {
                    CropImage.activity(imguri).setAspectRatio(3, 4)
                            .start(New_Canvas.this);
                }
            }
        }
    }

    private File persistImg(Bitmap bitmap) {
        File fileDir = context.getApplicationContext().getFilesDir();
        File imageFile = new File(fileDir, System.currentTimeMillis() + ".jpg");
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap ", e);
        }
        return imageFile;
    }
}
