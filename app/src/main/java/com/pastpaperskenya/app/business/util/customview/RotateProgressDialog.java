package com.pastpaperskenya.app.business.util.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by Md Sahidul Islam on 24-Jan-19.
 */
public class RotateProgressDialog {

    private Dialog dialog;
    private ProgressBar progressBar;
    private RotateProgress progress;

    public RotateProgressDialog(Context context) {
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        progress = new RotateProgress(context);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        //RelativeLayout.LayoutParams layoutParams_progress = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParams_progress = new RelativeLayout.LayoutParams(120,120);
        layoutParams_progress.addRule(RelativeLayout.CENTER_IN_PARENT);

        progressBar = new ProgressBar(context);
        LinearLayout.LayoutParams linearlayoutParams_progress = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearlayoutParams_progress.gravity = Gravity.CENTER;
        progress.setLayoutParams(layoutParams_progress);

        relativeLayout.addView(progress);

        dialog.getWindow().setContentView(relativeLayout, layoutParams);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void show() {
        if (!dialog.isShowing() && dialog != null) {
            progress.show();
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog.isShowing() && dialog != null) {
            progress.hide();
            dialog.dismiss();
        }
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
    }

    public void setColor(int colour) {
        progressBar.getIndeterminateDrawable().setColorFilter(colour, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public boolean isShowing() {

        return dialog.isShowing();
    }

}