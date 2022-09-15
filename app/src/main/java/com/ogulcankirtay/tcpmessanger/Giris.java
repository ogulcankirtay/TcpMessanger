package com.ogulcankirtay.tcpmessanger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Giris extends AppCompatActivity {
    EditText e;
    EditText p;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        e=(EditText)findViewById(R.id.editTextTextPersonName);
        p=(EditText)findViewById(R.id.editTextTextPort);
        sharedPreferences=this.getSharedPreferences("com.ogulcankirtay.tcpmessanger", Context.MODE_PRIVATE);

        String x=sharedPreferences.getString("host","0");
        String y=sharedPreferences.getString("port","0");
        if(!e.getText().toString().equals("0")){
        e.setText(x);
        p.setText(y);
        }
    }

    public void go(View view) {
        String host=e.getText().toString();
        String port=p.getText().toString();

        if( !host.isEmpty()){
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("host",host);
            intent.putExtra("port",port);
            sharedPreferences.edit().putString("host",host).apply();
            sharedPreferences.edit().putString("port",port).apply();
            startActivity(intent);
             }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Plase Enter the Host", Toast.LENGTH_LONG);
            toast.show();
        }


    }
}