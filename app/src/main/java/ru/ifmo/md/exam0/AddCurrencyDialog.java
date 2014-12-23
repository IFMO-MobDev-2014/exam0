package ru.ifmo.md.exam0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by dimatomp on 23.12.14.
 */
public class AddCurrencyDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText text = new EditText(getActivity());
        builder.setTitle("Add currency").setView(text)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getContentResolver().insert(
                                Uri.parse("content://net.dimatomp.wallet.provider/currency?name=" +
                                        Uri.encode(text.getText().toString())), null);
                        getActivity().getLoaderManager().restartLoader(0, null,
                                (LoaderManager.LoaderCallbacks<Cursor>) getActivity());
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
