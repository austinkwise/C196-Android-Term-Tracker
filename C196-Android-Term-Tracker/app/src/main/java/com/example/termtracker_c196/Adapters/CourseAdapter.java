package com.example.termtracker_c196.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.termtracker_c196.Activity.AddCourseActivity;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private Context context;
    private Cursor cursor;

    public CourseAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;

        public CourseViewHolder(View v){
            super(v);
            nameText = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_item, v, false);
        return new CourseViewHolder(view);
    }

    public void onBindViewHolder(CourseViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_COL_1));
        final String title = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_2));
        final String status = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_3));
        final String startDate = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_4));
        final Integer startAlert = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_COL_5));
        final String endDate = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_6));
        final Integer endAlert = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_COL_7));
        final String notes = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_8));
        final String mentor = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_9));
        final String assessment = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_10));
        final Integer alertCode = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_COL_11));
        final String term = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_COL_12));

        viewHolder.nameText.setText(title);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCourseActivity.class);
                intent.putExtra("COURSE_COL_1", id);
                intent.putExtra("COURSE_COL_2", title);
                intent.putExtra("COURSE_COL_3", status);
                intent.putExtra("COURSE_COL_4", startDate);
                intent.putExtra("COURSE_COL_5", startAlert);
                intent.putExtra("COURSE_COL_6", endDate);
                intent.putExtra("COURSE_COL_7", endAlert);
                intent.putExtra("COURSE_COL_8", notes);
                intent.putExtra("COURSE_COL_9", mentor);
                intent.putExtra("COURSE_COL_10", assessment);
                intent.putExtra("COURSE_COL_11", alertCode);
                intent.putExtra("COURSE_COL_12", term);

                context.startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }

        cursor  = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
