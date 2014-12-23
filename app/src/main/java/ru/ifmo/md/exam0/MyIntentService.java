package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if("1sec".equals(intent.getAction()))
                handleActionFoo();
        else
            handleActionBar();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {
        // TODO: Handle action Foo
        ContentValues cv = new ContentValues();
//        cv.put(CurrencyProvider.NAME, "USD");
        Uri uri = Uri.parse("content://ru.ifmo.md.exam0/currency");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        for(int i = 0;;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex("course"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double course = Double.parseDouble(data);
                course += Math.random() * 2 - 1;
                cv.put(CurrencyProvider.COURSE, course);
                getContentResolver().update(uri, cv, CurrencyProvider.NAME + "= \"" + name + "\"", null);
            }
            if(i % 10 == 0) {
                handleActionBar();
            }
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
    private void handleActionBar() {
        // TODO: Handle action Foo
        ContentValues cv = new ContentValues();
//        cv.put(CurrencyProvider.NAME, "USD");
        Uri uri = Uri.parse("content://ru.ifmo.md.exam0/currency");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex("course"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double course = Double.parseDouble(data);
                if(Math.random() > 0.5)
                    course++;
                else
                    course--;
                cv.put(CurrencyProvider.COURSE, course);
                getContentResolver().update(uri, cv, CurrencyProvider.NAME + "= \"" + name + "\"", null);
            }

        //throw new UnsupportedOperationException("Not yet implemented");
    }

}
