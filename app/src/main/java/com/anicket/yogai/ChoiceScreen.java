package com.anicket.yogai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_screen);

        // Reference to Sign In Button
        Button signInButton = findViewById(R.id.signInButton);


        // Reference to Register Button
        Button registerButton = findViewById(R.id.registerButton);

        // Launch Login Activity on click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceScreen.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        // Launch Register Activity on Click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceScreen.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}