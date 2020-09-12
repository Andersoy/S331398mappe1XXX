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

public class PreferanseActivity extends AppCompatActivity {

    private Button oppgaver5Button, oppgaver10Button, oppgaver25Button, norskButton, tyskButton, hovedmenyButton;
    SharedPreferences deltePreferanser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferanse);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);

        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }


        /**Knytter knapper til variabler*/
        oppgaver5Button = findViewById(R.id.oppgaver5Button);
        oppgaver10Button = findViewById(R.id.oppgaver10Button);
        oppgaver25Button = findViewById(R.id.oppgaver25Button);

        hovedmenyButton = findViewById(R.id.hovedmenyPrefButton);

        norskButton = findViewById(R.id.norgeButton);
        tyskButton = findViewById(R.id.tysklandButton);

        /**Markerer knappen som viser antall oppgaver som er valgt*/
        markerKnapp(deltePreferanser.getInt("AntallOppgaver",0));

        /**Definerer onClick-metoder til knappene som bytter antall oppgaver*/
        oppgaver5Button.setOnClickListener(view -> {
            bytteAntallOppgaver(5);
            markerKnapp(5);
        });

        oppgaver10Button.setOnClickListener(view -> {
            bytteAntallOppgaver(10);
            markerKnapp(10);
        });

        oppgaver25Button.setOnClickListener(view -> {
            bytteAntallOppgaver(25);
            markerKnapp(25);
        });

        norskButton.setOnClickListener(view -> forandreSpraak("no"));
        tyskButton.setOnClickListener(view -> forandreSpraak("de"));

        hovedmenyButton.setOnClickListener(view -> {
            finish();
        });
    }

    /**Metode som forandrer skriftstørrelsen på valgt knapp*/
    void markerKnapp(int antallOppgaver){
        if(antallOppgaver==5){
            oppgaver5Button.setTextSize(27);
            oppgaver10Button.setTextSize(14);
            oppgaver25Button.setTextSize(14);
        }
        else if(antallOppgaver==10){
            oppgaver5Button.setTextSize(14);
            oppgaver10Button.setTextSize(27);
            oppgaver25Button.setTextSize(14);
        }
        else {
            oppgaver5Button.setTextSize(14);
            oppgaver10Button.setTextSize(14);
            oppgaver25Button.setTextSize(27);
        }
    }
    /**Metode som oppdaterer antall oppgaver i sharedpreferences*/
    void bytteAntallOppgaver(int antallOpg){
        SharedPreferences.Editor editor = deltePreferanser.edit();
        editor.putInt("AntallOppgaver", antallOpg);
        editor.commit();
    }

    /**Metode som bytter språk*/
    private void forandreSpraak(String landskode){
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