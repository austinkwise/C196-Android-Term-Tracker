package com.example.termtracker_c196.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "programTracker.db";
    private static final int DATABASE_VERSION = 8;
    private static DBHelper instance = null;

    public static final String TERM_TABLE_NAME = "termTable";
    public static final String TERM_COL_1 = "termId";
    public static final String TERM_COL_2 = "name";
    public static final String TERM_COL_3 = "startDate";
    public static final String TERM_COL_4 = "endDate";

    public static final String MENTOR_TABLE_NAME = "mentorTable";
    public static final String MENTOR_COL_1 = "mentorId";
    public static final String MENTOR_COL_2 = "name";
    public static final String MENTOR_COL_3 = "phone";
    public static final String MENTOR_COL_4 = "emailAdress";

    public static final String COURSE_TABLE_NAME = "courseTable";
    public static final String COURSE_COL_1 = "courseId";
    public static final String COURSE_COL_2 = "courseTitle";
    public static final String COURSE_COL_3 = "courseStatus";
    public static final String COURSE_COL_4 = "startDate";
    public static final String COURSE_COL_5 = "startDateAlert";
    public static final String COURSE_COL_6 = "endDate";
    public static final String COURSE_COL_7 = "endDateAlert";
    public static final String COURSE_COL_8 = "notesId";
    public static final String COURSE_COL_9 = "mentor";
    public static final String COURSE_COL_10 = "assessmentId";
    public static final String COURSE_COL_11 = "alertCode";
    public static final String COURSE_COL_12 = "termId";

    public static final String ASSESSMENT_TABLE_NAME = "assessmentTable";
    public static final String ASSESSMENT_COL_1 = "assessmentId";
    public static final String ASSESSMENT_COL_2 = "assessmentType";
    public static final String ASSESSMENT_COL_3 = "assessmentTitle";
    public static final String ASSESSMENT_COL_4 = "dueDate";
    public static final String ASSESSMENT_COL_5 = "dueDateAlert";
    public static final String ASSESSMENT_COL_6 = "goalDate";
    public static final String ASSESSMENT_COL_7 = "goalDateAlert";
    public static final String ASSESSMENT_COL_8 = "alertCode";
    public static final String ASSESSMENT_COL_9 = "courseId";

    public static final String NOTE_TABLE_NAME = "noteTable";
    public static final String NOTE_COL_1 = "noteId";
    public static final String NOTE_COL_2 = "noteMessage";
    public static final String NOTE_COL_3 = "courseId";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String termCreateStmt = "create table " + TERM_TABLE_NAME + "(" + TERM_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TERM_COL_2 + " TEXT, " + TERM_COL_3 + " TEXT, " + TERM_COL_4 + " TEXT)";
        db.execSQL(termCreateStmt);

        String mentorCreateStmt = "create table " + MENTOR_TABLE_NAME + "(" + MENTOR_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MENTOR_COL_2 + " TEXT, " + MENTOR_COL_3 + " TEXT, " +  MENTOR_COL_4 + " TEXT)";
        db.execSQL(mentorCreateStmt);

        String courseCreateStmt = "create table " + COURSE_TABLE_NAME + "(" + COURSE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_COL_2 + " TEXT, " + COURSE_COL_3 + " TEXT, " + COURSE_COL_4 + " TEXT, " + COURSE_COL_5 + " INTEGER, " + COURSE_COL_6 + " TEXT, " + COURSE_COL_7 + " INTEGER, " + COURSE_COL_8 + " TEXT, " + COURSE_COL_9 + " TEXT, " + COURSE_COL_10 + " TEXT, " + COURSE_COL_11 + " INTEGER, " + COURSE_COL_12 + " INTEGER REFERENCES " + TERM_TABLE_NAME + ")";
        db.execSQL((courseCreateStmt));

        String assessmentCreateStmt = "create table " + ASSESSMENT_TABLE_NAME + "(" + ASSESSMENT_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ASSESSMENT_COL_2 + " TEXT, " + ASSESSMENT_COL_3 + " TEXT, " + ASSESSMENT_COL_4 + " TEXT, " + ASSESSMENT_COL_5 + " INTEGER, " + ASSESSMENT_COL_6 + " TEXT, " + ASSESSMENT_COL_7 + " INTEGER, " + ASSESSMENT_COL_8 + " TEXT, " + ASSESSMENT_COL_9 + " INTEGER REFERENCES " + COURSE_TABLE_NAME + ")";
        db.execSQL(assessmentCreateStmt);

        String noteCreateStmt = "create table " + NOTE_TABLE_NAME +  "(" + NOTE_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_COL_2 + " TEXT, " + NOTE_COL_3 + " INTEGER REFERENCES " + COURSE_TABLE_NAME + ")";
        db.execSQL(noteCreateStmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String termDrop = "DROP TABLE IF EXISTS " + TERM_TABLE_NAME;
        db.execSQL(termDrop);

        String mentorDrop = "DROP TABLE IF EXISTS " + MENTOR_TABLE_NAME;
        db.execSQL(mentorDrop);

        String courseDrop = "DROP TABLE IF EXISTS " + COURSE_TABLE_NAME;
        db.execSQL(courseDrop);

        String assessmentDrop = "DROP TABLE IF EXISTS " + ASSESSMENT_TABLE_NAME;
        db.execSQL(assessmentDrop);

        String noteDrop = "DROP TABLE IF EXISTS " + NOTE_TABLE_NAME;
        db.execSQL(noteDrop);

        onCreate(db);
    }

    public static DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    /*
    ============
    TERMS
    ============
     */
    public boolean insertTermData(String name, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COL_2, name);
        contentValues.put(TERM_COL_3, startDate);
        contentValues.put(TERM_COL_4, endDate);

        long result = db.insert(TERM_TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateTermData(Integer Id, String name, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TERM_COL_2, name);
        contentValues.put(TERM_COL_3, startDate);
        contentValues.put(TERM_COL_4, endDate);

        long result = db.update(TERM_TABLE_NAME, contentValues, TERM_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteTerm(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TERM_TABLE_NAME, TERM_COL_1 + " = " + Id, null);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*
    ===============
    MENTORS
    ===============
     */
    public boolean insertMentorData(String name, String phone, String email){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COL_2, name);
        contentValues.put(MENTOR_COL_3, phone);
        contentValues.put(MENTOR_COL_4, email);

        long result = db.insert(MENTOR_TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateMentorData(Integer Id, String name, String phone, String email){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MENTOR_COL_2, name);
        contentValues.put(MENTOR_COL_3, phone);
        contentValues.put(MENTOR_COL_4, email);

        long result = db.update(MENTOR_TABLE_NAME, contentValues, MENTOR_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteMentorData(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(MENTOR_TABLE_NAME, MENTOR_COL_1 + " = " + Id, null);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    /*
    ===============
    COURSES
    ===============
     */
    public boolean insertCourseData(String title, String status, String startDate, Integer startDateAlert, String endDate, Integer endDateAlert, String mentor, Integer alertCode, String termId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_2, title);
        contentValues.put(COURSE_COL_3, status);
        contentValues.put(COURSE_COL_4, startDate);
        contentValues.put(COURSE_COL_5, startDateAlert);
        contentValues.put(COURSE_COL_6, endDate);
        contentValues.put(COURSE_COL_7, endDateAlert);
        contentValues.put(COURSE_COL_9, mentor);
        contentValues.put(COURSE_COL_11, alertCode);
        Log.d("termcheck", "term in insertCourseData is: " + termId);
        contentValues.put(COURSE_COL_12, termId);

        long result = db.insert(COURSE_TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateCourseData(Integer Id, String title, String status, String startDate,
                                    Integer startDateAlert, String endDate, Integer endDateAlert,
                                    String notesId, String mentor, String assessmentId, Integer alertCode,
                                    String termId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_COL_2, title);
        contentValues.put(COURSE_COL_3, status);
        contentValues.put(COURSE_COL_4, startDate);
        contentValues.put(COURSE_COL_5, startDateAlert);
        contentValues.put(COURSE_COL_6, endDate);
        contentValues.put(COURSE_COL_7, endDateAlert);
        contentValues.put(COURSE_COL_8, notesId);
        contentValues.put(COURSE_COL_9, mentor);
        contentValues.put(COURSE_COL_10, assessmentId);
        contentValues.put(COURSE_COL_11, alertCode);
        contentValues.put(COURSE_COL_12, termId);

        long result = db.update(COURSE_TABLE_NAME, contentValues, COURSE_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteCourseData(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(COURSE_TABLE_NAME, COURSE_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }




    /*
    ===============
    ASSESSMENTS
    ===============
     */
    public boolean insertAssessmentData(String type, String title, String dueDate, Integer dueDateAlert, String goalDate, Integer goalDateAlert, Integer alertCode, String courseId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_2, type);
        contentValues.put(ASSESSMENT_COL_3, title);
        contentValues.put(ASSESSMENT_COL_4, dueDate);
        contentValues.put(ASSESSMENT_COL_5, dueDateAlert);
        contentValues.put(ASSESSMENT_COL_6, goalDate);
        contentValues.put(ASSESSMENT_COL_7, goalDateAlert);
        contentValues.put(ASSESSMENT_COL_8, alertCode);
        contentValues.put(ASSESSMENT_COL_9, courseId);

        long result = db.insert(ASSESSMENT_TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateAssessmentData(Integer Id, String type, String title, String dueDate, Integer dueDateAlert, String goalDate, Integer goalDateAlert, Integer alertCode, String courseId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_2, type);
        contentValues.put(ASSESSMENT_COL_3, title);
        contentValues.put(ASSESSMENT_COL_4, dueDate);
        contentValues.put(ASSESSMENT_COL_5, dueDateAlert);
        contentValues.put(ASSESSMENT_COL_6, goalDate);
        contentValues.put(ASSESSMENT_COL_7, goalDateAlert);
        contentValues.put(ASSESSMENT_COL_8, alertCode);
        contentValues.put(ASSESSMENT_COL_9, courseId);

        long result = db.update(ASSESSMENT_TABLE_NAME, contentValues, ASSESSMENT_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteAssessmentDate(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(ASSESSMENT_TABLE_NAME, ASSESSMENT_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateCourseAssessment(String oldCourseName, String newCourseName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSESSMENT_COL_9, newCourseName);
        long result = db.update(ASSESSMENT_TABLE_NAME, contentValues, ASSESSMENT_COL_9 + " = '" + oldCourseName + "'", null);
        if(result == -1)
            return false;
        else
            return true;

    }

    public boolean updateCourseNotes(String oldCourseName, String newCourseName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_3, newCourseName);
        long result = db.update(NOTE_TABLE_NAME, contentValues, NOTE_COL_3 + " = '" + oldCourseName + "'", null);
        if(result == -1)
            return false;
        else
            return true;
    }

    /*
    ===============
    NOTES
    ===============
     */

    public boolean insertNoteData(String message, String courseId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, message);
        contentValues.put(NOTE_COL_3, courseId);

        long result = db.insert(NOTE_TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateNoteData(Integer Id, String message, String courseId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, message);
        contentValues.put(NOTE_COL_3, courseId);

        long result = db.update(NOTE_TABLE_NAME, contentValues, NOTE_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteNoteData(Integer Id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(NOTE_TABLE_NAME, NOTE_COL_1 + " = " + Id, null);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }


    /*
    ===============
    Spinners
    ===============
     */
    public ArrayList<String> getSpinnerContent(String tableName) {
        String query = "Select * from " + tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                spinnerContent.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return spinnerContent;

    }

    public Integer getSpinnerPosition (SpinnerAdapter s, String v)
    {
        for (int i = 0; i < s.getCount(); i++)
            if (s.getItem(i).toString().equals(v))
                return i;
        return -1;
    }
}
