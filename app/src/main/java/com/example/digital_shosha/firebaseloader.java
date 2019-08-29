package com.example.digital_shosha;

import java.util.List;

import androidx.viewpager.widget.ViewPager;

public interface firebaseloader {


    void onfirebaseloadsucess(List<cardviewgetter> cardviewgetterList);
    void onfirebaseloadfailed(String message);
}
