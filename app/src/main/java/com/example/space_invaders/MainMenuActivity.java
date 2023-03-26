package com.example.space_invaders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainMenuActivity extends AppCompatActivity {

    private Button btInvaders;
    private Button btMemory;
    private Button btNotes;
    private Button btInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // adding advertisements
        MobileAds.initialize(this, getString(R.string.app_identifier));

        AdView adView = (AdView)findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdView adView2 = (AdView)findViewById(R.id.adView2);
        adView2.loadAd(adRequest);

        AdView adView3 = (AdView)findViewById(R.id.adView);
        adView3.loadAd(adRequest);


        btInvaders = findViewById(R.id.bt_space_invaders);
        btInvaders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                openSpaceInvadersActivity();
            }
        });

        btMemory = findViewById(R.id.bt_matching_game);
        btMemory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                openMemoryGameActivity();
            }
        });

        btNotes = findViewById(R.id.bt_happy_notes);
        btNotes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                openNotesActivity();
            }
        });

        btInstruction = findViewById(R.id.bt_instruction);
        btInstruction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showInstructionDialog();
            }
        });
    }

    void openSpaceInvadersActivity(){
        Intent intent = new Intent(this, SpaceInvadersActivity0.class);
        startActivity(intent);
    }

    void openMemoryGameActivity(){
        Intent intent = new Intent(this, MemoryGameActivity2.class);
        startActivity(intent);
    }

    void openNotesActivity(){
        Intent intent = new Intent(this, NotesActivity0.class);
        startActivity(intent);
    }

    void showInstructionDialog(){
        new AlertDialog.Builder(this).setTitle("Oto instrukcja do wszystkich aktywności w aplikacji.").
                setMessage("Muzyk_Space_Invaders:\n\n" +
                        "To gra, w której musisz oprzeć się inwazji kosmitów atakujących Twój statek." +
                        "Po wciśnięciu OK, kliknij gdziekolwiek, aby rozpocząć grę.\n" +
                        "Klikając w prawy lub w lewy dolny róg ekranu, poruszasz swoim statkiem.\n" +
                        "Żeby wystrzelić pocisk, kliknij gdziekolwiek na górnej połowie ekranu./ n" +
                        "Każdy z kosmitów ma na sobie obrazek z nutką na pięciolinii. Jaka to nutka?\n" +
                        "Musisz to wiedzieć, aby obronić się przed inwazją!\n" +
                        "Pamiętaj, możesz strzelać tylko do kosmitów, którzy mają na sobie obrazek nutki na pięciolinii, który odpowiada pokazanej w lewym górnym rogu nucie literowej i zagranemu dźwiękowi.\n" +
                        "Uważaj! Jeśli zestrzelisz niewłaściwego kosmitę, stracisz jedno z żyć.\n\n\n\n" +

                        "Muzyczne_Memory\n" +
                        "Tryb jednego gracza:\n\n" +
                        "Grając w tą grę ćwiczysz zarowno swoją pamięć, jak i umiejętność czytania z nut!\n" +
                        "Po wciśnięciu OK, rozpopczniesz grę, a co za tym idzie- ruszy stoper odmierzający czas Twoej rozgrywki.\n" +
                        "Aby ukończyć grę, musisz skompletować wszystkie pary.\n" +
                        "Żeby skompletować parę musisz odkryć w jednej turze kartę posiadającą na odwrocie nutę literową/ n\" +\n" +
                        "oraz drugą kartę, zawierającą jej odpowiednik na pięciolinii.\n" +
                        "Musisz połączyć wszystkie nuty aby zakończyć rozgrywkę.\n" +
                        "Spiesz się, bo czas będzie uciekał!\n\n" +

                        "Tryb rywalizacji dwóch graczy:\n\n" +
                        "Grając w tą grę oboje ćwiczycie zarowno swoją pamięć, jak i umiejętność czytania z nut.\n" +
                        "Po wciśnięciu OK, rozpopczniecie grę.\n" +
                        "Aby ukończyć grę, musicie skompletować wszystkie pary.\n" +
                        "Żeby skompletować parę musicie odkryć w jednej turze kartę posiadającą na odwrocie nutę literową\n" +
                        "oraz drugą kartę, zawierającą jej odpowiednik na pięciolinii.\n" +
                        "Za każdą znalecioną parę otrzymuje się jeden punkt, a na koniec wygrywa ten, kto zdobędzie najwięcej punktów.\n" +
                        "Rozgrywkę rozpoczyna gracz pierwszy, oznaczony ciemniejszym kolorem.\n" +
                        "Jeśli odnajdzie on parę, może On kontynuować swoją rozgrywkę szukając następnej.\n" +
                        "Jeśli jednak odkryje dwie nie pasujące do siebie karty, jego tura dobiega końca i rozpoczyna się aktywna rozgrywka drugiego gracza.\n" +
                        "Nazwa gracza, który w danej turze powinien być aktywny zawsze oznaczona jest kolorem ciemniejszym, a gracza czekającego na swój ruch- jaśniejszymym.\n" +
                        "Niech wygra najlepszy!\n\n\n\n" +

                        "Wesołe Nutki:\n\n" +
                        "Po prostu kliknij na jedną z kolorowych nutek na pięciolinii, aby zagrać dźwięk!\n"
                ).setPositiveButton("ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        })
                .create().show();
    }
}
