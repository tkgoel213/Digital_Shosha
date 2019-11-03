package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class socialmedia extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private TextView progress1, progress2;
    private RecyclerView recyclerView;

    ArrayList<searchcourses> list = new ArrayList<>();
    private String uid;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialmedia);


        recyclerView = (RecyclerView) findViewById(R.id.recyclercourses);
        progressBar=findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
        uid = user.getUid();
         database = FirebaseDatabase.getInstance();
         ref = FirebaseDatabase.getInstance().getReference("courses").child("brand positioning");
        ref.keepSynced(true);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add(ds.getValue(searchcourses.class));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                searchadapter adp = new searchadapter(getApplicationContext(), list);
                recyclerView.setAdapter(adp);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}