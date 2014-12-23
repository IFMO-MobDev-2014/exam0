package ru.ifmo.md.exam0;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.ifmo.md.exam0.db.CurrencyContentProvider;
import ru.ifmo.md.exam0.db.CurrencyTable;
import ru.ifmo.md.exam0.service.CurrencyUpdateService;


public class ExchangeActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EX_CUR = "exchange_currency";

    public static final String WALLET = "ru.ru.ifmo.md.exam0.wallet.";
    public static final String WALLET_RUB = "ru.ifmo.md.exam0.wallet.RUB";
    public static final String WALLET_USD = "ru.ifmo.md.exam0.wallet.USD";
    public static final String WALLET_GBP = "ru.ifmo.md.exam0.wallet.GBP";
    public static final String WALLET_EUR = "ru.ifmo.md.exam0.wallet.EUR";

    private String currentCurrency;
    private float currentForeignValue;
    private float currentRubValue;
    private float currentRate;

    private TextView curName;
    private TextView curValue;
    private TextView rubValue;
    private TextView curRate;

    private final int LOADER_ID = 0;
    private final ExchangeActivity self = this;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLoaderManager().restartLoader(LOADER_ID, null, self);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        curName = (TextView) findViewById(R.id.cur_name);
        curValue = (TextView) findViewById(R.id.cur_value);
        rubValue = (TextView) findViewById(R.id.rub_value);
        curRate = (TextView) findViewById(R.id.cur_rate);

        currentCurrency = getIntent().getStringExtra(EX_CUR);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(CurrencyUpdateService.ACTION_UPDATE));

        updateWallet();
        updateTextViews();

    }

    private void updateWallet() {
        String curToken = WALLET + currentCurrency;
        try {
            currentRubValue = Settings.System.getFloat(getContentResolver(), WALLET_RUB);
        } catch (Settings.SettingNotFoundException e) {
            currentRubValue = 10000;
            Settings.System.putFloat(getContentResolver(), WALLET_RUB, currentRubValue);
        }

        try {
            currentForeignValue = Settings.System.getInt(getContentResolver(), curToken);
        } catch (Settings.SettingNotFoundException e) {
            currentForeignValue = 0;
            Settings.System.putFloat(getContentResolver(), curToken, currentForeignValue);
        }
    }

    private void writeWallet() {
        Settings.System.putFloat(getContentResolver(), WALLET_RUB, currentRubValue);
        Settings.System.putFloat(getContentResolver(), WALLET + currentCurrency, currentForeignValue);
    }

    private void updateTextViews() {
        curName.setText(currentCurrency);
        curValue.setText(Float.toString(currentForeignValue));
        rubValue.setText(Float.toString(currentRubValue));
        curRate.setText(Float.toString(currentRate));
    }

    public void buy(View view) {
        if(currentRubValue < currentRate) {
            Toast.makeText(this, "You have no roubles to buy money :(", Toast.LENGTH_SHORT).show();
            return;
        }
        currentRubValue -= currentRate;
        currentForeignValue++;
        writeWallet();
    }

    public void sell(View view) {
        if(currentForeignValue == 0) {
            Toast.makeText(this, "You have no money to sell :(", Toast.LENGTH_SHORT).show();
            return;
        }
        currentRubValue += currentRate;
        currentForeignValue--;
        writeWallet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
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
            Currency cur = Currency.fromString(curJson);
            switch (currentCurrency) {
                case Currency.USD:
                    currentRate = (float)cur.usd;
                    break;
                case Currency.EUR:
                    currentRate = (float)cur.eur;
                    break;
                case Currency.GBP:
                    currentRate = (float)cur.gbp;
                    break;
            }
            updateTextViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do fukken nothing
    }
}
