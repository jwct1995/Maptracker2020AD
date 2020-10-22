package com.technicallskillz.maptracker2020ad.Room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class RoomDatabaseClass extends RoomDatabase {
    public abstract MyDao myDao();
}
