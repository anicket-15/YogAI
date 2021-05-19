package com.anicket.yogai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Locale;

public class MeditationActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 60000;
    MediaPlayer mediaPlayer;
    TextView countDownTimerTextView;
    CountDownTimer countDownTimer;
    boolean timerRunning;
    long timeLeftInMillis = START_TIME_IN_MILLIS;
    ImageButton playPauseButton;


    public MeditationActivity(){
        timerRunning = false;
    }

    ChipNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        this.getWindow().setStatusBarColor(getResources().getColor(R.color.meditationColor));

        playPauseButton = findViewById(R.id.playPauseButton);
        countDownTimerTextView = findViewById(R.id.countDownTimerTextView);
        updateCountDownText();


        playPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!timerRunning){
                            playPauseButton.setImageResource(R.drawable.ic_round_pause_circle_filled_24);
                            if(mediaPlayer==null){
                                mediaPlayer = MediaPlayer.create(MeditationActivity.this,R.raw.music);
                            }
                            mediaPlayer.start();
                            startTimer();
                        }else{
                            playPauseButton.setImageResource(R.drawable.ic_round_play_circle_filled_24);
                            if(mediaPlayer !=null){
                                mediaPlayer.pause();
                            }
                            pauseTimer();
                        }
                    }
                }
        );

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

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                mediaPlayer.release();
                mediaPlayer=null;
                resetTimer();
            }
        }.start();
        timerRunning = true;
    }

    private void resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        playPauseButton.setImageResource(R.drawable.ic_round_play_circle_filled_24);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }


    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis/1000)/60;
        int seconds = (int) (timeLeftInMillis/1000)%60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDownTimerTextView.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}