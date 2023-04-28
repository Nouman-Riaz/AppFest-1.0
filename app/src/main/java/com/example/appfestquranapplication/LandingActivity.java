package com.example.appfestquranapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView=findViewById(R.id.nav_view);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Button button = findViewById(R.id.drawer_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        String [] engNames = {"Dr Mohsin Khan                  ","Mufti Taqi Usmani","None"};
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

        listOfRadioButtonsEng.get(0).setChecked(true);
        listOfRadioButtonsUrdu.get(0).setChecked(true);


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
                    intent = new Intent(LandingActivity.this, SuratActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.bookmark_list) {
                    intent = new Intent(LandingActivity.this, BookmarkActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.create_course) {
                    intent = new Intent(LandingActivity.this, CreateCourseActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.add_student) {
                    intent = new Intent(LandingActivity.this, AddStudentActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.view_courses) {
                    intent = new Intent(LandingActivity.this, ViewCoursesActivity.class);
                    intent.putExtra("translateEng", translateEng[0]);
                    intent.putExtra("translateUrdu", translateUrdu[0]);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(LandingActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });
    }
}