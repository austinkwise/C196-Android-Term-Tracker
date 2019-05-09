package com.example.termtracker_c196.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.termtracker_c196.Activity.AddTermActivity;
import com.example.termtracker_c196.Helper.DBHelper;
import com.example.termtracker_c196.R;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.TermsViewHolder> {
    private Context context;
    private Cursor cursor;

    public TermsAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public class TermsViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView startDate;
        public TextView endDate;

        public TermsViewHolder(View v){
            super(v);
            name = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public TermsViewHolder onCreateViewHolder(ViewGroup v, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.term_item, v, false);
        return new TermsViewHolder(view);
    }

    public void onBindViewHolder(TermsViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        final Integer id = cursor.getInt(cursor.getColumnIndex(DBHelper.TERM_COL_1));
        final String name = cursor.getString(cursor.getColumnIndex(DBHelper.TERM_COL_2));
        final String start = cursor.getString(cursor.getColumnIndex(DBHelper.TERM_COL_3));
        final String end = cursor.getString(cursor.getColumnIndex(DBHelper.TERM_COL_4));

        viewHolder.name.setText(name + ": " + start + " - " + end);
        //  viewHolder.startText.setText(start);
        //  viewHolder.endText.setText(end);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTermActivity.class);
                intent.putExtra("TERM_COL_1", id);
                intent.putExtra("TERM_COL_2", name);
                intent.putExtra("TERM_COL_3", start);
                intent.putExtra("TERM_COL_4", end);

                context.startActivity(intent);
            }

            ;
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
