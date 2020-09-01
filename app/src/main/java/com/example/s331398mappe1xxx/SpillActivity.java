package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class SpillActivity extends AppCompatActivity {
    //TODO: Legge inn private i alle aktiviteter
    private int antallOppgaver;
    private TextView oppgaveTextView;
    private Button avsluttSpillKnapp, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9, knapp0;
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

        avsluttSpillKnapp = findViewById(R.id.avsluttSpillKnapp);

        Button[] tallknapper = {knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9};
        for(int j = 0; j <tallknapper.length-1; j++){
            int knappverdi = j;
            String knappnavn = "knapp"+j;
            tallknapper[j] = findViewById(getResources().getIdentifier(knappnavn, "id", getPackageName()));
            tallknapper[j].setOnClickListener((View v) -> { kontroll(knappverdi); });
        }

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


        //lager en liste med tilfeldige tall mellom 1 og 25. Listen er like lang som antall oppgaver man har valgt
        // og ingen tall gjentar seg.
        while (tilfeldigeTall.size() < antallOppgaver) {
            int random = randomGenerator.nextInt(25);
            if (!tilfeldigeTall.contains(random)) {
                tilfeldigeTall.add(random);
            }
        }
        //skriver første oppgave til skjerm
        oppgaveTextView.setText(oppgaver[tilfeldigeTall.get(teller)]);

    }

    void kontroll(int knappNummer){

        int intSvar = arraySvar[tilfeldigeTall.get(teller)];

        // kontrollerer om spilleren har trykket på riktig svar.
        if (intSvar == knappNummer) {

            //Hvorfor blir ikke dette skrevet ut selv om jeg når jeg ber om 2 sek delay?
            // oppgaveTextView.setText(knappNummer+" er riktig svar!");

            antallRiktigeSvar++;
        } else {
            //oppgaveTextView.setText(knappNummer+" er feil. Riktig svar var: "+svar[teller]);
            antallGaleSvar++;
        }

        // Ønsker å få programmet til å vente 2 sekunder her for at spilleren skal se resultatet.

        //øker teller
        teller++;

        //skriver nytt regnestykke eller avslutter spill.
        nyttRegnestykke();

    }

    void nyttRegnestykke(){
        // Skriver nytt regnestykke til skjerm hvis man ikke har fullført alle:
        if (teller < antallOppgaver) {
            oppgaveTextView.setText(oppgaver[tilfeldigeTall.get(teller)]);
        } else {
            // Lagrer antallRiktigeSvar, antallGaleSvar og antallOppgaver til statistikksiden.
            lagre();

            Intent byttTilNyActivity = new Intent(getApplicationContext(), ResultatActivity.class);
            startActivity(byttTilNyActivity);
        }
    }


    void lagre(){
        SharedPreferences.Editor editor = deltePreferanser.edit();

        //Oppdaterer "Forrige spill"
        editor.putInt("AntallOppgaverForrige", antallOppgaver);
        editor.putInt("AntallRiktigeForrige", antallRiktigeSvar);
        editor.putInt("AntallFeilForrige", antallGaleSvar);

        //Oppdaterer "Alle spill";
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

