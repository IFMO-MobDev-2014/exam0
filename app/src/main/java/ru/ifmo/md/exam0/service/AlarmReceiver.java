package ru.ifmo.md.exam0.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, CurrencyUpdateService.class);
        newIntent.setAction(CurrencyUpdateService.ACTION_UPDATE);
        context.startService(newIntent);
    }
}
