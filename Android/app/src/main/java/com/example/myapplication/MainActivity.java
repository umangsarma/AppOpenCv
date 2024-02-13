package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button selectButton,CameraButton;
    ImageView imageView;
    Bitmap bitmap;
    int SELECT_CODE=100,CAMERA_CODE=102;
    Mat mat;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_CODE && data!=null){
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
          imageView.setImageBitmap(bitmap);

          mat=new Mat();
                Utils.bitmapToMat(bitmap,mat);

                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
                Utils.matToBitmap(mat,bitmap);

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(requestCode==CAMERA_CODE && data!=null){
            bitmap=(Bitmap) data.getExtras().get("data");


            imageView.setImageBitmap(bitmap);

            mat=new Mat();
            Utils.bitmapToMat(bitmap,mat);

            Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
            Utils.matToBitmap(mat,bitmap);

            imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getpermission();

  if(OpenCVLoader.initDebug())
  {
    //   Toast.makeText(this,"Hello Javatpoint",Toast.LENGTH_SHORT).show();
      Log.d("umang","success opencv");
  }else{
      //Toast.makeText(this,"oooooo",Toast.LENGTH_SHORT).show();
      Log.d("umang","fail opencv load");
  }

  selectButton=(Button) findViewById(R.id.selectButton);
        CameraButton=(Button) findViewById(R.id.cameraButton);
        imageView=(ImageView) findViewById(R.id.imageView);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_CODE);
            }
        });

CameraButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_CODE);
    }
});

    }

    private void getpermission() {
        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{android.Manifest.permission.CAMERA},103);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==103 && grantResults.length>0){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                getpermission();
            }
        }
    }
}