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
    //ting å gå gjennom felles:
    // TODO: Se over og kommenter koden noe
    // TODO: Landscapemode
    // TODO: Juster layout etter designregler
    // TODO: Rapport

    //ting som kan gjøres hver for oss:
    // TODO: Oversette gjenværende til tysk
    // TODO: Lag universell endre språk metode for alle klassene

    private Button spillKnapp, preferanseKnapp, statistikkKnapp;
    private SharedPreferences deltePreferanser;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        editor = deltePreferanser.edit();

        /**Dersom første gang man åpner spillet settes verdien til 5. Verdien forandres ikke hvis spillet har vært åpnet før.
        velger å definere det på MainActivity for å slippe å teste med if setning i Spillactivity og Preferanseactivity*/

        if(deltePreferanser.getInt("AntallOppgaver", 0)==0) {
            editor.putInt("AntallOppgaver", 5);
            editor.commit();
        }

        /**Dersom første gang man spiller lagres landskode "no" i sharedpreferences */
        if(deltePreferanser.getString("spraakKode", null) == null){
            forandreSpraak("no");
        }

        /** Kontrollerer at riktig språk valgt ved onCreate() for å unngå at det skifter tilbake til default språk ved rotasjon.
         *  Denne kontrollen er lagt inn i alle aktiviteter*/
        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

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
     private void forandreSpraak(String landskode){
        editor = deltePreferanser.edit();
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

    //TODO: Spør lærer
    @Override
    protected void onResume() {
        super.onResume();
        /** Kontrollerer at riktig språk er valgt når man returnerer fra en barnaktivitet*/
        if(deltePreferanser.getBoolean("skiftetSpraak", false)){
            editor.putBoolean("skiftetSpraak", false);
            editor.commit();
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        /**Nullstiller verdiene i sharedpreferences ved retur fra barneaktiviteter, hovedsaklig spillaktivitet*/
        editor.putBoolean("aktivtSpill", false);
        editor.remove("oppgaveTeller");
        editor.remove("aktiveRiktige");
        editor.remove(("aktiveFeil"));
        editor.commit();
    }
}