package com.mulganov.project.mysor;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.mulganov.project.DrawThread;
import com.mulganov.project.R;
import com.mulganov.project.layout.Layouts;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;
import com.mulganov.project.tools.Vector;
import com.mulganov.project.tools.Vectors;

public class createImage {

    public static Image fon;

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
                            fon.sei.x = 0;
                            fon.sei.y = 0;
                            fon.sei.zoom = 1;
                            for (Image i: Layouts.Layout_MAIN2.get()){
                                i.sei.x = 0;
                                i.sei.y = 0;
                                i.sei.zoom = 1;
                            }
                        }else{
                            fon.sei.prevTime = now;
                            fon.sei.old1 = new Vector(event.getX(0), event.getY(0));
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
                        now = System.currentTimeMillis();
                        if (pointerCount == 1 && fon.sei.move && now - fon.sei.prevTime > 150){
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
                        }else
                        if (pointerCount == 2){
                            fon.sei.move = false;
                            if (fon.sei.old2 == null) break;

                            Vector v1 = new Vector(event.getX(0), event.getY(0));
                            Vector v2 = new Vector(event.getX(1), event.getY(1));

                            Vector right = null, oldRight = null;
                            Vector left =null, oldLeft = null;

                            if (v1.X <= v2.X){
                                left = v1;
                                oldLeft = fon.sei.old1;
                                right = v2;
                                oldRight = fon.sei.old2;
                            }else{
                                left = v2;
                                oldLeft = fon.sei.old2;
                                right = v1;
                                oldRight = fon.sei.old1;
                            }

                            Vector r1 = Vectors.minus(oldLeft, left);
                            Vector r2 = Vectors.minus(oldRight, right);

                            if ((r1.X > 0 && r2.X < 0)){
                                fon.sei.zoom += Math.abs(r1.X + r2.X) * 0.005f;
                                for (Image i: Layouts.Layout_MAIN2.get()){
                                    i.sei.zoom += Math.abs(r1.X + r2.X) * 0.005f;
                                }
                            }else
                            if ((r2.X > 0 && r1.X < 0)){
                                fon.sei.zoom -= Math.abs(r1.X + r2.X) * 0.005f;
                                for (Image i: Layouts.Layout_MAIN2.get()){
                                    i.sei.zoom -= Math.abs(r1.X + r2.X) * 0.005f;
                                }
                            }

                            if (fon.sei.zoom > 3.0f)   {
                                fon.sei.zoom = 3;

                                for (Image i: Layouts.Layout_MAIN2.get()){
                                    i.sei.zoom = 3;
                                }

                            }
                            if (fon.sei.zoom < 0.5f)   {
                                //fon.sei.zoom = 0.5f;
                                for (Image i: Layouts.Layout_MAIN2.get()){
                                    //i.sei.zoom = 0.5f;
                                }
                            }


                            fon.sei.old1 = v1;
                            fon.sei. old2 = v2;


                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:
                        if (event.getActionMasked() == 5){
                            fon.sei.move = false;
                            fon.sei.old2 = new Vector(event.getX(1), event.getY(1));
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


}
