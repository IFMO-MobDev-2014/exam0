package ru.ifmo.md.exam0;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kna on 23.12.14.
 */
public class MoneyStuff {
    /*public class MoneyType {
        private final String label;
        private float course;
        private int courseChanges;

        public void fluctuate() {
            courseChanges++;
            course += new Random().nextFloat() / 10.0f;
            courseChanges++;
            if(courseChanges%10 == 0) {
                course += new Random().nextBoolean() ? 1 : -1;
            }
        }

        public String getLabel() {
            return label;
        }

        public float getCourse() {
            return course;
        }

        public MoneyType(String label, float course) {
            this.label = label;
            this.course = course;
            this.courseChanges = 0;
        }
    }

    private ArrayList<MoneyType> types = new ArrayList<MoneyType>();
    private KeyValueStore keyvalues;

    public MoneyStuff(Context context) {
        keyvalues = new KeyValueStore(context);
        load();
    }

    public Cursor getTypesCursor() {
        return new Cursor() {

            int position = -1;

            @Override
            public int getCount() {
                return types.size();
            }

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public boolean move(int offset) {
                position += offset;
                return true;
            }

            @Override
            public boolean moveToPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public boolean moveToFirst() {
                position = 0;
                return true;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                if(position >= getCount())
                    return false;
                position++;
                return true;
            }

            @Override
            public boolean moveToPrevious() {
                if(position <= -1)
                    return false;

                position--;
                return true;
            }

            @Override
            public boolean isFirst() {
                return position == 0;
            }

            @Override
            public boolean isLast() {
                return position == getCount() - 1;
            }

            @Override
            public boolean isBeforeFirst() {
                return position == -1;
            }

            @Override
            public boolean isAfterLast() {
                return position == getCount();
            }

            @Override
            public int getColumnIndex(String columnName) {
                return -1;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return -1;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return null;
            }

            @Override
            public String getString(int columnIndex) {
                if(columnIndex == 0)
                    return types.get(position).getLabel();
                if(columnIndex == 1)
                    return types.get(position).getCourse() + "";
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return types.get(position).getCourse();
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
    }*/
}
