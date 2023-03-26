package com.example.space_invaders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SpaceInvadersActivity0 extends AppCompatActivity {

    private Button bt_standard;
    private Button bt_hardcore;
    SharedPreferences prefs;
    ImageView iv;

    private void showDialog(){

        new AlertDialog.Builder(this).setTitle("Nie tak łatwo!").
                setMessage("Musisz najpierw przejść całą gę w trybie standardowym\n" +
                        " i ukończyć level 7, aby odblokować ten tryb gry!\n\n" +
                        "Powodzenia!").
                setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_invaders0);

        prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        final boolean unlocked = prefs.getBoolean("level 7 completed", false);
        if(unlocked) {
            iv = findViewById(R.id.imageView);
            iv.setImageResource(R.drawable.start_space_invaders2);
        }

        bt_standard = (Button) findViewById(R.id.bt_standardowy);
        bt_standard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editor.putBoolean("spaceInvadersGameMode", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    editor.apply();
                }
                openMainActivity();
            }
        });

        bt_hardcore = (Button) findViewById(R.id.bt_hardcore);
        bt_hardcore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(unlocked) {
                    editor.putBoolean("spaceInvadersGameMode", false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        editor.apply();
                    }
                    openMainActivity();
                }else{
                    showDialog();
                }
            }
        });
    }

    protected void openMainActivity(){

        Intent intent = new Intent(this, SpaceInvadersActivity.class);
        startActivity(intent);
    }
}
