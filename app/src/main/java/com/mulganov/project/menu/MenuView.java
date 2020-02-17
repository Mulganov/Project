package com.mulganov.project.menu;



import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mulganov.project.layout.Layouts;
import com.mulganov.project.tools.Image;


public class MenuView extends SurfaceView implements SurfaceHolder.Callback {
    private MenuThread menuThread;

    public MenuView(Context context) {
        super(context);
        getHolder().addCallback(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX();
        float ey = event.getY();
        for (int id = Layouts.menu_number - 1; id >= 0; id--){
            for (Image i: Layouts.getLayoutMenu(id).get()){
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
        menuThread = new MenuThread(getHolder(), getResources());
        menuThread.setRunning(true);
        menuThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        menuThread.setRunning(false);
        while (retry) {
            try {
                menuThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }
}



