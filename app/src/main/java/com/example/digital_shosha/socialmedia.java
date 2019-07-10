package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class socialmedia extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference mdatabase;
    private FirebaseRecyclerAdapter<retriever, viewholder> mPeopleRVAdapter;
    private FirebaseDatabase mfirebasedatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialmedia);
        mfirebasedatabase=FirebaseDatabase.getInstance();
        mdatabase=FirebaseDatabase.getInstance().getReference().child("social media");
        mdatabase.keepSynced(true);
        Query personsQuery = mdatabase.orderByKey();

        mrecyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<retriever>().setQuery(personsQuery, retriever.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<retriever, viewholder>(personsOptions) {
            @Override
            protected void onBindViewHolder(viewholder holder, int position, retriever model) {
                Picasso.get().load(model.getImage()).into(holder.imageviewr, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(socialmedia.this, "could not get image", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.headingview.setText(model.getHeading());
            }

            @Override
            public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.socialmediacourses, parent, false);

                return new viewholder(view);
            }
        };

        mrecyclerview.setAdapter(mPeopleRVAdapter);



    }

    @Override
    protected void onStart() {
super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if(adapter!=null){
            adapter.startListening();
        }
        super.onStop();
    }
}
