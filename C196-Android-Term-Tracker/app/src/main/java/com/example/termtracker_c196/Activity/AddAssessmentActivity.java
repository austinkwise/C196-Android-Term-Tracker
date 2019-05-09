package com.example.termtracker_c196.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.Helper.DateHelper;
import com.example.termtracker_c196.Helper.MyReceiver;
import com.example.termtracker_c196.Model.Assessment;
import com.example.termtracker_c196.R;

import java.util.Calendar;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddAssessmentActivity extends AppCompatActivity {
    private DBHelper myDb;
    private EditText name;
    private TextView dueDate, goalDate;
    private CheckBox goalDateAlert, dueDateAlert;
    private Spinner typeSpinner, courseSpinner;
    private Button saveBtn, deleteBtn, cancelBtn;
    private Assessment assessment;
    private DatePickerDialog.OnDateSetListener dateSetListener, adateSetListener;
    private int alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        myDb = DBHelper.getInstance(this);

        name = findViewById(R.id.name);
        dueDate = findViewById(R.id.dueDate);
        goalDate = findViewById(R.id.goalDate);
        goalDateAlert = findViewById(R.id.goalDateAlert);
        dueDateAlert = findViewById(R.id.dueDateAlert);

        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        setSpinners();
        setupDatePickers();
        getIncomingIntent();
    }

    private void setSpinners(){
        courseSpinner = findViewById(R.id.courseSpinner);
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getSpinnerContent(DBHelper.COURSE_TABLE_NAME));
        courseSpinner.setAdapter(spinnerCourseAdapter);

        typeSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> spinnerTypeAdapter =  ArrayAdapter.createFromResource(this,
                R.array.spinner_type, android.R.layout.simple_spinner_item);
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerTypeAdapter);
    }

    private boolean getIncomingIntent() {

        if (getIntent().hasExtra("ASSESSMENT_COL_2") && getIntent().hasExtra("ASSESSMENT_COL_3") && getIntent().hasExtra("ASSESSMENT_COL_4") && getIntent().hasExtra("ASSESSMENT_COL_5") && getIntent().hasExtra("ASSESSMENT_COL_6") && getIntent().hasExtra("ASSESSMENT_COL_7") && getIntent().hasExtra("ASSESSMENT_COL_8")) {
            Integer id = getIntent().getIntExtra("ASSESSMENT_COL_1", -1);
            String typeText = getIntent().getStringExtra("ASSESSMENT_COL_2");
            String titleText = getIntent().getStringExtra("ASSESSMENT_COL_3");
            String dueDateText = getIntent().getStringExtra("ASSESSMENT_COL_4");
            Integer dueAlert = getIntent().getIntExtra("ASSESSMENT_COL_5", -1);
            String goalDateText = getIntent().getStringExtra("ASSESSMENT_COL_6");
            Integer goalAlert = getIntent().getIntExtra("ASSESSMENT_COL_7", -1);
            Integer alertCode = getIntent().getIntExtra("ASSESSMENT_COL_8", -1);
            String courseId = getIntent().getStringExtra("ASSESSMENT_COL_9");

            assessment = new Assessment(id, courseId, typeText, titleText, dueDateText, dueAlert, goalDateText, goalAlert, alertCode);
            setAssessment(assessment);
            updateData(id);
            deleteData(id);
            cancelData();
            return true;
        } else {
            if(getIntent().hasExtra("COURSE_COL_2")){
                String courseName = getIntent().getStringExtra("COURSE_COL_2");
                courseSpinner.setSelection(myDb.getSpinnerPosition(courseSpinner.getAdapter(), courseName));
            }
            deleteBtn.setVisibility(View.INVISIBLE);
            saveData();
            cancelData();
            return false;
        }
    }

    public void deleteData ( final Integer id){
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isDeleted = myDb.deleteAssessmentDate(id);

                        if (isDeleted) {
                            Toast.makeText(AddAssessmentActivity.this, "Assessment Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddAssessmentActivity.this, "Assessment Not Deleted", Toast.LENGTH_LONG).show();
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

    private void setAssessment(Assessment assessment){
        name.setText(assessment.getTitle(), EDITABLE);
        dueDate.setText(assessment.getDueDate(), EDITABLE);
        dueDateAlert.setChecked(cbChecked(assessment.getGoalDateAlert()));
        goalDate.setText(assessment.getGoalDate(), EDITABLE);
        goalDateAlert.setChecked(cbChecked(assessment.getGoalDateAlert()));
        typeSpinner.setSelection(myDb.getSpinnerPosition(typeSpinner.getAdapter(), assessment.getType()));
        courseSpinner.setSelection(myDb.getSpinnerPosition(courseSpinner.getAdapter(), assessment.getCourseId()));

    }

    private boolean cbChecked(Integer i){
        if(i == 1){
            return true;
        }
        return false;
    }

    public void updateData(final Integer id){
        saveBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        Integer goalAlert = 0;
                        if(goalDateAlert.isChecked()){
                            goalAlert = 1;
                        }

                        Integer dueAlert = 0;
                        if(dueDateAlert.isChecked()){
                            goalAlert = 1;
                        }

                        int alarm = MyReceiver.getNextAlarmId(AddAssessmentActivity.this);

                        boolean isUpdated = myDb.updateAssessmentData(id, typeSpinner.getSelectedItem().toString(), name.getText().toString(), dueDate.getText().toString(), dueAlert, goalDate.getText().toString(), goalAlert, assessment.getAlarmCode(), courseSpinner.getSelectedItem().toString());

                        if(dueAlert == 1  && DateHelper.getDateTimestamp(dueDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("due_alert_title");
                        }
                        if (goalAlert == 1 && DateHelper.getDateTimestamp(goalDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("goal_alert_title");
                        }


                        else if (goalAlert == 0 && assessment.getGoalDateAlert() == 1)
                            cancelNotifications();

                        if(isUpdated){
                            Toast.makeText(AddAssessmentActivity.this, "Assessment Updated", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddAssessmentActivity.this, "Assessment Not Updated", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }


    public void saveData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer goalAlert = 0;
                        if(goalDateAlert.isChecked()){
                            goalAlert = 1;
                        }

                        Integer dueAlert = 0;
                        if(dueDateAlert.isChecked()){
                            goalAlert = 1;
                        }

                        alarm = MyReceiver.getNextAlarmId(AddAssessmentActivity.this);

                        boolean isInserted = myDb.insertAssessmentData(typeSpinner.getSelectedItem().toString(), name.getText().toString(), dueDate.getText().toString(), dueAlert, goalDate.getText().toString(), goalAlert, alarm, courseSpinner.getSelectedItem().toString());

                        MyReceiver.incrementNextAlarmId((AddAssessmentActivity.this));

                        if(dueAlert == 1  && DateHelper.getDateTimestamp(dueDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("due_alert_title");
                        }
                        if (goalAlert == 1 && DateHelper.getDateTimestamp(goalDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("goal_alert_title");
                        }

                        if(isInserted){
                            Toast.makeText(AddAssessmentActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddAssessmentActivity.this, "Data Not Inserted",Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }

    private void setupDatePickers(){
        dueDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddAssessmentActivity.this,
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
                dueDate.setText(date);
            }
        };

        goalDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddAssessmentActivity.this,
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
                goalDate.setText(date);
            }
        };
    }

    private void enableNotifications(String startOrEnd)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class );
        intent.putExtra("assessment_name", name.getText().toString());
        intent.putExtra("course_name", courseSpinner.getSelectedItem().toString());

        Integer alarmId = 0;
        String dateText = "";

        if (assessment != null)
            alarmId = assessment.getAlarmCode();
        else
            alarmId = alarm;

        intent.putExtra("id", alarmId);

        if (startOrEnd == "due_alert_title") {
            alarmId++;
            intent.putExtra(startOrEnd, "Reminder: A course is ending today!");
            dateText = dueDate.getText().toString();
        }
        else if (startOrEnd == "goal_alert_title") {
            intent.putExtra(startOrEnd, "Reminder: A course is starting today!");
            dateText = goalDate.getText().toString();
        }

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, DateHelper.getDate(dateText), pendingIntent);
    }

    private void cancelNotifications()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class );
        intent.putExtra("assessment_name", name.getText().toString());
        intent.putExtra("course_name", courseSpinner.getSelectedItem().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                assessment.getAlarmCode(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
