package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Snopi on 01.12.2014.
 */
public class MyTrueService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyTrueService(String name) {
        super(name);
    }

    public MyTrueService() {
        super("tral");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       /* String name = intent.getStringExtra(Provider.Constants.TITLE);
        Float value = intent.getFloatExtra(Provider.Constants.VALUE, 0);
        ContentValues cv = new ContentValues();
        cv.put(Provider.Constants.TITLE, name);
        cv.put(Provider.Constants.VALUE, value);
        int ans = 0;
        for (int i = 0; i < 10000000; i++) {
            for (int j = 0; j < 100; j++) {
                ans += j;
            }
        }
       */
        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Random rnd = new Random();
        ContentValues cv = new ContentValues();

        while (true) {
            float sum = 0;
            for (int i = 0; i < 10; i++) {
                Cursor x = getContentResolver().query(Provider.Constants.CONTENT_URI, new String[]{Provider.Constants.TITLE,
                        Provider.Constants.VALUE, Provider.Constants.STASH}, Provider.Constants.ROUBLE + "<> 3", null, null);
                x.moveToFirst();
                sum = 0;
                do {
                    cv.clear();
                    String name = x.getString(0);
                    float val = x.getFloat(1);
                    float stas = x.getInt(2);

                    if (name.toUpperCase().equals("RUB")) {
                        sum += stas;
                        continue;
                    }
                    if (name.toUpperCase().equals("SUM")) {
                        continue;
                    }
                    cv.put(Provider.Constants.VALUE, val +  rnd.nextFloat() * 0.1 * (rnd.nextBoolean() ? -1 : 1));
                    sum += stas * val;
                    getContentResolver().update(Provider.Constants.CONTENT_URI, cv, Provider.Constants.TITLE + "=?", new String[]{name});
                    cv.clear();
                } while (x.moveToNext());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cv.clear();
                cv.put(Provider.Constants.STASH, sum);
                getContentResolver().update(Provider.Constants.CONTENT_URI, cv, Provider.Constants.TITLE + "=?", new String[]{"SUM"});
                cv.clear();
                Log.i("sum", String.valueOf(sum));
            }

            Cursor x = getContentResolver().query(Provider.Constants.CONTENT_URI, new String[]{Provider.Constants.TITLE,
                    Provider.Constants.VALUE}, null, null, null);
            x.moveToFirst();
            do {
                cv.clear();
                String name = x.getString(0);
                float val = x.getFloat(1);
                cv.put(Provider.Constants.VALUE, val + (rnd.nextBoolean() ? -1 : 1));
                getContentResolver().update(Provider.Constants.CONTENT_URI, cv, Provider.Constants.TITLE + "=?", new String[]{name});

            } while (x.moveToNext());
        }
    }
}
