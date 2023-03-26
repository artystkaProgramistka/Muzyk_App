package com.example.space_invaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class LostScreenActivity extends AppCompatActivity {

    private Button playButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_screen);

        // adding the advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView1 = (AdView)findViewById(R.id.adView1);
        adView1.loadAd(adRequest);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });

        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainMenuActivity();
            }
        });

        final MediaPlayer gameOverSound = MediaPlayer.create(this, R.raw.game_over);
        gameOverSound.start();
    }

    protected void openMainActivity(){

        Intent intent = new Intent(this, SpaceInvadersActivity.class);
        startActivity(intent);
    }

    protected void openMainMenuActivity(){

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}
