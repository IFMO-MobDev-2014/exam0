package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;

import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PriceFluctuatorService extends IntentService {
    public static void start(Context context) {
        Intent intent = new Intent(context, PriceFluctuatorService.class);
        context.startService(intent);
    }

    public PriceFluctuatorService() {
        super("PriceFluctuatorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleAction();
        }
    }

    private void handleAction() {
        try {
            int iters = 0;
            Random rnd = new Random();
            while (true) {
                Cursor cr = getContentResolver().query(MoneyContentProvider.URI_CURRENCY_DIR, MoneyDatabase.Structure.FULL_CURRENCY_PROJECTION, null, null, null);
                cr.moveToFirst();
                ContentValues cv = new ContentValues();
                while(!cr.isAfterLast()) {
                    float start = cr.getFloat(1);
                    float add = rnd.nextFloat() / 10.0f;
                    add -= Math.floor(add * 1000.0f) / 1000.0f;
                    start += add;
                    if(iters % 10 == 0)
                        start += rnd.nextBoolean() ? 1 : -1;
                    cv.put(MoneyDatabase.Structure.COLUMN_COURSE, start);
                    getContentResolver().update(MoneyContentProvider.URI_CURRENCY_DIR.buildUpon().appendPath(cr.getString(0)).build(), cv, null, null);
                    cr.moveToNext();
                }
                cr.close();
                getContentResolver().notifyChange(MoneyContentProvider.URI_CURRENCY_DIR, null);
                iters++;
                Thread.sleep(1000);
            }
        } catch(InterruptedException ex) {

        }
    }
}
