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

import com.example.termtracker_c196.Adapters.CourseAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class CourseActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private RecyclerView rv;
    private CourseAdapter adapter;
    private String termName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        DBHelper dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        getIncomingIntent();

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CourseAdapter(this, getSelectedItems(termName));
        rv.setAdapter(adapter);
        rv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent selectedCourseIntent = new Intent(getBaseContext(), AddCourseActivity.class);
                startActivity(selectedCourseIntent);
            }
        });

        Button courseButton = findViewById(R.id.addCourseBtn);
        courseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent addCourseIntent = new Intent(getApplicationContext(), AddCourseActivity.class);
                startActivity(addCourseIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (termName == null){
                    Intent courseIntent = new Intent(getApplicationContext(), CourseActivity.class);
                    startActivity(courseIntent);
                }
                else{
                    adapter.swapCursor(getSelectedItems(termName));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent courseIntent = new Intent(getBaseContext(), AddCourseActivity.class);
        startActivity(courseIntent);
        adapter.swapCursor(getSelectedItems(termName));
        return super.onOptionsItemSelected(item);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("TERM_COL_2")){
            termName = getIntent().getStringExtra("TERM_COL_2");
            return true;
        }
        else{
            termName = null;
            return false;
        }
    }

    private Cursor getSelectedItems(String termName){
        if(termName == null){
            return db.query(DBHelper.COURSE_TABLE_NAME, null, null, null, null, null, null);
        }
        return db.query(DBHelper.COURSE_TABLE_NAME, null, DBHelper.COURSE_COL_12 + " = '" + termName + "'", null, null, null, null);
    }


}
