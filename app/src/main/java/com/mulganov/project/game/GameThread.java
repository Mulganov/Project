package com.mulganov.project.game;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.mulganov.project.FullscreenActivity;
import com.mulganov.project.R;
import com.mulganov.project.layout.Layouts;
import com.mulganov.project.maps.create.MySurfaceView;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;

public class GameThread extends Thread {

    private boolean runFlag = false;
    public static SurfaceHolder surfaceHolder;


    public GameThread(SurfaceHolder surfaceHolder, final Resources resources){
        this.surfaceHolder = surfaceHolder;

        Layouts.init();

        float zx, zy;

//        game_mode = new Image(BitmapFactory.decodeResource(resources, R.drawable.menu_game_mode), Layouts.Menu_MAIN, "menu") {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//
//            }
//        };
//
//        game_mode.setOnClick(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//
//        game_mode.setMatrixInfo(new MatrixInfo(0.3f, 0.3f, FullscreenActivity.window.X / 2 - 600, 300));




    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {

            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null) return;
                synchronized (surfaceHolder) {
                    canvas.drawColor(Color.WHITE);

                    Paint p = new Paint();

                    for (int id = 0; id < Layouts.game_number; id++){
                        for (Image i: Layouts.getLayoutMenu(id).get()){
                            if (i.isDraw()){
                                i.draw(canvas);
                            }
                        }
                    }
                }
            }
            finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            // }

        }
    }


}
