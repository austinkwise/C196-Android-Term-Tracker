package com.example.termtracker_c196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.Model.Notes;
import com.example.termtracker_c196.R;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddNotesActivity extends AppCompatActivity {
    private DBHelper myDb;
    private EditText message;
    private Spinner courseSpinner;
    private Button saveBtn, deleteBtn, cancelBtn, shareBtn;
    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        myDb = DBHelper.getInstance(this);

        message = findViewById(R.id.notesDescription);
        courseSpinner = findViewById(R.id.courseSpinner);

        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        shareBtn = findViewById(R.id.shareBtn);

        setSpinners();
        getIncomingIntent();
    }

    private boolean getIncomingIntent() {

        if (getIntent().hasExtra("NOTE_COL_2") && getIntent().hasExtra("NOTE_COL_3")) {
            Integer id = getIntent().getIntExtra("NOTE_COL_1", -1);
            String message = getIntent().getStringExtra("NOTE_COL_2");
            String courseId = getIntent().getStringExtra("NOTE_COL_3");

            notes = new Notes(id, message, courseId);
            setNotes(notes);
            updateData(id);
            deleteData(id);
            cancelData();
            shareNote();
            return true;
        } else {
            shareBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            saveData();
            cancelData();
            return false;
        }
    }

    private void setSpinners(){
        courseSpinner = findViewById(R.id.courseSpinner);
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getSpinnerContent(DBHelper.COURSE_TABLE_NAME));
        courseSpinner.setAdapter(spinnerCourseAdapter);
    }

    public void deleteData (final Integer id){
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isDeleted = myDb.deleteNoteData(id);

                        if (isDeleted) {
                            Toast.makeText(AddNotesActivity.this, "Notes Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddNotesActivity.this, "Notes Not Deleted", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });
    }

    public void cancelData () {
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    public void shareNote(){
        shareBtn.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View v){
                       Log.d("termcheck", "click is clicked");
                       Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                       sharingIntent.setType("text/plain");
                       String shareBody = "Here are my course notes for " + courseSpinner.getSelectedItem().toString() + ": " + message.getText().toString();
                       sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Course Notes for " + courseSpinner.getSelectedItem().toString());
                       sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                       startActivity(Intent.createChooser(sharingIntent, "Share via"));
                   }
               });
    }


    private void setNotes(Notes notes){
        message.setText(notes.getMessage(), EDITABLE);
        courseSpinner.setSelection(myDb.getSpinnerPosition(courseSpinner.getAdapter(), notes.getCourseId()));
    }

    public void saveData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertNoteData(message.getText().toString(), courseSpinner.getSelectedItem().toString());

                        if(isInserted){
                            Toast.makeText(AddNotesActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddNotesActivity.this, "Data Not Inserted",Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }

    public void updateData(final Integer id){
        saveBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean isUpdated = myDb.updateNoteData(id, message.getText().toString(), courseSpinner.getSelectedItem().toString());

                        if(isUpdated){
                            Toast.makeText(AddNotesActivity.this, "Notes Updated", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddNotesActivity.this, "Notes Not Updated", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }

}
