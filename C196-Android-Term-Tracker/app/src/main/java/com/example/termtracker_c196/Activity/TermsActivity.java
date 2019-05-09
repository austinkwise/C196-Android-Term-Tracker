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

import com.example.termtracker_c196.Adapters.TermsAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class TermsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private RecyclerView rv;
    private TermsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        DBHelper dbHelper = DBHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TermsAdapter(this, getAllItems());
        rv.setAdapter(adapter);
        rv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent startIntent = new Intent(getBaseContext(), AddTermActivity.class);
                startActivity(startIntent);
            }
        });

        Button termButton = (Button) findViewById(R.id.addTermsButton);
        termButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent startIntent = new Intent(getApplicationContext(), AddTermActivity.class);
                startActivity(startIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent termIntent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(termIntent);
            }
        });
    }

    private Cursor getAllItems(){
        return db.query(DBHelper.TERM_TABLE_NAME, null, null, null, null, null, null);
    }
}
