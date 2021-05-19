package com.anicket.yogai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class YogaActivity extends AppCompatActivity {


    ChipNavigationBar bottomNavigationBar;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<YogaPose,YogaAdapter.YogaViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;
    static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);


        this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorYoga));



        //Reference to the bottom Chip Navigation Bar
        bottomNavigationBar= findViewById(R.id.bottomNavigationBar);
        //To set the current tile as selected
        bottomNavigationBar.setItemSelected(R.id.yoga,true);

        progressBar = findViewById(R.id.progressBar);

        //Recycler View
        recyclerView = findViewById(R.id.yogaRecyclerView);
        layoutManager = new LinearLayoutManager(this);

        FirebaseRecyclerOptions<YogaPose> yogaPoseFirebaseRecyclerOptions = new
                FirebaseRecyclerOptions.Builder<YogaPose>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("YogaPose"),YogaPose.class)
                .build();

        adapter = new YogaAdapter(yogaPoseFirebaseRecyclerOptions);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // Listener to handle changes on selecting new tile
        bottomNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i) {
//                    case R.id.home :
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        finish();
//                        break;

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

//                    case R.id.profile :
//                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        finish();
//                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}