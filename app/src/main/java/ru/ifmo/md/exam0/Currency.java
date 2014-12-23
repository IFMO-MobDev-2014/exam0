package ru.ifmo.md.exam0;

import android.content.ContentValues;

public class Currency {
    private String name;
    private double value;
    private int count;
    private int id;

    public Currency(String name, double value, int count, int id) {
        this.name = name;
        this.value = value;
        this.count = count;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(CurrencyDatabaseHelper.CURRENCY_ID, id);
        cv.put(CurrencyDatabaseHelper.CURRENCY_NAME, name);
        cv.put(CurrencyDatabaseHelper.CURRENCY_VALUE, value);
        cv.put(CurrencyDatabaseHelper.CURRENCY_COUNT, count);
        return cv;
    }
}