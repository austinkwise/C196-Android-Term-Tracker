package com.example.termtracker_c196.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.termtracker_c196.Activity.AddNotesActivity;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private Context context;
    private Cursor cursor;

    public NotesAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;

        public NotesViewHolder(View v){
            super(v);
            nameText = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notes_item, v, false);
        return new NotesViewHolder(view);
    }

    public void onBindViewHolder(NotesViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DBHelper.NOTE_COL_1));
        final String message = cursor.getString(cursor.getColumnIndex(DBHelper.NOTE_COL_2));
        final String courseId = cursor.getString(cursor.getColumnIndex(DBHelper.NOTE_COL_3));

        viewHolder.nameText.setText(message);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNotesActivity.class);
                intent.putExtra("NOTE_COL_1", id);
                intent.putExtra("NOTE_COL_2", message);
                intent.putExtra("NOTE_COL_3", courseId);

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
