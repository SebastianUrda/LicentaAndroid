package com.example.sebi.licentatest;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sebi.licentatest.activities.MainActivity;
import com.example.sebi.licentatest.entities.Data;
import com.example.sebi.licentatest.services.DataService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class BluetoothService extends IntentService {
    private static final String TAG = "ExampleIntentService";
    public static volatile boolean keepGoing = true;
    private static boolean sentOk = true;
    private int userId;
    private int deviceId;
    private Retrofit retrofit;
    private static double latitude, longitude;
    private LocationManager locationManager;
    private boolean sent;
    private BufferedReader in;
    private PowerManager.WakeLock wakeLock;
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    public BluetoothService() {
        super("BluetoothService");
        setIntentRedelivery(true);
        this.sent = false;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
        userId = Integer.parseInt(s.getString("userId", ""));
        deviceId = Integer.parseInt(s.getString("deviceId", ""));
        Log.d("USERID", String.valueOf(userId));
        Log.d("DEVICEID", String.valueOf(deviceId));
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Example:WakeLock");
        wakeLock.acquire();
        Log.d(TAG, "wakelock acquired");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!isMyServiceRunning()) {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            startService(i);
            Log.d("GPSBT", "Started GPS service");
        }
        Notification notification = createNotification("Bluetooth  Service", "Working...");
        startForeground(1, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(getApplicationContext(), GPSService.class);
        stopService(i);

        Log.d(TAG, "onDestroy");
        wakeLock.release();
        try {
            mBTSocket.close();
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "wakhhirwerfjerf");
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onhandleintent");

        assert intent != null;
        String address = intent.getStringExtra("address");
        //bluetooth
        boolean fail = false;
        BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

        try {
            mBTSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            fail = true;
            createNotification("Bluetooth fail!", "Socket creation failed");
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
                createNotification("Bluetooth fail!", "Socket creation failed");
            }
        }
        if (!fail) {
            InputStream tmpIn = null;
            createNotification("Bluetooth Connected!", "Connected to " + address);
            try {
                tmpIn = mBTSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream mmInStream = tmpIn;

            while (keepGoing) {
                in = new BufferedReader(new InputStreamReader(mmInStream));
                try {
                    String response = in.readLine();
                    if (response.contains("{") && response.contains("}")) {
                        JSONObject json = new JSONObject(response);
                        Log.d("JSON", json.toString());
                        FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_APPEND);
                        OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                        getLocation();
                        NumberFormat format = new DecimalFormat("#0.0000");
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = dateFormat.format(date);

                        double lpg, co, smoke, co2, backTemp, humidity, dust, pressure, frontTemp, vis, ir, uv, frontTempDht, frontHumidityDht;
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
                        ir = Double.parseDouble(json.getString("si1145Ir"));
                        uv = Double.parseDouble(json.getString("si1145Uv"));
                        frontTempDht = Double.parseDouble(json.getString("dht11FrontTemp"));
                        frontHumidityDht = Double.parseDouble(json.getString("dht11FrontHumidity"));
//                        if ((lpg > 0 || co > 0 || smoke > 0) & !sent) {
//                            SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
//                            SmsAlert.sendSMS(s.getString("userName", "") + " might be in presence of gas!");
//                            sent = true;
//                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Licenta").child("new");
                        Data data = new Data(userId, deviceId, formattedDate, format.format(latitude), format.format(longitude), lpg, co, smoke, co2, backTemp, humidity, dust, pressure, frontTemp, vis, ir, uv, frontTempDht, frontHumidityDht);
                        Log.d("DataToSend", data.toString());
                        myRef.push().setValue(data);
                        Log.d("FireBase", "sent to firebase");

                        outputWriter.write(data.toJson());
                        outputWriter.close();

                        DataService dataService = retrofit.create(DataService.class);
                        dataService.send(data).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("SENDDATA", "MERGE! ");
                                    sentOk = true;
                                } else {
                                    sentOk = false;
                                    try {
                                        assert response.errorBody() != null;
                                        Log.d("SENDDATA", "FAIL" + response.message() + " " + response.body() + " " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                Log.d("SENDDATA", t.getMessage());
                                sentOk = false;
                            }
                        });
                        if (sentOk) {
                            createNotification("PEMS Backpack", data.toString() + "\nServer insertion succeeded!");
                        } else {
                            createNotification("PEMS Backpack", data.toString() + "\nServer insertion failed!");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (!keepGoing) {
                stopSelf();
            }
        }
    }

    Notification createNotification(String title, String message) {

        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true) // so when data is updated don't make sound and alert in android 8.0+
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
        SystemClock.sleep(3000);
        return notification;
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GPSService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void getLocation() {
        SharedPreferences s = getSharedPreferences("location_update", MODE_PRIVATE);
        String longitudeS = s.getString("longitude", "");
        String latitudeS = s.getString("latitude", "");
        Log.d("GPSBT", longitudeS + " " + latitudeS);
        longitude = Double.valueOf(longitudeS != null ? longitudeS : null);
        latitude = Double.valueOf(latitudeS != null ? latitudeS : null);
        if (longitude == 0 || latitude == 0) {
            Log.d("GPSBT", "Came 0 and updated");
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            @SuppressLint("MissingPermission") Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            @SuppressLint("MissingPermission") Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else if (location1 != null) {
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();

            } else if (location2 != null) {
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();

            } else {

                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
