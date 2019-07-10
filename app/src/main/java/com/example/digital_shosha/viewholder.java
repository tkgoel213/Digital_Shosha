package com.example.digital_shosha;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewholder extends RecyclerView.ViewHolder {

   public TextView headingview;
    public ImageView  imageviewr;

    public viewholder(@NonNull View itemView) {
        super(itemView);


        headingview = itemView.findViewById(R.id.posttitle);
         imageviewr = itemView.findViewById(R.id.postimage);


    }
}