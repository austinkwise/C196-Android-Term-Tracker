package com.example.termtracker_c196.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.termtracker_c196.Adapters.AssessmentAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class AssessmentActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private RecyclerView rv;
    private AssessmentAdapter adapter;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        DBHelper dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        getIncomingIntent();

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AssessmentAdapter(this, getSelectedItems(courseName));
        rv.setAdapter(adapter);
        rv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent selectedAssessmentIntent = new Intent(getBaseContext(), AddAssessmentActivity.class);
                startActivity(selectedAssessmentIntent);
            }
        });

        Button assessmentButton = findViewById(R.id.addAssessmentBtn);
        assessmentButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent addAssIntent = new Intent(getApplicationContext(), AddAssessmentActivity.class);
                startActivity(addAssIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (courseName == null){
                    Intent assessmentIntent = new Intent(getApplicationContext(), AssessmentActivity.class);
                    startActivity(assessmentIntent);
                }
                else{
                    adapter.swapCursor(getSelectedItems(courseName));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent courseIntent = new Intent(getBaseContext(), AddCourseActivity.class);
        startActivity(courseIntent);
        adapter.swapCursor(getSelectedItems(courseName));
        return super.onOptionsItemSelected(item);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("COURSE_COL_2")){
            courseName = getIntent().getStringExtra("COURSE_COL_2");
            return true;
        }
        else{
            courseName = null;
            return false;
        }
    }

    private Cursor getSelectedItems(String courseName){
        if(courseName == null){
            return db.query(DBHelper.ASSESSMENT_TABLE_NAME, null, null, null, null, null, null);
        }
        return db.query(DBHelper.ASSESSMENT_TABLE_NAME, null, DBHelper.ASSESSMENT_COL_9 + " = '" + courseName + "'", null, null, null, null);

    }
}
