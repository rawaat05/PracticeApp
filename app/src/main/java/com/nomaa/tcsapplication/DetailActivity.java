package com.nomaa.tcsapplication;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nomaa.tcsapplication.data.DogContract;
import com.nomaa.tcsapplication.data.DogDbHelper;

import static com.nomaa.tcsapplication.MainActivity.INDEX_DOG_IMG;
import static com.nomaa.tcsapplication.MainActivity.INDEX_DOG_NAME;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView dogName;
    private ImageView dogImage;
    private Button deleteDog;

    private Uri uriWithId;

    private static final int DOG_LOADER_ID = 188;

    public static final String[] projection = {
            DogContract.DogEntry.COLUMN_NAME,
            DogContract.DogEntry.COLUMN_IMAGE_PATH
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dogName = findViewById(R.id.dog_name);
        dogImage = findViewById(R.id.dog_image);
        deleteDog = findViewById(R.id.delete_button);

        uriWithId = getIntent().getData();

        getSupportLoaderManager().initLoader(DOG_LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteDog(View view) {

        DogDbHelper dbHelper = new DogDbHelper(getBaseContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor checkCursor = db.query(DogContract.DogEntry.TABLE_NAME, null, "name = ?",
                new String[] {dogName.getText().toString()}, null, null, null);

        checkCursor.moveToPosition(0);

        Log.e("CHECK", "" + (checkCursor == null? true : false) + " : " + checkCursor.getCount());

        if(checkCursor != null){
            Log.e("DELETE", dogName.getText().toString());
            db.delete(DogContract.DogEntry.TABLE_NAME, "name = ?", new String[] {dogName.getText().toString().toLowerCase()});
            Toast.makeText(getApplicationContext(), "Removed from list", Toast.LENGTH_SHORT).show();
        }

        checkCursor.close();

        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uriWithId, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToPosition(0);

        if(data.getCount() != 0) {
            String nameRaw = data.getString(INDEX_DOG_NAME);
            String name = Character.toUpperCase(nameRaw.charAt(0)) + nameRaw.substring(1);

            dogName.setText(name);
            Glide.with(this).load(data.getString(INDEX_DOG_IMG)).into(dogImage);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
