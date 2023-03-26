package com.example.space_invaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MemoryGameActivity2 extends AppCompatActivity {

    private Button onePlayerButton;
    private Button twoPlayersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game2);

        // adding the advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView1 = (AdView)findViewById(R.id.adView1);
        adView1.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        onePlayerButton = (Button) findViewById(R.id.bt_standardowy);
        onePlayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putBoolean("memoryGameMode", false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });

        twoPlayersButton = (Button) findViewById(R.id.bt_hardcore);
        twoPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putBoolean("memoryGameMode", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });
    }

    protected void openMainActivity(){

        Intent intent = new Intent(this, MemoryGame_choose_octave_Activity3.class);
        startActivity(intent);
    }
}
