package com.example.termtracker_c196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class MainActivity extends AppCompatActivity {
    DBHelper myHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        myHelper = new DBHelper(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button termButton = (Button) findViewById(R.id.termButton);
        termButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent termsIntent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(termsIntent);
            }
        });

        Button mentorButton = (Button) findViewById(R.id.mentorButton);
        mentorButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent mentorIntent = new Intent(getApplicationContext(), MentorActivity.class);
                startActivity(mentorIntent);
            }
        });

        Button courseButton = (Button) findViewById(R.id.courseButton);
        courseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent courseIntent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(courseIntent);
            }
        });

        Button assessmentButton = (Button) findViewById(R.id.assessmentButton);
        assessmentButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent assessmentIntent = new Intent(getApplicationContext(), AssessmentActivity.class);
                startActivity(assessmentIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
