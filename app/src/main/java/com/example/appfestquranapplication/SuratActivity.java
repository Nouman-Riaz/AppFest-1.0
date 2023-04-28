package com.example.appfestquranapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class SuratActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView=findViewById(R.id.nav_view);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        String [] engNames = {"Dr Mohsin Khan","Mufti Taqi Usmani"};
        String [] urduNames = {"Fateh Muhammad Jalandhri","Mehmood-ul-Hassan"};

        QuranDAO quranDAO = new QuranDAO(SuratActivity.this);
        Intent intent = getIntent();
        int translateEng = intent.getIntExtra("translateEng",0);
        int translateUrdu = intent.getIntExtra("translateUrdu",0);
        List<SurahNames> array = quranDAO.getSurahNames();

        RecyclerView recyclerView = findViewById(R.id.surah_recycler_view);
        RecyclerAdapterSurah recyclerAdapter = new RecyclerAdapterSurah(SuratActivity.this, array,translateEng,translateUrdu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SuratActivity.this);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}