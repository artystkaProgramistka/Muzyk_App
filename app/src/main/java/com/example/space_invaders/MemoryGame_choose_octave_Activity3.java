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

public class MemoryGame_choose_octave_Activity3 extends AppCompatActivity {

    private Button btMala;
    private Button bt1;
    private Button bt2;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_octave);

        // adding the advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView1 = (AdView)findViewById(R.id.adView1);
        adView1.loadAd(adRequest);

        prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        btMala = (Button) findViewById(R.id.btMala);
        btMala.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putInt("memoryGameMode2", 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });

        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putInt("memoryGameMode2", 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });

        bt2 = (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putInt("memoryGameMode2", 3);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });
    }

    protected void openMainActivity(){

        Intent intent = new Intent(this, MemoryGameActivity.class);
        startActivity(intent);
    }
}
