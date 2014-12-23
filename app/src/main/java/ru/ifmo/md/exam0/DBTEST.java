package ru.ifmo.md.exam0;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class DBTEST extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public Button wallet;
    public static final int CONSTANT_LOADER = 1;
    private static final String[] PROJECTION = new String[] {
            Provider.Constants._ID, Provider.Constants.TITLE,
            Provider.Constants.VALUE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        setListAdapter(new MyCursorAdapter(this, null, 0));
        wallet = (Button) findViewById(R.id.wallet_button);
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DBTEST.this, wallet.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(CONSTANT_LOADER, null, this);
        Intent x = new Intent(getApplicationContext(), MyTrueService.class);
        startService(x);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONSTANT_LOADER:
                return new CursorLoader(getApplicationContext(),
                        Provider.Constants.CONTENT_URI,
                        PROJECTION, Provider.Constants.ROUBLE + "=0", null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((MyCursorAdapter) getListAdapter()).changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((MyCursorAdapter) getListAdapter()).changeCursor(null);
    }


}
