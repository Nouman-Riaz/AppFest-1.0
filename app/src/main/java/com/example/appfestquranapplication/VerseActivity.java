package com.example.appfestquranapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class VerseActivity extends AppCompatActivity {

    private RecyclerAdapterVerse recyclerAdapter;
    ArrayList<Ayat> verses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse);

        Intent intent = getIntent();
        int id = intent.getIntExtra("SurahId",-1);
        int translateEng = intent.getIntExtra("translateEng",0);
        int translateUrdu = intent.getIntExtra("translateUrdu",0);
        String nameEng = intent.getStringExtra("NameEng");
        String nameUrdu = intent.getStringExtra("NameUrdu");
        int index = intent.getIntExtra("StaringIndex",0);

        QuranDAO quranDAO = new QuranDAO(this);
        quranDAO.setTranslation(translateEng,translateUrdu);

        verses = quranDAO.getAyatBySurah(id);
        TextView textView = findViewById(R.id.verseTitleEng);
        textView.setText(nameEng);
        TextView textView1 = findViewById(R.id.verseTitleUrdu);
        textView1.setText(nameUrdu);

        RecyclerView recyclerView = findViewById(R.id.ayat_recycler_view);
        recyclerAdapter = new RecyclerAdapterVerse(VerseActivity.this, verses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VerseActivity.this);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(index);
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

    }
    private void filterList(String text) {
        List<Ayat> filteredList = new ArrayList<>();
        for (Ayat ayat: verses){
            if(ayat.getTranslateEng().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(ayat);
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