package com.example.sibasubramaniam.xpose;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton captured;
    ImageButton uploaded;
    public static int reqcam=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captured=(ImageButton)findViewById(R.id.capture);
        uploaded=(ImageButton)findViewById(R.id.upload);
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void capture(View view){
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)){
            callCamera();
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this,"Camera Resource Required",Toast.LENGTH_LONG).show();
            }
        }
        requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},reqcam);
    }
    @Override
    public void onRequestPermissionsResult(int requestcode,String[] permission ,int[] grantResults) {
        if (requestcode == reqcam) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCamera();
            } else {
                Toast.makeText(this, "No Permission Granted", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestcode, permission, grantResults);
        }
    }




    public void upload(View view){
        Intent intent =new Intent(getApplicationContext(),UploadActivity.class);
        startActivity(intent);
    }
    public void callCamera(){
        Intent intent =new Intent(getApplicationContext(),capture.class);
        startActivity(intent);
    }
}
