package ru.ifmo.md.exam0;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExchangeActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String CURRENCY_ID_EXTRA = "currency_id";
    private int id = -1;
    private Currency currency;
    private TextView currencyValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        Intent intent = getIntent();
        id = intent.getIntExtra(CURRENCY_ID_EXTRA, -1);
        currencyValue = (TextView) findViewById(R.id.currencyValue);
        final TextView walletSize = (TextView) findViewById(R.id.walletSizeTextView);
        walletSize.setText("" + Wallet.getLeft());
        Button buyButton = (Button) findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currency.getValue() <= Wallet.getLeft()) {
                    Log.d("LOGS", "Gotta but");
                    Wallet.setSum(Wallet.getLeft() - currency.getValue());
                    ContentValues cv = new ContentValues();
                    currency.setCount(currency.getCount() + 1);
                    cv.put(CurrencyDatabaseHelper.CURRENCY_COUNT, currency.getCount());
                    getApplication().getContentResolver().update(CurrencyContentProvider.CURRENCY_CONTENT_URI, cv,
                            CurrencyDatabaseHelper.CURRENCY_ID + " = " + currency.getId(), null);
                }
                walletSize.setText("" + Wallet.getLeft());
            }
        });
        Button sellButton = (Button) findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currency.getCount() > 0) {
                    Log.d("LOGS", "Gotta sell");
                    Wallet.setSum(Wallet.getLeft() + currency.getValue());
                    currency.setCount(currency.getCount() - 1);
                    ContentValues cv = new ContentValues();
                    cv.put(CurrencyDatabaseHelper.CURRENCY_COUNT, currency.getCount());
                    getApplication().getContentResolver().update(CurrencyContentProvider.CURRENCY_CONTENT_URI, cv,
                            CurrencyDatabaseHelper.CURRENCY_ID + " =  " + currency.getId(), null);
                }
                walletSize.setText("" + Wallet.getLeft());
            }
        });
        Log.d("LOGS", Integer.toString(id));
        getLoaderManager().restartLoader(1, null, this);
        CurrencyUpdaterService.setHandler(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                getLoaderManager().restartLoader(1, null, ExchangeActivity.this);
                return true;
            }
        }));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getBaseContext(), CurrencyContentProvider.CURRENCY_CONTENT_URI, null, CurrencyDatabaseHelper.CURRENCY_ID + " = " + this.id,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        currency = CurrencyDatabaseHelper.CurrencyCursor.getCurrency(cursor);
        setTitle(currency.getName());
        currencyValue.setText(Double.toString(currency.getValue()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
