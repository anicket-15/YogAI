package com.anicket.yogai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MeditationActivity extends AppCompatActivity {

    ChipNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        this.getWindow().setStatusBarColor(getResources().getColor(R.color.meditationColor));

        bottomNavigationBar= findViewById(R.id.bottomNavigationBar);
        bottomNavigationBar.setItemSelected(R.id.meditation,true);
        // Listener to handle changes on selecting new tile
        bottomNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i) {
                    case R.id.home :
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.meditation :
                        startActivity(new Intent(getApplicationContext(),MeditationActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.yoga :
                        startActivity(new Intent(getApplicationContext(),YogaActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;

                    case R.id.profile :
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }
            }
        });
    }
}