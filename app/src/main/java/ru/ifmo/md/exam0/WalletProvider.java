package ru.ifmo.md.exam0;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Random;

public class WalletProvider extends ContentProvider implements BaseColumns {
    public static final String CURRENCIES = "Currencies";
    public static final String NAME = "CurrencyName";
    public static final String VALUE = "InRubles";
    private WalletHelper helper;
    private Random random = new Random();

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri.getPathSegments().size() == 1 && "currency".equals(uri.getLastPathSegment())) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues(2);
            contentValues.put(NAME, uri.getQueryParameter("name"));
            contentValues.put(VALUE, 0);
            db.insert(CURRENCIES, null, contentValues);
            return null;
        }
        throw new IllegalArgumentException("Bad insert URI");
    }

    @Override
    public boolean onCreate() {
        helper = new WalletHelper(getContext().getApplicationContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uri.getPathSegments().size() == 1) {
            SQLiteDatabase db = helper.getReadableDatabase();
            switch (uri.getLastPathSegment()) {
                case "currencyNames":
                    return db.query(CURRENCIES, new String[]{_ID, NAME}, null, null, null, null, null);
                case "centralBank":
                    return db.query(CURRENCIES, null, null, null, null, null, null);
            }
        }
        throw new IllegalArgumentException("Bad query URI");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class WalletHelper extends SQLiteOpenHelper {
        private WalletHelper(Context context) {
            super(context, "wallet.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CURRENCIES + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NAME + " TEXT NOT NULL," +
                    VALUE + " FLOAT NOT NULL);");
            ContentValues contentValues = new ContentValues(2);
            contentValues.put(NAME, "RUR");
            contentValues.put(VALUE, 1000);
            db.insert(CURRENCIES, null, contentValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CURRENCIES + ";");
        }
    }
}
