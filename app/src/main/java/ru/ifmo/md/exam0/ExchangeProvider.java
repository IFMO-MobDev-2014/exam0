package ru.ifmo.md.exam0;

import android.net.Uri;
import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

public class ExchangeProvider extends ContentProvider {

    static final String PROVIDER_NAME = "ru.ifmo.md.exam0.ExchangeProvider";
    static final String URL_CURRENCY = "content://" + PROVIDER_NAME + "/currency";
    static final Uri CURRENCY_URI = Uri.parse(URL_CURRENCY);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String VALUE = "value";
    static final String AMOUNT = "amount";
    static final String HIDDEN = "hidden";

    private static HashMap<String, String> CURRENCY_PROJECTION_MAP, CITIES_PROJECTION_MAP;

    static final int CURRENCY = 1;
    static final int CURRENCY_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "currency", CURRENCY);
        uriMatcher.addURI(PROVIDER_NAME, "currency/#", CURRENCY_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Currency";
    static final String CURRENCY_TABLE_NAME = "currency";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TABLE =
            " CREATE TABLE " + CURRENCY_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT NOT NULL, " +
                    VALUE + " INTEGER," +
                    HIDDEN + " BOOLEAN," +
                    AMOUNT + " INTEGER);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CURRENCY_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = -1;
        switch (uriMatcher.match(uri)) {
            case CURRENCY:
                rowID = db.insert(CURRENCY_TABLE_NAME, "", values);
                break;
        }

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case CURRENCY:
                qb.setTables(CURRENCY_TABLE_NAME);
                qb.setProjectionMap(CURRENCY_PROJECTION_MAP);
                break;
            case CURRENCY_ID:
                qb.setTables(CURRENCY_TABLE_NAME);
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.equals("")){
            sortOrder = NAME;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case CURRENCY:
                count = db.delete(CURRENCY_TABLE_NAME, selection, selectionArgs);
                break;
            case CURRENCY_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(CURRENCY_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case CURRENCY:
                count = db.update(CURRENCY_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case CURRENCY_ID:
                count = db.update(CURRENCY_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}