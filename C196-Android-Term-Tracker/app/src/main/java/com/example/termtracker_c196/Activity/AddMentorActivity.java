package com.example.termtracker_c196.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.Model.Mentor;
import com.example.termtracker_c196.R;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddMentorActivity extends AppCompatActivity {
    private DBHelper myDb;
    private EditText name, phone, email;
    private Button saveBtn, deleteBtn, cancelBtn;
    private Mentor mentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);

        myDb = DBHelper.getInstance(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        getIncomingIntent();
    }

    public void saveData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertMentorData(name.getText().toString(), phone.getText().toString(), email.getText().toString());

                        if(isInserted){
                            Toast.makeText(AddMentorActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddMentorActivity.this, "Data Not Inserted",Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }

    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("MENTOR_COL_2") && getIntent().hasExtra("MENTOR_COL_3") && getIntent().hasExtra("MENTOR_COL_4"))
        {
            Integer id = getIntent().getIntExtra("MENTOR_COL_1", -1);
            String nameText = getIntent().getStringExtra("MENTOR_COL_2");
            String phoneText = getIntent().getStringExtra("MENTOR_COL_3");
            String emailText = getIntent().getStringExtra("MENTOR_COL_4");

            mentor = new Mentor(id, nameText, phoneText, emailText);
            setMentor(mentor);
            updateData(id);
            deleteData(id);
            cancelData();
            return true;
        }
        else {
            deleteBtn.setVisibility(View.INVISIBLE);
            saveData();
            cancelData();
            return false;
        }
    }

    private void setMentor(Mentor mentor){
        name.setText(mentor.getName(), EDITABLE);
        phone.setText(mentor.getPhoneNumber(), EDITABLE);
        email.setText(mentor.getEmailAddress(), EDITABLE);
    }

    public void updateData(final Integer id){
        saveBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean isUpdated = myDb.updateMentorData(id, name.getText().toString(), phone.getText().toString(), email.getText().toString());

                        if(isUpdated){
                            Toast.makeText(AddMentorActivity.this, "Mentor Updated", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddMentorActivity.this, "Mentor Not Updated", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }


    public void deleteData(final Integer id){
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isDeleted = myDb.deleteMentorData(id);

                        if (isDeleted) {
                            Toast.makeText(AddMentorActivity.this, "Mentor Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddMentorActivity.this, "Mentor Not Deleted", Toast.LENGTH_LONG).show();
                        }
                        finish();
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
}
