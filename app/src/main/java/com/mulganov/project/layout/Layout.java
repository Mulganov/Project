package com.mulganov.project.layout;


import com.mulganov.project.tools.Image;

import java.util.ArrayList;

public class Layout {

    private int id;
    private ArrayList<Image> list = new ArrayList<Image>();

    Layout(int id, String type){
        this.id = id;
        switch (type){
            case "maps":    Layouts.maps_number++;
                break;
            case "menu":    Layouts.menu_number++;
                break;
            case "game":    Layouts.game_number++;
                break;
        }
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