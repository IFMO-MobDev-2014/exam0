package ru.ifmo.md.exam0;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by flyingleafe on 23.12.14.
 */
public class Currency implements Serializable {
    public static final String EUR = "EUR";
    public static final String USD = "USD";
    public static final String GBP = "GBP";
    public static final String RUB = "RUB";

    public double eur = 0;
    public double usd = 0;
    public double gbp = 0;

    @Override
    public String toString() {
        JSONObject o = new JSONObject();
        try {
            o.put(EUR, eur);
            o.put(USD, usd);
            o.put(GBP, gbp);
            return o.toString();
        } catch (JSONException ignore) {
            return "";
        }
    }

    public static Currency fromString(String s) {
        try {
            JSONObject o = new JSONObject(s);
            Currency c = new Currency();
            c.eur = o.getDouble(EUR);
            c.usd = o.getDouble(USD);
            c.gbp = o.getDouble(GBP);
            return c;
        } catch (JSONException ignore) {
            return new Currency();
        }
    }
}
