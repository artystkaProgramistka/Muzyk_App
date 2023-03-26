package com.example.space_invaders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class NotesActivity extends AppCompatActivity {

    private Button cButton;
    private Button dButton;
    private Button eButton;
    private Button fButton;
    private Button gButton;
    private Button aButton;
    private Button hButton;
    private Button c2Button;

    private TextView text;

    private ImageView imageView;

    private Context context = this;

    private void showStartDialog(boolean firstStart){

        new AlertDialog.Builder(this).setTitle("Krótka, jednorazowa instrukcja").
                setMessage("Kliknij na jedną z kolorowych nutek na pięciolinii, aby zagrać dźwięk!").
                setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }

        if(!firstStart){
            editor.putBoolean("secondStart", false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        int mode = prefs.getInt("notesActivityMode", 0);

        switch(mode){
            case 1: setContentView(R.layout.activity_notes_mala);
                break;
            case 2: setContentView(R.layout.activity_notes);
                break;
            case 3: setContentView(R.layout.activity_notes_dwukreslna);
                break;
            default: new AlertDialog.Builder(this).setTitle("Error").
                    setMessage("Failed to load xml file.").
                    setPositiveButton("Wróć do menu głównego", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){ // toDo get back to the dashboard
                            dialog.dismiss();
                        }
                    })
                    .create().show();
                break;
        }

        //show the instruction only once after installation
        boolean firstStart = prefs.getBoolean("firstStart", true);
        boolean secondStart = prefs.getBoolean("secondStart", true);

        if(secondStart){
            showStartDialog(firstStart);
        }

        // adding the advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView1 = (AdView)findViewById(R.id.adView1);
        adView1.loadAd(adRequest);

        imageView = findViewById(R.id.imageView);

        text = (TextView) findViewById(R.id.textView);

        cButton = (Button) findViewById(R.id.cButton);
        final MediaPlayer c_sound;
        switch(mode) {
            case 1:
                c_sound = MediaPlayer.create(context, R.raw.c_mala);
                cButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        c_sound.start();
                        text.setText("C'");

                    }
                });
                break;

            case 2:
                c_sound = MediaPlayer.create(context, R.raw.c);
                cButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        c_sound.start();
                        text.setText("C'");

                    }
                });
                break;

            case 3:
                c_sound = MediaPlayer.create(context, R.raw.c_dwukreslna);
                cButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        c_sound.start();
                        text.setText("C'");

                    }
                });
                break;
        }

        dButton = (Button) findViewById(R.id.dButton);
        final MediaPlayer d_sound;
        switch(mode) {
            case 1:
                d_sound = MediaPlayer.create(context, R.raw.d_mala);
                dButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        d_sound.start();
                        text.setText("D'");

                    }
                });
                break;

            case 2:
                d_sound = MediaPlayer.create(context, R.raw.d);
                dButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        d_sound.start();
                        text.setText("D'");

                    }
                });
                break;

            case 3:
                d_sound = MediaPlayer.create(context, R.raw.d_dwukreslna);
                dButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        d_sound.start();
                        text.setText("D'");

                    }
                });
                break;
        }

        eButton = (Button) findViewById(R.id.eButton);
        final MediaPlayer e_sound;
        switch(mode) {
            case 1:
                e_sound = MediaPlayer.create(context, R.raw.e_mala);
                eButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        e_sound.start();
                        text.setText("E'");
                    }
                });
                break;

            case 2:
                e_sound = MediaPlayer.create(context, R.raw.e);
                eButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        e_sound.start();
                        text.setText("E'");
                    }
                });
                break;

            case 3:
                e_sound = MediaPlayer.create(context, R.raw.e_dwukreslna);
                eButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        e_sound.start();
                        text.setText("E'");
                    }
                });
                break;
        }

        fButton = (Button) findViewById(R.id.fButton);
        final MediaPlayer f_sound;
        switch(mode) {
            case 1:
                f_sound = MediaPlayer.create(context, R.raw.f_mala);
                fButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        f_sound.start();
                        text.setText("F'");
                    }
                });
                break;

            case 2:
                f_sound = MediaPlayer.create(context, R.raw.f);
                fButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        f_sound.start();
                        text.setText("F'");
                    }
                });
                break;

            case 3:
                f_sound = MediaPlayer.create(context, R.raw.f_dwukreslna);
                fButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        f_sound.start();
                        text.setText("F'");
                    }
                });
                break;
        }

        gButton = (Button) findViewById(R.id.gButton);
        final MediaPlayer g_sound;
        switch(mode) {
            case 1:
                g_sound = MediaPlayer.create(context, R.raw.g_mala);
                gButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        g_sound.start();
                        text.setText("G'");
                    }
                });
                break;

            case 2:
                g_sound = MediaPlayer.create(context, R.raw.g);
                gButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        g_sound.start();
                        text.setText("G'");
                    }
                });
                break;

            case 3:
                g_sound = MediaPlayer.create(context, R.raw.g_dwukreslna);
                gButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        g_sound.start();
                        text.setText("G'");
                    }
                });
                break;
        }

        aButton = (Button) findViewById(R.id.aButton);
        final MediaPlayer a_sound;
        switch(mode) {
            case 1:
                a_sound = MediaPlayer.create(context, R.raw.a_mala);
                aButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        a_sound.start();
                        text.setText("A'");
                    }
                });
                break;
            case 2:
                a_sound = MediaPlayer.create(context, R.raw.a);
                aButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        a_sound.start();
                        text.setText("A'");
                    }
                });
                break;
            case 3:
                a_sound = MediaPlayer.create(context, R.raw.a_dwukreslna);
                aButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        a_sound.start();
                        text.setText("A'");
                    }
                });
                break;
        }

        hButton = (Button) findViewById(R.id.hButton);
        final MediaPlayer h_sound;
        switch(mode) {
            case 1:
                h_sound = MediaPlayer.create(context, R.raw.h_mala);
                hButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        h_sound.start();
                        text.setText("H'");
                    }
                });
                break;

            case 2:
                h_sound = MediaPlayer.create(context, R.raw.h);
                hButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        h_sound.start();
                        text.setText("H'");
                    }
                });
                break;

            case 3:
                h_sound = MediaPlayer.create(context, R.raw.h_dwukreslna);
                hButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        h_sound.start();
                        text.setText("H'");
                    }
                });
                break;
        }

        c2Button = (Button) findViewById(R.id.c2Button);
        final MediaPlayer c2_sound;
        switch(mode) {
            case 1:
                c2_sound = MediaPlayer.create(context, R.raw.c);
                c2Button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        c2_sound.start();
                        text.setText("C''");
                    }
                });
                break;
            case 2:
                c2_sound = MediaPlayer.create(context, R.raw.c_dwukreslna);
                c2Button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        c2_sound.start();
                        text.setText("C''");
                    }
                });
                break;
            case 3:
                c2_sound = MediaPlayer.create(context, R.raw.c_trzykreslna); // toDo add the correct sound to the raw folder
                c2Button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        c2_sound.start();
                        text.setText("C''");
                    }
                });
                break;
        }
    }
}
