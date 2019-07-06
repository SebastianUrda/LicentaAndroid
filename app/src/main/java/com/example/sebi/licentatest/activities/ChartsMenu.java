package com.example.sebi.licentatest.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sebi.licentatest.R;
import com.example.sebi.licentatest.services.DataService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartsMenu extends AppCompatActivity {
    EditText meters;
    EditText minutes;
    EditText startDateInput, endDateInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts_menu);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.name);
        SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
        name.setText(s.getString("userName", ""));
        startDateInput = findViewById(R.id.date1);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener startDateEvent = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartTime(myCalendar);
            }

        };
        final DatePickerDialog.OnDateSetListener endDateEvent = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndTime(myCalendar);
            }

        };


        startDateInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new DatePickerDialog(ChartsMenu.this, startDateEvent, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });

        endDateInput = findViewById(R.id.date2);
        endDateInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new DatePickerDialog(ChartsMenu.this, endDateEvent, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }

        });
        Button backVsFrontTemp = findViewById(R.id.btft);
        backVsFrontTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
                String url = "http://licenta.ddns.net:8080/reports/#!/charts/" +
                        startDateInput.getText().toString() + "/" + endDateInput.getText().toString() +
                        "/" + s.getString("userId", "");
                Intent myIntent = new Intent(getApplicationContext(), Charts.class);
                myIntent.putExtra("URL", url);
                startActivity(myIntent);
            }
        });
        Button ato = findViewById(R.id.ato);
        meters = findViewById(R.id.geoDistance);
        minutes = findViewById(R.id.timeDistance);
        ato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
                String url = "http://licenta.ddns.net:8080/reports/#!/answersToObservations/" +
                        meters.getText().toString() + "/" + minutes.getText().toString() + "/" +
                        s.getString("userId", "");
                Intent myIntent = new Intent(getApplicationContext(), Charts.class);
                myIntent.putExtra("URL", url);
                startActivity(myIntent);
            }
        });
        Button measurementsAround = findViewById(R.id.measurementsAround);
        measurementsAround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = getSharedPreferences("location_update", MODE_PRIVATE);
                String longitudeS = s.getString("longitude", "");
                String latitudeS = s.getString("latitude", "");
                Log.d("Measurements", longitudeS + " " + latitudeS);
                double longitude = Double.valueOf(longitudeS != null ? longitudeS : null);
                double latitude = Double.valueOf(latitudeS != null ? latitudeS : null);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.server_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                DataService dataService = retrofit.create(DataService.class);
                dataService.getAroundMeasurements(latitude, longitude, 10, 100).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String around = response.body().string();
                            Log.d("Around", around);
                            AlertDialog alertDialog = new AlertDialog.Builder(ChartsMenu.this).create();
                            alertDialog.setTitle("Observations Around");
                            alertDialog.setMessage(around);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } catch (IOException e) {
                            Log.d("Around", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ChartsMenu.this).create();
                        alertDialog.setTitle("Observations Around");
                        alertDialog.setMessage("An error has occurred. Please try again later! ");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });

            }
        });

    }

    private void updateStartTime(Calendar myCalendar) {
        String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        startDateInput.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndTime(Calendar myCalendar) {
        String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        endDateInput.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                SharedPreferences.Editor e = getSharedPreferences("Auth", MODE_PRIVATE).edit();

                e.putString("userId", "").apply();
                e.putString("userName", "").apply();
                e.putString("deviceId", "").apply();
                e.putString("userRole", "").apply();
                e.putString("loggedIn", "false").apply();
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
