package ru.ifmo.md.exam0;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static ru.ifmo.md.exam0.WalletProvider.NAME;

public class CurrencyList extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == android.R.id.text2)
                ((TextView) view).setText(String.format("%.2f",
                        connection.getCurrency(cursor.getString(columnIndex))));
            else
                ((TextView) view).setText(cursor.getString(columnIndex));
            return true;
        }
    };
    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            getLoaderManager().restartLoader(0, null, CurrencyList.this);
        }
    };
    private RetrieverConnection connection = new RetrieverConnection();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.parse("content://net.dimatomp.wallet.provider/currencyNames"), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
                new String[]{NAME, NAME}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        adapter.setViewBinder(binder);
        setListAdapter(adapter);
        bindService(new Intent(this, CurrencyRetriever.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, CurrencyMonitor.class);
        intent.putExtra("foreignCurrency", ((TextView) v.findViewById(android.R.id.text1)).getText());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_currency:
                AddCurrencyDialog dialog = new AddCurrencyDialog();
                dialog.show(getFragmentManager(), "addCurrency");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RetrieverConnection implements ServiceConnection {
        CurrencyRetriever.CurrencyBinder binder;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (CurrencyRetriever.CurrencyBinder) service;
            binder.subscribe(observer);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder.unsubscribe(observer);
        }

        public float getCurrency(String name) {
            if (binder.checkForExistence(name))
                return binder.getInRubles(name);
            return Float.NaN;
        }
    }
}
