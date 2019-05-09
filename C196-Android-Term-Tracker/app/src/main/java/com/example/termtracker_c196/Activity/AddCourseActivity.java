package com.example.termtracker_c196.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker_c196.Adapters.CourseAdapter;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.Helper.DateHelper;
import com.example.termtracker_c196.Helper.MyReceiver;
import com.example.termtracker_c196.Model.Course;
import com.example.termtracker_c196.R;

import java.util.Calendar;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddCourseActivity extends AppCompatActivity {
    private DBHelper myDb;
    private EditText courseTitle;
    private TextView startDate, endDate;
    private CheckBox startDateAlert, endDateAlert;
    private Spinner termSpinner, statusSpinner, mentorSpinner;
    private Button saveBtn, deleteBtn, cancelBtn, notesBtn, assessmentBtn;
    private Course course;
    private Integer courseId;
    private int alarm;
    private DatePickerDialog.OnDateSetListener dateSetListener, adateSetListener;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        myDb = DBHelper.getInstance(this);

        courseTitle = findViewById(R.id.courseTitle);
        startDate = findViewById(R.id.startDatetv);
        endDate = findViewById(R.id.endDatetv);
        startDateAlert = findViewById(R.id.startDateAlert);
        endDateAlert = findViewById(R.id.endDateAlert);

        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        notesBtn = findViewById(R.id.notesBtn);
        assessmentBtn = findViewById(R.id.assessmentBtn);

        setSpinners();
        setupDatePickers();
        getIncomingIntent();
    }

    public void notesClick(){
        notesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent notesIntent = new Intent(getApplicationContext(), NotesActivity.class);
                        notesIntent.putExtra("COURSE_COL_2", course.getTitle());
                        startActivity(notesIntent);
                    }
                });
    }

    public void assessmentClick(){
        assessmentBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent assessmentIntent = new Intent(getApplicationContext(), AssessmentActivity.class);
                        assessmentIntent.putExtra("COURSE_COL_2", course.getTitle());
                        startActivity(assessmentIntent);
                    }
                });
    }

    private void setSpinners(){
        termSpinner = findViewById(R.id.termSpinner);
        ArrayAdapter<String> spinnerTermAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getSpinnerContent(DBHelper.TERM_TABLE_NAME));
        termSpinner.setAdapter(spinnerTermAdapter);

        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> spinnerStatusAdapter =  ArrayAdapter.createFromResource(this,
                R.array.spinner_status, android.R.layout.simple_spinner_item);
        spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerStatusAdapter);

        mentorSpinner = findViewById(R.id.mentorSpinner);
        ArrayAdapter<String> spinnerMentorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                myDb.getSpinnerContent(DBHelper.MENTOR_TABLE_NAME));
        mentorSpinner.setAdapter(spinnerMentorAdapter);
    }

    private boolean getIncomingIntent(){

        if(getIntent().hasExtra("COURSE_COL_2") && getIntent().hasExtra("COURSE_COL_3") && getIntent().hasExtra("COURSE_COL_4") && getIntent().hasExtra("COURSE_COL_5") && getIntent().hasExtra("COURSE_COL_6") && getIntent().hasExtra("COURSE_COL_7") && getIntent().hasExtra("COURSE_COL_8") && getIntent().hasExtra("COURSE_COL_9") && getIntent().hasExtra("COURSE_COL_10") && getIntent().hasExtra("COURSE_COL_11") && getIntent().hasExtra("COURSE_COL_12"))
        {
            courseId = getIntent().getIntExtra("COURSE_COL_1", -1);
            String titleText = getIntent().getStringExtra("COURSE_COL_2");
            String statusText = getIntent().getStringExtra("COURSE_COL_3");
            String startText = getIntent().getStringExtra("COURSE_COL_4");
            Integer startAlert = getIntent().getIntExtra("COURSE_COL_5", -1);
            String endText = getIntent().getStringExtra("COURSE_COL_6");
            Integer endAlert = getIntent().getIntExtra("COURSE_COL_7", -1);
            String notesId = getIntent().getStringExtra("COURSE_COL_8");
            String mentorId = getIntent().getStringExtra("COURSE_COL_9");
            String assessmentId = getIntent().getStringExtra("COURSE_COL_10");
            Integer alertCode = getIntent().getIntExtra("COURSE_COL_11", -1);
            String courseTerm = getIntent().getStringExtra("COURSE_COL_12");

            Log.d("termcheck", "term in get Incoming Intent is: " + courseTerm);//*****

            course = new Course(courseId, courseTerm, titleText, statusText, startText,
                    startAlert, endText, endAlert, assessmentId, notesId, mentorId, alertCode);
            setCourse(course);
            notesClick();
            assessmentClick();
            updateData(courseId);
            deleteData(courseId);
            cancelData();
            return true;
        }
        else {
            if(getIntent().hasExtra("TERM_COL_2")){
                String termName = getIntent().getStringExtra("TERM_COL_2");
                termSpinner.setSelection(myDb.getSpinnerPosition(termSpinner.getAdapter(), termName));
            }
            notesBtn.setVisibility(View.INVISIBLE);
            assessmentBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            saveData();
            cancelData();
            return false;
        }
    }

    private void setCourse(Course course){
        courseTitle.setText(course.getTitle(), EDITABLE);
        statusSpinner.setSelection(myDb.getSpinnerPosition(statusSpinner.getAdapter(), course.getStatus()));
        startDate.setText(course.getStartDate(), EDITABLE);
        startDateAlert.setChecked(cbChecked(course.getStartDateAlert()));
        endDate.setText(course.getEndDate(), EDITABLE);
        endDateAlert.setChecked(cbChecked(course.getEndDateAlert()));
        mentorSpinner.setSelection(myDb.getSpinnerPosition(mentorSpinner.getAdapter(), course.getMentors()));

        String termHelper = course.getTermId();
        Log.d("termcheck", "term in setCourse() is: " + termHelper);//*****
        termSpinner.setSelection(myDb.getSpinnerPosition(termSpinner.getAdapter(), course.getTermId()));
    }

    private boolean cbChecked(Integer i){
        if(i == 1){
            return true;
        }
        return false;
    }

    public void saveData(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer startAlert = 0;
                        if(startDateAlert.isChecked()){
                            startAlert = 1;
                        }
                        Integer endAlert = 0;
                        if(endDateAlert.isChecked()){
                            endAlert = 1;
                        }

                        alarm = MyReceiver.getNextAlarmId(AddCourseActivity.this);

                        String termHelper = termSpinner.getSelectedItem().toString();

                        boolean isInserted = myDb.insertCourseData(courseTitle.getText().toString(), statusSpinner.getSelectedItem().toString(), startDate.getText().toString(), startAlert, endDate.getText().toString(), endAlert, mentorSpinner.getSelectedItem().toString(), alarm, termSpinner.getSelectedItem().toString());

                        MyReceiver.incrementNextAlarmId(AddCourseActivity.this);

                        if(startAlert == 1 && DateHelper.getDateTimestamp(startDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("start_alert_title");
                        }
                        if(endAlert == 1  && DateHelper.getDateTimestamp(endDate.getText().toString()) > System.currentTimeMillis()){
                            enableNotifications("end_alert_title");
                        }

                        if(isInserted){
                            Toast.makeText(AddCourseActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddCourseActivity.this, "Data Not Inserted",Toast.LENGTH_LONG).show();
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
                    public void onClick(View v) {

                        Integer startAlert = 0;
                        if (startDateAlert.isChecked()) {
                            startAlert = 1;
                        }
                        Integer endAlert = 0;
                        if (endDateAlert.isChecked()) {
                            endAlert = 1;
                        }

                        alarm = MyReceiver.getNextAlarmId(AddCourseActivity.this);

                        boolean isUpdated = myDb.updateCourseData(id, courseTitle.getText().toString(), statusSpinner.getSelectedItem().toString(), startDate.getText().toString(), startAlert, endDate.getText().toString(), endAlert, course.getNotesId(), mentorSpinner.getSelectedItem().toString(), course.getAssessmentsId(), alarm, termSpinner.getSelectedItem().toString());

                        if (course.getTitle() != courseTitle.getText().toString()) {
                            myDb.updateCourseAssessment(course.getTitle(), courseTitle.getText().toString());
                            myDb.updateCourseNotes(course.getTitle(), courseTitle.getText().toString());
                        }

                        if (startAlert == 1 && DateHelper.getDateTimestamp(startDate.getText().toString()) > System.currentTimeMillis()) {
                            enableNotifications("start_alert_title");
                        } else if (startAlert == 0 && course.getStartDateAlert() == 1) {
                            cancelNotifications("start_alert_title");
                        }

                        if (endAlert == 1 && DateHelper.getDateTimestamp(endDate.getText().toString()) > System.currentTimeMillis()) {
                            enableNotifications("end_alert_title");
                        } else if (endAlert == 0 && course.getEndDateAlert() == 1){
                            cancelNotifications("end_alert_title");
                        }

                        if(isUpdated){
                            Toast.makeText(AddCourseActivity.this, "Course Updated", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(AddCourseActivity.this, "Course Not Updated", Toast.LENGTH_LONG).show();
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
                            boolean isDeleted = myDb.deleteCourseData(id);

                            if (isDeleted) {
                                Toast.makeText(AddCourseActivity.this, "Course Deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddCourseActivity.this, "Course Not Deleted", Toast.LENGTH_LONG).show();
                            }
                            finish();
                    }
                });
    }

    public void cancelData(){

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
                        AddCourseActivity.this,
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

        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        endDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddCourseActivity.this,
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

    private void enableNotifications(String startOrEnd)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class );
        intent.putExtra("course_name", courseTitle.getText().toString());
        intent.putExtra("term_name", termSpinner.getSelectedItem().toString());

        Integer alarmId = 0;
        String dateText = "";

        if (course != null)
            alarmId = course.getAlertCode();
        else
            alarmId = alarm;

        intent.putExtra("id", alarmId);

        if (startOrEnd == "end_alert_title") {
            alarmId++;
            intent.putExtra(startOrEnd, "Reminder: A course is ending today!");
            dateText = endDate.getText().toString();
        }
        else if (startOrEnd == "start_alert_title") {
            intent.putExtra(startOrEnd, "Reminder: A course is starting today!");
            dateText = startDate.getText().toString();
        }

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, DateHelper.getDate(dateText), pendingIntent);
    }

    private void cancelNotifications(String startOrEnd)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class );
        intent.putExtra("course_name", courseTitle.getText().toString());
        intent.putExtra("term_name", termSpinner.getSelectedItem().toString());
        intent.putExtra(startOrEnd, "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                course.getAlertCode(), intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
