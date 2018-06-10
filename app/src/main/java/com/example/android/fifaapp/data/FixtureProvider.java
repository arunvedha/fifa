package com.example.android.fifaapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class FixtureProvider extends ContentProvider {

    private static final int MATCH = 100;
 private static final int MATCH_ID = 101;
 private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
       sUriMatcher.addURI("com.example.android.fifaapp", "fifa", MATCH);
        sUriMatcher.addURI("com.example.android.fifaapp", "fifa/#", MATCH_ID);
    }
public static final String LOG_TAG = FixtureProvider.class.getSimpleName();
    private FifaDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FifaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
         Cursor cursor = null;
         int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH:
                cursor = database.query(Contract.MatchEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MATCH_ID:
                 selection = Contract.MatchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(Contract.MatchEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH:
                return Contract.MatchEntry.CONTENT_LIST_TYPE;
            case MATCH_ID:
                return Contract.MatchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH:
                return insertMatch(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertMatch(Uri uri,ContentValues values){
        String name1 = values.getAsString(Contract.MatchEntry.COLUMN_TEAM__A_NAME);
        if (name1 == null) {
            throw new IllegalArgumentException("Match requires a team name");
        }
        String name2 = values.getAsString(Contract.MatchEntry.COLUMN_TEAM__B_NAME);
        if (name2 == null) {
            throw new IllegalArgumentException("Match requires a team name");
        }
        String date = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_DATE);
        if (date == null) {
            throw new IllegalArgumentException("Match requires valid date");
        }
        String venue = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_VENUE);
        if (venue == null) {
            throw new IllegalArgumentException("Match requires valid venue");
        }
        String time = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_TIME);
        if (time == null) {
            throw new IllegalArgumentException("Match requires valid time");
        }
        String icon1 = values.getAsString(Contract.MatchEntry.COLUMN_TEAMA_ICON);
        if (icon1 == null) {
            throw new IllegalArgumentException("Match requires valid icon");
        }
        String icon2 = values.getAsString(Contract.MatchEntry.COLUMN_TEAMB_ICON);
        if (icon2 == null) {
            throw new IllegalArgumentException("Match requires valid icon");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(Contract.MatchEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MATCH:
                rowsDeleted = database.delete(Contract.MatchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MATCH_ID:

                selection = Contract.MatchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Contract.MatchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH:
                return updateMatch(uri, contentValues, selection, selectionArgs);
            case MATCH_ID:
                selection = Contract.MatchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMatch(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateMatch(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(Contract.MatchEntry.COLUMN_TEAM__A_NAME)) {
            String name = values.getAsString(Contract.MatchEntry.COLUMN_TEAM__A_NAME);
            if (name == null) {
                throw new IllegalArgumentException("the team requires a name");
            }
        }
        if (values.containsKey(Contract.MatchEntry.COLUMN_TEAM__B_NAME)) {
            String name = values.getAsString(Contract.MatchEntry.COLUMN_TEAM__B_NAME);
            if (name == null) {
                throw new IllegalArgumentException("The team requires a name");
            }
        }
        if (values.containsKey(Contract.MatchEntry.COLUMN_MATCH_DATE)) {
            String date = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_DATE);
            if (date == null) {
                throw new IllegalArgumentException("Match requires valid date");
            }
        }


        if (values.containsKey(Contract.MatchEntry.COLUMN_MATCH_VENUE)) {
            String venue = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_VENUE);
            if (venue == null) {
                throw new IllegalArgumentException("Match requires valid venue");
            }
        }
        if (values.containsKey(Contract.MatchEntry.COLUMN_MATCH_TIME)) {
            String time = values.getAsString(Contract.MatchEntry.COLUMN_MATCH_TIME);
            if (time == null) {
                throw new IllegalArgumentException("Match requires valid time");
            }
        }
        if (values.containsKey(Contract.MatchEntry.COLUMN_TEAMA_ICON)) {
            String team1icon = values.getAsString(Contract.MatchEntry.COLUMN_TEAMA_ICON);
            if (team1icon == null) {
                throw new IllegalArgumentException("Match requires valid icon");
            }
        }
        if (values.containsKey(Contract.MatchEntry.COLUMN_TEAMB_ICON)) {
            String team2icon = values.getAsString(Contract.MatchEntry.COLUMN_TEAMB_ICON);
            if (team2icon == null) {
                throw new IllegalArgumentException("Match requires valid icon");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.MatchEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
         return rowsUpdated;


    }
}
