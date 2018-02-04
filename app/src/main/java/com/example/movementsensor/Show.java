package com.example.movementsensor;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Show extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ImageView imageView=(ImageView)findViewById(R.id.image_view);
        imageView.setImageBitmap(TestJobService.image);
    }
    public void saveImage(View v){
        try {
            FileOutputStream outputStream=new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/save.jpeg");
            TestJobService.image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Toast.makeText(getApplicationContext(),"saved image to location : "+Environment.getExternalStorageDirectory().toString()+"/save.jpeg",Toast.LENGTH_LONG).show();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
