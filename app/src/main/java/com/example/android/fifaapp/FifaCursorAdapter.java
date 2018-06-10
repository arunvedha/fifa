package com.example.android.fifaapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.fifaapp.data.Contract;

public class FifaCursorAdapter extends CursorAdapter {
public FifaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);

        int name_A_index = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAM__A_NAME);
        int name_B_index = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAM__B_NAME);

        String name_A = cursor.getString(name_A_index);
        String name_b = cursor.getString(name_B_index);
        nameTextView.setText(name_A + " VS " + name_b);


    }
}
