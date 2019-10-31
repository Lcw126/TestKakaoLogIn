package com.lcw.testkakaologin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    BottomNavigationView bnv;

    FragmentManager fragmentManager= getSupportFragmentManager();

    Page1FragHome page1FragHome= new Page1FragHome();
    Page2Fragphoto page2Fragphoto= new Page2Fragphoto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();


        bnv=findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.home:
                        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();

                        break;
                    case R.id.daily:
                        transaction.replace(R.id.frame_layout, page2Fragphoto).commitAllowingStateLoss();
                        break;
                    case R.id.feel:
                        break;
                    case R.id.chat:
                        break;
                    case R.id.profile:
                        break;

                }
                return true;
            }
        });
    }
}
