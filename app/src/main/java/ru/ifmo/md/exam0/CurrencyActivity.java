package ru.ifmo.md.exam0;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CurrencyActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private CurrencyAdapter currencyAdapter;
    private ListView listView;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);
        listView = (ListView) findViewById(R.id.currencyListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = currencyAdapter.getItem(position);
                Intent intent = new Intent(CurrencyActivity.this, ExchangeActivity.class);
                intent.putExtra(ExchangeActivity.CURRENCY_ID_EXTRA, currency.getId());
                startActivity(intent);
            }
        });
        Wallet.setApplication(getApplication());
        Cursor cursor = getApplication().getContentResolver().query(CurrencyContentProvider.CURRENCY_CONTENT_URI, null,
                CurrencyDatabaseHelper.CURRENCY_NAME + " = 'RUB'", null, null);
        cursor.moveToFirst();
        Wallet.setSum(cursor.getInt(cursor.getColumnIndex(CurrencyDatabaseHelper.CURRENCY_COUNT)));
        final TextView walletSize = (TextView) findViewById(R.id.walletSizeTextView);
        walletSize.setText("" + Wallet.getLeft());
        AlarmManagerHelper.disableServiceAlarm(getApplicationContext());
        AlarmManagerHelper.enableServiceAlarm(getApplicationContext(), 1000); //1 second
        CurrencyUpdaterService.setHandler(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                getLoaderManager().restartLoader(1, null, CurrencyActivity.this);
                walletSize.setText("" + Wallet.getLeft());
                return true;
            }
        }));
        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getBaseContext(), CurrencyContentProvider.CURRENCY_CONTENT_URI, null, CurrencyDatabaseHelper.CURRENCY_NAME + " != 'RUB'",
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Currency> itemList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Currency c = CurrencyDatabaseHelper.CurrencyCursor.getCurrency(cursor);
            itemList.add(c);
        }
        if (currencyAdapter == null) {
            currencyAdapter = new CurrencyAdapter(itemList);
            listView.setAdapter(currencyAdapter);
        } else {
            currencyAdapter.clear();
            for (Currency currency : itemList) {
                currencyAdapter.addCurrency(currency);
            }
        }
        currencyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        currencyAdapter = null;
    }
}
