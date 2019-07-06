package com.example.sebi.licentatest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sebi.licentatest.R;

public class Questionnaire extends AppCompatActivity {
    private Button temperature;
    private Button humidity;
    private Button dust;
    private Button light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.name);
        SharedPreferences s = getSharedPreferences("Auth", MODE_PRIVATE);
        name.setText(s.getString("userName",""));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.log_out:
                SharedPreferences.Editor e=getSharedPreferences("Auth",MODE_PRIVATE).edit();

                e.putString("userId", "").apply();
                e.putString("userName", "").apply();
                e.putString("deviceId", "").apply();
                e.putString("userRole", "").apply();
                e.putString("loggedIn","false").apply();
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
