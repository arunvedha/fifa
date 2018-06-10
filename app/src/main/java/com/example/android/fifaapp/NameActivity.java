package com.example.android.fifaapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.fifaapp.data.Contract;
import com.example.android.fifaapp.data.Contract.MatchEntry;

public class NameActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int MATCH_LOADER=0;
    FifaCursorAdapter CursorAdapter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        ListView MatchListView = (ListView) findViewById(R.id.MatchListView);
        CursorAdapter=new FifaCursorAdapter(this,null);
        MatchListView.setAdapter(CursorAdapter);
        getLoaderManager().initLoader(MATCH_LOADER,null,this);



    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MatchEntry._ID, MatchEntry.COLUMN_TEAM__A_NAME, MatchEntry.COLUMN_TEAM__B_NAME};
        String selection = MatchEntry.COLUMN_TEAM__A_NAME + "=? OR " + MatchEntry.COLUMN_TEAM__B_NAME + "=?";
        String[] selectionArgs = new String[]{name, name};
        return new CursorLoader(this, MatchEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        CursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        CursorAdapter.swapCursor(null);
    }
}
