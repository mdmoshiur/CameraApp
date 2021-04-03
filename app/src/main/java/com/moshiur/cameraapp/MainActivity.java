package com.moshiur.cameraapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private Button take_photo, toast_button;
    private ImageView imageView;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        take_photo = findViewById(R.id.take_photo);
        imageView = findViewById(R.id.imageView);
        toast_button = findViewById(R.id.toast_button);

        take_photo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //Toasty.info(MainActivity.this, "onclick", Toast.LENGTH_SHORT).show();
                if (checkCameraHardware(MainActivity.this)){
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else {
                    Toast.makeText(MainActivity.this,"This phone has no camera", Toast.LENGTH_SHORT).show();
                }

            }
        });

        toast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.custom(MainActivity.this, "This is custom toast.", R.drawable.ic_android_black_24dp, R.color.teal_200, Toast.LENGTH_LONG, true, true).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toasty.success(MainActivity.this, "camera permission granted", Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toasty.error(MainActivity.this, "Permission dined", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            Glide.with(MainActivity.this).load(photo).into(imageView);
            // Load photo from url
            // Glide.with(MainActivity.this).load(photo_url).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            take_photo.setText("Retake Photo");
        } else {
            Toasty.error(MainActivity.this, "Failed to take image", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toasty.error(MainActivity.this, "This device doesn't have camera", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}