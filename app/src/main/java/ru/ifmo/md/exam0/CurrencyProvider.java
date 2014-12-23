package ru.ifmo.md.exam0;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ignat on 23.12.14.
 */
public class CurrencyProvider  extends ContentProvider {
    final String LOG_TAG = "WeatherProvider";
    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String TABLE = "currency";

    // Поля
    static final String ID = "_id";
    static final String NAME = "name";
    static final String COURSE = "course";

    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + TABLE + "("
            + ID + " integer primary key autoincrement, "
            + NAME + " text, "
            + COURSE + " double" + ");";

    static final String AUTHORITY = "ru.ifmo.md.exam0";

    static final String COURSE_PATH = "currency";
    static final int URI_CURRENCIES = 1;

    static final int URI_ID = 2;
    static final String WEATHER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + COURSE_PATH;

    static final String WEATHER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + COURSE_PATH;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, URI_CURRENCIES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/*", URI_ID);
    }

    public static final Uri WEATHER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + COURSE_PATH);

    public CurrencyProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCIES: // общий Uri
                Log.d(LOG_TAG, "URI_CURRENCIES");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NAME + " ASC";
                }
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + "=" + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE, strings, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),
                WEATHER_CONTENT_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCIES:
                return WEATHER_CONTENT_TYPE;
            case URI_ID:
                return WEATHER_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_CURRENCIES)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(WEATHER_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCIES:
                Log.d(LOG_TAG, "URI_CURRENCIES");
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCIES:
                Log.d(LOG_TAG, "URI_CURRENCIES");

                break;
            case URI_ID:
                String name = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + name);
                /*if (TextUtils.isEmpty(selection)) {
                    //selection = NAME + "=" + "\"" + values.get("name") + "\"";
                } else {
                    selection = selection + " AND " + NAME + " = " + "\"USD\"";
                }*/
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "currency", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();

            cv.put(NAME, "USD");
            cv.put(COURSE, 55);
            db.insert(TABLE, null, cv);

            cv.put(NAME, "EUR");
            cv.put(COURSE, 65);
            db.insert(TABLE, null, cv);

            cv.put(NAME, "GBP");
            cv.put(COURSE, 75);
            db.insert(TABLE, null, cv);
            Log.d("lla", "Created db");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
