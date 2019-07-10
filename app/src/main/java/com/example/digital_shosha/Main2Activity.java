package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView  bottomNav = findViewById(R.id.bottomnavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                    new explore_fragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.explore:
                            selectedFragment = new explore_fragment();
                            break;
                        case R.id.courses:
                            selectedFragment = new course_fragment();
                            break;
                        case R.id.favorites:
                            selectedFragment = new fav_fragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                            selectedFragment).commit();

                    return true;
                }
            };
}
