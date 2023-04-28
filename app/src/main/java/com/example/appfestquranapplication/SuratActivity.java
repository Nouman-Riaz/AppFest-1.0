package com.example.appfestquranapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class SuratActivity extends AppCompatActivity {

    RecyclerAdapterSurah recyclerAdapter;

    List<SurahNames> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat);

        Intent intent = getIntent();
        int translateEng = intent.getIntExtra("translateEng",0);
        int translateUrdu = intent.getIntExtra("translateUrdu",0);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        QuranDAO quranDAO = new QuranDAO(SuratActivity.this);
        array = quranDAO.getSurahNames();

        RecyclerView recyclerView = findViewById(R.id.surah_recycler_view);
        recyclerAdapter = new RecyclerAdapterSurah(SuratActivity.this, array, translateEng, translateUrdu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SuratActivity.this);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void filterList(String text) {
        List<SurahNames> filteredList = new ArrayList<>();
//        for (int i = 0; i < array.size(); i++) {
//            if(array.get(i).getUrdu().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(array.get(i));
//            }
//        }
        for (SurahNames surahNames : array){
            if(surahNames.getEng().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(surahNames);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"No data Found",Toast.LENGTH_SHORT).show();
        }
        else{
            recyclerAdapter.setFilterList(filteredList);
        }
    }
}