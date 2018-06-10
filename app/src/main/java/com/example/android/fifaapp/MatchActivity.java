package com.example.android.fifaapp;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.fifaapp.data.Contract;

import java.io.FileNotFoundException;

public class MatchActivity extends AppCompatActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_MATCH_LOADER = 0;
    private Uri mCurrentMatchUri;
    private TextView mname1;


    private TextView mname2;

    private TextView mdate;
    private TextView mvenue;
    private TextView mtime;
    private ImageView micon1;
    private ImageView micon2;
    private String muri1;
    private String muri2;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Intent intent = getIntent();
        mCurrentMatchUri = intent.getData();

        setTitle("VIEW FIXTURE");
        getLoaderManager().initLoader(EXISTING_MATCH_LOADER, null, MatchActivity.this);
        mname1 = (TextView) findViewById(R.id.teamAname);
        mname2 = (TextView) findViewById(R.id.teamBname);
        mdate = (TextView) findViewById(R.id.date);
        mvenue = (TextView) findViewById(R.id.venue);
        mtime = (TextView) findViewById(R.id.time);
        micon1 = (ImageView) findViewById(R.id.team1flag);
        micon2 = (ImageView) findViewById(R.id.team2flag);


    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Contract.MatchEntry._ID,
                Contract.MatchEntry.COLUMN_TEAM__A_NAME,
                Contract.MatchEntry.COLUMN_TEAM__B_NAME,
                Contract.MatchEntry.COLUMN_MATCH_DATE,
                Contract.MatchEntry.COLUMN_MATCH_VENUE,
                Contract.MatchEntry.COLUMN_MATCH_TIME,
                Contract.MatchEntry.COLUMN_TEAMA_ICON,
                Contract.MatchEntry.COLUMN_TEAMB_ICON};

        return new CursorLoader(this,   // Parent activity context
                mCurrentMatchUri,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                   // Default sort order
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int name_A_ColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAM__A_NAME);
            int name_B_ColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAM__B_NAME);
            int dateColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_MATCH_DATE);
            int venueColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_MATCH_VENUE);
            int timeColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_MATCH_TIME);
            int icon1ColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAMA_ICON);
            int icon2ColumnIndex = cursor.getColumnIndex(Contract.MatchEntry.COLUMN_TEAMB_ICON);



            final String name1 = cursor.getString(name_A_ColumnIndex);
            final String name2 = cursor.getString(name_B_ColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String venue = cursor.getString(venueColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String icon1 = cursor.getString(icon1ColumnIndex);
            String icon2 = cursor.getString(icon2ColumnIndex);

            mname1.setText(name1);
            mname2.setText(name2);
            mdate.setText(date);
            mvenue.setText(venue);
            mtime.setText(time);

            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(icon1)));
                micon1.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {

                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(icon2)));
                micon2.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            micon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MatchActivity.this, NameActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", name1);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            micon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MatchActivity.this, NameActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", name2);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        return;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentMatchUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_edit: {
                Intent intent = new Intent(MatchActivity.this, EditorActivity.class);
                intent.setData(mCurrentMatchUri);
                startActivity(intent);
            }
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
