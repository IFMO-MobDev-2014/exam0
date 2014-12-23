package ru.ifmo.md.exam0;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kna on 10.11.2014.
 */
public class MoneyDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "forex.db";

    private static final int DB_VERSION = 3;
    public MoneyDatabase(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Structure.CURRENCIES_TABLE +
                "(" +
                Structure.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Structure.COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                Structure.COLUMN_COURSE + " REAL NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Structure.WALLET_TABLE +
                "(" +
                Structure.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Structure.COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                Structure.COLUMN_AMOUNT + " REAL NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Structure.HISTORY_TABLE +
                "(" +
                Structure.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Structure.COLUMN_NAME + " TEXT NOT NULL, " +
                Structure.COLUMN_COURSE + " REAL NOT NULL," +
                Structure.COLUMN_TIME + " INTEGER NOT NULL" +
                ");");


        ContentValues cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "USD");
        cv.put(Structure.COLUMN_COURSE, 60.0f);
        db.insert(Structure.CURRENCIES_TABLE, null, cv);
        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "EUR");
        cv.put(Structure.COLUMN_COURSE, 100.0f);
        db.insert(Structure.CURRENCIES_TABLE, null, cv);
        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "GBP");
        cv.put(Structure.COLUMN_COURSE, 150.0f);
        db.insert(Structure.CURRENCIES_TABLE, null, cv);

        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "USD");
        cv.put(Structure.COLUMN_AMOUNT, 0.0f);
        db.insert(Structure.WALLET_TABLE, null, cv);
        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "EUR");
        cv.put(Structure.COLUMN_AMOUNT, 0.0f);
        db.insert(Structure.WALLET_TABLE, null, cv);
        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "GBP");
        cv.put(Structure.COLUMN_AMOUNT, 0.0f);
        db.insert(Structure.WALLET_TABLE, null, cv);
        cv = new ContentValues();
        cv.put(Structure.COLUMN_NAME, "RUB");
        cv.put(Structure.COLUMN_AMOUNT, 10000.0f);
        db.insert(Structure.WALLET_TABLE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + Structure.CURRENCIES_TABLE + ";");
            db.execSQL("DROP TABLE " + Structure.WALLET_TABLE + ";");
            onCreate(db);
        }
    }

    public static class Structure {
        public static final String CURRENCIES_TABLE = "currency";
        public static final String WALLET_TABLE = "wallet";
        public static final String HISTORY_TABLE = "history";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";

        public static final String COLUMN_COURSE = "course";
        public static final String COLUMN_AMOUNT = "amount";

        public static final String[] FULL_CURRENCY_PROJECTION = {COLUMN_NAME, COLUMN_COURSE, COLUMN_ID };
        public static final String[] FULL_WALLET_PROJECTION = {COLUMN_NAME, COLUMN_AMOUNT, COLUMN_ID };
        public static final String[] FULL_HISTORY_PROJECTION = {COLUMN_NAME, COLUMN_COURSE, COLUMN_TIME, COLUMN_ID };
    }
}
