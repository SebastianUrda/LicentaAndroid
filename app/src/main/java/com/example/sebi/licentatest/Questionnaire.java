package com.example.sebi.licentatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sebi.licentatest.list.QuizList;

public class Questionnaire extends AppCompatActivity {
    private Button temperature;
    private Button humidity;
    private Button dust;
    private Button light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire);

        temperature=findViewById(R.id.temperature_questions);
        humidity=findViewById(R.id.humidity_questions);
        dust=findViewById(R.id.dust_questions);
        light=findViewById(R.id.light_questions);

        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), QuizList.class);
                intent.putExtra("TYPE","temperature");
                startActivity(intent);
            }});

        humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), QuizList.class);
                intent.putExtra("TYPE","humidity");
                startActivity(intent);
            }});

        dust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), QuizList.class);
                intent.putExtra("TYPE","dust");
                startActivity(intent);
            }});

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), QuizList.class);
                intent.putExtra("TYPE","light");
                startActivity(intent);
            }});

    }
}
