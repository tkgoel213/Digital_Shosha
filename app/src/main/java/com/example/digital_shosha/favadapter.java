package com.example.digital_shosha;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class favadapter extends RecyclerView.Adapter<favadapter.myviewholder>{

    Context context;
    ArrayList<searchcourses> searchcoursesArrayList;
    DatabaseReference ref,ref1;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;
    int flag=1;
    String progress;


    public favadapter(Context context, ArrayList<searchcourses> searchcoursesArrayList) {
        this.searchcoursesArrayList = searchcoursesArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favlistlayout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position) {

        holder.course.setText(searchcoursesArrayList.get(position).getCoursename());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = mAuth.getCurrentUser();
        String uid1 = user1.getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid1).child("resume");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid1);
        reference.child("resume").child(searchcoursesArrayList.get(position).getCoursename()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("progress").getValue() == null) {
                    holder.courseimages.setImageResource(R.drawable.book);
                } else {

                    double progressinfirebase = (Double.valueOf(String.valueOf(dataSnapshot.child("progress").getValue())));
                    if (progressinfirebase == 0) {
                        holder.courseimages.setImageResource(R.drawable.book);
                    }

                    if (progressinfirebase == 100.00) {

                        holder.courseimages.setImageResource(R.drawable.scholarship);
                    } else {
                        holder.courseimages.setImageResource(R.drawable.reading);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    if (key.equals(holder.course.getText())) {
                        searchcoursesArrayList.get(position).setProgress(ds.child("progress").getValue().toString() + " % completed");
                        holder.progress.setText(searchcoursesArrayList.get(position).getProgress());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("courses").orderByChild("coursename").equalTo(searchcoursesArrayList.get(position).getCoursename()).getRef();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            final String key = ds.getKey();
                            DatabaseReference ref2 = reference.child(key);
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        String key1 = dataSnapshot1.getKey();
                                        String value = dataSnapshot1.child("coursename").getValue().toString();
                                        if (value.equals(searchcoursesArrayList.get(position).getCoursename())) {

                                            Intent i = new Intent(context, socialmediacardsread.class);
                                            i.putExtra("key", key1);
                                            i.putExtra("course", key);
                                            i.putExtra("course name", searchcoursesArrayList.get(position).getCoursename());
                                            context.startActivity(i);
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {

            FirebaseAuth mauth=FirebaseAuth.getInstance();
            FirebaseUser user1 = mauth.getCurrentUser();
            String uid1 = user1.getUid();

            @Override
            public void onClick(View v) {
                final String coursename = searchcoursesArrayList.get(position).getCoursename();

              final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid1);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot: dataSnapshot.child("favs").getChildren()) {
                            if(appleSnapshot.child("coursename").getValue().toString().equals(coursename)){
                                appleSnapshot.getRef().removeValue();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Error:"+databaseError.toException(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }


    @Override
    public int getItemCount() {
        return searchcoursesArrayList.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView course,progress;
        CardView cardView;
        ImageView delete,courseimages;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            course=itemView.findViewById(R.id.coursenamesearch);
            progress=itemView.findViewById(R.id.progresssearch);
            cardView= itemView.findViewById(R.id.coursecard);
            delete=itemView.findViewById(R.id.deletedata);
            courseimages=itemView.findViewById(R.id.courseimages);

        }
    }
}
