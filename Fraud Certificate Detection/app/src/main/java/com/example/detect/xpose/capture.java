package com.example.sibasubramaniam.xpose;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class capture extends AppCompatActivity {
    ImageView captured;
    public static final int imageCapture =0;
    public String mImageLocation="";
    Context context=this;
    Button capturephoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        captured=(ImageView)findViewById(R.id.capturedimage);
        capturephoto=(Button)findViewById(R.id.capturebutton);
        capturephoto.setVisibility(View.INVISIBLE);
        Intent callCamera =new Intent();
        callCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=null;
        try {
            photoFile=createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("sdf"+photoFile);
        callCamera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, getApplicationContext().getPackageName()+".provider",photoFile) );
        startActivityForResult(callCamera,imageCapture);
    }

    protected void onActivityResult(int requestcode,int resultcode,Intent data){
        if(resultcode ==RESULT_OK&& requestcode==0){
            Bitmap bitmap = BitmapFactory.decodeFile(mImageLocation);
            captured.setImageBitmap(bitmap);
            capturephoto.setVisibility(View.VISIBLE);

            }}

    File createImageFile() throws IOException {
        String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imagefilename= "IMAGE_" +timestamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imagefilename,".jpg",storageDirectory);
        mImageLocation=image.getAbsolutePath();
        return image;
    }
}
//FileProvider.getUriForFile(context, getApplicationContext().getPackageName()+".provider",photoFile)
//Uri.fromFile(photoFile)