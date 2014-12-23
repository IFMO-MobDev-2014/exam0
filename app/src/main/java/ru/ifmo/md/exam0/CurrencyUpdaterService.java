package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CurrencyUpdaterService extends IntentService {
    private static List<Handler> handlers = new ArrayList<>();
    public static final Random random = new Random();

    public CurrencyUpdaterService() {
        super(CurrencyUpdaterService.class.getSimpleName());
    }

    public static void setHandler(Handler handler) {
        CurrencyUpdaterService.handlers.add(handler);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor allCurrencies = getApplication().getContentResolver().
                query(CurrencyContentProvider.CURRENCY_CONTENT_URI, null, CurrencyDatabaseHelper.CURRENCY_NAME + " != 'RUB'", null, null);
        while (allCurrencies.moveToNext()) {
            Currency currency = CurrencyDatabaseHelper.CurrencyCursor.getCurrency(allCurrencies);
            currency.setValue(currency.getValue() + random.nextDouble());
            if (random.nextBoolean()) {
                currency.setValue(currency.getValue() + 1);
            } else {
                currency.setValue(currency.getValue() - 1);
            }
            if (currency.getValue() <= 0) {
                currency.setValue(1);
            }
            update(currency);
        }
        for (Handler handler : handlers) {
            handler.sendEmptyMessage(1);
        }
    }

    private boolean update(Currency currency) {
        if (currency == null) {
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put(CurrencyDatabaseHelper.CURRENCY_VALUE, currency.getValue());
        getContentResolver().update(CurrencyContentProvider.CURRENCY_CONTENT_URI, cv,
                CurrencyDatabaseHelper.CURRENCY_ID + " = " + currency.getId(), null);
        return true;
    }
}