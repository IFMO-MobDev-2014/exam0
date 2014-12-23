package ru.ifmo.md.exam0;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CurrencyContentProvider extends ContentProvider {
    public static final String AUTHORITY = CurrencyContentProvider.class.getName();

    public static final Uri CURRENCY_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + CurrencyDatabaseHelper.CURRENCY_TABLE_NAME);
    static final String CURRENCY_CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CurrencyDatabaseHelper.CURRENCY_TABLE_NAME;
    public static final int URI_CURRENCY_ID = 1;
    public static final String WRONG_URI = "Wrong URI";
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, URI_CURRENCY_ID);
    }
    private CurrencyDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new CurrencyDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                Cursor cursor = dbHelper.getReadableDatabase().query(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), CURRENCY_CONTENT_URI);
                return cursor;
            default:
                throw new IllegalArgumentException(WRONG_URI);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                return CURRENCY_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException(WRONG_URI);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                long id = dbHelper.getWritableDatabase().insert(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, null, contentValues);
                Uri result = ContentUris.withAppendedId(CURRENCY_CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(result, null);
                return result;
            default:
                throw new IllegalArgumentException(WRONG_URI);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                int cnt = dbHelper.getWritableDatabase().delete(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            default:
                throw new IllegalArgumentException(WRONG_URI);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                int cnt = dbHelper.getWritableDatabase().update(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            default:
                throw new IllegalArgumentException(WRONG_URI);
        }
    }
}
