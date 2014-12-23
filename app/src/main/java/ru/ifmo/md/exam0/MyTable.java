package ru.ifmo.md.exam0;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Вячеслав on 23.12.2014.
 */

public class MyTable {

    public static final String TABLE_NAME = "table_name";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_TYPE = "type_";
    public static final String COLUMN_USD = "usd";
    public static final String COLUMN_EUR = "eur";
    public static final String COLUMN_GBP = "gbp";
    public static final String COLUMN_RUB = "rub";
    public static final String COLUMN_ID = "_id";

    private static final String DB_CREATE = "CREATE TABLE " +
            TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TYPE + " INTEGER, " +
            COLUMN_USD + " REAL, " +
            COLUMN_EUR + " REAL, " +
            COLUMN_GBP + " REAL, " +
            COLUMN_RUB + " REAL, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_VALUE + " REAL);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_CREATE);
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (1, 'USD', 54)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (1, 'EUR', 65)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (1, 'GBP', 75)");

        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (2, 'USD', 10)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (2, 'EUR', 0)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (2, 'GBP', 0)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (2, 'RUB', 10000)");
        database.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TYPE + ", " + COLUMN_NAME + ", " + COLUMN_VALUE + ") VALUES (2, 'Total', 10540)");

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
