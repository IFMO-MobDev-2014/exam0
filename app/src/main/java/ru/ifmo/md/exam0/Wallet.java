package ru.ifmo.md.exam0;

import android.app.Application;
import android.content.ContentValues;

public class Wallet {
    private static double sum = 0;
    private static Application application;

    public static void setApplication(Application application) {
        Wallet.application = application;
    }

    public static void setSum(double sum) {
        Wallet.sum = sum;
        ContentValues cv = new ContentValues();
        cv.put(CurrencyDatabaseHelper.CURRENCY_COUNT, sum);
        application.getContentResolver().update(CurrencyContentProvider.CURRENCY_CONTENT_URI, cv, CurrencyDatabaseHelper.CURRENCY_NAME + " = 'RUB'", null);
    }

    public static double getLeft() {
        return sum;
    }
}
