package ru.ifmo.md.exam0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CurrencyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_currency";
    public static final int DATABASE_VERSION = 4;

    public static final String CURRENCY_TABLE_NAME = "currency";
    public static final String CURRENCY_ID = "_id";
    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_VALUE = "value";
    public static final String CURRENCY_COUNT = "count";

    public static final String CURRENCY_CREATE_TABLE_QUERY =
            "create table " + CURRENCY_TABLE_NAME + " (" + CURRENCY_ID +
                    " integer primary key autoincrement, " +
                    CURRENCY_NAME + " varchar(30), " +
                    CURRENCY_VALUE + " float, " +
                    CURRENCY_COUNT + " integer)";

    public CurrencyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CURRENCY_CREATE_TABLE_QUERY);
        ContentValues cv = new ContentValues();
        cv.put(CURRENCY_NAME, "USD");
        cv.put(CURRENCY_VALUE, 54.0);
        cv.put(CURRENCY_COUNT, 0);
        db.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(CURRENCY_NAME, "EUR");
        cv.put(CURRENCY_VALUE, 65.0);
        cv.put(CURRENCY_COUNT, 0);
        db.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(CURRENCY_NAME, "GBP");
        cv.put(CURRENCY_VALUE, 75.0);
        cv.put(CURRENCY_COUNT, 0);
        db.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(CURRENCY_NAME, "RUB");
        cv.put(CURRENCY_VALUE, 1.0);
        cv.put(CURRENCY_COUNT, 10000);
        db.insert(CURRENCY_TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + CURRENCY_TABLE_NAME);
        onCreate(db);
    }

    public static class CurrencyCursor extends CursorWrapper {

        public CurrencyCursor(Cursor cursor) {
            super(cursor);
        }

        public static Currency getCurrency(Cursor cursor) {
            return new Currency(cursor.getString(cursor.getColumnIndex(CURRENCY_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(CURRENCY_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(CURRENCY_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(CURRENCY_ID)));
        }

    }
}
