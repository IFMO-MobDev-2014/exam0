package ru.ifmo.md.exam0;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class wallet extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int CONSTANT_LOADER = 2;
    private static final String[] PROJECTION = new String[] {
            Provider.Constants._ID, Provider.Constants.TITLE,
            Provider.Constants.STASH};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        setListAdapter(new MyCursorAdapter(this, null, 0));
        getLoaderManager().initLoader(CONSTANT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONSTANT_LOADER:
                return new CursorLoader(getApplicationContext(),
                        Provider.Constants.CONTENT_URI,
                        PROJECTION, Provider.Constants.ROUBLE + "<>-1", null, null);
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
