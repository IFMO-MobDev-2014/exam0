package odeen.exam0;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Женя on 24.11.2014.
 */
public class ValueContentProvider extends ContentProvider {

    private static final String TAG = "ValueProvider";

    //----------------
    //DB constants
    //----------------
    private static final String DB_NAME = "exam.sqlite";
    private static final int VERSION = 1;


    public static final String TABLE_NAME = "name";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_CUR = "current";


    public static final String TABLE_VALUE = "value";
    public static final String COLUMN_VALUE_ID = "id";
    public static final String COLUMN_VALUE_TIME = "time";
    public static final String COLUMN_VALUE_VALUE = "value";

    public static final String TABLE_ACCOUNT = "account";
    public static final String COLUMN_ACCOUNT_ID = "id";
    public static final String COLUMN_ACCOUNT_VALUE = "value";


    //----------------
    //Uri constants
    //----------------
    private static final String AUTHORITY = "odeen.exam.providers.exam_provider";
    public static final Uri VALUE_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_VALUE);
    public static final Uri NAME_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_NAME);
    public static final Uri ACCOUNT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_ACCOUNT);



    private static final int URI_VALUE = 1;
    private static final int URI_VALUE_ID = 2;
    private static final int URI_NAME = 3;
    private static final int URI_NAME_ID = 4;
    private static final int URI_ACCOUNT = 5;
    private static final int URI_ACCOUNT_ID = 6;


    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, TABLE_VALUE, URI_VALUE);
        mUriMatcher.addURI(AUTHORITY, TABLE_VALUE + "/#", URI_VALUE_ID);
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME, URI_NAME);
        mUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", URI_NAME_ID);
        mUriMatcher.addURI(AUTHORITY, TABLE_ACCOUNT, URI_ACCOUNT);
        mUriMatcher.addURI(AUTHORITY, TABLE_ACCOUNT + "/#", URI_ACCOUNT_ID);

    }

    private DatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = null;
        Uri contentUri = null;
        switch (mUriMatcher.match(uri)) {
            case URI_VALUE:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_VALUE_ID + " asc";
                }
                table = TABLE_VALUE;
                contentUri = VALUE_CONTENT_URI;
                break;
            case URI_VALUE_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_VALUE;
                contentUri = VALUE_CONTENT_URI;
                break;
            case URI_NAME:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_NAME_ID + " asc";
                }
                table = TABLE_NAME;
                contentUri = NAME_CONTENT_URI;
                break;
            case URI_NAME_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_NAME;
                contentUri = NAME_CONTENT_URI;
                break;
            case URI_ACCOUNT:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_ACCOUNT_ID + " asc";
                }
                table = TABLE_ACCOUNT;
                contentUri = ACCOUNT_CONTENT_URI;
                break;
            case URI_ACCOUNT_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ACCOUNT_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_ACCOUNT_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_ACCOUNT;
                contentUri = ACCOUNT_CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor = mHelper.getReadableDatabase().query(table, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), contentUri);
        //Log.d(TAG, contentUri.toString());
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String table = null;
        Uri content = null;
        switch (mUriMatcher.match(uri)) {
            case URI_VALUE:
                table = TABLE_VALUE;
                content = VALUE_CONTENT_URI;
                break;
            case URI_NAME:
                table = TABLE_NAME;
                content = NAME_CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        long id = mHelper.getWritableDatabase().insert(table, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(content, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = null;
        switch (mUriMatcher.match(uri)) {
            case URI_VALUE:
                table = TABLE_VALUE;
                break;
            case URI_VALUE_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_VALUE;
                break;
            case URI_NAME:
                table = TABLE_NAME;
                break;
            case URI_NAME_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = mHelper.getWritableDatabase().delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = null;
        switch (mUriMatcher.match(uri)) {
            case URI_VALUE:
                table = TABLE_VALUE;
                break;
            case URI_VALUE_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_VALUE_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_VALUE;
                break;
            case URI_NAME:
                table = TABLE_NAME;
                break;
            case URI_NAME_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_NAME_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_NAME;
                break;

            case URI_ACCOUNT:
                table = TABLE_ACCOUNT;
                break;
            case URI_ACCOUNT_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ACCOUNT_ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " and " + COLUMN_ACCOUNT_ID + " = " + uri.getLastPathSegment();
                }
                table = TABLE_ACCOUNT;
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = mHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table value (" +
                            "id integer " +
                            "time integer, " +
                            "value integer)"
            );

            db.execSQL("create table name (" +
                            "_id integer primary key autoincrement, " +
                            "name string, current double default 50)"
            );

            db.execSQL("create table account (" +
                            "id integer, " +
                            "value double)"
            );

            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_ACCOUNT_ID, 0);
            cv1.put(COLUMN_ACCOUNT_VALUE, 1000);
            db.insert("account", null, cv1);
            cv1.put(COLUMN_ACCOUNT_ID, 1);
            cv1.put(COLUMN_ACCOUNT_VALUE, 0);
            db.insert("account", null, cv1);
            cv1.put(COLUMN_ACCOUNT_ID, 2);
            cv1.put(COLUMN_ACCOUNT_VALUE, 0);
            db.insert("account", null, cv1);
            cv1.put(COLUMN_ACCOUNT_ID, 3);
            cv1.put(COLUMN_ACCOUNT_VALUE, 0);
            db.insert("account", null, cv1);

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_NAME, "USD");
            db.insert("name", null, cv);
            cv.put(COLUMN_NAME_NAME, "EUR");
            db.insert("name", null, cv);
            cv.put(COLUMN_NAME_NAME, "GBP");
            db.insert("name", null, cv);


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {}
    }

    public static class ValueCursor extends CursorWrapper {
        public ValueCursor(Cursor cursor) {
            super(cursor);
        }
        public Value getValue() {
            if (isBeforeFirst() || isAfterLast())
                return null;
            Value res = new Value();
            res.setName(getString(getColumnIndex(COLUMN_NAME_NAME)));
            res.setId(getLong(getColumnIndex(COLUMN_NAME_ID)));
            res.setValue(getDouble(getColumnIndex(COLUMN_NAME_CUR)));
            return res;
        }
    }

}

