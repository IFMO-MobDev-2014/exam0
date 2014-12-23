package odeen.exam0;

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Женя on 23.12.2014.
 */
public class TradeActivity extends Activity {
    public static String EXTRA_ID = "ID";

    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_activity);
        mId = getIntent().getLongExtra(EXTRA_ID, -1);
        final Value val = ValueManager.getInstance(this).getValue(mId);
        final TextView a = (TextView) findViewById(R.id.val_tV);
        a.setText(val.getName() + ": " +  String.format("%.2f", ValueManager.getInstance(this).getAmount(val.getId())));

        final TextView b = (TextView) findViewById(R.id.rub_tV);
        b.setText("RUB" + ": " +  String.format("%.2f", ValueManager.getInstance(this).getAmount(0)));
        TextView cr = (TextView) findViewById(R.id.cur_tV);
        cr.setText(String.format("%.2f", val.getValue()));
        Button buy = (Button) findViewById(R.id.buy_btn);
        Button sell = (Button) findViewById(R.id.sell_btn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double nowa = ValueManager.getInstance(TradeActivity.this).getAmount(mId);
                double nowr = ValueManager.getInstance(TradeActivity.this).getAmount(0);
                ValueManager.getInstance(TradeActivity.this).updateAccount(mId, nowa + 1);
                ValueManager.getInstance(TradeActivity.this).updateAccount(0, nowr - val.getValue());
                a.setText(val.getName() + ": " + String.format("%.2f", nowa + 1));
                b.setText("RUB: " + String.format("%.2f", nowr - val.getValue()));
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double nowa = ValueManager.getInstance(TradeActivity.this).getAmount(mId);
                double nowr = ValueManager.getInstance(TradeActivity.this).getAmount(0);
                ValueManager.getInstance(TradeActivity.this).updateAccount(mId, nowa - 1);
                ValueManager.getInstance(TradeActivity.this).updateAccount(0, nowr + val.getValue());
                a.setText(val.getName() + ": " + String.format("%.2f", nowa - 1));
                b.setText("RUB: " + String.format("%.2f", nowr + val.getValue()));
            }
        });
    }
}
