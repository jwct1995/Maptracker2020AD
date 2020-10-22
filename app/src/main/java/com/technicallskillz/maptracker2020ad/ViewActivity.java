package com.technicallskillz.maptracker2020ad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import com.technicallskillz.maptracker2020ad.Room.Item;
import com.technicallskillz.maptracker2020ad.Room.RoomDatabaseClass;

import java.util.List;

public class ViewActivity extends AppCompatActivity {


    RecyclerView recyclerview;
    RecyclerviewAdapter adapter;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        //appbar /toolbar
        toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Previous Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);


        //recyclerview initilization
        recyclerview=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(linearLayoutManager);


        //get adata from previous record from phone memory and show in list form
        RoomDatabaseClass database = Room.databaseBuilder(this, RoomDatabaseClass.class, "myDatabase").allowMainThreadQueries().build();
        List<Item> list= database.myDao().getData();
        adapter=new RecyclerviewAdapter(list,this);
        recyclerview.setAdapter(adapter);



    }
}