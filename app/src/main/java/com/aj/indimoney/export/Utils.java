package com.aj.indimoney.export;

import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.TextView;

public class Utils {
    public static void showSnackBar(View view, String message) {
        // styling for action text
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setAllCaps(true);

        View sbView = snackbarView;
        sbView.setBackgroundColor(Color.BLACK);
        snackbar.show();
    }
}
