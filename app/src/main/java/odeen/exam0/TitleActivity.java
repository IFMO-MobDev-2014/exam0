package odeen.exam0;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

/**
 * Created by Женя on 23.12.2014.
 */
public class TitleActivity extends ListActivity {


    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValueContentProvider.ValueCursor c = ValueManager.getInstance(this).getValues();
        ValueCursorAdapter va = new ValueCursorAdapter(this, c);
        setListAdapter(va);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmReceiver.INTERVAL, AlarmReceiver.INTERVAL, pi);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i1 = new Intent(this, AlarmReceiver.class);
        i1.putExtra("1", 1);
        PendingIntent pi1 = PendingIntent.getBroadcast(this, 0, i1, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager1.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmReceiver.INTERVAL * 10, AlarmReceiver.INTERVAL, pi1);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, TradeActivity.class);
        i.putExtra(TradeActivity.EXTRA_ID, id);
        startActivity(i);
    }

    private static class ValueCursorAdapter extends CursorAdapter {
        private ValueContentProvider.ValueCursor mCursor;

        public ValueCursorAdapter(Context context, ValueContentProvider.ValueCursor cursor) {
            super(context, cursor, true);
            mCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.list_element, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Value val = mCursor.getValue();
            TextView name = (TextView) view.findViewById(R.id.element_textView);
            name.setText(val.getName() + ": " + String.format("%.2f", val.getValue()));
        }

        public Value get(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getValue();
        }
    }





}
