package com.pastpaperskenya.app.business.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.pastpaperskenya.app.R;

import java.util.Calendar;

public class StoreTimeHelper {
    // Change Date and Time Here
    public static final int startDay = Calendar.FRIDAY;
    public static final int endDay = Calendar.SATURDAY;
    public static final int startTime = 18;
    public static final int endTime = 18;

    public static boolean isStoreOpen() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case startDay:
                return calendar.get(Calendar.HOUR_OF_DAY) < startTime;
            case endDay:
                return calendar.get(Calendar.HOUR_OF_DAY) >= endTime;
            default:
                return true;
        }
    }

    public static void showCloseDialogue(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_store_close);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button dialogBtn_cancel = dialog.findViewById(R.id.close_button);
        dialogBtn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
