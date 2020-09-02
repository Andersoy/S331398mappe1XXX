package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultatActivity extends AppCompatActivity {

    ImageView smilefjes;
    Button hovedmenyButton, nyttSpillButton;
    TextView tilbakemelding, antallOppgaverResTextView, antallRiktigeSvarResTextView, antallFeilSvarResTextView;
    SharedPreferences deltePreferanser;
    int antallOppgaver;
    int antallRiktigSvar;
    int antallFeilSvar;
    String tilbakemeldingstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        /**Knytter Buttons og textviews til riktig ID*/
        smilefjes= findViewById(R.id.smilefjes);
        tilbakemelding = findViewById(R.id.tilbakemelding);
        hovedmenyButton = findViewById(R.id.hovedmenyResButton);
        nyttSpillButton = findViewById(R.id.spillPaaNyttResButton);
        antallOppgaverResTextView = findViewById(R.id.antallOpgResTextView);
        antallRiktigeSvarResTextView = findViewById(R.id.antRiktSvarResTextView);
        antallFeilSvarResTextView = findViewById(R.id.antFeilSvarResTextView);


        hovedmenyButton.setOnClickListener(view -> {
            Intent byttTilNyActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(byttTilNyActivity);
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

    void giTilbakemelding(){
        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);

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
        antallOppgaverResTextView.setText(getResources().getString(R.string.antallOppgaver)+antallOppgaver);
        antallRiktigeSvarResTextView.setText(getResources().getString(R.string.antallRiktigeSvar)+ antallRiktigSvar);
        antallFeilSvarResTextView.setText(getResources().getString(R.string.antallFeilSvar)+antallFeilSvar);
    }
}