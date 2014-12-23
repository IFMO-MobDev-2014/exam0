package odeen.exam0;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static android.net.Uri.*;

/**
 * Created by Женя on 24.11.2014.
 */

public class ValueManager {


    private Context mContext;
    private static ValueManager sManager;

    private Uri mNameUri = parse("content://odeen.exam.providers.exam_provider/name");
    private Uri mValueUri = parse("content://odeen.exam.providers.exam_provider/value");
    private Uri mAccountUri = parse("content://odeen.exam.providers.exam_provider/account");

    private ValueManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ValueManager getInstance(Context context) {
        if (sManager == null)
            sManager = new ValueManager(context);
        return sManager;
    }

    public void updateValue(long id, double value)
    {
        ContentValues cv = new ContentValues();
        cv.put(ValueContentProvider.COLUMN_NAME_CUR, value);
        mContext.getContentResolver().update(Uri.withAppendedPath(mNameUri, id + ""), cv, null, null);


        ContentValues cv1 = new ContentValues();
        cv1.put(ValueContentProvider.COLUMN_VALUE_ID, id);
        cv1.put(ValueContentProvider.COLUMN_VALUE_VALUE, value);
        mContext.getContentResolver().update(mValueUri, cv1, null, null);
    }

    public ValueContentProvider.ValueCursor getValues() {
        return new ValueContentProvider.ValueCursor(mContext.getContentResolver().query(mNameUri, null, null, null, null));
    }

    public Value getValue(long id) {
        ValueContentProvider.ValueCursor c = new ValueContentProvider.ValueCursor(
                mContext.getContentResolver().query(Uri.withAppendedPath(mNameUri, id + ""), null, null, null, null));
        c.moveToFirst();
        return c.getValue();
    }

    public void updateAccount(long id, double am) {
        ContentValues cv = new ContentValues();
        cv.put(ValueContentProvider.COLUMN_ACCOUNT_VALUE, am);
        mContext.getContentResolver().update(Uri.withAppendedPath(mAccountUri, id+""), cv, null, null);
    }

    public double getAmount(long id) {
        Cursor c = mContext.getContentResolver().query(Uri.withAppendedPath(mAccountUri, id + ""), null, null, null, null);
        c.moveToFirst();
        Log.d("HUI", id + "");
        return c.getDouble(c.getColumnIndex(ValueContentProvider.COLUMN_ACCOUNT_VALUE));
    }

}
