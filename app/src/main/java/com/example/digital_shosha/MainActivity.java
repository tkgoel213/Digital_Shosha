package com.example.digital_shosha;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    private ViewPager mslideviewpager;
    private LinearLayout mdotlayout;
    private TextView[] mdots;
    private slideradapter slideradapter;
    private Button prevbutton1;
    private Button nextbutton1;
    private int currentpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mslideviewpager=(ViewPager)findViewById(R.id.slideviewpager);
        mdotlayout=(LinearLayout)findViewById(R.id.dotlayout);

        prevbutton1=(Button)findViewById(R.id.prevbutton);
        nextbutton1=(Button)findViewById(R.id.nextbuton);
        slideradapter=new slideradapter(this);
        mslideviewpager.setAdapter(slideradapter);
        adddotsindicator(0);
        mslideviewpager.addOnPageChangeListener(viewlistener);

nextbutton1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mslideviewpager.setCurrentItem(currentpage +1);
    }
});
        prevbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mslideviewpager.setCurrentItem(currentpage -1);
            }
        });

    }

    public void adddotsindicator(int position){
        mdots=new TextView[3];
        mdotlayout.removeAllViews();


        for(int i=0;i<mdots.length;i++){
            mdots[i]=new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorgrey));
            mdotlayout.addView(mdots[i]);

        }
        if(mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.colorwhite));

        }
    }
    ViewPager.OnPageChangeListener viewlistener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
adddotsindicator(i);
currentpage=i;
if(i==0){
    prevbutton1.setEnabled(false);
    nextbutton1.setEnabled(true);
    prevbutton1.setVisibility(View.INVISIBLE);

    nextbutton1.setText("Next");
    prevbutton1.setText("");

       }
           else if(i==mdots.length-1){
                prevbutton1.setEnabled(true);
                nextbutton1.setEnabled(true);
                prevbutton1.setVisibility(View.VISIBLE);
                nextbutton1.setVisibility(View.VISIBLE);
                nextbutton1.setText("Finish");
                prevbutton1.setText("Back");

    nextbutton1.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, socialmedia.class);
            startActivity(intent);
        }
    });
            }
            else{
                        prevbutton1.setEnabled(true);
                      nextbutton1.setEnabled(true);
                        prevbutton1.setVisibility(View.VISIBLE);
                        nextbutton1.setVisibility(View.VISIBLE);
                        nextbutton1.setText("Next");
                        prevbutton1.setText("Back");
}
            }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
