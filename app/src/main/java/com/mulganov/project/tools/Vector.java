package com.mulganov.project.tools;

public class Vector {
    public float X;
    public float Y;

    public Vector(){
    }

    public Vector(float x, float y){
        this.X = x;
        this.Y = y;
    }

    public void minus(){
        if (X != 0) X = -X;
        if (Y != 0) Y = -Y;
    }

    public Vector copy() {
        return new Vector(X, Y);
    }

    public String toString(){
        return "X: " + X + " Y: " + Y;
    }


}
