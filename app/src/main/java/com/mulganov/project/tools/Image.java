package com.mulganov.project.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.mulganov.project.layout.Layouts;

import java.util.ArrayList;

public abstract class Image {
    public static ArrayList<Image> images = new ArrayList<Image>();

    public SupportEventImage sei = new SupportEventImage();

    private Bitmap bitmap;
    private Vector vectorBegin, vectorEnd, vectorStartTranslate, vectorStartScore;
    private MatrixInfo matrixInfo;
    private Runnable onClick;
    private boolean toucheEvent = false;
    private boolean draw = false;
    private int layout;
    private MotionEvent motionEvent;
    public Paint paint = new Paint();

    public Image(Bitmap bitmap, int layout, String type){
        this.bitmap = bitmap;
        images.add(this);
        this.layout = layout;
        switch (type){
            case "maps":    Layouts.getLayoutMaps(layout).add(this);
                break;
            case "menu":    Layouts.getLayoutMenu(layout).add(this);
                break;
            case "game":    Layouts.getLayoutGame(layout).add(this);
                break;
        }

        draw = true;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setVectorStartTranslate(Vector v){
        vectorStartTranslate = v;
    }

    public Vector getVectorStartTranslate(){
        return vectorStartTranslate;
    }

    public void setVectorStartScore(Vector v){
        vectorStartScore = v;
    }

    public Vector getVectorStartScore(){
        return vectorStartScore;
    }

    public int getLayout(){
        return layout;
    }

    public Vector getVectorBegin(){
        return vectorBegin;
    }

    public Vector getVectorEnd(){
        return vectorEnd;
    }

    public boolean isToucheEvent(){
        return toucheEvent;
    }

    public boolean isDraw(){
        return draw;
    }

    public void setDraw(boolean d){
        this.draw = d;
    }

    public void setToucheEvent(boolean event){
        this.toucheEvent = event;
    }

    public void setMatrixInfo(MatrixInfo matrixInfo){
        this.matrixInfo = matrixInfo;
        vectorBegin = new Vector(matrixInfo.getTranslate().X, matrixInfo.getTranslate().Y);
        vectorEnd = new Vector(matrixInfo.getTranslate().X + getSize().X, matrixInfo.getTranslate().Y + getSize().Y);
        //System.out.println(vectorEnd.toString());
    }

    public MatrixInfo getMatrixInfo(){
        return matrixInfo;
    }

    public Vector getSize(){
        return new Vector(bitmap.getWidth() * matrixInfo.getScale().X , bitmap.getHeight() * matrixInfo.getScale().Y);
    }

    public void draw(Canvas canvas){
        if (draw){
            canvas.drawBitmap(bitmap, matrixInfo.create(), paint);
        }else
            System.out.println("[Image]->[draw()] draw == false ");
    }

    public void onClick(){
        if (toucheEvent){
            onClick.run();
        }else
            System.out.println("[Image]->[onClick()] toucheEvent == false");
    }

    public abstract void onTouchEvent(MotionEvent event);



    public void setOnClick(Runnable runnable){
        this.onClick = runnable;
        this.toucheEvent = true;
    }

    public String getEvent(){
        if (onClick != null) return "onClick";
            else return "else";
    }

}
