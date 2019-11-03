package com.example.digital_shosha;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class resume_fragment extends Fragment {


    private RecyclerView mrecyclerview;
    private DatabaseReference ref,reference;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    EditText searchresume;
    ValueEventListener listener;
    CircleImageView profilepicture;
    String beforevalue;

    ArrayList<searchcourses> mylist = new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.resume, container, false);
            mauth = FirebaseAuth.getInstance();
            currentuser = mauth.getCurrentUser();
            mrecyclerview = (RecyclerView) view.findViewById(R.id.myrecyclerview);
            mrecyclerview.setHasFixedSize(true);
            profilepicture=(CircleImageView) view.findViewById(R.id.propic) ;
            String uid = currentuser.getUid();
            reference=FirebaseDatabase.getInstance().getReference("users").child(uid);
            searchresume=view.findViewById(R.id.searchbarresume);
            searchresume.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                     beforevalue= String.valueOf(s);


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    searchstart(s.toString());


                }

                @Override
                public void afterTextChanged(Editable s) {





                }
            });

            ref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("resume");
            mylist.clear();
            listener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("image").getValue().toString().isEmpty()){
                        profilepicture.setImageResource(R.drawable.badge1c);
                    }

                    else {
                        String uri = (dataSnapshot.child("image").getValue().toString());
                        if(!uri.isEmpty()){
                            Picasso.get().load(uri).into(profilepicture);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference.addValueEventListener(listener);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


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

            return  view;


        }
    private void searchstart(String searched) {
        ArrayList<searchcourses> searchlist = new ArrayList<>();
        for (searchcourses object : mylist) {
            if (object.getCoursename().toLowerCase().contains(searched.toLowerCase())) {
                searchlist.add(object);
                searchadapter adp1 = new searchadapter(getApplicationContext(),searchlist);
                mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mrecyclerview.setAdapter(adp1);
            }
        }

    }


}



