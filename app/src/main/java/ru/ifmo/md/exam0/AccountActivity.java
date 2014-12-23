package ru.ifmo.md.exam0;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private CurrencyAccountAdapter currencyAdapter;
    private ListView listView;
    private TextView totalWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        totalWallet = (TextView) findViewById(R.id.totalWallet);
        listView = (ListView) findViewById(R.id.currencyListView);
        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getBaseContext(), CurrencyContentProvider.CURRENCY_CONTENT_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Currency> itemList = new ArrayList<>();
        double total = 0;
        while (cursor.moveToNext()) {
            Currency c = CurrencyDatabaseHelper.CurrencyCursor.getCurrency(cursor);
            total += c.getValue() * c.getCount();
            itemList.add(c);
        }
        if (currencyAdapter == null) {
            currencyAdapter = new CurrencyAccountAdapter(itemList);
            listView.setAdapter(currencyAdapter);
        } else {
            currencyAdapter.clear();
            for (Currency currency : itemList) {
                currencyAdapter.addCurrency(currency);
            }
        }
        currencyAdapter.notifyDataSetChanged();
        totalWallet.setText(Double.toString(Math.round(total * 100) / 100.0));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        currencyAdapter = null;
    }
}
