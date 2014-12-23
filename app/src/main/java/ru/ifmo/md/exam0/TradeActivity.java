package ru.ifmo.md.exam0;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.net.URI;


public class TradeActivity extends ActionBarActivity {

    String foreign;
    float course;
    float roubles;
    float notRoubles;

    ContentObserver observer;
    LoaderManager.LoaderCallbacks<Cursor> loaders[] = new LoaderManager.LoaderCallbacks[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        foreign = getIntent().getStringExtra("currency");
        observer = new ContentObserver(new Handler(getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                getLoaderManager().restartLoader(1, null, loaders[1]);
            }
        };

        getContentResolver().registerContentObserver(MoneyContentProvider.URI_WALLET_DIR, false, observer);
        final Uri loaderUri = MoneyContentProvider.URI_CURRENCY_DIR.buildUpon().appendPath(foreign).build();

        loaders[0] = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), loaderUri, MoneyDatabase.Structure.FULL_CURRENCY_PROJECTION, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.i("sas", data.getCount() + "");
                data.moveToFirst();
                TextView tv = (TextView) findViewById(R.id.currentCourse);
                course = data.getFloat(1);
                tv.setText("" + course);
                data.close();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) { }
        };

        loaders[1] =  new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), MoneyContentProvider.URI_WALLET_DIR, MoneyDatabase.Structure.FULL_WALLET_PROJECTION, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                data.moveToFirst();
                while(!data.isAfterLast()) {
                    String cur = data.getString(0);
                    if(cur.equals(foreign)) {
                        TextView tv = (TextView) findViewById(R.id.textForeign);
                        notRoubles = data.getFloat(1);
                        tv.setText(foreign + ": " + notRoubles);

                    } else if(cur.equals("RUB")) {
                        TextView tv = (TextView) findViewById(R.id.textRubles);
                        roubles = data.getFloat(1);
                        tv.setText("RUB: " + roubles);
                    }
                    data.moveToNext();
                }
                data.close();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) { }
        };

        loaders[2] = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), MoneyContentProvider.URI_HISTORY_DIR.buildUpon().appendPath(foreign).build(), MoneyDatabase.Structure.FULL_HISTORY_PROJECTION, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                // data.moveToFirst();
                // TODO
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) { }
        };

        getLoaderManager().initLoader(0, null, loaders[0]);
        getLoaderManager().initLoader(1, null, loaders[1]);
        getLoaderManager().initLoader(2, null, loaders[2]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }

    public void onBuyClick(View v) {
        if(roubles >= course) {
            ContentValues cv = new ContentValues();
            cv.put(MoneyDatabase.Structure.COLUMN_NAME, foreign);
            cv.put(MoneyDatabase.Structure.COLUMN_AMOUNT, notRoubles + 1);
            notRoubles++;
            getContentResolver().update(MoneyContentProvider.URI_WALLET_DIR.buildUpon().appendPath(foreign).build(), cv, null, null);
            roubles -= course;
            cv.put(MoneyDatabase.Structure.COLUMN_NAME, "RUB");
            cv.put(MoneyDatabase.Structure.COLUMN_AMOUNT, roubles);
            getContentResolver().update(MoneyContentProvider.URI_WALLET_DIR.buildUpon().appendPath("RUB").build(), cv, null, null);
        }
        getContentResolver().notifyChange(MoneyContentProvider.URI_WALLET_DIR, null);
    }

    public void onSellClick(View w) {
        if(notRoubles > 0) {
            ContentValues cv = new ContentValues();
            cv.put(MoneyDatabase.Structure.COLUMN_NAME, foreign);
            cv.put(MoneyDatabase.Structure.COLUMN_AMOUNT, notRoubles - 1);
            notRoubles--;
            getContentResolver().update(MoneyContentProvider.URI_WALLET_DIR.buildUpon().appendPath(foreign).build(), cv, null, null);
            roubles += course;
            cv.put(MoneyDatabase.Structure.COLUMN_NAME, "RUB");
            cv.put(MoneyDatabase.Structure.COLUMN_AMOUNT, roubles);
            getContentResolver().update(MoneyContentProvider.URI_WALLET_DIR.buildUpon().appendPath("RUB").build(), cv, null, null);
        }
        getContentResolver().notifyChange(MoneyContentProvider.URI_WALLET_DIR, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
