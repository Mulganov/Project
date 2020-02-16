package com.mulganov.project;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Button;

import com.mulganov.project.layout.Layouts;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;
import com.mulganov.project.tools.Vector;
import com.mulganov.project.tools.Vectors;
import com.mulganov.project.tools.createImage;

public class DrawThread extends Thread{
    private boolean runFlag = false;
    public static SurfaceHolder surfaceHolder;
    private long prevTime;
    private Vector window;

    public static Vector add_rec_b = new Vector(), add_rec_e = new Vector();
    public static Vector main_bar_act_b = new Vector(), main_bar_act_e = new Vector();
    public static Vector main2_act_b = new Vector(), main2_act_e = new Vector();
    public static Vector main_bar_edit_act_b = new Vector(), main_bar_edit_act_e = new Vector();
    public static Image main_bar_image_act;
    public static Image main_bar_edit_image_act;
    public static Image main2_image_act;
    public static boolean calibration;
    public static Image add_element;
    public static Vector main_vector_event;

    public static boolean menu;

    public static String mode = "";

    public boolean copy;

    private Handler mHandler;

    public static Image zoom_in, zoom_out, edit, del, move, Import, export;

    public DrawThread(SurfaceHolder surfaceHolder, final Resources resources){
        this.surfaceHolder = surfaceHolder;

        window = new Vector();

        Layouts.init();

        final createImage create = new createImage();

        create.createFon(resources);
        create.createAddElements(resources);


        float zx;
        float zy;


        Image menu = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_menu), Layouts.MAIN_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {

            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_menu).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_menu).getHeight() / 2.75f / 36;
        menu.setMatrixInfo(new MatrixInfo(zx, zy, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_menu).getWidth() / 2.75f * zx, 0));
        menu.setOnClick(new Runnable() {
            @Override
            public void run() {
                for (int id = Layouts.number - 1; id >= 0; id--){
                    for (Image i: Layouts.getLayout(id).get()){
                       i.setDraw(false);
                    }
                }

                DrawThread.menu = true;
                Import.setDraw(true);
                export.setDraw(true);
            }
        });

        Image add = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_add), Layouts.MAIN_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {

            }
        };
        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_add).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_add).getHeight() / 2.75f / 36;
        add.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_add).getWidth() / 2.75f * zx / 100, 100));
        add.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (mode.contains("ADD_BAR")){
                    mode = "";
                    for (Image i: Layouts.Layout_ADD_BAR.get()){
                        i.setDraw(false);
                    }

                    for (Image i: Layouts.Layout_MAIN.get()){
                        i.setToucheEvent(true);
                    }
                    for (Image i: Layouts.Layout_MAIN2.get()){
                        i.setToucheEvent(true);
                    }

                    if (add_element == null){
                        return;
                    }

                    createImage.createMain2(resources, add_element);


                }else{
                    mode = "ADD_BAR";

                    float x = 0, y = FullscreenActivity.window.Y / 2;

                    for (Image i: Layouts.Layout_ADD_BAR.get()){
                        i.setMatrixInfo(new MatrixInfo(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y, x, y));
                        x += i.getSize().X ;
                        i.setDraw(true);
                    }

                    for (Image i: Layouts.Layout_MAIN.get()){
                        i.setToucheEvent(false);
                    }
                    for (Image i: Layouts.Layout_MAIN2.get()){
                        i.setToucheEvent(false);
                    }
                }

            }
        });

