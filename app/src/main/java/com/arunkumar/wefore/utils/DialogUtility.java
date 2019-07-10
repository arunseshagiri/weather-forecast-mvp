package com.arunkumar.wefore.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtility {

    private DialogUtility() {
    }

    public static void showDialog(
            Context context,
            int title,
            int message,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(title))
                .setMessage(context.getString(message))
                .setPositiveButton(android.R.string.yes, positiveListener)
                .setNegativeButton(android.R.string.no, negativeListener)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
