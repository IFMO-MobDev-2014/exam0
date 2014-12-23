package ru.ifmo.md.exam0;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class ExchangeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ContentValues getCurrencyValues(String name, int value, int amount, boolean hidden) {
        ContentValues cv = new ContentValues();
        cv.put(ExchangeProvider.NAME, name);
        cv.put(ExchangeProvider.VALUE, value);
        cv.put(ExchangeProvider.AMOUNT, amount);
        cv.put(ExchangeProvider.HIDDEN, hidden);
        return cv;
    }

    private void initValues() {
        ContentResolver resolver = getContentResolver();
        resolver.insert(ExchangeProvider.CURRENCY_URI, getCurrencyValues("RUB", 100, 10000, true));
        resolver.insert(ExchangeProvider.CURRENCY_URI, getCurrencyValues("USD", 5400, 0, false));
        resolver.insert(ExchangeProvider.CURRENCY_URI, getCurrencyValues("EUR", 6500, 0, false));
        resolver.insert(ExchangeProvider.CURRENCY_URI, getCurrencyValues("GBP", 7500, 0, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (getSharedPreferences("FirstRun", MODE_PRIVATE).getBoolean("FirstRun", true)) {
            initValues();
            getSharedPreferences("FirstRun", MODE_PRIVATE).edit().putBoolean("FirstRun", false).commit();
        }

        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent oneSec = new Intent(this, ChangeService.class);
        oneSec.putExtra("randAbs", 10);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, oneSec, 0);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 1000, pendingIntent);

        Intent tenSec = new Intent(this, ChangeService.class);
        oneSec.putExtra("randAbs", 100);
        PendingIntent pendingIntent1 = PendingIntent.getService(this, 0, tenSec, 0);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 1000, pendingIntent1);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mNavigationDrawerFragment == null)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, "EUR"))
                    .commit();
        else
            fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, mNavigationDrawerFragment.getTitle(position)))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle = mNavigationDrawerFragment.getTitle(number - 1);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.exchange, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private String currency;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String currency) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("currency", currency);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        static class FragmentObserver extends ContentObserver {
            PlaceholderFragment fragment;

            public FragmentObserver(Handler handler, PlaceholderFragment fragment) {
                super(handler);
                this.fragment = fragment;
            }

            @Override
            public void onChange(boolean selfChange) {
                this.onChange(selfChange, ExchangeProvider.CURRENCY_URI);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                fragment.refresh();
            }
        }

        public void refresh() {
            int currencyAmount, RUBAmount, value;

            try {
                Cursor c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{currency}, null);
                c.moveToNext();
                value = c.getInt(c.getColumnIndex(ExchangeProvider.VALUE));
                ((TextView) rootView.findViewById(R.id.valueLabel)).setText(
                        Double.toString((double) value / 100));
                currencyAmount = c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT));
                ((TextView) rootView.findViewById(R.id.currencyLabel)).setText(currency + ": " + currencyAmount);
                c.close();

                c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{"RUB"}, null);
                c.moveToNext();
                RUBAmount = c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT));
                ((TextView) rootView.findViewById(R.id.RUBLabel)).setText("RUB: " + RUBAmount);
                c.close();

                ((Button) rootView.findViewById(R.id.sellButton)).setEnabled(currencyAmount > 0);
                ((Button) rootView.findViewById(R.id.buyButton)).setEnabled(RUBAmount > value / 100);
            }
            catch (Exception ignored) {}
        }

        View.OnClickListener sellListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{currency}, null);
                c.moveToNext();

                ContentValues cv = new ContentValues();
                cv.put(ExchangeProvider.NAME, c.getString(c.getColumnIndex(ExchangeProvider.NAME)));
                cv.put(ExchangeProvider.AMOUNT, c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT)) - 1);
                int value = c.getInt(c.getColumnIndex(ExchangeProvider.VALUE));
                cv.put(ExchangeProvider.VALUE, c.getInt(c.getColumnIndex(ExchangeProvider.VALUE)));
                cv.put(ExchangeProvider.HIDDEN, c.getString(c.getColumnIndex(ExchangeProvider.HIDDEN)));

                getActivity().getContentResolver().delete(ExchangeProvider.CURRENCY_URI, ExchangeProvider._ID + "=?",
                        new String[]{Integer.toString(c.getInt(c.getColumnIndex(ExchangeProvider._ID)))});
                getActivity().getContentResolver().insert(ExchangeProvider.CURRENCY_URI, cv);
                c.close();

                c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{"RUB"}, null);
                c.moveToNext();

                cv = new ContentValues();
                cv.put(ExchangeProvider.NAME, c.getString(c.getColumnIndex(ExchangeProvider.NAME)));
                cv.put(ExchangeProvider.AMOUNT, c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT)) + value / 100);
                cv.put(ExchangeProvider.VALUE, c.getInt(c.getColumnIndex(ExchangeProvider.VALUE)));
                cv.put(ExchangeProvider.HIDDEN, c.getString(c.getColumnIndex(ExchangeProvider.HIDDEN)));

                getActivity().getContentResolver().delete(ExchangeProvider.CURRENCY_URI, ExchangeProvider._ID + "=?",
                        new String[]{Integer.toString(c.getInt(c.getColumnIndex(ExchangeProvider._ID)))});
                getActivity().getContentResolver().insert(ExchangeProvider.CURRENCY_URI, cv);
                c.close();
            }
        };

        View.OnClickListener buyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{currency}, null);
                c.moveToNext();

                ContentValues cv = new ContentValues();
                cv.put(ExchangeProvider.NAME, c.getString(c.getColumnIndex(ExchangeProvider.NAME)));
                cv.put(ExchangeProvider.AMOUNT, c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT)) + 1);
                int value = c.getInt(c.getColumnIndex(ExchangeProvider.VALUE));
                cv.put(ExchangeProvider.VALUE, c.getInt(c.getColumnIndex(ExchangeProvider.VALUE)));
                cv.put(ExchangeProvider.HIDDEN, c.getString(c.getColumnIndex(ExchangeProvider.HIDDEN)));

                getActivity().getContentResolver().delete(ExchangeProvider.CURRENCY_URI, ExchangeProvider._ID + "=?",
                        new String[]{Integer.toString(c.getInt(c.getColumnIndex(ExchangeProvider._ID)))});
                getActivity().getContentResolver().insert(ExchangeProvider.CURRENCY_URI, cv);
                c.close();

                c = getActivity().getContentResolver().query(ExchangeProvider.CURRENCY_URI,
                        null, ExchangeProvider.NAME + "=?", new String[]{"RUB"}, null);
                c.moveToNext();

                cv = new ContentValues();
                cv.put(ExchangeProvider.NAME, c.getString(c.getColumnIndex(ExchangeProvider.NAME)));
                cv.put(ExchangeProvider.AMOUNT, c.getInt(c.getColumnIndex(ExchangeProvider.AMOUNT)) - value / 100);
                cv.put(ExchangeProvider.VALUE, c.getInt(c.getColumnIndex(ExchangeProvider.VALUE)));
                cv.put(ExchangeProvider.HIDDEN, c.getString(c.getColumnIndex(ExchangeProvider.HIDDEN)));

                getActivity().getContentResolver().delete(ExchangeProvider.CURRENCY_URI, ExchangeProvider._ID + "=?",
                        new String[]{Integer.toString(c.getInt(c.getColumnIndex(ExchangeProvider._ID)))});
                getActivity().getContentResolver().insert(ExchangeProvider.CURRENCY_URI, cv);
                c.close();
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_exchange, container, false);
            this.rootView = rootView;
            ((Button)rootView.findViewById(R.id.sellButton)).setOnClickListener(sellListener);
            ((Button)rootView.findViewById(R.id.buyButton)).setOnClickListener(buyListener);

            refresh();
            getActivity().getContentResolver().registerContentObserver(ExchangeProvider.CURRENCY_URI,
                    true, new FragmentObserver(new Handler(), this));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ExchangeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
            currency = getArguments().getString("currency");
        }
    }

}
