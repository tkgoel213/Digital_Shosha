package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class summary extends AppCompatActivity {


    String keys;
    String course;
    DatabaseReference reference;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> listadapter;
    gettersummary get;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        listView=(ListView)findViewById(R.id.listsummary);
        get=new gettersummary();
        Intent intent = getIntent();
        keys = intent.getStringExtra("key");
        course = intent.getStringExtra("course");
        list=new ArrayList<>();
        listadapter=new ArrayAdapter<String>(this,R.layout.summarylistview,R.id.summarytext,list);
        reference= FirebaseDatabase.getInstance().getReference("courses").child(course).child(keys).child("summary");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                   get=(ds.getValue(gettersummary.class));
                   list.add(get.content.toString());
                    Toast.makeText(summary.this, ""+ds.getKey(), Toast.LENGTH_SHORT).show();
                }
                   listView.setAdapter(listadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
