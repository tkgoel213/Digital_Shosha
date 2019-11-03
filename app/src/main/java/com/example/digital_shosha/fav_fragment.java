package com.example.digital_shosha;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class fav_fragment extends Fragment {

    private RecyclerView mrecyclerview;
    private DatabaseReference ref,reference;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    String currentuserid;
    CircleImageView profilepicture;
    EditText searchfav;
    ArrayList<searchcourses> mlist = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favfragment, container, false);
        mauth = FirebaseAuth.getInstance();
         currentuser = mauth.getCurrentUser();

        currentuserid=currentuser.getUid();
        searchfav=view.findViewById(R.id.searchfav);
        profilepicture=(CircleImageView) view.findViewById(R.id.propic) ;


        reference=FirebaseDatabase.getInstance().getReference("users").child(currentuserid);


        ref = FirebaseDatabase.getInstance().getReference("users").child(currentuserid).child("favs");
        mrecyclerview = (RecyclerView) view.findViewById(R.id.myrecyclerview);
        mrecyclerview.setHasFixedSize(true);
        searchfav.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();
                searchstart(String.valueOf(s));

            }
        });
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





        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mlist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mlist.add(ds.getValue(searchcourses.class));

                }
                mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                favadapter adp = new favadapter(getApplicationContext(), mlist);
                mrecyclerview.setAdapter(adp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return view;
    }
    private void searchstart(String searched) {
        ArrayList<searchcourses> searchlist = new ArrayList<>();
        for (searchcourses object : mlist) {
            if (object.getCoursename().toLowerCase().contains(searched.toLowerCase())) {
                searchlist.add(object);
                favadapter adp1= new favadapter(getApplicationContext(), mlist);
                mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mrecyclerview.setAdapter(adp1);
            }
        }

    }

}





