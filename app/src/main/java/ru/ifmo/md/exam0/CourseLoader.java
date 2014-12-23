package ru.ifmo.md.exam0;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import java.util.concurrent.TimeUnit;

/**
 * Created by ignat on 23.12.14.
 */
public class CourseLoader extends CursorLoader {

    public CourseLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        Uri uri = Uri.parse("content://ru.ifmo.md.exam0/currency");
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        return cursor;
    }
}
