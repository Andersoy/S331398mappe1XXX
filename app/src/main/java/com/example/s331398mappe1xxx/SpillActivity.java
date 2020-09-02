package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class SpillActivity extends AppCompatActivity {
    //TODO: Lage edittext felt under oppgavetextview og tilbakeknapp og leverknapp.
    //TODO: Lag counter på antall riktige
    //TODO: Legge inn private i alle aktiviteter?
    private int antallOppgaver;
    private TextView oppgaveTextView, innfyllingTextview;
    Button knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9, knapp0;
    Button avsluttSpillKnapp, slettKnapp, leverKnapp;
    private int teller;
    private int antallRiktigeSvar;
    private int antallGaleSvar;

    /**Oppretter arraylist og random for å kunne lage en liste med tilfeldige tall*/
    ArrayList<Integer> tilfeldigeTall = new ArrayList<Integer>();

    //Lager nye array basert på array fra values/array.xml
    String[] oppgaver;
    int[] arraySvar;

    SharedPreferences deltePreferanser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        antallOppgaver = deltePreferanser.getInt("AntallOppgaver", 0);

        oppgaveTextView = findViewById(R.id.oppgaverTextView);
        innfyllingTextview = findViewById(R.id.innfyllingTextview);
        innfyllingTextview.setText("");

        avsluttSpillKnapp = findViewById(R.id.avsluttSpillKnapp);
        slettKnapp = findViewById(R.id.slettKnapp);
        leverKnapp = findViewById(R.id.leverKnapp);

        Button[] tallknapper = {knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9};
        for(int j = 0; j <tallknapper.length; j++){
            int knappverdi = j;
            String knappnavn = "knapp"+j;
            tallknapper[j] = findViewById(getResources().getIdentifier(knappnavn, "id", getPackageName()));
            tallknapper[j].setOnClickListener((View v) -> { fyllInn(knappverdi); });
        }

        slettKnapp.setOnClickListener(view -> {
            String svar = innfyllingTextview.getText().toString();
            if(svar.length() > 0){
                svar = svar.substring(0,svar.length()-1);
            }
            innfyllingTextview.setText(svar);
        });

        leverKnapp.setOnClickListener(view -> {
            int svar = Integer.parseInt(innfyllingTextview.getText().toString());
            kontroll(svar);
        });

        avsluttSpillKnapp.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent byttTilNyActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(byttTilNyActivity);
                        finish();
                    case DialogInterface.BUTTON_NEGATIVE:
                        //do nothing
                        break;
                }
            };
            String erDuSikker = getString(R.string.er_du_sikker);
            String ja= getString(R.string.ja);
            String nei = getString(R.string.nei);
            AlertDialog.Builder builder = new AlertDialog.Builder(SpillActivity.this);
            builder.setMessage(erDuSikker).setPositiveButton(ja, dialogClickListener).setNegativeButton(nei, dialogClickListener).show();
        });

        startSpill(antallOppgaver);
    }

    void startSpill(int antallOppgaver){

        oppgaver = getResources().getStringArray(R.array.Oppgaver);
        arraySvar = getResources().getIntArray(R.array.Svar);
        teller = 0;
        antallRiktigeSvar = 0;
        antallGaleSvar = 0;
        Random randomGenerator = new Random();


        /**lager en liste med tilfeldige tall mellom 1 og 25. Listen er like lang som antall oppgaver man har valgt*/
        /** og ingen tall gjentar seg.*/
        while (tilfeldigeTall.size() < antallOppgaver) {
            int random = randomGenerator.nextInt(25);
            if (!tilfeldigeTall.contains(random)) {
                tilfeldigeTall.add(random);
            }
        }

        /**skriver første oppgave til skjerm*/
        oppgaveTextView.setText(oppgaver[tilfeldigeTall.get(teller)]);
    }

    void fyllInn(int tall){
        String svar = innfyllingTextview.getText().toString();
        if(svar.length() > 0){
            svar = svar + tall;
        }
        else{
            svar = String.valueOf(tall);
        }
        innfyllingTextview.setText(svar);

    }

    /** kontrollerer om spilleren har trykket på riktig svar.*/
     void kontroll(int tall){

        int intSvar = arraySvar[tilfeldigeTall.get(teller)];

        if (intSvar == tall) {
            oppgaveTextView.setText(tall+" er riktig svar!");
            antallRiktigeSvar++;
            oppgaveTextView.setTextSize(30);
        } else {
            oppgaveTextView.setText(tall+" er feil.\n Riktig svar var: "+intSvar);
            antallGaleSvar++;
            oppgaveTextView.setTextSize(30);
        }

         /** Følgende utføres for å få programmet til å vente 2 sekunder mellom spørsmålene slik at
          * spilleren kan se om de har svar riktig eller galt*/
            //TODO: Fikse slik at man ikke kan dobbelklikke seg videre i spørsmålene
         final Handler barnevakt = new Handler();
         barnevakt.postDelayed(() -> {
             teller++;
             nyttRegnestykke();
         }, 200);
    }

    void nyttRegnestykke(){
        /** Skriver nytt regnestykke til skjerm hvis man ikke har fullført alle*/
        if (teller < antallOppgaver) {
            oppgaveTextView.setTextSize(36);
            oppgaveTextView.setText(oppgaver[tilfeldigeTall.get(teller)]);
        } else {
            /** Lagrer antallRiktigeSvar, antallGaleSvar og antallOppgaver til statistikksiden.*/
            lagre();

            /**Bytter til resultatsiden*/
            Intent byttTilNyActivity = new Intent(getApplicationContext(), ResultatActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        }
    }

    void lagre(){
        SharedPreferences.Editor editor = deltePreferanser.edit();

        /**Oppdaterer "Forrige spill"*/
        editor.putInt("AntallOppgaverForrige", antallOppgaver);
        editor.putInt("AntallRiktigeForrige", antallRiktigeSvar);
        editor.putInt("AntallFeilForrige", antallGaleSvar);

        /**Oppdaterer "Alle spill"*/
        int antSpill = deltePreferanser.getInt("AntallSpill", 0) + 1;
        int totaltOppgaver = deltePreferanser.getInt("TotaltAntOppgaver", 0) + antallOppgaver;
        int totaltRiktig = deltePreferanser.getInt("TotaltRiktig", 0) + antallRiktigeSvar;
        int totaltFeil = deltePreferanser.getInt("TotaltFeil", 0) + antallGaleSvar;
        editor.putInt("AntallSpill", antSpill);
        editor.putInt("TotaltAntOppgaver", totaltOppgaver);
        editor.putInt("TotaltRiktig", totaltRiktig);
        editor.putInt("TotaltFeil", totaltFeil);
        editor.commit();
    }
}

