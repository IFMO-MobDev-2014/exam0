package ru.ifmo.md.exam0.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import java.nio.LongBuffer;
import java.util.Random;

import ru.ifmo.md.exam0.Currency;
import ru.ifmo.md.exam0.db.CurrencyContentProvider;
import ru.ifmo.md.exam0.db.CurrencyTable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CurrencyUpdateService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_UPDATE = "ru.ifmo.md.exam0.service.action.UPDATE";
    public static final String NEW_CUR = "newcur";

    private Random rnd = new Random();
    private static int tryCount = 0;
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startUpdate(Context context) {
        Intent intent = new Intent(context, CurrencyUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        context.startService(intent);
    }

    public CurrencyUpdateService() {
        super("CurrencyUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tryCount++;
        Long ts = System.currentTimeMillis() / 1000;
        // clear very old data

        getContentResolver().delete(CurrencyContentProvider.CURRENCY_CONTENT_URL,
                CurrencyTable.COLUMN_DATE + " < ?",
                new String[] {Long.toString(ts - 200000)});
        Cursor c = getContentResolver().query(CurrencyContentProvider.CURRENCY_CONTENT_URL,
                new String[] {"*"},
                CurrencyTable.COLUMN_DATE + " >= ?",
                new String[] {Long.toString(ts - 5)},
                CurrencyTable.COLUMN_DATE + " DESC");
        Currency cur;
        if(c.getCount() > 0) {
            c.moveToFirst();
            String curJson = c.getString(c.getColumnIndexOrThrow(CurrencyTable.COLUMN_VALUE));
            cur = Currency.fromString(curJson);
        } else {
            cur = new Currency();
            cur.usd = 54;
            cur.eur = 65;
            cur.gbp = 75;
        }
        if(tryCount < 10) {
            double max = 0.05;
            double ch1 = max * 2 * rnd.nextDouble() - max;
            double ch2 = max * 2 * rnd.nextDouble() - max;
            double ch3 = max * 2 * rnd.nextDouble() - max;
            cur.usd += ch1;
            cur.eur += ch2;
            cur.gbp += ch3;
        } else {
            tryCount = 0;
            double max = 10;
            double ch = max * 2 * rnd.nextDouble() - max;
            cur.usd += ch;
            cur.eur += ch;
            cur.gbp += ch;
        }

        ContentValues row = new ContentValues();
        row.put(CurrencyTable.COLUMN_DATE, ts);
        row.put(CurrencyTable.COLUMN_VALUE, cur.toString());
        getContentResolver().insert(CurrencyContentProvider.CURRENCY_CONTENT_URL, row);

        Intent localIntent = new Intent(ACTION_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
