package com.mulganov.project.tools;

public class SupportEventImage {
    public float zoom = 1;
    public float x = 0;
    public float y = 0;
    public Vector last = new Vector();
    public long prevTime = System.currentTimeMillis();

    public Vector old1;
    public Vector old2;
    public Vector oldMove;
    public boolean move;
}
