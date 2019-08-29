package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class firebasesearch extends AppCompatActivity {
    private EditText searchhere;
    private ImageButton Searchstart;
    private RecyclerView recyclerView;
    private DatabaseReference ref;
    ArrayList<searchcourses> list = new ArrayList<>();
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasesearch);

        searchhere = (EditText) findViewById(R.id.searchbar);
        Searchstart = (ImageButton) findViewById(R.id.searchstart);
        recyclerView = (RecyclerView) findViewById(R.id.recyclersearch);

        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();

        String uid = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference("courses");

        ref.keepSynced(true);


        Intent intent = getIntent();
        String text = intent.getStringExtra("value");
        Toast.makeText(firebasesearch.this, "fetched" + text, Toast.LENGTH_SHORT).show();
        searchhere.setText(text);



        Searchstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searched = searchhere.getText().toString();
                if(searched.equals(null)){
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    searchadapter adp = new searchadapter(getApplicationContext(),list);
                    recyclerView.setAdapter(adp);

                }

                searchstart(searched);

            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String reference = ds.getKey();
                    DatabaseReference ref2 = ref.child(reference);

                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                list.add(ds.getValue(searchcourses.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                searchadapter adp = new searchadapter(getApplicationContext(),list);
                recyclerView.setAdapter(adp);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void searchstart(String searched) {
        ArrayList<searchcourses> mylist = new ArrayList<>();
        for (searchcourses object : list) {
            if (object.getCoursename().toLowerCase().contains(searched.toLowerCase())) {
                mylist.add(object);
               searchadapter adp1 = new searchadapter(getApplicationContext(),mylist);
                recyclerView.setAdapter(adp1);

            }
        }

    }

    @Override
    public void onBackPressed() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchadapter adp = new searchadapter(getApplicationContext(),list);
        recyclerView.setAdapter(adp);

        //super.onBackPressed();
    }
}




