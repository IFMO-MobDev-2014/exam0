package ru.ifmo.md.exam0.db;

import android.database.sqlite.SQLiteDatabase;

import ru.ifmo.md.exam0.Currency;

/**
 * Created by flyingleafe on 23.12.14.
 */
public class CurrencyTable extends BaseTable {
    public static final String TABLE_NAME = "currency";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_VALUE = "value";


    private static final String SQL_CREATE_CITIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATE + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_VALUE + TEXT_TYPE +
                    ");";

    private static final String SQL_DELETE_CITIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static void create(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CITIES);
        Currency c = new Currency();
        c.usd = 54;
        c.eur = 65;
        c.gbp = 75;
        Long ts = System.currentTimeMillis() / 1000;
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_DATE + ", " + COLUMN_VALUE +
            ") VALUES (" + ts + ", '" + c + "');");
    }

    public static void delete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_CITIES);
    }
}
