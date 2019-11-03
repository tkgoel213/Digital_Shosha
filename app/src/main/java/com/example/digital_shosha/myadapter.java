package com.example.digital_shosha;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.ContentHandler;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class myadapter extends PagerAdapter {

    Context context;
    List<cardviewgetter> cardviewgetterList;
    LayoutInflater inflater;
    ViewPager viewPager;
    String coursename;
    Integer lastposition;


    public myadapter(Context context, List<cardviewgetter> cardviewgetterList, LayoutInflater inflater, ViewPager viewPager,String coursename) {

        this.context = context;
        this.cardviewgetterList = cardviewgetterList;
        this.inflater = inflater;
        this.viewPager=viewPager;
        this.coursename=coursename;


    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return  super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return cardviewgetterList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


//        FirebaseAuth mauth=FirebaseAuth.getInstance();
//        FirebaseUser user1 = mauth.getCurrentUser();
//        String uid1 = user1.getUid();
//
//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid1);
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 lastposition= (Integer.parseInt(String.valueOf((dataSnapshot.child("lastposition").child(coursename).child("lastposition").getValue()))));
//                viewPager.setCurrentItem(lastposition);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=inflater.inflate(R.layout.viewpageritemcards,(container),false);
        ImageView imagecard=(ImageView)view.findViewById(R.id.cardimage) ;
        TextView textView=(TextView)view.findViewById(R.id.coursename);
        textView.setText(cardviewgetterList.get(position).getDescription());
        Picasso.get().load(cardviewgetterList.get(position).image).into(imagecard);

        container.addView(view);
        return view;

    }
}
