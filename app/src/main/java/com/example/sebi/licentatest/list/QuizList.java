package com.example.sebi.licentatest.list;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sebi.licentatest.GPSService;
import com.example.sebi.licentatest.R;
import com.example.sebi.licentatest.data.AnswerService;
import com.example.sebi.licentatest.data.QuestionsService;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizList extends AppCompatActivity {
    private QuestionAdapter adapter;
    private List<Question> questions;
    private List<Answer> answers;
    private Button sendQuestions;
    private static double latitude, longitude;
    private LocationManager locationManager;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void getLocation() {
        SharedPreferences s=getSharedPreferences("location_update",MODE_PRIVATE);
        String longitudeS=s.getString("longitude","");
        String latitudeS=s.getString("latitude","");
        Log.d("GPSQuiz",longitudeS+" "+latitudeS);
        longitude=Double.valueOf(longitudeS);
        latitude=Double.valueOf(latitudeS);
        if(longitude==0||latitude==0) {
            Log.d("GPSQuiz","Came 0 and updated");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        questions = new ArrayList<>();
        if(!isMyServiceRunning(GPSService.class)) {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            startService(i);
            Log.d("GPSBT","Started GPS service");
        }


        Intent intent = getIntent();
        String type=intent.getStringExtra("TYPE");

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("http://licenta.ddns.net:8080/mightWork/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuestionsService dataService=retrofit.create(QuestionsService.class);
        dataService.getQuestions(type).enqueue(new Callback<List<QuestionA>>() {


            @Override
            public void onResponse(Call<List<QuestionA>> call, Response<List<QuestionA>> response) {
                List<QuestionA> received=response.body();

                for(QuestionA rec:received){
                    questions.add(new Question(rec.getId(),rec.getText()));
                }
                Log.d("Parameters Work",questions.toString());
                adapter = new QuestionAdapter(getApplicationContext(), questions);
                ListView listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<QuestionA>> call, Throwable t) {
                Log.d("Parameters Fail",t.getMessage());
            }
        });

        adapter = new QuestionAdapter(this, questions);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        sendQuestions = findViewById(R.id.answerQuestionnaire);
        sendQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberFormat format = new DecimalFormat("#0.0000");
                getLocation();
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = dateFormat.format(date);
                answers = new ArrayList<>();
                for (Question q:QuestionAdapter.getObjects()) {
                    answers.add(new Answer(formattedDate,format.format(latitude), format.format(longitude),q.getId(),1,q.getResponse()));
                }
                Log.d("Questions", questions.toString());
                Log.d("Answers", answers.toString());
                Retrofit retrofit =new Retrofit.Builder()
                        .baseUrl("http://licenta.ddns.net:8080/mightWork/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AnswerService answerService=retrofit.create(AnswerService.class);
                answerService.sendAnswers(answers).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("Answers Sent","Worked");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Answers Sent","Fail");
                    }
                });

            }
        });
    }
}
