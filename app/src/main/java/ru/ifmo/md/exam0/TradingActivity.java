package ru.ifmo.md.exam0;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class TradingActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    private String valueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        valueName = getIntent().getExtras().getString("value");

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;

                double value_count;
                double rub_count;
                double value_value;

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "2"}, null);
                cursor.moveToFirst();
                value_count = cursor.getDouble(0);
                cursor.close();

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"RUB", "2"}, null);
                cursor.moveToFirst();
                rub_count = cursor.getDouble(0);
                cursor.close();

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "1"}, null);
                cursor.moveToFirst();
                value_value = cursor.getDouble(0);
                cursor.close();

                ContentValues values;

                if (rub_count >= value_value) {
                    rub_count -= value_value;
                    value_count += 1;

                    values = new ContentValues();
                    values.put(MyTable.COLUMN_VALUE, value_count);
                    getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                            MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "2"});


                    values = new ContentValues();
                    values.put(MyTable.COLUMN_VALUE, rub_count);
                    getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                            MyTable.COLUMN_TYPE + " = ?", new String[] {"RUB", "2"});

                    Toast.makeText(getBaseContext(), "RUB: " + Double.toString(rub_count) + valueName + " : "+ Double.toString(value_count), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), "Not enough rubles!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;

                double value_count;
                double rub_count;
                double value_value;

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "2"}, null);
                cursor.moveToFirst();
                value_count = cursor.getDouble(0);
                cursor.close();

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {"RUB", "2"}, null);
                cursor.moveToFirst();
                rub_count = cursor.getDouble(0);
                cursor.close();

                cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, new String[]{MyTable.COLUMN_VALUE}, MyTable.COLUMN_NAME + " = ? AND " +
                        MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "1"}, null);
                cursor.moveToFirst();
                value_value = cursor.getDouble(0);
                cursor.close();

                ContentValues values;

                if (value_count > 0) {
                    rub_count += value_value;
                    value_count -= 1;

                    values = new ContentValues();
                    values.put(MyTable.COLUMN_VALUE, value_count);
                    getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                            MyTable.COLUMN_TYPE + " = ?", new String[] {valueName, "2"});


                    values = new ContentValues();
                    values.put(MyTable.COLUMN_VALUE, rub_count);
                    getContentResolver().update(MyContentProvider.CONTENT_URI, values, MyTable.COLUMN_NAME + " = ? AND " +
                            MyTable.COLUMN_TYPE + " = ?", new String[] {"RUB", "2"});

                    Toast.makeText(getBaseContext(), "RUB: " + Double.toString(rub_count) + valueName + " : "+ Double.toString(value_count), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), "Not enough currency!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        fillData();

        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

    }

    private void fillData() {

        String[] from = new String[]{ MyTable.COLUMN_NAME, MyTable.COLUMN_VALUE};
        int[] to = new int[]{R.id.name, R.id.value};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.my_item_1, null, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { MyTable.COLUMN_ID, MyTable.COLUMN_NAME, MyTable.COLUMN_VALUE };
        return new CursorLoader(this, MyContentProvider.CONTENT_URI, projection, MyTable.COLUMN_NAME + " = ? AND " + MyTable.COLUMN_TYPE + " = ?", new String[]{valueName, "1"}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

}
