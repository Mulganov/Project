package com.mulganov.project.tools;

public class Vectors {

    public static Vector UP = new Vector(0, -1);
    public static Vector DOWN = new Vector(0, 1);
    public static Vector LEFT = new Vector(-1, 0);
    public static Vector RIGHT = new Vector(1, 0);

    public static Vector minus(Vector v1, Vector v2){
        Vector v = new Vector();
        v.X = v1.X - v2.X;
        v.Y = v1.Y - v2.Y;
        return v;
    }

    public static Vector ser(Vector v1, Vector v2){
        return new Vector( (v1.X + v2.X) / 2, (v1.Y + v2.Y) / 2 );
    }

    public static Vector multiply(Vector v1, Vector v2){
        return new Vector(v1.X * v2.X, v1.Y * v2.Y);
    }

    public static Vector plus(Vector v1, Vector v2) {
        return new Vector(v1.X + v2.X, v1.Y + v2.Y);
    }
}
