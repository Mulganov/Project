package com.mulganov.project.tools;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mulganov.project.DrawThread;
import com.mulganov.project.FullscreenActivity;
import com.mulganov.project.MySurfaceView;
import com.mulganov.project.R;
import com.mulganov.project.layout.Layouts;

public class createImage {

    public static Image fon;

    public static void calibration(float ex, float ey){

        float cx = FullscreenActivity.window.X / 2;
        float cy = FullscreenActivity.window.Y / 2;

        Vector r = Vectors.minus(new Vector(ex ,ey), new Vector(cx, cy));

        fon.sei.last = new Vector(fon.sei.x, fon.sei.y);

        fon.sei.x -= r.X;
        fon.sei.y -= r.Y;
    }

    public Image createFon(Resources resources){
        fon = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_fon_1), Layouts.MAIN) {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // TODO Auto-generated method stub

                // число касаний
                int pointerCount = event.getPointerCount();
                //System.out.println(event.toString());
                int action = event.getAction();

                switch(action){
                    case MotionEvent.ACTION_DOWN:
                        long now = System.currentTimeMillis();
                        fon.sei.move = true;
                        if (now - fon.sei.prevTime < 150){
//
//                            if (DrawThread.main_bar_image_act != null){
//                                DrawThread.main_vector_event = new Vector(event.getX(), event.getY());
//                                DrawThread.main_bar_image_act.onTouchEvent(event);
//                            }else{
//                                System.out.println("else zoom = 1");
//
//                                if (DrawThread.zoom_in.sei.zoom == 1 ){
//                                    calibration(event.getX(), event.getY());
//                                }else{
//                                    DrawThread.zoom_in.sei.zoom = 1;
//
//                                    System.out.println(fon.sei.last.X);
//
//                                    fon.sei.x = fon.sei.last.X*4;
//                                    fon.sei.y = fon.sei.last.Y*4;
//
//                                    fon.sei.zoom = 1;
//
//                                    DrawThread.zoom_in.setToucheEvent(true);
//                                    DrawThread.zoom_in.paint.setAlpha(255);
//
//                                    DrawThread.zoom_out.setToucheEvent(true);
//                                    DrawThread.zoom_out.paint.setAlpha(255);
//                                }


//                                calibration(event.getX(), event.getY());

//                            }
                        }else{
                            fon.sei.prevTime = now;
                            fon.sei.oldMove = new Vector(event.getX(0), event.getY(0));
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // сохраняем текущее время
                        fon.sei.move = false;
                        fon.sei.prevTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        fon.sei.move = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pointerCount == 1){
                            Vector newMove = new Vector(event.getX(0), event.getY(0));

                            Vector move = Vectors.minus(fon.sei.oldMove, newMove);

                            //System.out.println();

                            fon.sei.x -= move.X;
                            fon.sei.y -= move.Y;

                            for (Image i: Layouts.Layout_MAIN2.get()){
                                i.sei.x -= move.X;
                                i.sei.y -= move.Y;
                            }

                            fon.sei.oldMove = newMove;
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:
                        if (event.getActionMasked() == 5){

                            break;
                        }

                }
                fon.setMatrixInfo(new MatrixInfo(
                        fon.getVectorStartScore().X * fon.sei.zoom,
                        fon.getVectorStartScore().Y * fon.sei.zoom,
                        fon.getVectorStartTranslate().X + fon.sei.x,
                        fon.getVectorStartTranslate().Y + fon.sei.y));

                for (Image i : Layouts.Layout_MAIN2.get()){
                    i.setMatrixInfo(new MatrixInfo(
                            i.getVectorStartScore().X * i.sei.zoom,
                            i.getVectorStartScore().Y * i.sei.zoom,
                            i.getVectorStartTranslate().X + i.sei.x,
                            i.getVectorStartTranslate().Y + i.sei.y));
                }
            }

        };
        fon.setMatrixInfo(new MatrixInfo(1f, 1f, 0, 0));
        fon.setVectorStartTranslate(new Vector(fon.getMatrixInfo().getTranslate().X, fon.getMatrixInfo().getTranslate().Y));
        fon.setVectorStartScore(new Vector(fon.getMatrixInfo().getScale().X, fon.getMatrixInfo().getScale().Y));

        fon.setToucheEvent(true);



        return fon;
    }

    public static Image el1, el2;

    public void createAddElements(Resources resources){

        el1 = new Image(BitmapFactory.decodeResource(resources, R.drawable.add_el1), Layouts.ADD_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }

        };

        el1.setMatrixInfo(new MatrixInfo(0.1f, 0.1f, 0, 0));
        el1.setDraw(false);
        el1.setToucheEvent(true);
        el1.setOnClick(new Runnable() {
            @Override
            public void run() {
                DrawThread.mode = "ADD_BAR_REC";
                DrawThread.add_element = el1;
                DrawThread.add_rec_b = new Vector(el1.getMatrixInfo().getTranslate().X, el1.getMatrixInfo().getTranslate().Y);
                DrawThread.add_rec_e = new Vector(el1.getMatrixInfo().getTranslate().X + el1.getSize().X, el1.getMatrixInfo().getTranslate().Y + el1.getSize().Y);
            }
        });



        el2 = new Image(BitmapFactory.decodeResource(resources, R.drawable.add_el2), Layouts.ADD_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }

        };

        el2.setMatrixInfo(new MatrixInfo(0.1f, 0.1f, 0, 0));
        el2.setDraw(false);
        el2.setToucheEvent(true);

        el2.setOnClick(new Runnable() {
            @Override
            public void run() {
                DrawThread.mode = "ADD_BAR_REC";
                DrawThread.add_element = el2;
                DrawThread.add_rec_b = new Vector(el2.getMatrixInfo().getTranslate().X, el2.getMatrixInfo().getTranslate().Y);
                DrawThread.add_rec_e = new Vector(el2.getMatrixInfo().getTranslate().X + el2.getSize().X, el2.getMatrixInfo().getTranslate().Y + el2.getSize().Y);
            }
        });
    }

    public static Image createMain2(Resources resources, Image image){
        Image i = new Image(image.getBitmap(), Layouts.MAIN2) {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int pointerCount = event.getPointerCount();
                int action = event.getAction();
                switch(action){
                    case MotionEvent.ACTION_DOWN:

                        DrawThread.main2_image_act = this;

                        this.sei.oldMove = new Vector(event.getX(0), event.getY(0));

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("0");
                        if (DrawThread.main2_image_act == this && DrawThread.main_bar_edit_image_act == DrawThread.move){
                            System.out.println("1");
                            if (pointerCount == 1){
                                System.out.println("2");
                                Vector newMove = new Vector(event.getX(0), event.getY(0));

                                Vector move = Vectors.minus(this.sei.oldMove, newMove);

                                //System.out.println();

                                this.sei.x -= move.X;
                                this.sei.y -= move.Y;

                                this.sei.oldMove = newMove;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:

                }
                this.setMatrixInfo(new MatrixInfo(
                        this.getVectorStartScore().X * this.sei.zoom,
                        this.getVectorStartScore().Y * this.sei.zoom,
                        this.getVectorStartTranslate().X + this.sei.x,
                        this.getVectorStartTranslate().Y + this.sei.y));
            }

        };

        i.setMatrixInfo(new MatrixInfo(
                image.getMatrixInfo().getScale().X, image.getMatrixInfo().getScale().Y,
                image.getMatrixInfo().getTranslate().X, image.getMatrixInfo().getTranslate().Y));
        i.setToucheEvent(true);

        i.setVectorStartTranslate(new Vector(i.getMatrixInfo().getTranslate().X, i.getMatrixInfo().getTranslate().Y));
        i.setVectorStartScore(new Vector(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y));

        return i;
    }

}
