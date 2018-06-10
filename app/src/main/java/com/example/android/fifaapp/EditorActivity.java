package com.example.android.fifaapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fifaapp.data.Contract;
import com.example.android.fifaapp.data.Contract.MatchEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_MATCH_LOADER = 0;

    private Uri mCurrentMatchUri;
    private EditText mName_A_EditText;
    private EditText mNAME_B_EditText;
    private EditText mDateEditText;
    private EditText mVenueEditText;
    private EditText mTimeEditText;
    private ImageView micon1;
    private ImageView micon2;
    private String muri1;
    private String muri2;

    private boolean flag;

    private boolean mFixtureHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mFixtureHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentMatchUri = intent.getData();

        if (mCurrentMatchUri == null) {
            setTitle("ADD NEW MATCH");
            invalidateOptionsMenu();

        } else {
            setTitle("EDIT MATCH");

            getLoaderManager().initLoader(EXISTING_MATCH_LOADER, null, this);
        }

        mName_A_EditText = (EditText) findViewById(R.id.edit_Team_A_name);
        mNAME_B_EditText = (EditText) findViewById(R.id.edit_Team_B_name);
        mDateEditText = (EditText) findViewById(R.id.match_date);
        mTimeEditText = (EditText) findViewById(R.id.match_time);
        mVenueEditText = (EditText) findViewById(R.id.match_venue);
        micon1 = (ImageView) findViewById(R.id.teamAflag);
        micon2 = (ImageView) findViewById(R.id.teamBflag);

        mName_A_EditText.setOnTouchListener(mTouchListener);
        mNAME_B_EditText.setOnTouchListener(mTouchListener);
        mDateEditText.setOnTouchListener(mTouchListener);
        mVenueEditText.setOnTouchListener(mTouchListener);
        mTimeEditText.setOnTouchListener(mTouchListener);
        micon1.setOnTouchListener(mTouchListener);
        micon2.setOnTouchListener(mTouchListener);

        micon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFixtureHasChanged = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setMessage("CHOOSE AN ACTION!");
                builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 10);
                    }
                });
                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takepicture, 11);
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        micon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFixtureHasChanged = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setMessage("CHOOSE AN ACTION!");
                builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 20);
                    }
                });
                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takepicture, 21);
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    micon1.setImageURI(selectedImage);
                    muri1 = selectedImage.toString();

                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    photo = crop(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    micon1.setImageURI(tempUri);
                    muri1 = tempUri.toString();

                }

                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    micon2.setImageURI(selectedImage);
                    muri2 = selectedImage.toString();
                }
                break;
            case 21:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    photo = crop(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    micon2.setImageURI(tempUri);
                    muri2 = tempUri.toString();
                }

                break;
        }
    }

    public Bitmap crop(Bitmap photo) {
        Bitmap yourBitmap = photo;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, 1300, 1300, true);
        return resized;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idc = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idc);
    }

    private void savematch() {
        String name_A_string = mName_A_EditText.getText().toString().trim();
        String name_B_string = mNAME_B_EditText.getText().toString().trim();
        String dateString = mDateEditText.getText().toString().trim();
        String venueString = mVenueEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();

       /* if (name_A_string.isEmpty() || name_B_string.isEmpty() || dateString.isEmpty() || venueString.isEmpty() || timeString.isEmpty()) {
            flag = false;
            Toast.makeText(this, "ENTER PROPER DETAILS",
                    Toast.LENGTH_SHORT).show();
        } else {*/

       // flag = true;


        ContentValues values = new ContentValues();
        values.put(MatchEntry.COLUMN_TEAM__A_NAME, name_A_string);
        values.put(MatchEntry.COLUMN_TEAM__B_NAME, name_B_string);
        values.put(MatchEntry.COLUMN_MATCH_DATE, dateString);
        values.put(MatchEntry.COLUMN_MATCH_VENUE, venueString);
        values.put(MatchEntry.COLUMN_MATCH_TIME, timeString);
        values.put(MatchEntry.COLUMN_TEAMA_ICON, muri1);
        values.put(MatchEntry.COLUMN_TEAMB_ICON, muri2);


        if (mCurrentMatchUri == null) {
            Uri newUri = getContentResolver().insert(MatchEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "INSERT FAILED",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "INSERT SUCCESSFUL",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentMatchUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "UPDATE FAILED",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "UPDATE SUCCESSFUL",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

//}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                savematch();
             //   if(flag)
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mFixtureHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

               DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mFixtureHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MatchEntry._ID,
                MatchEntry.COLUMN_TEAM__A_NAME,
                MatchEntry.COLUMN_TEAM__B_NAME,
                MatchEntry.COLUMN_MATCH_DATE,
                MatchEntry.COLUMN_MATCH_VENUE,
                MatchEntry.COLUMN_MATCH_TIME,
                MatchEntry.COLUMN_TEAMA_ICON,
                MatchEntry.COLUMN_TEAMB_ICON};

        return new CursorLoader(this,   // Parent activity context
                mCurrentMatchUri,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int name_A_ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_TEAM__A_NAME);
            int name_B_ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_TEAM__B_NAME);
            int dateColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_DATE);
            int venueColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_VENUE);
            int timeColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_TIME);
            int icon1ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_TEAMA_ICON);
            int icon2ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_TEAMB_ICON);


            String name_A_string = cursor.getString(name_A_ColumnIndex);
            String name_B_string = cursor.getString(name_B_ColumnIndex);
            String dateString = cursor.getString(dateColumnIndex);
            String venueString = cursor.getString(venueColumnIndex);
            String timeString = cursor.getString(timeColumnIndex);
            muri1 = cursor.getString(icon1ColumnIndex);
            muri2 = cursor.getString(icon2ColumnIndex);

           mName_A_EditText.setText(name_A_string);
           mNAME_B_EditText.setText(name_B_string);
           mDateEditText.setText(dateString);
           mVenueEditText.setText(venueString);
           mTimeEditText.setText(timeString);
            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(muri1)));
                micon1.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {

                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(muri2)));
                micon2.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName_A_EditText.setText("");
        mNAME_B_EditText.setText("");
        mDateEditText.setText("");
        mVenueEditText.setText("");
        mTimeEditText.setText("");
        micon1.setImageResource(R.drawable.ic_launcher_background);
        micon2.setImageResource(R.drawable.ic_launcher_background);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("THERE ARE UNSAVED CHANGES");
        builder.setPositiveButton("DISCARD", discardButtonClickListener);
        builder.setNegativeButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("DO YOU WANT TO DELETE?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteMatch();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMatch() {
        if (mCurrentMatchUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentMatchUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Fixture deleted",
                        Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }
}
