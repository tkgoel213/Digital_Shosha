package com.example.digital_shosha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class fav_fragment extends Fragment {

    private RecyclerView mrecyclerview;
    private DatabaseReference ref;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    FirebaseRecyclerOptions<coursescardview> options;
    FirebaseRecyclerAdapter<coursescardview, adapter> recyclerAdapter;



    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.favfragment, container, false);
        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();

        String uid=user.getUid();
        ref= FirebaseDatabase.getInstance().getReference("users").child(uid).child("wishlist");
        ref.keepSynced(true);
        mrecyclerview = (RecyclerView) view.findViewById(R.id.myrecyclerview);
        mrecyclerview.setHasFixedSize(true);

                options=new FirebaseRecyclerOptions.Builder<coursescardview>().setQuery(ref,coursescardview.class).build();

               recyclerAdapter=new FirebaseRecyclerAdapter<coursescardview, adapter>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull adapter adapter, int i, @NonNull coursescardview coursescardview) {

                       adapter.course.setText(coursescardview.getCoursename());
                       adapter.progress.setText(coursescardview.getProgressname());


                   }

                   @NonNull
                   @Override
                   public adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.mycardviewfav,parent,false);
                                return new adapter(view);
                   }
               };

        mrecyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        recyclerAdapter.startListening();
        mrecyclerview.setAdapter(recyclerAdapter);

            return view;
        }

    @Override
    public void onStart() {
        super.onStart();
        if (recyclerAdapter!=null){
            recyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recyclerAdapter!=null){
            recyclerAdapter.stopListening();}
    }


    @Override
    public void onResume() {
        super.onResume();
        if (recyclerAdapter!=null){
            recyclerAdapter.startListening();
        }
    }
}



