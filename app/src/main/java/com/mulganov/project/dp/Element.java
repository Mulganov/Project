package com.mulganov.project.dp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Element {
    @PrimaryKey()
    public int key;

    public int id;

    public float sx, sy;
    public float tx, ty;

    public String type;


    public void setKey(int key){
        this.key = key;
    }

    public int getKey(){
        return key;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public float getSx(){
        return sx;
    }

    public void setSx(float sx){
        this.sx = sx;
    }

    public float getSy(){
        return sy;
    }

    public void setSy(float sy){
        this.sy = sy;
    }


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public float getTx(){
        return tx;
    }

    public void setTx(float tx){
        this.tx = tx;
    }

    public float getTy(){
        return ty;
    }

    public void setTy(float ty){
        this.ty = ty;
    }
}
