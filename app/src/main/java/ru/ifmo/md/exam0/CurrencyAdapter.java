package ru.ifmo.md.exam0;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends ArrayAdapter<String> {
    ArrayList<String> names;
    ArrayList<Integer> values;

    public CurrencyAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        names = objects;
        values = null;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public String getName(int pos) {
        return names.get(pos);
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public String getItem(int position) {
        if (values == null)
            return names.get(position);
        return names.get(position) + ": " + (double)values.get(position) / 100;
    }
}
