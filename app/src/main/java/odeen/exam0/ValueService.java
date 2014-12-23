package odeen.exam0;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Женя on 24.11.2014.
 */
public class ValueService extends IntentService {
    private static final String TAG = "ValueService";


    public ValueService() {
        super("ValueService");
    }

    private Random rnd = new Random();

    private double randFromTo() {
        if (rnd.nextInt() % 2 == 0)
            return rnd.nextDouble();
        return -rnd.nextDouble();
    }
    private int randFromTo1() {
        if (rnd.nextInt() % 2 == 0)
            return rnd.nextInt()%2;
        return -rnd.nextInt()%2;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ValueContentProvider.ValueCursor c = ValueManager.getInstance(getApplicationContext()).getValues();
        c.moveToFirst();
        while (!c.isBeforeFirst() && !c.isAfterLast()) {
            Value v = c.getValue();
            if (!intent.hasExtra("1"))
                ValueManager.getInstance(getApplicationContext()).updateValue(v.getId(), v.getValue() + randFromTo());
            else
                ValueManager.getInstance(getApplicationContext()).updateValue(v.getId(), v.getValue() + randFromTo1());
            c.moveToNext();
        }
    }
}
