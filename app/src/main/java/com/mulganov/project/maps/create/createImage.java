package com.mulganov.project.maps.create;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.widget.TextView;

import com.mulganov.project.layout.Layout;
import com.mulganov.project.maps.create.DrawThread;
import com.mulganov.project.FullscreenActivity;
import com.mulganov.project.R;
import com.mulganov.project.layout.Layouts;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;
import com.mulganov.project.tools.Vector;
import com.mulganov.project.tools.Vectors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        fon = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_fon_1), Layouts.Maps_MAIN, "maps") {
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

                        fon.sei.prevTime = now;
                        fon.sei.oldMove = new Vector(event.getX(0), event.getY(0));


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

                            for (Image i: Layouts.Maps_Layout_MAIN2.get()){
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

                for (Image i : Layouts.Maps_Layout_MAIN2.get()){
                    i.setMatrixInfo(new MatrixInfo(
                            i.getVectorStartScore().X * i.sei.zoom,
                            i.getVectorStartScore().Y * i.sei.zoom,
                            i.getVectorStartTranslate().X + i.sei.x,
                            i.getVectorStartTranslate().Y + i.sei.y));
                }
            }

        };
        fon.setId(R.drawable.maps_fon_1);
        fon.setMatrixInfo(new MatrixInfo(1f, 1f, 0, 0));
        fon.setVectorStartTranslate(new Vector(fon.getMatrixInfo().getTranslate().X, fon.getMatrixInfo().getTranslate().Y));
        fon.setVectorStartScore(new Vector(fon.getMatrixInfo().getScale().X, fon.getMatrixInfo().getScale().Y));

        fon.setDraw(true);
        fon.setToucheEvent(true);



        return fon;
    }

    public void createAddElements(Resources resources){

        Layouts.Maps_Layout_ADD_BAR.reset();

        int panelId = 0;

        String str = "add_el_";

        if (DrawThread.mode.contains("ADD_BAR_FON")) str = "fon_";

        for (int i = 1; true; i++){
            panelId = FullscreenActivity.clas.getResources().getIdentifier("maps_" + str + i,"drawable",FullscreenActivity.clas.getPackageName());

            if (panelId == 0) break;
            System.out.println(panelId);
            final Image el = new Image(BitmapFactory.decodeResource(resources, panelId), Layouts.Maps_ADD_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }

            };

            el.setMatrixInfo(new MatrixInfo(0.1f, 0.1f, 0, 0));
            el.setDraw(false);
            el.setToucheEvent(true);
            el.setOnClick(new Runnable() {
                @Override
                public void run() {
                    DrawThread.mode = "ADD_BAR_REC";
                    DrawThread.add_element = el;
                    DrawThread.add_rec_b = new Vector(el.getMatrixInfo().getTranslate().X, el.getMatrixInfo().getTranslate().Y);
                    DrawThread.add_rec_e = new Vector(el.getMatrixInfo().getTranslate().X + el.getSize().X, el.getMatrixInfo().getTranslate().Y + el.getSize().Y);
                }
            });
            el.setId(panelId);
        }

    }



    public static Image createMain2(Resources resources, Image image){
        Image i = new Image(image.getBitmap(), Layouts.Maps_MAIN2, "maps") {
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

        i.setId(image.getId());
        i.setDraw(true);
        i.setMatrixInfo(new MatrixInfo(
                image.getMatrixInfo().getScale().X, image.getMatrixInfo().getScale().Y,
                image.getMatrixInfo().getTranslate().X, image.getMatrixInfo().getTranslate().Y));
        i.setToucheEvent(false);

        i.setVectorStartTranslate(new Vector(i.getMatrixInfo().getTranslate().X, i.getMatrixInfo().getTranslate().Y));
        i.setVectorStartScore(new Vector(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y));

        return i;
    }

}
