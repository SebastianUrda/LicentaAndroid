package com.example.sebi.licentatest;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sebi.licentatest.entities.AlertA;
import com.example.sebi.licentatest.services.AlertService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlertsService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.server_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        AlertService alertService = retrofit.create(AlertService.class);
                        alertService.getAlerts().enqueue(new Callback<List<AlertA>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<AlertA>> call, @NonNull Response<List<AlertA>> response) {
                                if (response.isSuccessful()) {
                                    List<AlertA> received = response.body();
                                    if (received != null) {
                                        Log.d("ALERTS", received.toString());
//                                        SharedPreferences.Editor e = getSharedPreferences("alerts", MODE_PRIVATE).edit();
//                                        e.putString("number", String.valueOf(received.size())).apply();
                                        Intent intent = new Intent("ALERTSNUMBER");
                                        intent.putExtra("ALERTSNUMBER", Integer.toString(received.size()));
                                        sendBroadcast(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<AlertA>> call, @NonNull Throwable t) {

                            }
                        });
                        sleep(20 * 1000);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
