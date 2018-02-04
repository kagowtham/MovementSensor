package com.example.movementsensor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("status",Context.MODE_PRIVATE);
        if(preferences.getBoolean("on",false)){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_entry);
        final CheckBox local=(CheckBox)findViewById(R.id.cb_localhost);
        final CheckBox cloud=(CheckBox)findViewById(R.id.cb_cloud);

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = vi.inflate(R.layout.add, null);
        TextView textView = (TextView) v.findViewById(R.id.add_textView);
        textView.setText("http://servlet-servlet.1d35.starter-us-east-1.openshiftapps.com/");
        final ViewGroup insertPoint = (ViewGroup) findViewById(R.id.cloud_layout);

        LayoutInflater vi1 = LayoutInflater.from(this);
        final View v1 = vi1.inflate(R.layout.add1, null);
        final EditText editText=(EditText) v1.findViewById(R.id.add_editText);
        final ViewGroup insertPoint1 = (ViewGroup) findViewById(R.id.localhost_layout);


        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    local.setChecked(false);

                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

                }else{
                    local.setChecked(true);
                    insertPoint.removeView(v);


                }
            }
        });



        local.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cloud.setChecked(false);
                    insertPoint1.addView(v1, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                }else{
                    cloud.setChecked(true);
                    insertPoint1.removeView(v1);


                }
            }
        });
        Button button=(Button)findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("address",Context.MODE_PRIVATE);
                Intent intent=new Intent(getApplicationContext(),Login.class);
                if(!local.isChecked()&&!cloud.isChecked()){
                    Toast.makeText(getApplicationContext(),"choose any one of the server",Toast.LENGTH_SHORT).show();
                }else if(local.isChecked()){
                   if(validateIpAddress(editText.getText().toString())){
                       preferences.edit().putString("address","http://"+editText.getText().toString()+":8080/ROOT/").apply();
                       startActivity(intent);
                       finish();
                   }else{
                       Toast.makeText(getApplicationContext(),"Enter a valid ip address",Toast.LENGTH_SHORT).show();

                   }
                }else{
                    preferences.edit().putString("address","http://servlet-servlet.1d35.starter-us-east-1.openshiftapps.com/").apply();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    boolean validateIpAddress(String ip){
        String IPADDRESS_PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
         Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
