package com.arunkumar.wefore.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class GenericUtils {
    private GenericUtils() {

    }

    public static int getScreenHeight(Context context) {
        int screenHeight = 0;
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }
}
