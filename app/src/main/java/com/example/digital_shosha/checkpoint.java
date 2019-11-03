package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class checkpoint extends AppCompatActivity {

    FirebaseUser currentuser;
    FirebaseAuth mauth;
    DatabaseReference ref;
    long count;
    ImageView socialimage;
    String key, course,coursename,uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);
        mauth = FirebaseAuth.getInstance();
        socialimage=(ImageView) findViewById(R.id.socialimage);

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("completed course");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count=dataSnapshot.getChildrenCount();
                if(count==2){
                    socialimage.setImageResource(R.drawable.badge1);
                }
                if(count==4){
                    socialimage.setImageResource(R.drawable.badge2);
                }
                if(count==6){
                    socialimage.setImageResource(R.drawable.badge3);
                }



                }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
