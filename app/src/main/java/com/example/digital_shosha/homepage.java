package com.example.digital_shosha;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class homepage extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference ref,ref1;
    TextView share;
    TextView feedback;

   ImageButton editprofile;
    TextView sign_out,totalcompcourses;
    TextView nameTV;
    TextView emailTV;
    TextView idTV;
    ImageView photoIV;
    TextView tofirebase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);



        user=mAuth.getCurrentUser();
        if(user==null){
            Intent intent=new Intent(homepage.this,signupactivity.class);
            startActivity(intent);
        }
        String uid = user.getUid();
        totalcompcourses=findViewById(R.id.numberofcompletedcourse);
        ref1 = FirebaseDatabase.getInstance().getReference("users").child(uid).child("completed course");
       share=(TextView)findViewById(R.id.share);


        database=FirebaseDatabase.getInstance();
        ref=database.getReference("users").child(uid);
        tofirebase=findViewById(R.id.tofirebase);
        sign_out = findViewById(R.id.log_out);
        nameTV = findViewById(R.id.name);
       editprofile=findViewById(R.id.editprofileuser);
        emailTV = findViewById(R.id.email);
        feedback=(TextView)findViewById(R.id.feedback);
        photoIV = findViewById(R.id.photo);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name="" +dataSnapshot.child("name").getValue();
                String email="" +dataSnapshot.child("emailid").getValue();
                nameTV.setText(name);
                emailTV.setText(email);

                if(dataSnapshot.child("image").getValue().toString().isEmpty()){
                    photoIV.setImageResource(R.drawable.badge1c);
                }

                else {
                    String uri = (dataSnapshot.child("image").getValue().toString());
                    if(!uri.isEmpty()){
                        Picasso.get().load(uri).into(photoIV);
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total=0;
                 total=dataSnapshot.getChildrenCount();
                totalcompcourses.setText(String.valueOf(total));
               progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        tofirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,Main2Activity.class);
                startActivity(i);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_SEND);

                //this is to get the app link in the playstore without launching your app.
                final String appPackageName = getApplicationContext().getPackageName();
                String strAppLink = "";

                try
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                catch (android.content.ActivityNotFoundException anfe)
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                // this is the sharing part
                a.setType("text/link");
                String shareBody = "Hey! Download by app for free and win amazing cash prizes." +
                        "\n"+""+strAppLink;
                String shareSub = "APP NAME/TITLE";
                a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                a.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(a, "Share Using"));

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent a = new Intent(Intent.ACTION_SEND);
                a.putExtra(Intent.EXTRA_EMAIL,new String[] {"tkgoel@gmail.com","agoshsir@gmail.com"});
                a.setType("message/rfc822");
                a.setPackage("com.google.android.gm");
                startActivity(a);

            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homepage.this, editprofile.class));
                finish();
            }
        });





        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(homepage.this);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
         //  Glide.with(this).load(personPhoto).into(photoIV);
           Picasso.get().load(personPhoto).into(photoIV);
        }
        //progressBar.setVisibility(View.INVISIBLE);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }



    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(homepage.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(homepage.this, signupactivity.class));
                        finish();
                    }
                });
    }
}