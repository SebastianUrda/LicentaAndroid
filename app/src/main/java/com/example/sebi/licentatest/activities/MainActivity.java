package com.example.sebi.licentatest.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebi.licentatest.AlertsService;
import com.example.sebi.licentatest.R;
import com.example.sebi.licentatest.entities.AlertA;
import com.example.sebi.licentatest.entities.Device;
import com.example.sebi.licentatest.entities.User;
import com.example.sebi.licentatest.services.AlertService;
import com.example.sebi.licentatest.services.DeviceService;
import com.example.sebi.licentatest.services.UserService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    // GUI Components
    private EditText username;
    private EditText password;
    private Retrofit retrofit;
    private TextView alerts;
    private String userID;
    private static final String TAG = "MainActivity";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //alert listening service
        Intent serviceIntent = new Intent(this, AlertsService.class);
        startService(serviceIntent);
        registerReceiver(alertsNumberReceiver, new IntentFilter("ALERTSNUMBER"));
        //permissions needed for SDK 23 up
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(this, PERMISSIONS)) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //check for internet connection
        checkForInternet();
        //custom actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.name);
        alerts = view.findViewById(R.id.alertsNumber);
        //check if user logged
        SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
        boolean loggedIn = Boolean.parseBoolean(s.getString("loggedIn", ""));
        if (loggedIn) {
            alerts.setVisibility(View.GONE);
            name.setText(s.getString("userName", ""));
            userID = s.getString("userId", "");
            Button questionnaire = findViewById(R.id.answerQuestionnaire);
            Button charts = findViewById(R.id.viewCharts);
            Button bpc = findViewById(R.id.backPackConnection);
            Button regDevice = findViewById(R.id.register_device);
            final EditText deviceName = findViewById(R.id.deviceName);
            bpc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), BackPackConnection.class);
                    startActivity(myIntent);
                }
            });
            charts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), ChartsMenu.class);
                    startActivity(myIntent);
                }
            });

            questionnaire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), Questionnaire.class);
                    startActivity(myIntent);
                }
            });
            regDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    device register
                    DeviceService deviceService = retrofit.create(DeviceService.class);
                    deviceService.registerDevice(deviceName.getText().toString(), Integer.parseInt(userID)).enqueue(new Callback<Device>() {
                        @Override
                        public void onResponse(Call<Device> call, Response<Device> response) {
                            if (response.isSuccessful()) {
                                Device device = response.body();
                                Log.d("DeviceInsert", device.toString());
                                SharedPreferences.Editor e = getSharedPreferences("Auth", MODE_PRIVATE).edit();
                                e.putString("deviceId", String.valueOf(device.getId())).apply();
                                Toast.makeText(getApplicationContext(), "Register succeeded !", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Register failed !", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Device> call, Throwable t) {
                            Log.d("DeviceInsert", t.getMessage());
                            Toast.makeText(getApplicationContext(), "Register failed !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } else {
            //if not logged go log in page

            alerts.setVisibility(View.GONE);
            setContentView(R.layout.log_in);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            Button logIn = findViewById(R.id.logIn);
            Button register = findViewById(R.id.register);

            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User(username.getText().toString(), password.getText().toString());

                    UserService userService = retrofit.create(UserService.class);
                    userService.authenticate(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                if (user != null) {
                                    Log.d("AUTH", user.toString());
                                    SharedPreferences.Editor e = getSharedPreferences("Auth", MODE_PRIVATE).edit();
                                    e.putString("userId", String.valueOf(user.getId())).apply();
                                    e.putString("deviceId", String.valueOf(user.getDeviceID())).apply();
                                    e.putString("userName", user.getUsername()).apply();
                                    e.putString("userRole", user.getRole()).apply();
                                    e.putString("loggedIn", "true").apply();
                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(myIntent);
                                }

                            } else {
                                try {
                                    assert response.errorBody() != null;
                                    Log.d("AUTH", "FAIL" + response.message() + " " + response.body() + " " + response.errorBody().string());
                                    Toast.makeText(getApplicationContext(), "Log in Failed. Please try again!", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                            Toast.makeText(getApplicationContext(), "Log in Failed. Please try again!", Toast.LENGTH_SHORT).show();
                            Log.d("AUTH", t.getMessage());
                        }
                    });
                }
            });
            //register possibility
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), Register.class);
                    startActivity(myIntent);
                }
            });
        }
    }

    public void showAlerts(View v) {
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AlertService alertService = retrofit.create(AlertService.class);
        alertService.getAlerts().enqueue(new Callback<List<AlertA>>() {
            @Override
            public void onResponse(@NonNull Call<List<AlertA>> call, @NonNull Response<List<AlertA>> response) {
                if (response.isSuccessful()) {
                    List<AlertA> received = response.body();
                    final SharedPreferences.Editor e = getSharedPreferences("alerts", MODE_PRIVATE).edit();
                    assert received != null;
                    e.putString("number", String.valueOf(received.size())).apply();
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alerts");
                    StringBuilder alertText = new StringBuilder();
                    for (AlertA alert : received) {
                        alertText.append("Description: ").append(alert.getDescription()).append("\n").append("Address: ").append(alert.getAddress()).append("\n");
                    }
                    alertDialog.setMessage(alertText.toString());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    e.putString("number", "0").apply();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AlertA>> call, @NonNull Throwable t) {
            }
        });
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

    private BroadcastReceiver alertsNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getStringExtra("ALERTSNUMBER").equals("0")) {
                alerts.setVisibility(View.VISIBLE);
                alerts.setText(intent.getStringExtra("ALERTSNUMBER"));
                Log.d(TAG, "show");
            } else {
                alerts.setVisibility(View.GONE);
                Log.d(TAG, "deleted");
            }
        }
    };

    private boolean checkPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    private void checkForInternet() {
        if (!isNetworkAvailable()) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Internet");
            alertDialog.setMessage("Please enable the internet connection!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}


