package ru.ifmo.md.exam0;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    CurrencyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PriceFluctuatorService.start(getApplicationContext());
        ListView lv = (ListView)findViewById(R.id.currenciesList);
        adapter = new CurrencyListAdapter(this, this, null, true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.textLabel);
                String str = tv.getText().toString();
                Intent intent = new Intent(getApplicationContext(), TradeActivity.class);
                intent.putExtra("currency", str);
                startActivity(intent);
            }
        });
        lv.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(),
                        MoneyContentProvider.URI_CURRENCY_DIR,
                        MoneyDatabase.Structure.FULL_CURRENCY_PROJECTION,
                        null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_wallet) {
            Intent intent = new Intent(this, WalletActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class CurrencyListAdapter extends CursorAdapter {

        private Activity activity;
        public CurrencyListAdapter(Activity activity, Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            this.activity = activity;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View w = activity.getLayoutInflater().inflate(R.layout.currency_entry, parent, false);
            bindView(w, context, cursor);
            return w;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv = (TextView) view.findViewById(R.id.textLabel);
            tv.setText(cursor.getString(0));
            tv = (TextView) view.findViewById(R.id.textValue);
            tv.setText(cursor.getString(1));
        }
    }
}
