package com.example.termtracker_c196.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.termtracker_c196.Activity.AddAssessmentActivity;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private Context context;
    private Cursor cursor;

    public AssessmentAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;

        public AssessmentViewHolder(View v){
            super(v);
            nameText = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public AssessmentViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.assessment_item, v, false);
        return new AssessmentViewHolder(view);
    }

    public void onBindViewHolder(AssessmentViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_1));
        final String type = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_2));
        final String title = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_3));
        final String dueDate = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_4));
        final Integer dueDateAlert = cursor.getInt(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_5));
        final String goalDate = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_6));
        final Integer goalDateAlert = cursor.getInt(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_7));
        final String alertCode = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_8));
        final String courseId = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_COL_9));

        viewHolder.nameText.setText(title);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAssessmentActivity.class);
                intent.putExtra("ASSESSMENT_COL_1", id);
                intent.putExtra("ASSESSMENT_COL_2", type);
                intent.putExtra("ASSESSMENT_COL_3", title);
                intent.putExtra("ASSESSMENT_COL_4", dueDate);
                intent.putExtra("ASSESSMENT_COL_5", dueDateAlert);
                intent.putExtra("ASSESSMENT_COL_6", goalDate);
                intent.putExtra("ASSESSMENT_COL_7", goalDateAlert);
                intent.putExtra("ASSESSMENT_COL_8", alertCode);
                intent.putExtra("ASSESSMENT_COL_9", courseId);

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
