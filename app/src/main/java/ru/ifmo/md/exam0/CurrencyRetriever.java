package ru.ifmo.md.exam0;

import android.app.Service;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import java.util.HashMap;
import java.util.Random;

public class CurrencyRetriever extends Service {
    final HashMap<String, Float> values = new HashMap<>();
    private Thread valueChanger = new Thread() {
        @Override
        public void run() {
            Looper.prepare();
            try {
                for (int cnt = 1; ; cnt = (cnt + 1) % 10) {
                    synchronized (values) {
                        for (String cur : values.keySet()) {
                            float delta = random.nextFloat() * 2 - 1;
                            if (cnt != 0)
                                delta /= 10;
                            values.put(cur, values.get(cur) + delta);
                        }
                        if (cnt == 0)
                            observable.notifyChanged();
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // shutting down
            }
        }
    };
    private final Random random = new Random();
    private DataSetObservable observable = new DataSetObservable();

    @Override
    public void onCreate() {
        super.onCreate();
        valueChanger.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CurrencyBinder();
    }

    public class CurrencyBinder extends Binder {
        public float getInRubles(String currency) {
            if (!"RUR".equals(currency))
                synchronized (values) {
                    return values.get(currency);
                }
            return 1;
        }

        public void subscribe(DataSetObserver listener) {
            observable.registerObserver(listener);
        }

        public void unsubscribe(DataSetObserver listener) {
            observable.unregisterObserver(listener);
        }

        public boolean checkForExistence(String curName) {
            if (!"RUR".equals(curName))
                synchronized (values) {
                    if (!values.containsKey(curName))
                        values.put(curName, random.nextFloat() * 90 + 10);
                }
            return true;
        }
    }
}
