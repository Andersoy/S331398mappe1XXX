package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // TODO: Lag appikon og navn på app
    // TODO: Landscapemode
    // TODO: Juster layout etter designregler

    private Button spillKnapp, preferanseKnapp, statistikkKnapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        SharedPreferences.Editor editor = deltePreferanser.edit();

        /**Dersom første gang man åpner spillet settes verdien til 5. Verdien forandres ikke hvis spillet har vært åpnet før.
        velger å definere det på MainActivity for å slippe å teste med if setning i Spillactivity og Preferanseactivity*/

        if(deltePreferanser.getInt("AntallOppgaver", 0)==0) {
            editor.putInt("AntallOppgaver", 5);
            editor.commit();
        }

        spillKnapp = findViewById(R.id.spillKnapp);
        preferanseKnapp = findViewById(R.id.preferanseKnapp);
        statistikkKnapp = findViewById(R.id.statistikkKnapp);

        /** Bytter til SpillActivity*/
        spillKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), SpillActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        });

        /** Bytter til StatistikkActivity*/
        statistikkKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), StatistikkActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        });

        /** Bytter til PreferanseActivity*/
        preferanseKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), PreferanseActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        });
    }
}