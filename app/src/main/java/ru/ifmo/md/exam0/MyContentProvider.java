package ru.ifmo.md.exam0;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Вячеслав on 23.12.2014.
 */

public class MyContentProvider extends ContentProvider {

    private DbOpenHelper database;

    private static final int VOTING = 1;

    private static final String AUTHORITY = "ru.ifmo.md.exam0";

    private static final String BASE_PATH = "base_path";
    public static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, VOTING);
    }

    @Override
    public boolean onCreate() {
        database = new DbOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        queryBuilder.setTables(MyTable.TABLE_NAME);

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        id = sqlDB.insert(MyTable.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = sqlDB.delete(MyTable.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = sqlDB.update(MyTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { MyTable.COLUMN_ID, MyTable.COLUMN_NAME, MyTable.COLUMN_VALUE,
        MyTable.COLUMN_USD, MyTable.COLUMN_EUR, MyTable.COLUMN_GBP, MyTable.COLUMN_RUB};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}


