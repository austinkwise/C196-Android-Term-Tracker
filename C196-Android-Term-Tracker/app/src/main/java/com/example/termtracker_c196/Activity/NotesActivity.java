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

import com.example.termtracker_c196.Adapters.NotesAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class NotesActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private RecyclerView rv;
    private NotesAdapter adapter;
    private String notesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        DBHelper dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        getIncomingIntent();

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotesAdapter(this, getSelectedItems(notesName));
        rv.setAdapter(adapter);
        rv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent selectedNotesIntent = new Intent(getBaseContext(), AddNotesActivity.class);
                startActivity(selectedNotesIntent);
            }
        });

        Button notesButton = (Button) findViewById(R.id.addNoteBtn);
        notesButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent addNotesIntent = new Intent(getApplicationContext(), AddNotesActivity.class);
                startActivity(addNotesIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (notesName == null){
                    Intent courseIntent = new Intent(getApplicationContext(), CourseActivity.class);
                    startActivity(courseIntent);
                }
                else{
                    adapter.swapCursor(getSelectedItems(notesName));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent courseIntent = new Intent(getBaseContext(), AddNotesActivity.class);
        startActivity(courseIntent);
        adapter.swapCursor(getSelectedItems(notesName));
        return super.onOptionsItemSelected(item);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("COURSE_COL_2")){
            notesName = getIntent().getStringExtra("COURSE_COL_2");
            return true;
        }
        else{
            notesName = null;
            return false;
        }
    }

    private Cursor getSelectedItems(String notesName){
        if(notesName == null){
            return db.query(DBHelper.NOTE_TABLE_NAME, null, null, null, null, null, null);
        }
        return db.query(DBHelper.NOTE_TABLE_NAME, null, DBHelper.NOTE_COL_3 + " = '" + notesName + "'", null, null, null, null);
    }
}
