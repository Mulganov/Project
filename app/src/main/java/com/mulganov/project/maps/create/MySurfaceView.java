package com.mulganov.project.maps.create;


import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mulganov.project.FullscreenActivity;
import com.mulganov.project.layout.Layouts;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;
import com.mulganov.project.tools.Vector;
import com.mulganov.project.tools.Vectors;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;

    public static float zoom = 1;
    public static float x = 0;
    public static float y = 0;
    private static long prevTime = System.currentTimeMillis();

    private static Vector old1;
    private static Vector old2;
    private static Vector oldMove;
    private static boolean move;

    public static String mode;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);

        mode = "MOVE_ALL";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (mode){
//            case "MOVE_ALL": move_all(event);
//                break;
//        }

        if (createImage.el1.isDraw()){
            move_add_bar(event);
        }

        move_all(event);

        return true; //processed
    }


    public Vector move_add_bar_oldMove;

    private void move_add_bar(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        if (!(FullscreenActivity.window.Y - 200 <= y && y <= FullscreenActivity.window.Y)) return;

        if ( 0 <= x && x <= FullscreenActivity.window.X - 200 ){

            switch(action){
                case MotionEvent.ACTION_DOWN:
                    move_add_bar_oldMove = new Vector(event.getX(0), event.getY(0));
                    break;
                case MotionEvent.ACTION_MOVE:
                    Vector newMove = new Vector(event.getX(0), event.getY(0));

                    Vector move = Vectors.minus(move_add_bar_oldMove, newMove);

                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        MatrixInfo mi = new MatrixInfo(
                                i.getMatrixInfo().getScale().X,i.getMatrixInfo().getScale().Y,
                                i.getMatrixInfo().getTranslate().X - move.X, i.getMatrixInfo().getTranslate().Y
                        );

                        i.setMatrixInfo(mi);
                    }

                    DrawThread.add_rec_b.X = DrawThread.add_rec_b.X - move.X;
                    DrawThread.add_rec_e.X = DrawThread.add_rec_e.X - move.X;

                    move_add_bar_oldMove = newMove;

                    break;

            }
        }else
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                    i.setDraw(false);
                }
                for (Image i: Layouts.Maps_Layout_MAIN.get()){
                    i.setToucheEvent(true);
                }
                for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                    i.setToucheEvent(true);
                }
                DrawThread.mode = "";
            }
        }



    }

    private void move_all(MotionEvent event) {

        int pointerCount = event.getPointerCount();
        //System.out.println(event.toString());
        int action = event.getAction();

        switch(action){
            case MotionEvent.ACTION_DOWN:

                float ex = event.getX();
                float ey = event.getY();

                for (int id = Layouts.maps_number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayoutMaps(id).get()){
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

                break;
            case MotionEvent.ACTION_UP:

                ex = event.getX();
                ey = event.getY();

                for (int id = Layouts.maps_number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayoutMaps(id).get()){
                        if (i.isToucheEvent() && i.isDraw()){
                            if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                                if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                                    if (i.getEvent().equalsIgnoreCase("else"))
                                        i.onTouchEvent(event);
                                    break;
                                }
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:

                ex = event.getX();
                ey = event.getY();

                for (int id = Layouts.maps_number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayoutMaps(id).get()){
                        if (i.isToucheEvent() && i.isDraw()){
                            if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                                if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                                    if (i.getEvent().equalsIgnoreCase("else"))
                                        i.onTouchEvent(event);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                ex = event.getX();
                ey = event.getY();

                for (int id = Layouts.maps_number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayoutMaps(id).get()){
                        if (i.isToucheEvent() && i.isDraw()){
                            if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                                if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                                    if (i.getEvent().equalsIgnoreCase("else"))
                                        i.onTouchEvent(event);
                                    break;
                                }
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                System.out.println("ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                System.out.println("ACTION_OUTSIDE");
                break;
            default:

                ex = event.getX();
                ey = event.getY();

                for (int id = Layouts.maps_number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayoutMaps(id).get()){
                        if (i.isToucheEvent() && i.isDraw()){
                            if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                                if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                                    if (i.getEvent().equalsIgnoreCase("else"))
                                        i.onTouchEvent(event);
                                    break;
                                }
                            }
                        }
                    }
                }


        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), getResources());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }
}



