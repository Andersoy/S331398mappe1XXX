package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ResultatActivity extends AppCompatActivity {

    ImageView smilefjes;
    Button hovedmenyButton, nyttSpillButton;
    TextView tilbakemelding, antallRiktigeSvarResTextView, antallFeilSvarResTextView;
    SharedPreferences deltePreferanser;
    int antallOppgaver;
    int antallRiktigSvar;
    int antallFeilSvar;
    String tilbakemeldingstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);

        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        /**Knytter Buttons og textviews til riktig ID*/
        smilefjes= findViewById(R.id.smilefjes);
        tilbakemelding = findViewById(R.id.tilbakemelding);
        hovedmenyButton = findViewById(R.id.hovedmenyResButton);
        nyttSpillButton = findViewById(R.id.spillPaaNyttResButton);
        antallRiktigeSvarResTextView = findViewById(R.id.antRiktSvarResTextView);
        antallFeilSvarResTextView = findViewById(R.id.antFeilSvarResTextView);

        hovedmenyButton.setOnClickListener(view -> {
            finish();
        });

        nyttSpillButton.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), SpillActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        });

        /**Kjører tilbakemeldingsmetode for å regne ut resultater og gi tilbakemelding*/
        giTilbakemelding();
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

    void giTilbakemelding(){

        /** Henter resultatet*/
        antallOppgaver = deltePreferanser.getInt("AntallOppgaverForrige", 0);
        antallRiktigSvar = deltePreferanser.getInt("AntallRiktigeForrige", 0);
        antallFeilSvar = deltePreferanser.getInt("AntallFeilForrige", 0);

        /**Gir skriftlig og visuell tilbakemelding basert på resultat*/
        if((antallOppgaver*0.9)<=antallRiktigSvar){
            smilefjes.setImageResource(R.drawable.smil);
            tilbakemeldingstring = getString(R.string.positivTilbakemelding);
        }
        else if((antallOppgaver*0.6)<=antallRiktigSvar){
            smilefjes.setImageResource(R.drawable.meh);
            tilbakemeldingstring = getString(R.string.noytralTilbakemelding);
        }
        else{
            smilefjes.setImageResource(R.drawable.sur);
            tilbakemeldingstring = getString(R.string.motiverendeTilbakemelding);
        }

        /** Skriver tilbakemelding til skjerm*/
        tilbakemelding.setText(tilbakemeldingstring);
        antallRiktigeSvarResTextView.setText(""+ antallRiktigSvar);
        antallFeilSvarResTextView.setText(""+antallFeilSvar);
    }
}