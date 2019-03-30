package com.example.sebi.licentatest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sebi.licentatest.data.Data;
import com.example.sebi.licentatest.data.DataList;
import com.example.sebi.licentatest.data.DataService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowDocument extends Activity {
    private Button BackBtn;
    private Button SendBtn;
    private TextView DocumentView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_document);
        BackBtn=(Button) findViewById(R.id.back);
        SendBtn=(Button)findViewById(R.id.send);
        DocumentView=(TextView)findViewById(R.id.document);
//        try {
//            FileInputStream fileIn=openFileInput("mytextfile.txt");
//            InputStreamReader InputRead= new InputStreamReader(fileIn);
//
//            char[] inputBuffer= new char[1024];
//            String s="";
//            int charRead;
//
//            while ((charRead=InputRead.read(inputBuffer))>0) {
//                // char to string conversion
//                String readstring=String.copyValueOf(inputBuffer,0,charRead);
//                s +=readstring;
//            }
//            InputRead.close();
//            DocumentView.setText(s);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("http://192.168.0.103:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataService dataService=retrofit.create(DataService.class);
        dataService.messages().enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {
                if(response.isSuccessful())
                {

                    DataList dates=response.body();
                    String s="";
                    for(Data d:dates.getDates())
                    {
                        s=s+d.toJson()+"\n";
                    }
                    DocumentView.setText(s);
                    Log.d("DATASPRING","MERE!");
                }
                else
                {
                    Log.d("DATASPRING","FAIL");
                }
            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
                Log.d("DATASPRING",t.getMessage());
            }
        });



        BackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
        SendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    FileInputStream fileIn=openFileInput("mytextfile.txt");
                    InputStreamReader InputRead= new InputStreamReader(fileIn);

                    char[] inputBuffer= new char[1024];
                    String s="";
                    int charRead;

                    while ((charRead=InputRead.read(inputBuffer))>0) {
                        // char to string conversion
                        String readstring=String.copyValueOf(inputBuffer,0,charRead);
                        s +=readstring;
                    }
                    InputRead.close();
                    String[] TO = {"george.georg12@gmail.com"};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");


                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Licenta");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, s);

                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    });
}}
