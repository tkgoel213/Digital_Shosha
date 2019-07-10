package com.example.digital_shosha;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class slideradapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public slideradapter(Context context){
        this.context=context;

    }

    public int[] slideimages={
            R.drawable.social,
            R.drawable.social,
            R.drawable.social

    };

    public String[] slideheadings={
            "DESCRIPTION",
            "FEATURES","LEARN"
    };

    public String[] slidedesc={"fbhbshbfhbhsbajhbfbabsfhahbhbasfhbfahbfash","fbhbshbfhbhsbajhbfbabsfhahbhbasfhbfahbfash","fbhbshbfhbhsbajhbfbabsfhahbhbasfhbfahbfash"};





    @Override
    public int getCount() {
        return slideheadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidelayout, container, false);

        ImageView slideimageview = (ImageView) view.findViewById(R.id.imageslider);
        TextView slideheading = (TextView) view.findViewById(R.id.header);
        TextView sliderdesc = (TextView) view.findViewById(R.id.description);

        slideimageview.setImageResource(slideimages[position]);
        slideheading.setText(slideheadings[position]);
        sliderdesc.setText(slidedesc[position]);

   container.addView(view);
   return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
