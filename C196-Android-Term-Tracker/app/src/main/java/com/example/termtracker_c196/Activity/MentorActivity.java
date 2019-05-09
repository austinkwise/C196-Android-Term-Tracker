package com.example.termtracker_c196.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.termtracker_c196.Adapters.MentorAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class MentorActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private RecyclerView rv;
    private MentorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        DBHelper dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MentorAdapter(this, getAllItems());
        rv.setAdapter(adapter);
        rv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent selectedMentorIntent = new Intent(getBaseContext(), AddMentorActivity.class);
                startActivity(selectedMentorIntent);
            }
        });

        Button mentorButton = (Button) findViewById(R.id.addMentorButton);
        mentorButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent addMentorIntent = new Intent(getApplicationContext(), AddMentorActivity.class);
                startActivity(addMentorIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent mentorIntent = new Intent(getApplicationContext(), MentorActivity.class);
                startActivity(mentorIntent);
            }
        });
    }
    private Cursor getAllItems(){
        return db.query(DBHelper.MENTOR_TABLE_NAME, null, null, null, null, null, null);
    }
}
