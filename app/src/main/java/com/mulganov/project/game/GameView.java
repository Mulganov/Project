package com.mulganov.project.game;


import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mulganov.project.layout.Layouts;
import com.mulganov.project.menu.MenuThread;
import com.mulganov.project.tools.Image;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread gameThread;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX();
        float ey = event.getY();
        for (int id = Layouts.game_number - 1; id >= 0; id--){
            for (Image i: Layouts.getLayoutGame(id).get()){
                if (i.isToucheEvent() && i.isDraw()){
                    if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                        if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                            if (i.getEvent().equalsIgnoreCase("onClick"))
                                i.onClick();
                            else
                                i.onTouchEvent(event);
                            break;
                        }
                    }
                }
            }
        }
        return true; //processed
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new GameThread(getHolder(), getResources());
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }
}



