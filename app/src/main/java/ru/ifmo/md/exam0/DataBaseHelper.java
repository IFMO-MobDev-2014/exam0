package ru.ifmo.md.exam0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Snopi on 30.11.2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "constants.db";
    private static final int SCHEMA = 27;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT name from sqlite_master where type = 'table'" +
                " AND name = 'constants'", null);
        try {
            if (c.getCount() == 0) {
                db.execSQL("CREATE TABLE constants " +
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " title TEXT," +
                        " value REAL," +
                        "stash REAL," +
                        "rouble integer);");
            }
            ContentValues cv = new ContentValues();
            cv.put(Provider.Constants.TITLE, "USD");
            cv.put(Provider.Constants.VALUE, 54);
            cv.put(Provider.Constants.STASH, 1);
            cv.put(Provider.Constants.ROUBLE, 0);
            db.insert("constants", Provider.Constants.TITLE, cv);

            cv.put(Provider.Constants.TITLE, "EUR");
            cv.put(Provider.Constants.VALUE, 65);
            cv.put(Provider.Constants.ROUBLE, 0);
            cv.put(Provider.Constants.STASH, 1);
            db.insert("constants", Provider.Constants.TITLE, cv);

            cv.put(Provider.Constants.TITLE, "GBP");
            cv.put(Provider.Constants.VALUE, 75);
            cv.put(Provider.Constants.ROUBLE, 0);
            cv.put(Provider.Constants.STASH, 1);
            db.insert("constants", Provider.Constants.TITLE, cv);

            cv.put(Provider.Constants.TITLE, "RUB");
            cv.put(Provider.Constants.VALUE, 75);
            cv.put(Provider.Constants.ROUBLE, 1);
            cv.put(Provider.Constants.STASH, 10000);
            db.insert("constants", Provider.Constants.TITLE, cv);

            cv.put(Provider.Constants.TITLE, "SUM");
            cv.put(Provider.Constants.ROUBLE, 1);
            cv.put(Provider.Constants.STASH, 0);
            db.insert("constants", Provider.Constants.TITLE, cv);

        } finally {
            c.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists constants");
        onCreate(db);
    }
}
