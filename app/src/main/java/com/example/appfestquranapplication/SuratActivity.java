package com.example.appfestquranapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SuratActivity extends AppCompatActivity {

    private ListView coursesLV;

    // creating a new array list.
    ArrayList<String> coursesArrayList;

    // creating a variable for database reference.
    DatabaseReference reference;

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
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(SuratActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });

        QuranDAO quranDAO = new QuranDAO(SuratActivity.this);
        List<SurahNames> array = quranDAO.getSurahNames();

        RecyclerView recyclerView = findViewById(R.id.surah_recycler_view);
        RecyclerAdapterSurah recyclerAdapter = new RecyclerAdapterSurah(SuratActivity.this, array, translateEng[0], translateUrdu[0]);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SuratActivity.this);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}