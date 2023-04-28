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

    private ListView coursesLV;

    // creating a new array list.
    ArrayList<String> coursesArrayList;

    RecyclerAdapterSurah recyclerAdapter;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    List<SurahNames> array;

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

        String [] engNames = {"Dr Mohsin Khan","Mufti Taqi Usmani","None"};
        String [] urduNames = {"Fateh Muhammad Jalandhri","Mehmood-ul-Hassan","None"};

        final int[] translateEng = {2};
        final int[] translateUrdu = {2};

        RadioGroup radioEng = (RadioGroup) navigationView.getMenu().findItem(R.id.translate_eng).getActionView();
        RadioGroup radioUrdu = (RadioGroup) navigationView.getMenu().findItem(R.id.translate_urdu).getActionView();

        int count = radioEng.getChildCount();
        ArrayList<RadioButton> listOfRadioButtonsEng = new ArrayList<RadioButton>();
        for (int i=0;i<count;i++) {
            View o = radioEng.getChildAt(i);
            if (o instanceof RadioButton) {
                ((RadioButton) o).setText(engNames[i]);
                listOfRadioButtonsEng.add((RadioButton)o);
            }
        }

        count = radioUrdu.getChildCount();
        ArrayList<RadioButton> listOfRadioButtonsUrdu = new ArrayList<RadioButton>();
        for (int i=0;i<count;i++) {
            View o = radioUrdu.getChildAt(i);
            if (o instanceof RadioButton) {
                ((RadioButton) o).setText(urduNames[i]);
                listOfRadioButtonsUrdu.add((RadioButton)o);
            }
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Intent intent;
                for (int i = 0; i < listOfRadioButtonsEng.size(); i++) {
                    if (listOfRadioButtonsEng.get(i).isChecked()) {
                        translateEng[0] = i;
                        break;
                    }
                }

                for (int i = 0; i < listOfRadioButtonsUrdu.size(); i++) {
                    if (listOfRadioButtonsUrdu.get(i).isChecked()) {
                        translateUrdu[0] = i;
                        break;
                    }
                }

                int itemId = menuItem.getItemId();
                if (itemId == R.id.surah_list) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.bookmark_list) {
                    intent = new Intent(SuratActivity.this, BookmarkActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.create_course) {
                    intent = new Intent(SuratActivity.this, CreateCourseActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.add_student) {
                    intent = new Intent(SuratActivity.this, AddStudentActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.view_courses) {
                    intent = new Intent(SuratActivity.this, ViewCoursesActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.logout) {
                    intent = new Intent(SuratActivity.this, LoginActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });

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

        Toast.makeText(SuratActivity.this, Integer.toString(translateEng[0]), Toast.LENGTH_SHORT).show();
        RecyclerView recyclerView = findViewById(R.id.surah_recycler_view);
        recyclerAdapter = new RecyclerAdapterSurah(SuratActivity.this, array, translateEng[0], translateUrdu[0]);
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