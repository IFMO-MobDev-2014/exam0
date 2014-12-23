package odeen.exam0;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.util.Log;

import odeen.exam0.ValueService;

/**
 * Created by Женя on 05.12.2014.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_SERVICE = 0;

    public static final long INTERVAL = 1 * 1000;
    public static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ValueService.class);
        context.startService(i);
    }
}
