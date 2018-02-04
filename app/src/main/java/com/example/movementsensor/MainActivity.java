package com.example.movementsensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Switch s=(Switch)findViewById(R.id.switch1);
        SharedPreferences preferences=getSharedPreferences("status",Context.MODE_PRIVATE);
        s.setChecked(preferences.getBoolean("on",false));
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences=getSharedPreferences("status",Context.MODE_PRIVATE);
                preferences.edit().putBoolean("on",b).apply();
                if(b){

                        //startService(i);

                    Util.scheduleJob(getApplicationContext());
                }else{

                    Util.stopShedule();
                }
            }
        });
        if(getIntent().getStringExtra("usr")!=null)
          getRequest();
    }

    void getRequest(){
        SharedPreferences preferences=getSharedPreferences("address", Context.MODE_PRIVATE);

        String URL = preferences.getString("address","")+"GetId";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
           public void onResponse(String response) {
                Log.i("VOLLEY", response);
                key=response;
                SharedPreferences preferences=getSharedPreferences("key", Context.MODE_PRIVATE);
                preferences.edit().putString("key",key).apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> m=new HashMap<>();
                m.put("usr",getIntent().getStringExtra("usr"));
                m.put("pwd",getIntent().getStringExtra("pwd"));
                return m;
            }
        };

        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
