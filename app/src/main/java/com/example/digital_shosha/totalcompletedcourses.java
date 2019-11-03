package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class totalcompletedcourses extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    RecyclerView mrecyclerview;
    ArrayList<searchcourses> mylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalcompletedcourses);
        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();
        String uid = currentuser.getUid();
        mrecyclerview = (RecyclerView)findViewById(R.id.mrecyclerview);
        mrecyclerview.setHasFixedSize(true);
        ref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("completed course");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    mylist.add(ds.getValue(searchcourses.class));
                }
                mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                searchadapter adp = new searchadapter(getApplicationContext(), mylist);
                mrecyclerview.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
