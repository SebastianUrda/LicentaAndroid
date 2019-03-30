package com.example.sebi.licentatest;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sebi.licentatest.data.Data;
import com.example.sebi.licentatest.data.DataService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sebi.licentatest.App.CHANNEL_ID;

public class IntentServiceExample extends IntentService {
    private static final String TAG="ExampleIntentService";
    private double lattitude, longitude;
    private LocationManager locationManager;
    private LocationListener listener;
    private PowerManager.WakeLock wakeLock;
    private BluetoothAdapter mBTAdapter;
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    public IntentServiceExample() {
        super("ExampleIntent");
        setIntentRedelivery(true);
    }
    private void getLocation() {

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        @SuppressLint("MissingPermission") Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        @SuppressLint("MissingPermission") Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null) {
            lattitude = location.getLatitude();
            longitude = location.getLongitude();

        } else if (location1 != null) {
            lattitude = location1.getLatitude();
            longitude = location1.getLongitude();

        } else if (location2 != null) {
            lattitude = location2.getLatitude();
            longitude = location2.getLongitude();

        } else {

            Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        PowerManager powerManager=(PowerManager) getSystemService(POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Example:WakeLock");
        wakeLock.acquire();
        Log.d(TAG,"wakelock acquired");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Intent i = new Intent("location_update");
//                i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude());
                longitude=location.getLongitude();
                lattitude=location.getLatitude();
//                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);
        Notification notification=createNotification("Bluetooth  Service","Working...");
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        wakeLock.release();
        try {
            mBTSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"wakhhirwerfjerf");
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onhandleintent");

        String address = intent.getStringExtra("address");
        //bluetooth
        boolean fail = false;
                    mBTAdapter=BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        createNotification("Bluetooth fail!","Socket creation failed");
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            createNotification("Bluetooth fail!","Socket creation failed");
                        }
                    }
                    if (!fail) {
                        InputStream tmpIn = null;
                        createNotification("Bluetooth Connected!","Connected to "+address);
                        try {
                            tmpIn = mBTSocket.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        InputStream mmInStream = tmpIn;

                        while (true) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(mmInStream));
                            try {
                                String response = in.readLine();
                                //de aici parse and send
                                if (response.contains("{") && response.contains("}")) {
                                    JSONObject json=new JSONObject(response);
                                    Log.d("JSON",json.toString());



                                    FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_APPEND);
                                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);

                                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        createNotification("GPS Error!","Please Provide GPS Access.");

                                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        getLocation();
                                    }
                                    NumberFormat format = new DecimalFormat("#0.0000");
                                    Calendar cal = Calendar.getInstance();
                                    Date date = cal.getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String formattedDate = dateFormat.format(date);




                                    double lpg, co , smoke , co2, sound=0, backTemp, humidity , dust , pressure , frontTemp , vis , ir , uv ;
                                    lpg = Double.parseDouble(json.getString("mq2Lpg"));
                                    co = Double.parseDouble(json.getString("mq2Co"));
                                    smoke = Double.parseDouble(json.getString("mq2Smoke"));
                                    co2 = Double.parseDouble(json.getString("mq135Co2"));
                                    backTemp = Double.parseDouble(json.getString("dht11BackTemp"));
                                    humidity = Double.parseDouble(json.getString("dht11Humidity"));
                                    dust = Double.parseDouble(json.getString("gp2Dust"));
                                    pressure = Double.parseDouble(json.getString("mpl3115Pressure"));
                                    frontTemp = Double.parseDouble(json.getString("mpl3115FrontTemp"));
                                    vis = Double.parseDouble(json.getString("si1145Vis"));
                                    ir=Double.parseDouble(json.getString("si1145Ir"));
                                    uv = Double.parseDouble(json.getString("si1145Uv"));
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Licenta").child("test");
                                    Data data = new Data(formattedDate, format.format(lattitude), format.format(longitude), lpg, co, smoke, co2, sound, backTemp, humidity, dust, pressure, frontTemp, vis, ir, uv);
                                    myRef.push().setValue(data);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://sebipc.ddns.net:8080/mightWork/")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    createNotification(address,data.toJson());

                                    outputWriter.write(data.toJson());
                                    outputWriter.close();
                                    Log.d("FireBase","sent to firebase");
                                    DataService dataService = retrofit.create(DataService.class);
                                    dataService.send(data).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Log.d("SENDDATA", "MERGE! ");
                                            } else {
                                                try {
                                                    Log.d("SENDDATA", "FAIL"+ response.message() +" "+response.body()+" "+response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.d("SENDDATA", t.getMessage());
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
    }
    Notification createNotification(String title, String message ){

        Intent activityIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,activityIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_bluetooth_connected)
                .setOnlyAlertOnce(true) // so when data is updated don't make sound and alert in android 8.0+
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
        SystemClock.sleep(3000);
        return notification;
    }
}
