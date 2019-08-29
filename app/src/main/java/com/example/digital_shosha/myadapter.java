package com.example.digital_shosha;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ContentHandler;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    int position;


    public myadapter(Context context, List<cardviewgetter> cardviewgetterList, LayoutInflater inflater, ViewPager viewPager,int position) {



        this.context = context;
        this.cardviewgetterList = cardviewgetterList;
        this.inflater = inflater;
        this.viewPager=viewPager;
        this.position=position;
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
        viewPager.setCurrentItem(1);
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=inflater.inflate(R.layout.viewpageritemcards,(container),false);
        TextView textView=(TextView)view.findViewById(R.id.coursename);
        textView.setText(cardviewgetterList.get(position).getDescription());
        container.addView(view);
        return view;

    }
}
