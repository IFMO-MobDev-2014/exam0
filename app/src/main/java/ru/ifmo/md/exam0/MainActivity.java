package ru.ifmo.md.exam0;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ru.ifmo.md.exam0.db.CurrencyContentProvider;
import ru.ifmo.md.exam0.db.CurrencyTable;
import ru.ifmo.md.exam0.service.AlarmStartService;
import ru.ifmo.md.exam0.service.CurrencyUpdateService;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public Currency currentCurrency;
    public TextView usdView;
    public TextView eurView;
    public TextView gbpView;

    private final int LOADER_ID = 0;
    private final MainActivity self = this;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLoaderManager().restartLoader(LOADER_ID, null, self);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usdView = (TextView) findViewById(R.id.usd_text);
        eurView = (TextView) findViewById(R.id.eur_text);
        gbpView = (TextView) findViewById(R.id.gbp_text);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(CurrencyUpdateService.ACTION_UPDATE));

        Intent alarm = new Intent(this, AlarmStartService.class);
        startService(alarm);
    }

    private void updateTextViews() {
        usdView.setText(Double.toString(currentCurrency.usd));
        eurView.setText(Double.toString(currentCurrency.eur));
        gbpView.setText(Double.toString(currentCurrency.gbp));
    }

    public void goToExchange(View view) {
        Intent intent = new Intent(this, ExchangeActivity.class);
        switch (view.getId()) {
            case R.id.usd_line:
                intent.putExtra(ExchangeActivity.EX_CUR, Currency.USD);
                break;
            case R.id.eur_line:
                intent.putExtra(ExchangeActivity.EX_CUR, Currency.EUR);
                break;
            case R.id.gbp_line:
                intent.putExtra(ExchangeActivity.EX_CUR, Currency.GBP);
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Long ts = System.currentTimeMillis() / 1000;
        return new CursorLoader(this,
                CurrencyContentProvider.CURRENCY_CONTENT_URL,
                new String[] {"*"},
                CurrencyTable.COLUMN_DATE + " >= ?",
                new String[] {Long.toString(ts - 5)},
                CurrencyTable.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() > 0) {
            data.moveToFirst();
            String curJson = data.getString(data.getColumnIndexOrThrow(CurrencyTable.COLUMN_VALUE));
            currentCurrency = Currency.fromString(curJson);
            updateTextViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do fukken nothing
    }
}
