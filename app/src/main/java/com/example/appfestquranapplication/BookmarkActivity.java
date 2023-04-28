package com.example.appfestquranapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Ayat> ayatArrayList = new QuranDAO(getApplicationContext()).getAyatByBookmark();
        if(ayatArrayList.isEmpty()){
            ListView listView = findViewById(R.id.listBookmarks);
            listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,new String[] {"No bookmarks saved!"}));
            recyclerView.setAdapter(new RecyclerAdapterBookmark(getApplicationContext(),new ArrayList<>(),0,0));
        }
        else{
            Intent intent = getIntent();
            int translateEng = intent.getIntExtra("translateEng",0);
            int translateUrdu = intent.getIntExtra("translateUrdu",0);
            recyclerView.setAdapter(new RecyclerAdapterBookmark(getApplicationContext(),ayatArrayList,translateEng,translateUrdu));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        QuranDAO quranDAO = new QuranDAO(this);
        Intent intent = getIntent();
        int translateEng = intent.getIntExtra("translateEng",0);
        int translateUrdu = intent.getIntExtra("translateUrdu",0);
        List<Ayat> array = quranDAO.getAyatByBookmark();

        recyclerView = findViewById(R.id.bookmark_recycler_view);
        RecyclerAdapterBookmark recyclerAdapter = new RecyclerAdapterBookmark(BookmarkActivity.this, array, translateEng, translateUrdu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookmarkActivity.this);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}