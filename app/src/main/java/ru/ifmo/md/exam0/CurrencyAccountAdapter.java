package ru.ifmo.md.exam0;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAccountAdapter extends BaseAdapter {
    private List<Currency> currencies;

    public CurrencyAccountAdapter(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public CurrencyAccountAdapter() {
        currencies = new ArrayList<>();
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
    }

    public void clear() {
        currencies.clear();
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Currency getItem(int position) {
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_adapter_list, parent, false);
        } else {
            v = convertView;
        }
        TextView nameTextView = (TextView) v.findViewById(R.id.currencyName);
        TextView valueTextView = (TextView) v.findViewById(R.id.currencyValue);
        nameTextView.setText(currencies.get(position).getName() + " ");
        Log.d("LOGS", currencies.get(position).getCount() + " " + currencies.get(position).getValue());
        valueTextView.setText(Double.toString(currencies.get(position).getCount()));
        return v;
    }
}
