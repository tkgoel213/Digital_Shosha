package com.example.digital_shosha;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class course_fragment extends Fragment  {

 String searched;
private EditText searchbar;
private ImageButton searchcourses;



    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.skills, container, false);

        searchbar=(EditText)view.findViewById(R.id.searchbarhai);
        searchcourses=(ImageButton)view.findViewById(R.id.searchstart);

        searchcourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),firebasesearch.class);
              searched=searchbar.getText().toString();
              i.putExtra("value",searched);
                startActivity(i);
                getActivity().finish();
            }
        });


            return view;

        }


    }



