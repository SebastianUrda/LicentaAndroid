package com.example.sebi.licentatest.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.sebi.licentatest.R;
import com.example.sebi.licentatest.data.Data;
import com.example.sebi.licentatest.data.DataList;
import com.example.sebi.licentatest.data.DataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecycledList extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private List<Data> datas=new ArrayList<Data>();
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sebipc.ddns.net:8081/mightWork/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        DataService dataService = retrofit.create(DataService.class);
        dataService.messages().enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {
                if (response.isSuccessful()) {

                    DataList dates = response.body();
                    datas.addAll(dates.getDates());
                    adapter.getObjects().addAll(dates.getDates());
                    adapter.notifyDataSetChanged();
                    Log.d("DATASPRING", "MERE!");
                } else {
                    try {
                        Log.d("DATASPRING", "FAIL"+response.message()+" "+response.body()+" "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
                Log.d("DATASPRING", t.getMessage());
            }
        });
        adapter=new RecyclerAdapter(datas,this.getApplicationContext());
        RecyclerView recyclerView=findViewById(R.id.idRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}
