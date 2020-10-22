package com.technicallskillz.maptracker2020ad.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {

    //Querry to store song in Room Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertSong(Item item);


    //    Querry to fetch stored song
    @Query("SELECT  * FROM  Item")
    List<Item> getData();
}
