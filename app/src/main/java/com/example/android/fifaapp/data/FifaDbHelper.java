package com.example.android.fifaapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;

import com.example.android.fifaapp.data.Contract.MatchEntry;

public class FifaDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FifaDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "fifawc.db";
   private static final int DATABASE_VERSION = 1;

    public FifaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FIFA_TABLE = "CREATE TABLE " + MatchEntry.TABLE_NAME + " ("
                +MatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +MatchEntry.COLUMN_TEAM__A_NAME + " TEXT NOT NULL, "
                +MatchEntry.COLUMN_TEAM__B_NAME + " TEXT NOT NULL, "
                +MatchEntry.COLUMN_MATCH_DATE + " DATE NOT NULL, "
                +MatchEntry.COLUMN_MATCH_TIME + " TIME NOT NULL, "
                + MatchEntry.COLUMN_MATCH_VENUE + " TEXT NOT NULL, "
                + MatchEntry.COLUMN_TEAMA_ICON + " TEXT NOT NULL, "
                + MatchEntry.COLUMN_TEAMB_ICON + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_FIFA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
