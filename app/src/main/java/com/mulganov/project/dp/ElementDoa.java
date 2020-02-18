package com.mulganov.project.dp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ElementDoa {

    // Добавление Element в бд
    @Insert
    void insertAll(Element... elements);

    // Удаление Element из бд
    @Delete
    void delete(Element... elements);

    // Получение всех Element из бд
    @Query("SELECT * FROM element")
    List<Element> getAllElement();

}