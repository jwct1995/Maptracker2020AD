package com.technicallskillz.maptracker2020ad.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {

    //Querry to store data in Room Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertDt(Item item);


    //    Querry to fetch stored data
    @Query("SELECT  * FROM  Item")
    List<Item> getData();
}
