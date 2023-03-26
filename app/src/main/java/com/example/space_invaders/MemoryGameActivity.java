package com.example.space_invaders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MemoryGameActivity extends AppCompatActivity {

    private TextView textView1, textView2, textView3;
    private ImageView iv11, iv12, iv13, iv21, iv22, iv23, iv31, iv32, iv33, iv41, iv42, iv43;
    private Integer[] cardsArray = {101, 102, 103, 104, 105, 106, 201, 202, 203, 204, 205, 206};
    private int image101, image102, image103, image104, image105, image106,
            image201, image202, image203, image204, image205, image206;
    private int firstCard, secondCard;
    private int clickedFirst, clickedSecond;
    private int cardNumber = 1;
    private int turn=1;
    private int playerPoints=0, cpuPoints=0;
    private boolean started = false;
    private boolean mode = true; // does player want to play with another person or prefers to do this on his own?
                                 // true- wants to play with the other player
                                 // false- wants to play alone
    private int mode2;

    private Chronometer chronometer;
    private Handler handler2;
    private long tMilliSec, tStart;
    private int sec, min, millisec;
    private String miN, seC, milliseC;

    private MediaPlayer sound1;
    private MediaPlayer sound2;
    private MediaPlayer sound3;
    private MediaPlayer sound4;
    private MediaPlayer sound5;
    private MediaPlayer sound6;

    private void showStartDialogForTwoPlayers(boolean firstStartMemoryGameTwoPlayersMode){

        new AlertDialog.Builder(this).setTitle("Memory").
                setMessage("Witaj w trybie rywalizacji z drugim graczem!\n" +
                        "Czy wiedzieliście, że grając w tą grę oboje ćwiczycie zarowno swoją pamięć, jak i umiejętność czytania z nut?\n\n" +
                        "Po wciśnięciu OK, rozpopczniecie grę.\n" +
                        "Aby ukończyć grę, musicie skompletować wszystkie pary.\n" +
                        "Żeby skompletować parę musicie odkryć w jednej turze kartę posiadającą na odwrocie nutę literową/ n" +
                        "oraz drugą kartę, zawierającą jej odpowiednik na pięciolinii.\n" +
                        "Za każdą znalecioną parę otrzymuje się jeden punkt, a na koniec wygrywa ten, kto zdobędzie najwięcej punktów.\n" +
                        "Rozgrywkę rozpoczyna gracz pierwszy, oznaczony ciemniejszym kolorem.\n" +
                        "Jeśli odnajdzie on parę, może On kontynuować swoją rozgrywkę szukając następnej.\n" +
                        "Jeśli jednak odkryje dwie nie pasujące do siebie karty, jego tura dobiega końca i rozpoczyna się aktywna rozgrywka drugiego gracza.\n" +
                        "Nazwa gracza, który w danej turze powinien być aktywny zawsze oznaczona jest kolorem ciemniejszym, a gracza czekającego na swój ruch- jaśniejszymym.\n" +
                        "\nZaczynajcie grę, niech wygra najlepszy!.\n").
                setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences saveData = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveData.edit();
        editor.putBoolean("firstStartMemoryGameTwoPlayersMode", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
        editor.commit();

        if(!firstStartMemoryGameTwoPlayersMode){
            editor.putBoolean("secondStartMemoryGameTwoPlayersMode", false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply();
            }
            editor.commit();
        }
    }

    private void showStartDialogForOnePlayer(boolean firstStartMemoryGameOnePlayerMode){

        new AlertDialog.Builder(this).setTitle("Memory").
                setMessage("Witaj w trybie dla jednego gracza!\n" +
                        "Grając w tą grę ćwiczysz zarowno swoją pamięć, jak i umiejętność czytania z nut!\n\n" +
                        "Po wciśnięciu OK, rozpopczniesz grę, a co za tym idzie- ruszy stoper odmierzający czas Twoej rozgrywki.\n" +
                        "Aby ukończyć grę, musisz skompletować wszystkie pary.\n" +
                        "Żeby skompletować parę musisz odkryć w jednej turze kartę posiadającą na odwrocie nutę literową/ n" +
                        "oraz drugą kartę, zawierającą jej odpowiednik na pięciolinii.\n" +
                        "Musisz połączyć wszystkie nuty aby zakończyć rozgrywkę.\n" +
                        "Spiesz się, bo czas będzie uciekał!\n").
                setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // create a new thread
                        handler2 = new Handler();

                        handler2.postDelayed(runnable2, 0);
                        //Thread thread = new Thread(runnable2);
                        //thread.start();
                        tStart = SystemClock.uptimeMillis();
                        chronometer.start();
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences saveData = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveData.edit();
        editor.putBoolean("firstStartMemoryGameOnePlayerMode", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
        editor.commit();

        if(!firstStartMemoryGameOnePlayerMode){
            editor.putBoolean("secondStartMemoryGameOnePlayerMode", false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply();
            }
            editor.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // find views by ids, set the tags, make the cards clickable
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        // adding the advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView1 = (AdView)findViewById(R.id.adView1);
        adView1.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        //SharedPreferences.Editor editoR = prefs.edit();
        mode = prefs.getBoolean("memoryGameMode", true);
        mode2 = prefs.getInt("memoryGameMode2", 0);

        SharedPreferences saveData = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveData.edit();

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        chronometer = findViewById(R.id.chronometer);
        if(mode){  // only if the player wants to play alone

            chronometer.setVisibility(View.INVISIBLE);
            textView3.setVisibility(View.INVISIBLE);

            boolean firstStartTwoPlayersMode = saveData.getBoolean("firstStartMemoryGameTwoPlayersMode", true);
            boolean secondStartTwoPlayersMode = saveData.getBoolean("secondStartMemoryGameTwoPlayersMode", true);

            if(secondStartTwoPlayersMode) {
                showStartDialogForTwoPlayers(firstStartTwoPlayersMode);
            }
        }else{

            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);

            boolean firstStartOnePlayerMode = saveData.getBoolean("firstStartMemoryGameOnePlayerMode", true);
            boolean secondStartOnePlayerMode = saveData.getBoolean("secondStartMemoryGameOnePlayerMode", true);

            if(secondStartOnePlayerMode){
                showStartDialogForOnePlayer(firstStartOnePlayerMode);
            }else{
                // create a new thread
                handler2 = new Handler();

                handler2.postDelayed(runnable2, 0);
                //Thread thread = new Thread(runnable2);
                //thread.start();
                tStart = SystemClock.uptimeMillis();
                chronometer.start();
            }
        }

        iv11 = (ImageView) findViewById(R.id.iv11);
        iv12 = (ImageView) findViewById(R.id.iv12);
        iv13 = (ImageView) findViewById(R.id.iv13);

        iv21 = (ImageView) findViewById(R.id.iv21);
        iv22 = (ImageView) findViewById(R.id.iv22);
        iv23 = (ImageView) findViewById(R.id.iv23);

        iv31 = (ImageView) findViewById(R.id.iv31);
        iv32 = (ImageView) findViewById(R.id.iv32);
        iv33 = (ImageView) findViewById(R.id.iv33);

        iv41 = (ImageView) findViewById(R.id.iv41);
        iv42 = (ImageView) findViewById(R.id.iv42);
        iv43 = (ImageView) findViewById(R.id.iv43);


        iv11.setTag("0");   iv21.setTag("3");   iv31.setTag("6");   iv41.setTag("9");
        iv12.setTag("1");   iv22.setTag("4");   iv32.setTag("7");   iv42.setTag("10");
        iv13.setTag("2");   iv23.setTag("5");   iv33.setTag("8");   iv43.setTag("11");

        if(mode2==1){

            iv11.setImageResource(R.drawable.frontcardr7);
            iv12.setImageResource(R.drawable.frontcardr7);
            iv13.setImageResource(R.drawable.frontcardr7);

            iv21.setImageResource(R.drawable.frontcardr7);
            iv22.setImageResource(R.drawable.frontcardr7);
            iv23.setImageResource(R.drawable.frontcardr7);

            iv31.setImageResource(R.drawable.frontcardr7);
            iv32.setImageResource(R.drawable.frontcardr7);
            iv33.setImageResource(R.drawable.frontcardr7);

            iv41.setImageResource(R.drawable.frontcardr7);
            iv42.setImageResource(R.drawable.frontcardr7);
            iv43.setImageResource(R.drawable.frontcardr7);

            FrameLayout gorny = findViewById(R.id.gorny);
            gorny.setBackgroundColor(getResources().getColor(R.color.darkOrange));

            FrameLayout dolny = findViewById(R.id.dolny);
            dolny.setBackgroundColor(getResources().getColor(R.color.lightOrange));

        }else if(mode2==3){

            iv11.setImageResource(R.drawable.frontcardr5);
            iv12.setImageResource(R.drawable.frontcardr5);
            iv13.setImageResource(R.drawable.frontcardr5);

            iv21.setImageResource(R.drawable.frontcardr5);
            iv22.setImageResource(R.drawable.frontcardr5);
            iv23.setImageResource(R.drawable.frontcardr5);

            iv31.setImageResource(R.drawable.frontcardr5);
            iv32.setImageResource(R.drawable.frontcardr5);
            iv33.setImageResource(R.drawable.frontcardr5);

            iv41.setImageResource(R.drawable.frontcardr5);
            iv42.setImageResource(R.drawable.frontcardr5);
            iv43.setImageResource(R.drawable.frontcardr5);

            FrameLayout gorny = findViewById(R.id.gorny);
            gorny.setBackgroundColor(getResources().getColor(R.color.darkBlue));

            FrameLayout dolny = findViewById(R.id.dolny);
            dolny.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        }

        // load the card images
        frontOfCardsResources();

        // shuffle the images
        Collections.shuffle(Arrays.asList(cardsArray));

        if(mode) {
            if(mode2==2) {
                // changing the color of the second player(inactive)
                textView2.setTextColor(getResources().getColor(R.color.lightOrange));
            }else if(mode2==3  || mode2 ==1) {
                textView1.setTextColor(getResources().getColor(R.color.black));
                textView2.setTextColor(getResources().getColor(R.color.darkGray));
            }
        }
        // make the cards clickable
        iv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv11, theCard);
            }
        });
        iv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv12, theCard);
            }
        });
        iv13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv13, theCard);
            }
        });
        iv21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv21, theCard);
            }
        });
        iv22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv22, theCard);
            }
        });
        iv23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv23, theCard);
            }
        });
        iv31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv31, theCard);
            }
        });
        iv32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv32, theCard);
            }
        });
        iv33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv33, theCard);
            }
        });
        iv41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv41, theCard);
            }
        });
        iv42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv42, theCard);
            }
        });
        iv43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt( (String) v.getTag());
                doStuff(iv43, theCard);
            }
        });

    }
    // for chronometer
    public Runnable runnable2 = new Runnable(){
        @Override
                public void run(){

            tMilliSec = SystemClock.uptimeMillis()-tStart;
            sec = (int) (tMilliSec/1000);
            min = sec/60;
            sec = sec%60;
            millisec = (int) (tMilliSec%100);
            miN = String.valueOf(min);
            seC = String.valueOf(sec);
            milliseC = String.valueOf(millisec);
            chronometer.setText(min + ":" + sec + ":" + millisec);
            handler2.postDelayed(this, 0);
            //Thread thread = new Thread(runnable2);
            //thread.start();
        }
    };

    private void doStuff(ImageView iv, int card){

        // what's the back side of the card that has been just clicked?
        if(cardsArray[card] == 101){
            sound1.start();
            iv.setImageResource(image101);
        }else if(cardsArray[card] == 102){
            sound2.start();
            iv.setImageResource(image102);
        }else if(cardsArray[card] == 103){
            sound3.start();
            iv.setImageResource(image103);
        }else if(cardsArray[card] == 104){
            sound4.start();
            iv.setImageResource(image104);
        }else if(cardsArray[card] == 105){
            sound5.start();
            iv.setImageResource(image105);
        }else if(cardsArray[card] == 106){
            sound6.start();
            iv.setImageResource(image106);
        }else if(cardsArray[card] == 201){
            sound1.start();
            iv.setImageResource(image201);
        }else if(cardsArray[card] == 202){
            sound2.start();
            iv.setImageResource(image102);
        }else if(cardsArray[card] == 203){
            sound3.start();
            iv.setImageResource(image203);
        }else if(cardsArray[card] == 204){
            sound4.start();
            iv.setImageResource(image204);
        }else if(cardsArray[card] == 205){
            sound5.start();
            iv.setImageResource(image205);
        }else if(cardsArray[card] == 206){
            sound6.start();
            iv.setImageResource(image206);
        }
        // Was it first or the second card that was clicked?
        if(cardNumber == 1){ // first
            firstCard = cardsArray[card];
            if(firstCard > 200){
                firstCard = firstCard - 100;
            }
            cardNumber = 2;
            clickedFirst = card;
            iv.setEnabled(false);

        }else if(cardNumber == 2){ // second
            secondCard = cardsArray[card];
            if(secondCard > 200){
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
            clickedSecond = card;
            //iv.setEnabled(false);

            iv11.setEnabled(false);     iv31.setEnabled(false);
            iv12.setEnabled(false);     iv32.setEnabled(false);
            iv13.setEnabled(false);     iv33.setEnabled(false);
            iv21.setEnabled(false);     iv41.setEnabled(false);
            iv22.setEnabled(false);     iv42.setEnabled(false);
            iv23.setEnabled(false);     iv43.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    // check if the selected images match each other
                    calculate();
                }
            }, 1000);
        }
    }

    private void calculate(){
        if(firstCard == secondCard){
            if(clickedFirst == 0){
                iv11.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 1){
                iv12.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 2){
                iv13.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 3){
                iv21.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 4){
                iv22.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 5){
                iv23.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 6){
                iv31.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 7){
                iv32.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 8){
                iv33.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 9){
                iv41.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 10){
                iv42.setVisibility(View.INVISIBLE);
            }else if(clickedFirst == 11){
                iv43.setVisibility(View.INVISIBLE);
            }

            if(clickedSecond == 0){
                iv11.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 1){
                iv12.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 2){
                iv13.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 3){
                iv21.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 4){
                iv22.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 5){
                iv23.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 6){
                iv31.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 7){
                iv32.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 8){
                iv33.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 9){
                iv41.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 10){
                iv42.setVisibility(View.INVISIBLE);
            }else if(clickedSecond == 11){
                iv43.setVisibility(View.INVISIBLE);
            }

            if(mode) { // do this only if the user choose to play with the other person
                // add points to the current player
                if (turn == 1) {
                    playerPoints++;
                    textView1.setText("P1: " + playerPoints);
                } else if (turn == 2) {
                    cpuPoints++;
                    textView2.setText("P2: " + cpuPoints);
                }
            }
        }else{
            if(mode2==1) {

                iv11.setImageResource(R.drawable.frontcardr7);
                iv12.setImageResource(R.drawable.frontcardr7);
                iv13.setImageResource(R.drawable.frontcardr7);
                iv21.setImageResource(R.drawable.frontcardr7);
                iv22.setImageResource(R.drawable.frontcardr7);
                iv23.setImageResource(R.drawable.frontcardr7);
                iv31.setImageResource(R.drawable.frontcardr7);
                iv32.setImageResource(R.drawable.frontcardr7);
                iv33.setImageResource(R.drawable.frontcardr7);
                iv41.setImageResource(R.drawable.frontcardr7);
                iv42.setImageResource(R.drawable.frontcardr7);
                iv43.setImageResource(R.drawable.frontcardr7);
            }else if(mode2==2) {

                iv11.setImageResource(R.drawable.frontcardr);
                iv12.setImageResource(R.drawable.frontcardr);
                iv13.setImageResource(R.drawable.frontcardr);
                iv21.setImageResource(R.drawable.frontcardr);
                iv22.setImageResource(R.drawable.frontcardr);
                iv23.setImageResource(R.drawable.frontcardr);
                iv31.setImageResource(R.drawable.frontcardr);
                iv32.setImageResource(R.drawable.frontcardr);
                iv33.setImageResource(R.drawable.frontcardr);
                iv41.setImageResource(R.drawable.frontcardr);
                iv42.setImageResource(R.drawable.frontcardr);
                iv43.setImageResource(R.drawable.frontcardr);
            }else if(mode2==3){
                iv11.setImageResource(R.drawable.frontcardr5);
                iv12.setImageResource(R.drawable.frontcardr5);
                iv13.setImageResource(R.drawable.frontcardr5);
                iv21.setImageResource(R.drawable.frontcardr5);
                iv22.setImageResource(R.drawable.frontcardr5);
                iv23.setImageResource(R.drawable.frontcardr5);
                iv31.setImageResource(R.drawable.frontcardr5);
                iv32.setImageResource(R.drawable.frontcardr5);
                iv33.setImageResource(R.drawable.frontcardr5);
                iv41.setImageResource(R.drawable.frontcardr5);
                iv42.setImageResource(R.drawable.frontcardr5);
                iv43.setImageResource(R.drawable.frontcardr5);

            }

            if(mode) {
                if (turn == 1) {
                    turn = 2;
                    if(mode2==3 || mode2 ==1) {
                        textView1.setTextColor(getResources().getColor(R.color.darkGray));
                        textView2.setTextColor(getResources().getColor(R.color.black));
                    }else {
                        textView1.setTextColor(getResources().getColor(R.color.lightOrange));
                        textView2.setTextColor(getResources().getColor(R.color.darkOrange));
                    }
                } else if (turn == 2) {
                    turn = 1;
                    if(mode2==3 || mode2 ==1) {
                        textView1.setTextColor(getResources().getColor(R.color.black));
                        textView2.setTextColor(getResources().getColor(R.color.darkGray));
                    }else {
                        textView1.setTextColor(getResources().getColor(R.color.darkOrange));
                        textView2.setTextColor(getResources().getColor(R.color.lightOrange));
                    }
                }
            }

        }
        iv11.setEnabled(true);
        iv12.setEnabled(true);
        iv13.setEnabled(true);
        iv21.setEnabled(true);
        iv22.setEnabled(true);
        iv23.setEnabled(true);
        iv31.setEnabled(true);
        iv32.setEnabled(true);
        iv33.setEnabled(true);
        iv41.setEnabled(true);
        iv42.setEnabled(true);
        iv43.setEnabled(true);

        checkEnd();
    }

    private void checkEnd(){

        if(iv11.getVisibility()==View.INVISIBLE &&
            iv12.getVisibility()==View.INVISIBLE &&
            iv13.getVisibility()==View.INVISIBLE &&
            iv21.getVisibility()==View.INVISIBLE &&
            iv22.getVisibility()==View.INVISIBLE &&
            iv23.getVisibility()==View.INVISIBLE &&
            iv31.getVisibility()==View.INVISIBLE &&
            iv32.getVisibility()==View.INVISIBLE &&
            iv33.getVisibility()==View.INVISIBLE &&
            iv41.getVisibility()==View.INVISIBLE &&
            iv42.getVisibility()==View.INVISIBLE &&
            iv43.getVisibility()==View.INVISIBLE ) {

            if (mode) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //there was a problem with this line
                alertDialogBuilder.setMessage("GameOver!\nP1: " + playerPoints + "\nP2: " + cpuPoints)
                        .setCancelable(false)
                        .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MemoryGameActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {// chcę tu wrócić
                // stop the chronometer
                chronometer.stop();
                chronometer.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                handler2.removeCallbacksAndMessages(runnable2);
                tMilliSec = SystemClock.uptimeMillis() - tStart;

                // if this is the first time player playes the game, set his score(time) as the best, whatever it is
                SharedPreferences saveData = this.getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveData.edit();
                boolean firstStartOnePlayerMode = saveData.getBoolean("firstStartMemoryGameOnePlayerMode", true);


                sec = (int) (tMilliSec / 1000);
                min = sec / 60;
                sec = sec % 60;
                millisec = (int) (tMilliSec % 1000);

                if (mode2 == 1) {
                    long tMillisec2 = saveData.getLong("bestTime_mala", 1000000000);

                    if (tMilliSec < tMillisec2) {
                        editor.putLong("bestTime_mala", tMilliSec);
                        editor.commit();
                        // toDo prise the player for beating his old best score
                        Intent intent = new Intent(this, NewHighScoreActivity.class);
                        startActivity(intent);
                    } else { // if the player hasn't beaten his old highscore
                        int sec2 = (int) (tMillisec2 / 1000);
                        int min2 = (int) sec / 60;
                        sec %= 60;
                        int millisec2 = (int) (tMillisec2 % 1000);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //there was a problem with this line
                        alertDialogBuilder.setMessage("GameOver!\nTwój czas: " + min + ":" + sec + ":" + millisec + "\nNajlepszy dotychczasowy czas: " + min2 + ":" + sec2 + ":" + millisec2)
                                .setCancelable(false)
                                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), MemoryGameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
                else if(mode2==2){
                    long tMillisec2 = saveData.getLong("bestTime_razkreslna", 1000000000);

                    if (tMilliSec < tMillisec2) {
                        editor.putLong("bestTime_razkreslna", tMilliSec);
                        editor.commit();
                        // toDo prise the player for beating his old best score
                        Intent intent = new Intent(this, NewHighScoreActivity.class);
                        startActivity(intent);
                    }else{
                        int sec2 = (int) (tMillisec2/1000);
                        int min2 = (int) sec/60;
                        sec %= 60;
                        int millisec2 = (int) (tMillisec2%1000);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //there was a problem with this line
                        alertDialogBuilder.setMessage("GameOver!\nTwój czas: " + min + ":"+ sec + ":" + millisec + "\nNajlepszy dotychczasowy czas: " + min2 + ":" + sec2 + ":" + millisec2)
                                .setCancelable(false)
                                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), MemoryGameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
                else {
                    long tMillisec2 = saveData.getLong("bestTime_dwukreslna", 1000000000);

                    if (tMilliSec < tMillisec2) {
                        editor.putLong("bestTime_dwukreslna", tMilliSec);
                        editor.commit();
                        // toDo prise the player for beating his old best score
                        Intent intent = new Intent(this, NewHighScoreActivity.class);
                        startActivity(intent);
                    }else{
                        int sec2 = (int) (tMillisec2/1000);
                        int min2 = (int) sec/60;
                        sec %= 60;
                        int millisec2 = (int) (tMillisec2%1000);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //there was a problem with this line
                        alertDialogBuilder.setMessage("GameOver!\nTwój czas: " + min + ":"+ sec + ":" + millisec + "\nNajlepszy dotychczasowy czas: " + min2 + ":" + sec2 + ":" + millisec2)
                                .setCancelable(false)
                                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), MemoryGameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }
        }
    }

    private void frontOfCardsResources(){

        final short clickedFirst, clickedSecond, clickedThird, clickedFourth, clickedFifth;

        Random generator = new Random();
        // 1
        short randomNumber = (short) generator.nextInt(7);
        clickedFirst = randomNumber;


        if(mode2==1) { // mala

            switch (randomNumber) {
                case 0:
                    image101 = R.drawable.c_mala;
                    image201 = R.drawable.c2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image101 = R.drawable.d_mala;
                    image201 = R.drawable.d2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image101 = R.drawable.e_mala;
                    image201 = R.drawable.e2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image101 = R.drawable.f_mala;
                    image201 = R.drawable.f2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image101 = R.drawable.g_mala;
                    image201 = R.drawable.g2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image101 = R.drawable.a_mala;
                    image201 = R.drawable.a2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image101 = R.drawable.h_mala;
                    image201 = R.drawable.h2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
            // 2
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst);
            clickedSecond = randomNumber;


            switch (randomNumber) {
                case 0:
                    image102 = R.drawable.c_mala;
                    image202 = R.drawable.c2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image102 = R.drawable.d_mala;
                    image202 = R.drawable.d2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image102 = R.drawable.e_mala;
                    image202 = R.drawable.e2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image102 = R.drawable.f_mala;
                    image202 = R.drawable.f2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image102 = R.drawable.g_mala;
                    image202 = R.drawable.g2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image102 = R.drawable.a_mala;
                    image202 = R.drawable.a2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image102 = R.drawable.h_mala;
                    image202 = R.drawable.h2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
            // 3
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond);
            clickedThird = randomNumber;


            switch (randomNumber) {
                case 0:
                    image103 = R.drawable.c_mala;
                    image203 = R.drawable.c2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image103 = R.drawable.d_mala;
                    image203 = R.drawable.d2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image103 = R.drawable.e_mala;
                    image203 = R.drawable.e2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image103 = R.drawable.f_mala;
                    image203 = R.drawable.f2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image103 = R.drawable.g_mala;
                    image203 = R.drawable.g2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image103 = R.drawable.a_mala;
                    image203 = R.drawable.a2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image103 = R.drawable.h_mala;
                    image203 = R.drawable.h2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
            // 4
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird);
            clickedFourth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image104 = R.drawable.c_mala;
                    image204 = R.drawable.c2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image104 = R.drawable.d_mala;
                    image204 = R.drawable.d2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image104 = R.drawable.e_mala;
                    image204 = R.drawable.e2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image104 = R.drawable.f_mala;
                    image204 = R.drawable.f2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image104 = R.drawable.g_mala;
                    image204 = R.drawable.g2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image104 = R.drawable.a_mala;
                    image204 = R.drawable.a2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image104 = R.drawable.h_mala;
                    image204 = R.drawable.h2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
            // 5
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth);
            clickedFifth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image105 = R.drawable.c_mala;
                    image205 = R.drawable.c2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image105 = R.drawable.d_mala;
                    image205 = R.drawable.d2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image105 = R.drawable.e_mala;
                    image205 = R.drawable.e2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image105 = R.drawable.f_mala;
                    image205 = R.drawable.f2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image105 = R.drawable.g_mala;
                    image205 = R.drawable.g2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image105 = R.drawable.a_mala;
                    image205 = R.drawable.a2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image105 = R.drawable.h_mala;
                    image205 = R.drawable.h2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
            // 6
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth || randomNumber == clickedFifth);


            switch (randomNumber) {
                case 0:
                    image106 = R.drawable.c_mala;
                    image206 = R.drawable.c2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.c_mala);
                    break;
                case 1:
                    image106 = R.drawable.d_mala;
                    image206 = R.drawable.d2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.d_mala);
                    break;
                case 2:
                    image106 = R.drawable.e_mala;
                    image206 = R.drawable.e2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.e_mala);
                    break;
                case 3:
                    image106 = R.drawable.f_mala;
                    image206 = R.drawable.f2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.f_mala);
                    break;
                case 4:
                    image106 = R.drawable.g_mala;
                    image206 = R.drawable.g2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.g_mala);
                    break;
                case 5:
                    image106 = R.drawable.a_mala;
                    image206 = R.drawable.a2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.a_mala);
                    break;
                case 6:
                    image106 = R.drawable.h_mala;
                    image206 = R.drawable.h2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.h_mala);
                    break;
            }
        }else if(mode2==2) { // razkreslna

            switch (randomNumber) {
                case 0:
                    image101 = R.drawable.ccardr;
                    image201 = R.drawable.c2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image101 = R.drawable.dcardr;
                    image201 = R.drawable.d2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image101 = R.drawable.ecardr;
                    image201 = R.drawable.e2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image101 = R.drawable.fcardr;
                    image201 = R.drawable.f2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image101 = R.drawable.gcardr;
                    image201 = R.drawable.g2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image101 = R.drawable.acardr;
                    image201 = R.drawable.a2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image101 = R.drawable.hcardr;
                    image201 = R.drawable.h2cardr;
                    sound1 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
            // 2
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst);
            clickedSecond = randomNumber;


            switch (randomNumber) {
                case 0:
                    image102 = R.drawable.ccardr;
                    image202 = R.drawable.c2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image102 = R.drawable.dcardr;
                    image202 = R.drawable.d2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image102 = R.drawable.ecardr;
                    image202 = R.drawable.e2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image102 = R.drawable.fcardr;
                    image202 = R.drawable.f2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image102 = R.drawable.gcardr;
                    image202 = R.drawable.g2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image102 = R.drawable.acardr;
                    image202 = R.drawable.a2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image102 = R.drawable.hcardr;
                    image202 = R.drawable.h2cardr;
                    sound2 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
            // 3
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond);
            clickedThird = randomNumber;


            switch (randomNumber) {
                case 0:
                    image103 = R.drawable.ccardr;
                    image203 = R.drawable.c2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image103 = R.drawable.dcardr;
                    image203 = R.drawable.d2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image103 = R.drawable.ecardr;
                    image203 = R.drawable.e2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image103 = R.drawable.fcardr;
                    image203 = R.drawable.f2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image103 = R.drawable.gcardr;
                    image203 = R.drawable.g2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image103 = R.drawable.acardr;
                    image203 = R.drawable.a2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image103 = R.drawable.hcardr;
                    image203 = R.drawable.h2cardr;
                    sound3 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
            // 4
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird);
            clickedFourth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image104 = R.drawable.ccardr;
                    image204 = R.drawable.c2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image104 = R.drawable.dcardr;
                    image204 = R.drawable.d2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image104 = R.drawable.ecardr;
                    image204 = R.drawable.e2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image104 = R.drawable.fcardr;
                    image204 = R.drawable.f2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image104 = R.drawable.gcardr;
                    image204 = R.drawable.g2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image104 = R.drawable.acardr;
                    image204 = R.drawable.a2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image104 = R.drawable.hcardr;
                    image204 = R.drawable.h2cardr;
                    sound4 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
            // 5
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth);
            clickedFifth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image105 = R.drawable.ccardr;
                    image205 = R.drawable.c2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image105 = R.drawable.dcardr;
                    image205 = R.drawable.d2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image105 = R.drawable.ecardr;
                    image205 = R.drawable.e2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image105 = R.drawable.fcardr;
                    image205 = R.drawable.f2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image105 = R.drawable.gcardr;
                    image205 = R.drawable.g2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image105 = R.drawable.acardr;
                    image205 = R.drawable.a2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image105 = R.drawable.hcardr;
                    image205 = R.drawable.h2cardr;
                    sound5 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
            // 6
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth || randomNumber == clickedFifth);


            switch (randomNumber) {
                case 0:
                    image106 = R.drawable.ccardr;
                    image206 = R.drawable.c2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.c);
                    break;
                case 1:
                    image106 = R.drawable.dcardr;
                    image206 = R.drawable.d2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.d);
                    break;
                case 2:
                    image106 = R.drawable.ecardr;
                    image206 = R.drawable.e2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.e);
                    break;
                case 3:
                    image106 = R.drawable.fcardr;
                    image206 = R.drawable.f2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.f);
                    break;
                case 4:
                    image106 = R.drawable.gcardr;
                    image206 = R.drawable.g2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.g);
                    break;
                case 5:
                    image106 = R.drawable.acardr;
                    image206 = R.drawable.a2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.a);
                    break;
                case 6:
                    image106 = R.drawable.hcardr;
                    image206 = R.drawable.h2cardr;
                    sound6 = MediaPlayer.create(this, R.raw.h);
                    break;
            }
        }else if(mode2==3) { // dwukreslna

            switch (randomNumber) {
                case 0:
                    image101 = R.drawable.c_dwukreslna_cardr;
                    image201 = R.drawable.c2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image101 = R.drawable.d_dwukreslna_cardr;
                    image201 = R.drawable.d2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image101 = R.drawable.e_dwukreslna_cardr;
                    image201 = R.drawable.e2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image101 = R.drawable.f_dwukreslna_cardr;
                    image201 = R.drawable.f2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image101 = R.drawable.g_dwukreslna_cardr;
                    image201 = R.drawable.g2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image101 = R.drawable.a_dwukreslna_cardr;
                    image201 = R.drawable.a2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image101 = R.drawable.h_dwukreslna_cardr;
                    image201 = R.drawable.h2_dwukreslna_cardr;
                    sound1 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
            // 2
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst);
            clickedSecond = randomNumber;


            switch (randomNumber) {
                case 0:
                    image102 = R.drawable.c_dwukreslna_cardr;
                    image202 = R.drawable.c2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image102 = R.drawable.d_dwukreslna_cardr;
                    image202 = R.drawable.d2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image102 = R.drawable.e_dwukreslna_cardr;
                    image202 = R.drawable.e2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image102 = R.drawable.f_dwukreslna_cardr;
                    image202 = R.drawable.f2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image102 = R.drawable.g_dwukreslna_cardr;
                    image202 = R.drawable.g2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image102 = R.drawable.a_dwukreslna_cardr;
                    image202 = R.drawable.a2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image102 = R.drawable.h_dwukreslna_cardr;
                    image202 = R.drawable.h2_dwukreslna_cardr;
                    sound2 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
            // 3
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond);
            clickedThird = randomNumber;


            switch (randomNumber) {
                case 0:
                    image103 = R.drawable.c_dwukreslna_cardr;
                    image203 = R.drawable.c2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image103 = R.drawable.d_dwukreslna_cardr;
                    image203 = R.drawable.d2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image103 = R.drawable.e_dwukreslna_cardr;
                    image203 = R.drawable.e2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image103 = R.drawable.f_dwukreslna_cardr;
                    image203 = R.drawable.f2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image103 = R.drawable.g_dwukreslna_cardr;
                    image203 = R.drawable.g2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image103 = R.drawable.a_dwukreslna_cardr;
                    image203 = R.drawable.a2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image103 = R.drawable.h_dwukreslna_cardr;
                    image203 = R.drawable.h2_dwukreslna_cardr;
                    sound3 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
            // 4
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird);
            clickedFourth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image104 = R.drawable.c_dwukreslna_cardr;
                    image204 = R.drawable.c2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image104 = R.drawable.d_dwukreslna_cardr;
                    image204 = R.drawable.d2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image104 = R.drawable.e_dwukreslna_cardr;
                    image204 = R.drawable.e2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image104 = R.drawable.f_dwukreslna_cardr;
                    image204 = R.drawable.f2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image104 = R.drawable.g_dwukreslna_cardr;
                    image204 = R.drawable.g2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image104 = R.drawable.a_dwukreslna_cardr;
                    image204 = R.drawable.a2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image104 = R.drawable.h_dwukreslna_cardr;
                    image204 = R.drawable.h2_dwukreslna_cardr;
                    sound4 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
            // 5
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth);
            clickedFifth = randomNumber;


            switch (randomNumber) {
                case 0:
                    image105 = R.drawable.c_dwukreslna_cardr;
                    image205 = R.drawable.c2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image105 = R.drawable.d_dwukreslna_cardr;
                    image205 = R.drawable.d2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image105 = R.drawable.e_dwukreslna_cardr;
                    image205 = R.drawable.e2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image105 = R.drawable.f_dwukreslna_cardr;
                    image205 = R.drawable.f2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image105 = R.drawable.g_dwukreslna_cardr;
                    image205 = R.drawable.g2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image105 = R.drawable.a_dwukreslna_cardr;
                    image205 = R.drawable.a2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image105 = R.drawable.h_dwukreslna_cardr;
                    image205 = R.drawable.h2_dwukreslna_cardr;
                    sound5 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
            // 6
            do {
                randomNumber = (short) generator.nextInt(7);
            } while (randomNumber == clickedFirst || randomNumber == clickedSecond || randomNumber == clickedThird || randomNumber == clickedFourth || randomNumber == clickedFifth);


            switch (randomNumber) {
                case 0:
                    image106 = R.drawable.c_dwukreslna_cardr;
                    image206 = R.drawable.c2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.c_dwukreslna);
                    break;
                case 1:
                    image106 = R.drawable.d_dwukreslna_cardr;
                    image206 = R.drawable.d2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.d_dwukreslna);
                    break;
                case 2:
                    image106 = R.drawable.e_dwukreslna_cardr;
                    image206 = R.drawable.e2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.e_dwukreslna);
                    break;
                case 3:
                    image106 = R.drawable.f_dwukreslna_cardr;
                    image206 = R.drawable.f2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.f_dwukreslna);
                    break;
                case 4:
                    image106 = R.drawable.g_dwukreslna_cardr;
                    image206 = R.drawable.g2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.g_dwukreslna);
                    break;
                case 5:
                    image106 = R.drawable.a_dwukreslna_cardr;
                    image206 = R.drawable.a2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.a_dwukreslna);
                    break;
                case 6:
                    image106 = R.drawable.h_dwukreslna_cardr;
                    image206 = R.drawable.h2_dwukreslna_cardr;
                    sound6 = MediaPlayer.create(this, R.raw.h_dwukreslna);
                    break;
            }
        }
    }
}
