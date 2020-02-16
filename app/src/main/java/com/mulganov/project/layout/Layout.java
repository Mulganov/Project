package com.mulganov.project.layout;


import com.mulganov.project.tools.Image;

import java.util.ArrayList;

public class Layout {

    private int id;
    private ArrayList<Image> list = new ArrayList<Image>();

    Layout(int id){
        this.id = id;
        Layouts.number++;
    }

    public void add(Image image){
        list.add(image);
    }

    public ArrayList<Image> get(){
        return list;
    }

    public void del(Image image){
        list.remove(image);
    }

}