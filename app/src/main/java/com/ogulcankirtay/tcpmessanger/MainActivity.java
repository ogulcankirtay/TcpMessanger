package com.ogulcankirtay.tcpmessanger;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.transform.Result;



public class MainActivity extends AppCompatActivity {

    TextView commingText;
   // TextView textView;
    PastDataAdapter pastDataAdapter;
    ArrayList<String> data=new ArrayList<String>();
    RecyclerView recyclerView;
    private static final int SELECT_PICTURE = 1;
    String path;
    Button bopen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commingText=(TextView) findViewById(R.id.commingText);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastDataAdapter=new PastDataAdapter(data);
        recyclerView.setAdapter(pastDataAdapter);
       // textView=(TextView)findViewById(R.id.textView);
        MessageSender ms= new MessageSender();
        ms.execute("connection request");
        // uygulama çalıştırma
        bopen=findViewById(R.id.button3);
        bopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i=getPackageManager().getLaunchIntentForPackage("com.ogulcankirtay.landmarkbook");
                Intent intent= new Intent(getApplicationContext(),RdpWeb.class);
                startActivityForResult(intent,1);
            }
        });

    }
        @SuppressLint("Range")
        String getFileName(Uri uri, Context context){
        String res=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
            try {
                if(cursor!=null && cursor.moveToFirst()){
                    res=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
            if(res==null){
                res=uri.getPath();
                int cutt=res.lastIndexOf("/");
                if(cutt!=-1){
                    res=res.substring(cutt+1);
                }

            }
            return res;
        }
        return res;
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                System.out.println(""+data.getData());

                path="/storage/emulated/0/Download/"+getFileName(data.getData(),MainActivity.this);
                System.out.println(path);
            }

        }
    }



    public void Select(View view){

        Intent intent = new Intent();
        //intent.setType("image/*");
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                SELECT_PICTURE);
        System.out.println("47");
    }
    public void sendFile(View view){

        System.out.println(path);
        Intent i = getIntent();
        String name = i.getStringExtra("host");
        commingText.setText(name);
        final Socket[] sock = new Socket[1];
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sock[0]=new Socket(name,7801);
                    File myFile = new File(path);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);
                    OutputStream os = sock[0].getOutputStream();
                    System.out.println("Sending...");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    sock[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Connecting...");
            }
        });
        thread.start();
        // sendfile
        data.add(path+" sending");
        pastDataAdapter=new PastDataAdapter(data);
        recyclerView.setAdapter(pastDataAdapter);

    }

    public void List(View view){
        MessageSender ms=new MessageSender();
        ms.execute("List");
        data.add("List");
        pastDataAdapter=new PastDataAdapter(data);
        recyclerView.setAdapter(pastDataAdapter);
    }
    public void print(View view){
        MessageSender ms=new MessageSender();
        ms.execute("~SPPSAP^");
        data.add("~SPPSAP^");

        pastDataAdapter=new PastDataAdapter(data);
        recyclerView.setAdapter(pastDataAdapter);
    }
    public void stop(View view){
        MessageSender ms=new MessageSender();
        ms.execute("~SPPSTP^");
        data.add("~SPPSTP^");
        pastDataAdapter=new PastDataAdapter(data);
        recyclerView.setAdapter(pastDataAdapter);
    }
   /* public void send(View view){
        MessageSender ms= new MessageSender();
        String cd=sendText.getText().toString();
        if(cd!=null){
        ms.execute(cd);
            System.out.println(cd);
            data.add(cd);
            String d="";
            for(int i=0;i<data.size();i++){
                d+=data.get(i)+"\n";
            }
            textView.setText(d);
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "Please Enter The Data You Want To Send!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }*/
    class MessageSender extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {
            Socket socket;
            InputStreamReader inputStreamReader;
            BufferedReader bufferedReader;
            PrintWriter printWriter;
            String result;
            try {
                Intent i = getIntent();
                String host = i.getStringExtra("host");
                int port=Integer.parseInt(i.getStringExtra("port"));
                commingText.setText(host);
                socket =new Socket(host,port);

                inputStreamReader=new InputStreamReader(socket.getInputStream());
                bufferedReader =new BufferedReader(inputStreamReader);
                printWriter=new PrintWriter(socket.getOutputStream());
                //write data
                printWriter.println(strings[0]);

                printWriter.flush();
                //read data
                result= bufferedReader.readLine();

                socket.close();
                inputStreamReader.close();
                bufferedReader.close();
                printWriter.close();

                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            commingText.setText(s);

        }
    }

}