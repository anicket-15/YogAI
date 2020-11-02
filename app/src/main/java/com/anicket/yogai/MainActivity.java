package com.anicket.yogai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To handle notch displays (Full Screen Splash Screen)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // For devices over Android P
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            this.getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES,WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        }
        //For devices below Android P
        else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        setContentView(R.layout.activity_main);


        // Set Animated GIF as Input to ImageView using Glide Library
        ImageView splashView = findViewById(R.id.splashView);
        Glide.with(this).load(R.drawable.splash).into(splashView);

        // Wait on splash Screen for 3000 ms and move to ChoiceScreen
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            private static final String TAG ="TAG";

            @Override
            public void run() {
                Boolean a=false;
                fAuth=FirebaseAuth.getInstance();
                if (fAuth.getCurrentUser() != null) {
                    a=true;
                    Log.d(TAG, "Current User " + fAuth.getCurrentUser().getUid());
                    startActivity(new Intent(MainActivity.this, TemporaryActivity.class));
                    finish();
                }
                if(a==false) {
                    Intent intent = new Intent(MainActivity.this, ChoiceScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        },8000);

    }
}