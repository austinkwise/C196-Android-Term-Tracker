package com.example.termtracker_c196.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.termtracker_c196.Activity.AddMentorActivity;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder> {
    private Context context;
    private Cursor cursor;

    public MentorAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public class MentorViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;

        public MentorViewHolder(View v){
            super(v);
            nameText = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public MentorViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mentor_item, v, false);
        return new MentorViewHolder(view);
    }

    public void onBindViewHolder(MentorViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DBHelper.MENTOR_COL_1));
        final String name = cursor.getString(cursor.getColumnIndex(DBHelper.MENTOR_COL_2));
        final String phone = cursor.getString(cursor.getColumnIndex(DBHelper.MENTOR_COL_3));
        final String email = cursor.getString(cursor.getColumnIndex(DBHelper.MENTOR_COL_4));

        viewHolder.nameText.setText(name);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMentorActivity.class);
                intent.putExtra("MENTOR_COL_1", id);
                intent.putExtra("MENTOR_COL_2", name);
                intent.putExtra("MENTOR_COL_3", phone);
                intent.putExtra("MENTOR_COL_4", email);

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
