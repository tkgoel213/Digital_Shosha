package com.example.digital_shosha;

import android.view.View;
import android.widget.TextView;

import com.example.digital_shosha.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adapter extends RecyclerView.ViewHolder {

    TextView course,progress;

    public adapter(@NonNull View itemView) {
        super(itemView);
        course=(TextView)itemView.findViewById(R.id.coursename);
        progress=(TextView)itemView.findViewById(R.id.progressbar);

    }
}
