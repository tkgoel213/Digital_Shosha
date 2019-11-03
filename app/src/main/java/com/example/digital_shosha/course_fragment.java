package com.example.digital_shosha;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class course_fragment extends Fragment  {

 String searched;
    private DatabaseReference reference;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
private EditText searchbar;
private ImageButton searchcourses;
CardView brand;
    String currentuserid;
    CircleImageView profilepicture;



    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.skills, container, false);

       // searchbar=(EditText)view.findViewById(R.id.searchbarhai);
        searchcourses=(ImageButton)view.findViewById(R.id.searchstart);
        brand=(CardView)view.findViewById(R.id.brandcard) ;
        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();

        currentuserid=currentuser.getUid();
        profilepicture=(CircleImageView) view.findViewById(R.id.propic) ;


        reference= FirebaseDatabase.getInstance().getReference("users").child(currentuserid);

        reference.addValueEventListener(new ValueEventListener() {
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
        });


//        searchcourses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(getActivity(),firebasesearch.class);
//              searched=searchbar.getText().toString();
//              i.putExtra("value",searched);
//                startActivity(i);
//                getActivity().finish();
//            }
//        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),socialmedia.class);
                startActivity(i);
                getActivity().finish();

            }
        });




            return view;

        }


    }



