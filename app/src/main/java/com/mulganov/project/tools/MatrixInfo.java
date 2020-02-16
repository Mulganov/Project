package com.mulganov.project.tools;

import android.graphics.Matrix;

public class MatrixInfo {

    private float sx, sy;
    private float tx, ty;

    public MatrixInfo(float sx, float sy, float tx, float ty){
        this.sx = sx;
        this.sy = sy;

        this.tx = tx;
        this.ty = ty;
    }

    public Matrix create(){
        Matrix m = new Matrix();
        m.postScale(sx, sy);
        m.postTranslate(tx, ty);
        return m;
    }

    public Vector getScale(){
        return new Vector(sx, sy);
    }

    public Vector getTranslate(){
        return new Vector(tx, ty);
    }

    public void setScale(Vector vector){
        sx = vector.X;
        sy = vector.Y;
    }

    public void setTranslate(Vector v){
        tx = v.X;
        ty = v.Y;
    }
}
