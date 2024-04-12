package com.salazarisaiahnoel.pacmanandroid;

import android.app.Activity;
import android.view.View;

public class Fullscreen {
    public static void enableFullscreen(Activity activity){
        int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        activity.getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
    }
}
