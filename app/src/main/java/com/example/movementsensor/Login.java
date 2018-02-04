package com.example.movementsensor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText editText_usr=(EditText)findViewById(R.id.usr_id);
        final EditText editText_pwd=(EditText)findViewById(R.id.usr_pwd);
        Button button=(Button)findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequest(editText_usr.getText().toString(),editText_pwd.getText().toString());
            }
        });
    }

    void getRequest(final String usr, final String pwd){
        SharedPreferences preferences=getSharedPreferences("address", Context.MODE_PRIVATE);

        String URL = preferences.getString("address","")+"Login";
        Log.i("VOLLEY request", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if(response.trim().equals("true")){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("usr",usr);
                    intent.putExtra("pwd",pwd);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"wrong user name or password",Toast.LENGTH_SHORT).show();
                }
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
                m.put("usr",usr);
                m.put("pwd",pwd);
                return m;
            }
        };

        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
