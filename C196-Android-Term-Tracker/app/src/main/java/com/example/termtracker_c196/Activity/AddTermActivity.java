package com.example.termtracker_c196.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.Model.Term;
import com.example.termtracker_c196.R;

import java.util.Calendar;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddTermActivity extends AppCompatActivity {
    private DBHelper myDb;
    private EditText termTitle;
    private TextView startDate, endDate;
    private Button saveBtn, deleteBtn, cancelBtn, courseBtn;
    private Term term;
    private DatePickerDialog.OnDateSetListener dateSetListener, adateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        myDb = DBHelper.getInstance(this);

        termTitle = findViewById(R.id.termTitle);
        startDate = findViewById(R.id.startDatetv);
        endDate = findViewById(R.id.endDatetv);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        courseBtn = findViewById(R.id.courseBtn);

        setupDatePickers();
        getIncomingIntent();
    }

    public void saveData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertTermData(termTitle.getText().toString(), startDate.getText().toString(), endDate.getText().toString());

                        if(isInserted){
                            Toast.makeText(AddTermActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddTermActivity.this, "Data Not Inserted",Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }

    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("TERM_COL_2") && getIntent().hasExtra("TERM_COL_3") && getIntent().hasExtra("TERM_COL_4"))
        {
            Integer id = getIntent().getIntExtra("TERM_COL_1", -1);
            String nameText = getIntent().getStringExtra("TERM_COL_2");
            String startText = getIntent().getStringExtra("TERM_COL_3");
            String endText = getIntent().getStringExtra("TERM_COL_4");

            term = new Term(id, nameText, startText, endText);
            setTerm(term);
            courseBtn();
            updateData(id);
            deleteData(id);
            cancelData();
            return true;
        }
        else {
            courseBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            saveData();
            cancelData();
            return false;
        }
    }

    private void setTerm(Term term){
        termTitle.setText(term.getName(), EDITABLE);
        startDate.setText(term.getStartDate(), EDITABLE);
        endDate.setText(term.getEndDate(), EDITABLE);
    }

    public void updateData(final Integer id){
        saveBtn.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    boolean isUpdated = myDb.updateTermData(id, termTitle.getText().toString(), startDate.getText().toString(), endDate.getText().toString());

                    if(isUpdated){
                        Toast.makeText(AddTermActivity.this, "Term Updated", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(AddTermActivity.this, "Term Not Updated", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        );
    }


    public void deleteData(final Integer id){
        //first check if the term has any courses set in it
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String query = "SELECT * FROM " + DBHelper.COURSE_TABLE_NAME + " WHERE " + DBHelper.COURSE_COL_12 + " = '" + term.getName() + "'";
                        Cursor cursor = myDb.getWritableDatabase().rawQuery(query, null);
                        if(cursor.getCount() != 0){
                            Toast.makeText(AddTermActivity.this, "Can't delete term with assigned course attached.", Toast.LENGTH_LONG).show();

                        }
                        else{
                            boolean isDeleted = myDb.deleteTerm(id);

                            if (isDeleted) {
                                Toast.makeText(AddTermActivity.this, "Term Deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddTermActivity.this, "Term Not Deleted", Toast.LENGTH_LONG).show();
                            }
                            finish();
                        }

                    }
                });
    }

    public void cancelData(){
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    public void courseBtn(){
        courseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent courseIntent = new Intent(getBaseContext(), CourseActivity.class);
                courseIntent.putExtra("TERM_COL_2", term.getName());
                startActivity(courseIntent);
            }
        });
    }

    private void setupDatePickers(){
        startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTermActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                startDate.setText(date);
            }
        };

        endDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTermActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        adateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        adateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                endDate.setText(date);
            }
        };
    }
}
