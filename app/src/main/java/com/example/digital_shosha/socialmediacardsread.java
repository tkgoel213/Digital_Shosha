package com.example.digital_shosha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import static androidx.viewpager.widget.PagerAdapter.POSITION_UNCHANGED;
import static java.lang.String.format;

public class socialmediacardsread extends AppCompatActivity implements firebaseloader {

    ViewPager viewPager;
    myadapter adapter;
    DatabaseReference ref, ref1,ref2;
    FirebaseUser currentuser;
    FirebaseAuth mauth;
    String key, course,coursename;
    firebaseloader firebaseloader1;
    int currentpage;
    ImageButton prev, next;
    List<cardviewgetter> cardviewgetterList = new ArrayList<>();
    int cardcount;
    TextView cardsleft, cardstotal;
    double progress;
    ValueEventListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course1sm);
        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();
        String uid=currentuser.getUid();
        cardsleft = (TextView) findViewById(R.id.cardsleft1);
        cardstotal = (TextView) findViewById(R.id.totalcards1);
        prev = (ImageButton) findViewById(R.id.previouscard);
        next = (ImageButton) findViewById(R.id.nextcard);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true, new depthpagetransformer());
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        course = intent.getStringExtra("course");
        coursename=intent.getStringExtra("course name");

        ref = FirebaseDatabase.getInstance().getReference("courses").child(course).child(key).child("cards");
        ref2 = FirebaseDatabase.getInstance().getReference("users").child(uid).child("resume").child(coursename);
        ref1 = FirebaseDatabase.getInstance().getReference("courses").child(course).child(key);

            readcards();
            viewPager.beginFakeDrag();

        firebaseloader1 = this;



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                if(position==0){
                cardstotal.setText(" / " + String.valueOf(cardcount));
                cardsleft.setText(String.valueOf(currentpage + 1));}
            }

            @Override
            public void onPageSelected(int position) {
                if(position>0){
                currentpage=position;
                cardstotal.setText(" / " + String.valueOf(cardcount));
                cardsleft.setText(String.valueOf(currentpage + 1));}
             readprogress(position);

                if (position == 0) {
                    prev.setEnabled(true);
                    next.setEnabled(true);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(currentpage + 1);
                        }
                    });
                }
                if (position > 0) {
                    prev.setEnabled(true);
                    next.setEnabled(true);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(currentpage + 1);
                        }
                    });
                }
                if (position == cardcount - 1) {
                    prev.setEnabled(true);
                    next.setEnabled(true);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(socialmediacardsread.this, coursecompleted.class);
                            i.putExtra("key", key);
                            i.putExtra("course", course);
                            startActivity(i);
                            finish();

                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentpage - 1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentpage + 1);
            }
        });


    }
    private void readprogress(final int position) {
        currentpage = position;
        progress = (((double) (position + 1) / (double) cardcount) * 100.0);
        HashMap<Object,Double> hashMap = new HashMap<>();
        progress= Double.parseDouble((String.format("%.2f", progress)));
        hashMap.put("progress",progress);
        ref2.setValue(hashMap);
    }



    public void readcards() {
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cardcount = (int) dataSnapshot.getChildrenCount();
                    for (DataSnapshot coursessnapshot : dataSnapshot.getChildren()) {
                        cardviewgetterList.add(coursessnapshot.getValue(cardviewgetter.class));
                        firebaseloader1.onfirebaseloadsucess(cardviewgetterList);
                    }

                    Toast.makeText(socialmediacardsread.this, "cards=" + cardcount, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseloader1.onfirebaseloadfailed(databaseError.getMessage());

                }
            };

            ref.addListenerForSingleValueEvent(listener);



        }



        @Override
        public void onfirebaseloadsucess (List < cardviewgetter > cardviewgetterList) {
            adapter = new myadapter(this,cardviewgetterList, getLayoutInflater(),viewPager,2);

            viewPager.setAdapter(adapter);
        }

        @Override
        public void onfirebaseloadfailed (String message){

            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

        }

}


