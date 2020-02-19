package com.mulganov.project.maps.create;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.room.Room;

import com.mulganov.project.FullscreenActivity;
import com.mulganov.project.R;
import com.mulganov.project.dp.DB;
import com.mulganov.project.dp.Element;
import com.mulganov.project.layout.Layout;
import com.mulganov.project.layout.Layouts;
import com.mulganov.project.menu.MenuView;
import com.mulganov.project.tools.Image;
import com.mulganov.project.tools.MatrixInfo;
import com.mulganov.project.tools.Vector;
import com.mulganov.project.tools.Vectors;

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

    public static boolean menuB;

    public static String mode = "";

    private Handler mHandler;

    public static Image zoom_in, zoom_out, edit, del, move, Import, export, menu, replaceFon, add;
    public static EditText text;

    public DrawThread(final SurfaceHolder surfaceHolder, final Resources resources){
        this.surfaceHolder = surfaceHolder;

        window = new Vector();

        Layouts.init();


        final createImage create = new createImage();

        create.createFon(resources);

        float zx;
        float zy;

        text = new EditText(FullscreenActivity.clas);
        ViewGroup.LayoutParams lp =new ViewGroup.LayoutParams((int)FullscreenActivity.window.X - 100,300);
        text.setText("maps_default");
        text.setX(100f);
        text.setY(FullscreenActivity.window.Y / 2 + 200);
        text.setTextSize(36);
        FullscreenActivity.clas.addContentView(text, lp);
        FullscreenActivity.clas.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.INVISIBLE);
            }
        });


        menu = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_menu), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {

            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_menu).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_menu).getHeight() / 2.75f / 36;
        menu.setMatrixInfo(new MatrixInfo(zx, zy, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_menu).getWidth() / 2.75f * zx, 0));
        menu.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (!menuB){
                    for (int id = Layouts.maps_number - 1; id >= 0; id--){
                        for (Image i: Layouts.getLayoutMaps(id).get()){
                            i.setDraw(false);
                        }
                    }

                    DrawThread.menuB = true;
                    Import.setDraw(true);
                    export.setDraw(true);
                    menu.setDraw(true);

                    FullscreenActivity.clas.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setVisibility(View.VISIBLE);
                        }
                    });

                }else{
                    for (int id = Layouts.maps_number - 1; id >= 0; id--){
                        for (Image i: Layouts.getLayoutMaps(id).get()){
                            i.setDraw(true);
                        }
                    }

                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        i.setDraw(false);
                    }

                    DrawThread.menuB = false;
                    Import.setDraw(false);
                    export.setDraw(false);

                    FullscreenActivity.clas.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });


        add = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_add), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {

            }
        };
        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_add).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_add).getHeight() / 2.75f / 36;
        add.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_add).getWidth() / 2.75f * zx / 100, 100));
        add.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (mode.contains("ADD_BAR")){
                    replaceFon.setToucheEvent(true);
                    replaceFon.paint.setAlpha(255);
                    mode = "";
                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        i.setDraw(false);
                    }

                    for (Image i: Layouts.Maps_Layout_MAIN.get()){
                        i.setToucheEvent(true);
                    }
                    for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                        i.setToucheEvent(true);
                    }

                    if (add_element == null){
                        return;
                    }

                    createImage.createMain2(resources, add_element);


                }else{
                    replaceFon.setToucheEvent(false);
                    replaceFon.paint.setAlpha(40);
                    mode = "ADD_BAR";

                    create.createAddElements(resources);

                    float x = 0, y = FullscreenActivity.window.Y / 2;

                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        i.setMatrixInfo(new MatrixInfo(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y, x, y));
                        x += i.getSize().X ;
                        i.setDraw(true);
                    }

                    for (Image i: Layouts.Maps_Layout_MAIN.get()){
                        i.setToucheEvent(false);
                    }
                    for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                        i.setToucheEvent(false);
                    }
                }

            }
        });

        edit = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_edit), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int action = event.getAction();
                switch(action){
                    case MotionEvent.ACTION_DOWN:

                        float ex = event.getX();
                        float ey = event.getY();

                            for (Image i: Layouts.Maps_Layout_MAIN2.get()){
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
        edit.setDraw(true);
        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_edit).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_edit).getHeight() / 2.75f / 36;
        edit.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_edit).getWidth() / 2.75f * zx / 100, 220));
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

                    for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                        i.setToucheEvent(true);
                    }
                }

            }
        });


        move = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_move), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        move.setDraw(true);
        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_move).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_move).getHeight() / 2.75f / 36;
        move.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_move).getWidth() / 2.75f * zx / 100, 330));
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

        del = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_del), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        del.setDraw(true);
        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_del).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_del).getHeight() / 2.75f / 36;
        del.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_main_bar_del).getWidth() / 2.75f * zx / 100, 480));
        del.setOnClick(new Runnable() {
            @Override
            public void run() {
                Layouts.Maps_Layout_MAIN2.del(main2_image_act);
                del.paint.setAlpha(40);
                main2_image_act = null;

                createImage.fon.setToucheEvent(true);
            }
        });
        del.paint.setAlpha(40);

        replaceFon = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_set_fon), Layouts.Maps_MAIN_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        replaceFon.setDraw(true);
        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_set_fon).getWidth() / 2.75f / 36;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_set_fon).getHeight() / 2.75f / 36;
        replaceFon.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X - BitmapFactory.decodeResource(resources, R.drawable.maps_set_fon).getWidth() / 2.75f * zx / 100, 600));
        replaceFon.setOnClick(new Runnable() {
            @Override
            public void run() {
                System.out.println(mode);
                if (mode.contains("ADD_BAR")){
                    add.setToucheEvent(true);
                    add.paint.setAlpha(255);
                    mode = "";
                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        i.setDraw(false);
                    }

                    for (Image i: Layouts.Maps_Layout_MAIN.get()){
                        i.setToucheEvent(true);
                    }
                    for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                        i.setToucheEvent(true);
                    }

                    if (add_element == null){
                        return;
                    }

                    createImage.fon.setBitmap(add_element.getBitmap());
                    createImage.fon.setId(add_element.getId());

                }else{
                    add.setToucheEvent(false);
                    add.paint.setAlpha(40);
                    mode = "ADD_BAR_FON";

                    create.createAddElements(resources);

                    float x = 0, y = FullscreenActivity.window.Y / 2;

                    for (Image i: Layouts.Maps_Layout_ADD_BAR.get()){
                        i.setMatrixInfo(new MatrixInfo(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y, x, y));
                        x += i.getSize().X ;
                        i.setDraw(true);
                    }

                    for (Image i: Layouts.Maps_Layout_MAIN.get()){
                        i.setToucheEvent(false);
                    }
                    for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                        i.setToucheEvent(false);
                    }
                }
            }
        });


        Import = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_menu_import), Layouts.Maps_MENU_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_menu_import).getWidth() / 2.75f / 100;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_menu_import).getHeight() / 2.75f / 100;
        Import.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X / 2 - BitmapFactory.decodeResource(resources, R.drawable.maps_menu_import).getWidth() / 2.75f * zx / 100 / 2 - 400, 400));
        Import.setOnClick(new Runnable() {
            @Override
            public void run() {

                Layouts.Maps_Layout_MAIN.reset();
                Layouts.Maps_Layout_MAIN2.reset();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DB db = Room.databaseBuilder(FullscreenActivity.clas.getApplicationContext(),
                                DB.class, text.getText() + "").build();

                        for (Element el: db.getElementDoa().getAllElement()){
                            String type = el.getType();

                            int id = el.getId();

                            float sx = el.getSx();
                            float sy = el.getSy();

                            float tx = el.getTx();
                            float ty = el.getTy();

                            if (type.equalsIgnoreCase("fon")){
                                createImage.fon.setBitmap(BitmapFactory.decodeResource(resources, id));
                                createImage.fon.setId(id);

                                createImage.fon.setMatrixInfo(new MatrixInfo( sx, sy, tx, ty ));
                            }else{
                                Image i = new Image(BitmapFactory.decodeResource(resources, id), Layouts.Maps_MAIN2, "maps") {
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

                                i.setId(id);
                                i.setDraw(true);

                                i.setToucheEvent(false);

                                i.setMatrixInfo(new MatrixInfo( sx, sy, tx, ty ));

                                i.setVectorStartTranslate(new Vector(i.getMatrixInfo().getTranslate().X, i.getMatrixInfo().getTranslate().Y));
                                i.setVectorStartScore(new Vector(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y));

                            }
                        }

                        db.close();
                    }
                }).start();

                menu.onClick();
            }
        });
        Import.setDraw(false);

        export = new Image(BitmapFactory.decodeResource(resources, R.drawable.maps_menu_export), Layouts.Maps_MENU_BAR, "maps") {
            @Override
            public void onTouchEvent(MotionEvent event) {
            }
        };

        zx = BitmapFactory.decodeResource(resources, R.drawable.maps_menu_export).getWidth() / 2.75f / 5;
        zy = BitmapFactory.decodeResource(resources, R.drawable.maps_menu_export).getHeight() / 2.75f / 5;
        export.setMatrixInfo(new MatrixInfo(zx/100, zy/100, FullscreenActivity.window.X / 2 - BitmapFactory.decodeResource(resources, R.drawable.maps_menu_export).getWidth() / 2.75f * zx / 100 / 2 + 400, 320));
        export.setOnClick(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DB db = Room.databaseBuilder(FullscreenActivity.clas.getApplicationContext(),
                                DB.class, text.getText() + "").build();

                        for (Element el: db.getElementDoa().getAllElement()){
                            db.getElementDoa().delete(el);
                        }

                        Element fon = new Element();

                        fon.setId(createImage.fon.getId());
                        fon.setType("fon");
                        MatrixInfo mi = createImage.fon.getMatrixInfo();
                        fon.setSx(mi.getScale().X);
                        fon.setSy(mi.getScale().Y);
                        fon.setTx(mi.getTranslate().X);
                        fon.setTy(mi.getTranslate().Y);

                        fon.setKey(0);

                        db.getElementDoa().insertAll(fon);

                        int ii = 1;
                        for (Image i: Layouts.Maps_Layout_MAIN2.get()){
                            Element el = new Element();

                            el.setId(createImage.fon.getId());
                            el.setType("main2");
                            el.setKey(ii);
                            MatrixInfo m = createImage.fon.getMatrixInfo();
                            el.setSx(m.getScale().X);
                            el.setSy(m.getScale().Y);
                            el.setTx(m.getTranslate().X);
                            el.setTy(m.getTranslate().Y);

                            db.getElementDoa().insertAll(el);
                            ii++;
                        }


                        db.close();
                    }

                }).start();

                View v = new MenuView(FullscreenActivity.clas);
                FullscreenActivity.clas.mContentView = v;
                FullscreenActivity.clas.setContentView(v);
                FullscreenActivity.clas.hide();
            }
        });
        export.setDraw(false);

        // сохраняем текущее время
        prevTime = System.currentTimeMillis();

        menuB = false;


        new Thread(new Runnable() {
            @Override
            public void run() {
                DB db = Room.databaseBuilder(FullscreenActivity.clas.getApplicationContext(),
                        DB.class, "maps_default").build();

                for (Element el: db.getElementDoa().getAllElement()){
                    String type = el.getType();

                    int id = el.getId();

                    float sx = el.getSx();
                    float sy = el.getSy();

                    float tx = el.getTx();
                    float ty = el.getTy();
                    
                    if (type.equalsIgnoreCase("fon")){
                        createImage.fon.setBitmap(BitmapFactory.decodeResource(resources, id));
                        createImage.fon.setId(id);

                        createImage.fon.setMatrixInfo(new MatrixInfo(sx, sy, tx, ty));
                        createImage.fon.setVectorStartTranslate(new Vector(createImage.fon.getMatrixInfo().getTranslate().X, createImage.fon.getMatrixInfo().getTranslate().Y));
                        createImage.fon.setVectorStartScore(new Vector(createImage.fon.getMatrixInfo().getScale().X, createImage.fon.getMatrixInfo().getScale().Y));

                    }else{
                        Image i = new Image(BitmapFactory.decodeResource(resources, id), Layouts.Maps_MAIN2, "maps") {
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

                        i.setId(id);
                        i.setDraw(true);

                        i.setToucheEvent(false);

                        i.setMatrixInfo(new MatrixInfo( sx, sy, tx, ty ));

                        i.setVectorStartTranslate(new Vector(i.getMatrixInfo().getTranslate().X, i.getMatrixInfo().getTranslate().Y));
                        i.setVectorStartScore(new Vector(i.getMatrixInfo().getScale().X, i.getMatrixInfo().getScale().Y));

                    }
                }

                db.close();
            }
        }).start();


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

                        if (menuB){
                            Import.draw(canvas);
                            export.draw(canvas);
                            menu.draw(canvas);


                        }else{
                            for (int id = 0; id < Layouts.maps_number; id++){
                                for (Image i: Layouts.getLayoutMaps(id).get()){
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
