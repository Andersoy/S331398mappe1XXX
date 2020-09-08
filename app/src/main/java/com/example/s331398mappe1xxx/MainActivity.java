package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // TODO: Legge inn private der egnet i alle aktiviteter?
    // TODO: Landscapemode
    // TODO: Juster layout etter designregler

    // TODO: lag foreldre og barn i manifest fila
    // TODO: lag fragmenter for ja nei boksene
    // TODO: lag språk sjekk i alle oncreate metodene
    // TODO: Reformater prefrences navn (og evt til xml fil)
    // TODO: Reformater preferanser til en preferanse aktivitet
    // TODO: Sjekke at fonten er konsekvendt
    // TODO: ikoner på lever og slett på spillaktiviteten
    // TODO: tommel opp og ned på resultat
    // TODO: fix bug ked seltta tall
    



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