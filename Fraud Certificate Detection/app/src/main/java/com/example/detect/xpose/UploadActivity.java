package com.example.sibasubramaniam.xpose;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.key;

public class UploadActivity extends AppCompatActivity {
    public static int galleryPermission = 1;
    ImageView capturedImage;
    Button loadedphot;
    String encodedImage;
    JSONObject jsonObject;
    JSONObject Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_upload);
        capturedImage=(ImageView)findViewById(R.id.capturedimage);
        loadedphot=(Button)findViewById(R.id.capturebutton);
        loadedphot.setVisibility(View.INVISIBLE);
        Intent photoPicker=new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureDirectorypath=pictureDirectory.getPath();
        Uri data =Uri.parse(pictureDirectorypath);
        photoPicker.setDataAndType(data, "image/*");
        startActivityForResult(photoPicker,galleryPermission);
    }
    protected void onActivityResult(int requestcode,int resultcode,Intent data){
        if(resultcode ==RESULT_OK){
            if(requestcode==1){
                Uri imageuri=data.getData();
                InputStream inputStream;
                Bitmap image = null;
                try {
                    inputStream =getContentResolver().openInputStream(imageuri);
                    image = BitmapFactory.decodeStream(inputStream);
                    capturedImage.setImageBitmap(image);
                    loadedphot.setVisibility(View.VISIBLE);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    Log.d("TAG",encodedImage);
                    new UploadImages().execute();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else{
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
            }
        }
        public void go(View view) {
            Intent sendStuff =new Intent(getApplicationContext(),ResultActivity.class);
            startActivity(sendStuff);
        }
    private class UploadImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Log.d("Siba", "encodedImage = " + encodedImage);
                jsonObject = new JSONObject();
                jsonObject.put("imageString", encodedImage);
                jsonObject.put("imageName", "+65685564475");
                String data = jsonObject.toString();
                String yourURL = "127.0.0.1";
                URL url = new URL(yourURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setFixedLengthStreamingMode(data.getBytes().length);
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(data);
                Log.d("Data", "Data to python = " + data);
                writer.flush();
                writer.close();
                out.close();
                connection.connect();

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                String result = sb.toString();
                Log.d("TAG", "Response from python = " + result);
                //Response = new JSONObject(result);
                connection.disconnect();
            } catch (Exception e) {
                Log.d("Vicky", "Error Encountered");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

        }
    }
}

