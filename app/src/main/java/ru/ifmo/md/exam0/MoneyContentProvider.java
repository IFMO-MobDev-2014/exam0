package ru.ifmo.md.exam0;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class MoneyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "ru.ifmo.md.exam0.money";
    public static final String BASE_URI = "currencies";
    public static final String WALLET_URI = "wallet";
    public static final String HISTORY_URI = "history";
    public static final Uri URI_CURRENCY_DIR = Uri.parse("content://" + AUTHORITY + "/" + BASE_URI);
    public static final Uri URI_WALLET_DIR = Uri.parse("content://" + AUTHORITY + "/" + WALLET_URI);
    public static final Uri URI_HISTORY_DIR = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_URI);
    public static final int URITYPE_CURRENCY_DIR = 1;
    public static final int URITYPE_CURRENCY = 2;
    public static final int URITYPE_WALLET = 3;
    public static final int URITYPE_WALLET_DEEP = 4;
    public static final int URITYPE_HISTORY = 5;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_URI, URITYPE_CURRENCY_DIR);
        uriMatcher.addURI(AUTHORITY, WALLET_URI, URITYPE_WALLET);
        uriMatcher.addURI(AUTHORITY, WALLET_URI + "/*", URITYPE_WALLET_DEEP);
        uriMatcher.addURI(AUTHORITY, BASE_URI + "/*", URITYPE_CURRENCY);
        uriMatcher.addURI(AUTHORITY, HISTORY_URI + "/*", URITYPE_HISTORY);
    }
    private MoneyDatabase database;

    public MoneyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        if (uriType == UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("Invalid URI to provider: " + uri.toString());
        }


        return database.getWritableDatabase().delete(MoneyDatabase.Structure.HISTORY_TABLE, MoneyDatabase.Structure.COLUMN_TIME + " < ?", new String[] {System.currentTimeMillis() / 1000L - 300 + ""});
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("This is not supported");
    }

    @Override
    public boolean onCreate() {
        database = new MoneyDatabase(getContext(), null);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int uriType = uriMatcher.match(uri);
        if (uriType == UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("Invalid URI to provider: " + uri.toString());
        }

        if (uriType == URITYPE_CURRENCY_DIR) {
            return database.getWritableDatabase().query(MoneyDatabase.Structure.CURRENCIES_TABLE, projection, null, null, null, null, null);
        } else if(uriType == URITYPE_WALLET) {
            return database.getWritableDatabase().query(MoneyDatabase.Structure.WALLET_TABLE, projection, null, null, null, null, null);
        } else if(uriType == URITYPE_CURRENCY) {
            return database.getWritableDatabase().query(MoneyDatabase.Structure.CURRENCIES_TABLE, projection, MoneyDatabase.Structure.COLUMN_NAME + " = ?", new String[] {uri.getLastPathSegment()}, null, null, null);
        } else if(uriType == URITYPE_HISTORY) {
            return database.getWritableDatabase().query(MoneyDatabase.Structure.HISTORY_TABLE, projection, MoneyDatabase.Structure.COLUMN_NAME + " = ?", new String[] {uri.getLastPathSegment()}, null, null, MoneyDatabase.Structure.COLUMN_TIME);
        } else {
            throw new UnsupportedOperationException("This is not supported");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        if (uriType == UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("Invalid URI to provider: " + uri.toString());
        }

        switch (uriType) {
            case URITYPE_CURRENCY:
                int rv = database.getWritableDatabase().update(MoneyDatabase.Structure.CURRENCIES_TABLE, values, MoneyDatabase.Structure.COLUMN_NAME + " = ?", new String[] {uri.getLastPathSegment()});
                ContentValues values2 = new ContentValues(values);
                values2.put(MoneyDatabase.Structure.COLUMN_NAME, uri.getLastPathSegment());
                values2.put(MoneyDatabase.Structure.COLUMN_TIME, System.currentTimeMillis() / 1000L);
                database.getWritableDatabase().insert(MoneyDatabase.Structure.HISTORY_TABLE, null, values2);
                delete(uri, null, null); // clean history
                return rv;
            case URITYPE_WALLET_DEEP:
                return database.getWritableDatabase().update(MoneyDatabase.Structure.WALLET_TABLE, values, MoneyDatabase.Structure.COLUMN_NAME + "= ?", new String[] {uri.getLastPathSegment()});
            default:
                throw new UnsupportedOperationException("Unsupported update type");
        }
    }
}
