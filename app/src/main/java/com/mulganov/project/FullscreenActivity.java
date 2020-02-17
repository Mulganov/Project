package com.mulganov.project;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.mulganov.project.layout.Layouts;
import com.mulganov.project.maps.create.MySurfaceView;
import com.mulganov.project.menu.MenuView;
import com.mulganov.project.tools.Vector;

public class FullscreenActivity extends AppCompatActivity{

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    public static FullscreenActivity clas;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    public View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    protected void onStart(){
        super.onStart();
        hide();
    }
    public void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public static Vector window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clas = this;
        Layouts.init();
        View content = new MenuView( this);

        setContentView(content);
        mContentView = content;

        hide();

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        window = new Vector(width, height);

        System.out.println(window.toString());
    }

}

