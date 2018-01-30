package com.example.pony.equallinearlayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;

public class UIUtils {

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    @TargetApi(17)
    public static int getScreenHeight(Activity activity) {
        if (Build.VERSION.SDK_INT < 17)
            return activity.getWindowManager().getDefaultDisplay().getHeight();

        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        return p.y;
    }

    @TargetApi(17)
    public static int getScreenWidth(Activity activity) {
        if (Build.VERSION.SDK_INT < 17)
            return activity.getWindowManager().getDefaultDisplay().getWidth();

        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        return p.x;
    }
}