//        zoom_in = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_in), Layouts.MAIN_BAR) {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//
//                if (zoom_in.sei.zoom == 1){
//                    zoom_in.sei.zoom = 2;
//
//                    System.out.println("plus zoom = 2");
//
//                    createImage.fon.sei.zoom = zoom_in.sei.zoom;
//
//                    createImage.calibration(event.getX() * 2 , event.getY() * 2);
//                }
//
//                if (zoom_in.sei.zoom == 2){
//                    DrawThread.main_bar_image_act = null;
//                    zoom_in.setToucheEvent(false);
//                    zoom_in.paint.setAlpha(40);
//                }
//            }
//        };
//
//        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_in).getWidth() / 2.75f / 36;
//        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_in).getHeight() / 2.75f / 36;
//        zoom_in.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_in).getWidth() / 2.75f * zx / 100, 220));
//        zoom_in.setOnClick(new Runnable() {
//            @Override
//            public void run() {
//                if (zoom_in.sei.zoom == 0.5f){
//                    zoom_in.setToucheEvent(false);
//                    zoom_in.paint.setAlpha(40);
//                }else
//                if (main_bar_image_act == zoom_in){
//                    main_bar_image_act = null;
//                }else{
//                    main_bar_image_act = zoom_in;
//                    main_bar_act_b = new Vector(zoom_in.getMatrixInfo().getTranslate().X, zoom_in.getMatrixInfo().getTranslate().Y);
//                    main_bar_act_e = new Vector(zoom_in.getMatrixInfo().getTranslate().X + zoom_in.getSize().X, zoom_in.getMatrixInfo().getTranslate().Y + zoom_in.getSize().Y);
//                }
//
//            }
//        });
//
//        zoom_out = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_out), Layouts.MAIN_BAR) {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//
//                if (zoom_in.sei.zoom == 1){
//                    zoom_in.sei.zoom = 0.5f;
//
//                    System.out.println("minus zoom = 0.5");
//
//                    createImage.fon.sei.zoom = zoom_in.sei.zoom;
//
//                    createImage.calibration(event.getX() * 0.50f , event.getY() * 0.5f);
//                }
//
//                if (zoom_in.sei.zoom == 0.5f){
//                    DrawThread.main_bar_image_act = null;
//                    zoom_out.setToucheEvent(false);
//                    zoom_out.paint.setAlpha(40);
//                }
//            }
//        };
//        zoom_out.sei.zoom = 0;
//        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_out).getWidth() / 2.75f / 36;
//        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_out).getHeight() / 2.75f / 36;
//        zoom_out.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_zoom_out).getWidth() / 2.75f * zx / 100, 320));
//        zoom_out.setOnClick(new Runnable() {
//            @Override
//            public void run() {
//
//                if (main_bar_image_act == zoom_out){
//                    main_bar_image_act = null;
//                }else {
//                    main_bar_image_act = zoom_out;
//                    main_bar_act_b = new Vector(zoom_out.getMatrixInfo().getTranslate().X, zoom_out.getMatrixInfo().getTranslate().Y);
//                    main_bar_act_e = new Vector(zoom_out.getMatrixInfo().getTranslate().X + zoom_out.getSize().X, zoom_out.getMatrixInfo().getTranslate().Y + zoom_out.getSize().Y);
//                }
//            }
//        });

        edit = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_edit), Layouts.MAIN_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int action = event.getAction();
                switch(action){
                    case MotionEvent.ACTION_DOWN:

                        float ex = event.getX();
                        float ey = event.getY();

                            for (Image i: Layouts.Layout_MAIN2.get()){
                                if (i.isToucheEvent() && i.isDraw()){
                                    if ( i.getVectorBegin().X <= ex && ex <= i.getVectorEnd().X ){
                                        if ( i.getVectorBegin().Y <= ey && ey <= i.getVectorEnd().Y ){
                                                i.onTouchEvent(event);
                                        }
                                    }
                                }
                            }


                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:


                }
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_edit).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_edit).getHeight() / 2.75f / 36;
        edit.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_edit).getWidth() / 2.75f * zx / 100, 220));
        edit.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (main_bar_image_act == edit){
                    main_bar_image_act = null;

                    if (main2_image_act != null){
                        main2_image_act.setToucheEvent(false);
                        main2_image_act = null;
                    }

                    createImage.fon.setToucheEvent(true);

                }else{
                    main_bar_image_act = edit;
                    main_bar_act_b = new Vector(edit.getMatrixInfo().getTranslate().X, edit.getMatrixInfo().getTranslate().Y);
                    main_bar_act_e = new Vector(edit.getMatrixInfo().getTranslate().X + edit.getSize().X, edit.getMatrixInfo().getTranslate().Y + edit.getSize().Y);

                    for (Image i: Layouts.Layout_MAIN2.get()){
                        i.setToucheEvent(true);
                    }
                }

            }
        });


        move = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_move), Layouts.MAIN_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_move).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_move).getHeight() / 2.75f / 36;
        move.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_move).getWidth() / 2.75f * zx / 100, 330));
        move.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (main_bar_edit_image_act != null){
                    main_bar_edit_image_act = null;

                    main2_image_act.setToucheEvent(false);
                    main2_image_act = null;
                    createImage.fon.setToucheEvent(true);
                }else{
                    main_bar_edit_image_act = move;
                    createImage.fon.setToucheEvent(false);
                }

            }
        });
        move.paint.setAlpha(40);

        del = new Image(BitmapFactory.decodeResource(resources, R.drawable.main_bar_del), Layouts.MAIN_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.main_bar_del).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.main_bar_del).getHeight() / 2.75f / 36;
        del.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.main_bar_del).getWidth() / 2.75f * zx / 100, 480));
        del.setOnClick(new Runnable() {
            @Override
            public void run() {
                Layouts.Layout_MAIN2.del(main2_image_act);
                del.paint.setAlpha(40);
                main2_image_act = null;

                createImage.fon.setToucheEvent(true);
            }
        });
        del.paint.setAlpha(40);

        Import = new Image(BitmapFactory.decodeResource(resources, R.drawable.menu_import), Layouts.MENU_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.menu_import).getWidth() / 2.75f / 100;
        zy = BitmapFactory.decodeResource(resources, R.drawable.menu_import).getHeight() / 2.75f / 100;
        Import.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X / 2 - BitmapFactory.decodeResource(resources, R.drawable.menu_import).getWidth() / 2.75f * zx / 100 / 2 - 400, 400));
        Import.setOnClick(new Runnable() {
            @Override
            public void run() {

            }
        });
        Import.setDraw(false);

        export = new Image(BitmapFactory.decodeResource(resources, R.drawable.menu_export), Layouts.MENU_BAR) {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.menu_export).getWidth() / 2.75f / 5;
        zy = BitmapFactory.decodeResource(resources, R.drawable.menu_export).getHeight() / 2.75f / 5;
        export.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X / 2 - BitmapFactory.decodeResource(resources, R.drawable.menu_export).getWidth() / 2.75f * zx / 100 / 2 + 400, 320));
        export.setOnClick(new Runnable() {
            @Override
            public void run() {
                String text = "";

                text += "[Fon] id: " + "[id]\n";
                text += "[Fon] sx: " + createImage.fon.getMatrixInfo().getScale().X + "\n";
                text += "[Fon] sy: " + createImage.fon.getMatrixInfo().getScale().Y + "\n";
                text += "[Fon] tx: " + createImage.fon.getMatrixInfo().getTranslate().X + "\n";
                text += "[Fon] ty: " + createImage.fon.getMatrixInfo().getTranslate().Y + "\n";


                text += "[0] id: " + "[id]\n";
                text += "[1] id: " + "[id]\n";

                for (Image i: Layouts.Layout_MAIN2.get()){
                    String n = "";
                    if (i.getBitmap().equals(createImage.el1.getBitmap())){
                        n = "0";
                    }else
                        n = "1";

                    text += "["+n+"] sx: " + createImage.fon.getMatrixInfo().getScale().X + "\n";
                    text += "["+n+"] sy: " + createImage.fon.getMatrixInfo().getScale().Y + "\n";
                    text += "["+n+"] tx: " + createImage.fon.getMatrixInfo().getTranslate().X + "\n";
                    text += "["+n+"] ty: " + createImage.fon.getMatrixInfo().getTranslate().Y + "\n";
                }

                ClipboardManager clipboard = (ClipboardManager) FullscreenActivity.clas.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", text);
                clipboard.setPrimaryClip(clip);

                copy = true;

            }
        });
        export.setDraw(false);

        // сохраняем текущее время
        prevTime = System.currentTimeMillis();

    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            // получаем текущее время и вычисляем разницу с предыдущим
            // сохраненным моментом времени
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            //if (elapsedTime > 30){
                // если прошло больше 30 миллисекунд - сохраним текущее время
                prevTime = now;

                canvas = null;
                try {
                    // получаем объект Canvas и выполняем отрисовку
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null) return;
                    synchronized (surfaceHolder) {
                        canvas.drawColor(Color.WHITE);

                        Paint p = new Paint();

                        if (menu){
                            Import.draw(canvas);
                            export.draw(canvas);

                            if (copy){
                                p.setTextSize(46);
                                canvas.drawText("Конфиг успешно скопирован", 800, 800, p);
                            }

                        }else{
                            for (int id = 0; id < Layouts.number; id++){
                                for (Image i: Layouts.getLayout(id).get()){
                                    if (i.isDraw()){
                                        i.draw(canvas);
                                    }
                                }
                            }

                            if (mode.contains("ADD_BAR")){
                                Vector window = FullscreenActivity.window;
                                p.setColor(Color.GRAY);
                                canvas.drawRect(0, window.Y - 200, window.X-200, window.Y, p);
                                p.setColor(Color.RED);
                                canvas.drawRect(window.X-200, window.Y - 200, window.X+200, window.Y, p);
                                if (mode.equalsIgnoreCase("ADD_BAR_REC")){
                                    p.setColor(Color.BLACK);
                                    p.setStrokeWidth(20);

                                    canvas.drawLine(add_rec_b.X, add_rec_b.Y, add_rec_e.X, add_rec_b.Y, p);
                                    canvas.drawLine(add_rec_b.X, add_rec_e.Y, add_rec_e.X, add_rec_e.Y, p);

                                    canvas.drawLine(add_rec_b.X, add_rec_b.Y-10, add_rec_b.X, add_rec_e.Y+10, p);
                                    canvas.drawLine(add_rec_e.X, add_rec_b.Y-10, add_rec_e.X, add_rec_e.Y+10, p);
                                }
                            }

                            if (main_bar_image_act != null){
                                p.setColor(Color.BLACK);
                                p.setStrokeWidth(10);

                                canvas.drawLine(main_bar_act_b.X-10, main_bar_act_b.Y, main_bar_act_e.X+10, main_bar_act_b.Y, p);
                                canvas.drawLine(main_bar_act_b.X-10, main_bar_act_e.Y, main_bar_act_e.X+10, main_bar_act_e.Y, p);

                                canvas.drawLine(main_bar_act_b.X, main_bar_act_b.Y-10, main_bar_act_b.X, main_bar_act_e.Y+10, p);
                                canvas.drawLine(main_bar_act_e.X, main_bar_act_b.Y-10, main_bar_act_e.X, main_bar_act_e.Y+10, p);
                            }

                            if ( main_bar_image_act != null && main2_image_act != null){
                                p.setColor(Color.BLACK);
                                p.setStrokeWidth(10);

                                DrawThread.main2_act_b = new Vector(main2_image_act.getMatrixInfo().getTranslate().X, main2_image_act.getMatrixInfo().getTranslate().Y);
                                DrawThread.main2_act_e = new Vector(main2_image_act.getMatrixInfo().getTranslate().X + main2_image_act.getSize().X, main2_image_act.getMatrixInfo().getTranslate().Y + main2_image_act.getSize().Y);

                                canvas.drawLine(main2_act_b.X-10, main2_act_b.Y, main2_act_e.X+10, main2_act_b.Y, p);
                                canvas.drawLine(main2_act_b.X-10, main2_act_e.Y, main2_act_e.X+10, main2_act_e.Y, p);

                                canvas.drawLine(main2_act_b.X, main2_act_b.Y-10, main2_act_b.X, main2_act_e.Y+10, p);
                                canvas.drawLine(main2_act_e.X, main2_act_b.Y-10, main2_act_e.X, main2_act_e.Y+10, p);
                            }

                            if ( main_bar_image_act != null && main2_image_act != null && main_bar_edit_image_act != null){
                                p.setColor(Color.BLACK);
                                p.setStrokeWidth(10);

                                DrawThread.main_bar_edit_act_b = new Vector(main_bar_edit_image_act.getMatrixInfo().getTranslate().X, main_bar_edit_image_act.getMatrixInfo().getTranslate().Y);
                                DrawThread.main_bar_edit_act_e = new Vector(main_bar_edit_image_act.getMatrixInfo().getTranslate().X + main_bar_edit_image_act.getSize().X, main_bar_edit_image_act.getMatrixInfo().getTranslate().Y + main_bar_edit_image_act.getSize().Y);

                                canvas.drawLine(main_bar_edit_act_b.X-10, main_bar_edit_act_b.Y, main_bar_edit_act_e.X+10, main_bar_edit_act_b.Y, p);
                                canvas.drawLine(main_bar_edit_act_b.X-10, main_bar_edit_act_e.Y, main_bar_edit_act_e.X+10, main_bar_edit_act_e.Y, p);

                                canvas.drawLine(main_bar_edit_act_b.X, main_bar_edit_act_b.Y-10, main_bar_edit_act_b.X, main_bar_edit_act_e.Y+10, p);
                                canvas.drawLine(main_bar_edit_act_e.X, main_bar_edit_act_b.Y-10, main_bar_edit_act_e.X, main_bar_edit_act_e.Y+10, p);
                            }

                            if (main2_image_act != null){
                                DrawThread.del.paint.setAlpha(255);

                                DrawThread.move.paint.setAlpha(255);
                            }else{
                                DrawThread.del.paint.setAlpha(40);

                                DrawThread.move.paint.setAlpha(40);

                                main_bar_edit_image_act = null;

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
