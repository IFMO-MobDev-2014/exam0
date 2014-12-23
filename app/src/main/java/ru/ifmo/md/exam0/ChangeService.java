package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import java.util.Random;

public class ChangeService extends IntentService {
    Random random = new Random();

    public ChangeService() {
        super("ChangeService");
    }

    private void randomize(Cursor c, int randAbs) {
        ContentValues cv = new ContentValues();
        cv.put(ExchangeProvider.NAME, c.getString(c.getColumnIndex(ExchangeProvider.NAME)));
        cv.put(ExchangeProvider.AMOUNT, c.getString(c.getColumnIndex(ExchangeProvider.AMOUNT)));
        cv.put(ExchangeProvider.VALUE, c.getInt(c.getColumnIndex(ExchangeProvider.VALUE)) +
                (random.nextInt() % (2 * randAbs) - randAbs));
        cv.put(ExchangeProvider.HIDDEN, c.getString(c.getColumnIndex(ExchangeProvider.HIDDEN)));

        getContentResolver().delete(ExchangeProvider.CURRENCY_URI,
                ExchangeProvider._ID + "=?", new String[]{Integer.toString(c.getInt(c.getColumnIndex(ExchangeProvider._ID)))});
        getContentResolver().insert(ExchangeProvider.CURRENCY_URI, cv);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int randAbs = intent.getExtras().getInt("randAbs");
        Cursor c = getContentResolver().query(ExchangeProvider.CURRENCY_URI, null, null, null, null);
        while (c.moveToNext()) {
            if (c.getInt(c.getColumnIndex(ExchangeProvider.HIDDEN)) == 0) {
                randomize(c, randAbs);
            }
        }
        c.close();
    }
}
