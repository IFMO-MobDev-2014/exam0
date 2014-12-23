package ru.ifmo.md.exam0;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created by Вячеслав on 23.12.2014.
 */

public class MyService extends IntentService {

    public static final String ACTION_MYSERVICE = "ru.ifmo.md.exam0.action1";

    public MyService() {
        super("superservice");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Random rand = new Random();
        long counter = 0;

        while (true) {

            counter++;

            ContentValues values;
            Cursor cursor;
            double cur_value;

            double usd_value;
            double eur_value;
            double gbp_value;


            if (counter % 10 == 0) {
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"USD", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                usd_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "USD");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextInt(2) * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"USD", "1"});

                cursor.close();
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"EUR", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                eur_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "EUR");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextInt(2) * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"EUR", "1"});

                cursor.close();
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"GBP", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                gbp_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "GBP");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextInt(2) * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"GBP", "1"});
                cursor.close();
            } else {
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"USD", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                usd_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "USD");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextDouble() * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"USD", "1"});

                cursor.close();
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"EUR", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                eur_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "EUR");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextDouble() * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"EUR", "1"});

                cursor.close();
                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"GBP", "1"}, null);
                cursor.moveToFirst();
                cur_value = cursor.getDouble(0);
                gbp_value = cur_value;
                values = new ContentValues();
                values.put(MyTable.COLUMN_NAME, "GBP");
                values.put(MyTable.COLUMN_VALUE, cur_value + rand.nextDouble() * 2 - 1);
                getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"GBP", "1"});
                cursor.close();
            }

            double usd_count;
            double eur_count;
            double gbp_count;
            double rub_count;

            cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                    MyTable.COLUMN_TYPE + " = ?", new String[] {"USD", "2"}, null);
            cursor.moveToFirst();
            usd_count = cursor.getDouble(0);
            cursor.close();

            cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                    MyTable.COLUMN_TYPE + " = ?", new String[] {"EUR", "2"}, null);
            cursor.moveToFirst();
            eur_count = cursor.getDouble(0);
            cursor.close();

            cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                    MyTable.COLUMN_TYPE + " = ?", new String[] {"GBP", "2"}, null);
            cursor.moveToFirst();
            gbp_count = cursor.getDouble(0);
            cursor.close();

            cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                    MyTable.COLUMN_TYPE + " = ?", new String[] {"RUB", "2"}, null);
            cursor.moveToFirst();
            rub_count = cursor.getDouble(0);
            cursor.close();

            double total = rub_count + usd_count * usd_value + eur_count * eur_value + gbp_count * gbp_value;

            values = new ContentValues();
            values.put(MyTable.COLUMN_VALUE, total);

            getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                MyTable.COLUMN_TYPE + " = ?", new String[]{"Total", "2"});

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
