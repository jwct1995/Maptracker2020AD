package com.technicallskillz.maptracker2020ad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technicallskillz.maptracker2020ad.Room.Item;

import java.util.List;

public class RecyclerviewAdapter  extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

    //all previous data
    List<Item>list;
    Context context;

    public RecyclerviewAdapter(List<Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //where need to show all previous data
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_tems,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //assign all previous data to items
        holder.locationName.setText(list.get(position).getLocationName());
        holder.dateTime.setText(list.get(position).getDate());
        holder.latlong.setText(list.get(position).getLatitude()+", "+list.get(position).getLongitude());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView locationName,latlong,dateTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //create variables for single items for showing data
            locationName=itemView.findViewById(R.id.locationName);
            latlong=itemView.findViewById(R.id.latlong);
            dateTime=itemView.findViewById(R.id.dateTime);
        }
    }
}
