package com.example.movementsensor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by manoj on 01-01-2018.
 */

public class TestJobService  extends JobService{
    String key="";
    static Bitmap image;
    @Override
    public boolean onStartJob(JobParameters params) {

        //getApplicationContext().startService(service);
        getRequest();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Util.scheduleJob(getApplicationContext()); // reschedule the job
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
    void getRequest(){
        SharedPreferences preferences=getSharedPreferences("address", Context.MODE_PRIVATE);
        SharedPreferences preferences1=getSharedPreferences("key", Context.MODE_PRIVATE);
        String URL = preferences.getString("address","")+"Test?status=img&key="+preferences1.getString("key","abc123");

        ImageRequest imageRequest=new ImageRequest (URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if(response!=null){
                  makeNotification(response);
                }
                //imageView.setImageBitmap(response);

            }
        },0,0, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });

        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);


    }
    void makeNotification(Bitmap bitmap){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Movement Sensor")
                        .setContentText("Some body enters");


        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap));
        Intent notificationIntent = new Intent(this, Show.class);
        image=bitmap;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        builder.setSound(alarmSound);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "motion detector sensor",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            manager.createNotificationChannel(channel);

        }

        manager.notify(1, builder.build());

    }
}
