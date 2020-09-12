package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // TODO: Legge inn private der egnet i alle aktiviteter?
    // TODO: Landscapemode
    // TODO: Juster layout etter designregler
    // TODO: lag fragmenter for ja nei boksene
    // TODO: Sjekke at fonten er konsekvent
    // TODO: ikoner på lever og slett på spillaktiviteten
    // TODO: tommel opp og ned på resultat
    // TODO: Nye oppgaver
    // TODO: Rapport




    private Button spillKnapp, preferanseKnapp, statistikkKnapp;
    SharedPreferences deltePreferanser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        SharedPreferences.Editor editor = deltePreferanser.edit();

        /**Dersom første gang man åpner spillet settes verdien til 5. Verdien forandres ikke hvis spillet har vært åpnet før.
        velger å definere det på MainActivity for å slippe å teste med if setning i Spillactivity og Preferanseactivity*/

        if(deltePreferanser.getInt("AntallOppgaver", 0)==0) {
            editor.putInt("AntallOppgaver", 5);
            editor.commit();
        }

        /**Dersom første gang man spiller lagres landskode "no" i sharedpreferences */
        if(!deltePreferanser.getString("spraakKode", null).equals("no") && !deltePreferanser.getString("spraakKode", null).equals("de")){
            editor.putString("spraakKode", "no");
            editor.commit();
            Locale mittSpraak = new Locale("no");
            Resources ress = getResources();
            DisplayMetrics visMet = ress.getDisplayMetrics();
            Configuration konfigurasjon = ress.getConfiguration();
            konfigurasjon.setLocale(mittSpraak);
            ress.updateConfiguration(konfigurasjon, visMet);
        }

        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        /**Nullstiller verdier ved oppstart*/
        editor.putBoolean("aktivtSpill", false);
        editor.remove("oppgaveTeller");
        editor.remove("aktiveRiktige");
        editor.remove(("aktiveFeil"));
        editor.commit();

        spillKnapp = findViewById(R.id.spillKnapp);
        preferanseKnapp = findViewById(R.id.preferanseKnapp);
        statistikkKnapp = findViewById(R.id.statistikkKnapp);

        /** Bytter til SpillActivity*/
        spillKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), SpillActivity.class);
            startActivity(byttTilNyActivity);

        });

        /** Bytter til StatistikkActivity*/
        statistikkKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), StatistikkActivity.class);
            startActivity(byttTilNyActivity);

        });

        /** Bytter til PreferanseActivity*/
        preferanseKnapp.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), PreferanseActivity.class);
            startActivity(byttTilNyActivity);

        });
    }

    /**Metode som bytter språk*/
     public void forandreSpraak(String landskode){
        SharedPreferences.Editor editor = deltePreferanser.edit();
        editor.putString("spraakKode", landskode);
        editor.commit();

        Locale mittSpraak = new Locale(landskode);
        Resources ress = getResources();
        DisplayMetrics visMet = ress.getDisplayMetrics();
        Configuration konfigurasjon = ress.getConfiguration();
         konfigurasjon.setLocale(mittSpraak);
        ress.updateConfiguration(konfigurasjon, visMet);
        recreate();
    }
}