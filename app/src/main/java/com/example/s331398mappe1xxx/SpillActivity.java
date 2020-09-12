package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SpillActivity extends AppCompatActivity {

    //TODO: Fikse porblemet med at spillet nullstilles når mobilen roteres

    private TextView oppgaveTextView, erlikTextview, innfyllingTextview, riktigeCounter;
    Button knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9;
    private Button avsluttSpillKnapp, slettKnapp, leverKnapp;
    private ImageView understrek;
    SharedPreferences.Editor editor;
    String[] utvalgteOppgaver;
    int[] utvalgteSvar;

    private int teller;
    private int antallOppgaver;
    private int antallRiktigeSvar;
    private int antallGaleSvar;
    private boolean venter = false;

    SharedPreferences deltePreferanser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        editor = deltePreferanser.edit();

        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){

            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        oppgaveTextView = findViewById(R.id.oppgaverTextView);
        erlikTextview = findViewById(R.id.erlikTextView);
        innfyllingTextview = findViewById(R.id.innfyllingTextview);
        riktigeCounter = findViewById(R.id.riktigeCounter);
        understrek = findViewById(R.id.understrek);

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
            sjekkBrukerSvar();
        });

        avsluttSpillKnapp.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        editor.putBoolean("aktivtSpill", false);
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

        startSpill();
    }

    /**Metode som bytter språk*/
    //TODO: Denne metoden må mest sannslynlig oppdateres.
    public void forandreSpraak(String landskode){

        editor.putString("spraakKode", landskode);
        editor.commit();

        Locale mittSpraak = new Locale(landskode);
        Resources ress = getResources();
        DisplayMetrics visMet = ress.getDisplayMetrics();
        Configuration konfigurasjon = ress.getConfiguration();
        konfigurasjon.locale = mittSpraak;
        ress.updateConfiguration(konfigurasjon, visMet);
        recreate();
    }

    void startSpill(){
        antallOppgaver = deltePreferanser.getInt("AntallOppgaver", 0);

        if(!deltePreferanser.getBoolean("aktivtSpill", false)){
            String [] arrayAlleOppgaver = getResources().getStringArray(R.array.Oppgaver);
            int[] arrayAlleSvar = getResources().getIntArray(R.array.Svar);
            Random randomGenerator = new Random();
            ArrayList<Integer> tilfeldigeTall = new ArrayList<Integer>();
            teller = 0;
            String alleOppgaver = "";
            String alleSvar = "";

            /**lager en liste med tilfeldige tall mellom 1 og 25. Listen er like lang som antall oppgaver man har valgt*/
            /** og ingen tall gjentar seg.*/
            while (tilfeldigeTall.size() < antallOppgaver) {
                int random = randomGenerator.nextInt(25);
                if (!tilfeldigeTall.contains(random)) {
                    tilfeldigeTall.add(random);
                }
            }

            for(int i = 0; i < antallOppgaver; i++){
                    alleOppgaver += arrayAlleOppgaver[tilfeldigeTall.get(i)]+",";
            }

            for(int i = 0; i < antallOppgaver; i++){
                alleSvar += arrayAlleSvar[tilfeldigeTall.get(i)]+",";
            }

            editor.putString("aktivRundeOppgaver", alleOppgaver);
            editor.putString("aktivRundeSvar", alleSvar);
            editor.putBoolean("aktivtSpill", true);
            editor.putInt("oppgaveTeller", teller);
            editor.commit();
        }

        else{
            teller = deltePreferanser.getInt("oppgaveTeller", 0);
            antallRiktigeSvar = deltePreferanser.getInt("aktiveRiktige",0);
            antallGaleSvar = deltePreferanser.getInt("aktiveFeil",0);
            riktigeCounter.setText(String.valueOf(antallRiktigeSvar));
        }
        
        utvalgteOppgaver = new String[antallOppgaver];
        utvalgteSvar = new int[antallOppgaver];

        String mellomlagring = deltePreferanser.getString("aktivRundeOppgaver", null);
        String[] mellomlagringSplit = mellomlagring.split(",");
        for(int i = 0; i<antallOppgaver; i++){
            utvalgteOppgaver[i] = mellomlagringSplit[i];
        }

        mellomlagring = deltePreferanser.getString("aktivRundeSvar", null);
        mellomlagringSplit = mellomlagring.split(",");
        for(int i = 0; i<antallOppgaver; i++){
            utvalgteSvar[i] = Integer.parseInt(mellomlagringSplit[i]);
        }

        /**skriver første oppgave til skjerm*/
        nyttRegnestykke();
    }

    void fyllInn(int tall){
        String svar = innfyllingTextview.getText().toString();
        /** sikrer at kanppen ikke gjør noe i ventemodus eller øker siffer antallet over int grensen*/
        if(venter || svar.length() > 8){
            return;
        }

        svar = svar + tall;
        innfyllingTextview.setText(svar);
    }

    /** kontrollerer om spilleren har trykket på riktig svar og gir tilbakemelding*/
     void sjekkBrukerSvar(){

         /** sikrer at kanppen ikke gjør noe når brukeren får tilbakemelding*/
         if(venter){
             return;
         }

         String svarStreng = innfyllingTextview.getText().toString();
         /** setter flagg -1 for tomt felt siden det ikke kan parses til int*/
         System.out.println("utskrift"+svarStreng);
         int svar = -1;
         if (!svarStreng.isEmpty()){

             svar = Integer.parseInt(svarStreng);
         }

         innfyllingTextview.setText("");
         erlikTextview.setText("");
         understrek.setVisibility(View.INVISIBLE);


         int intSvar = utvalgteSvar[teller];
         String tilbakemelding;
         if (intSvar == svar) {
             tilbakemelding = svar + " " + getString(R.string.riktigSvarTilbakemelding);
             antallRiktigeSvar++;
             riktigeCounter.setText(String.valueOf(antallRiktigeSvar));
             editor.putInt("aktiveRiktige", antallRiktigeSvar);

         } else if (svar == -1){
             tilbakemelding = getString(R.string.blanktSvarTilbakemelding) + " " + intSvar;
             antallGaleSvar++;
             editor.putInt("aktiveFeil", antallGaleSvar);

         } else{
             tilbakemelding = svar + " " + getString(R.string.feilSvarTilbakemelding) + " " + intSvar;
             antallGaleSvar++;
             editor.putInt("aktiveFeil", antallGaleSvar);

         }

         editor.commit();
         oppgaveTextView.setText(tilbakemelding);
         oppgaveTextView.setTextSize(30);

         /** Følgende utføres for å få programmet til å vente 2 sekunder mellom spørsmålene slik at
          * spilleren kan se om de har svar riktig eller galt*/
         venter = true;
         final Handler barnevakt = new Handler();
         barnevakt.postDelayed(() -> {
             teller++;
             editor.putInt("oppgaveTeller", teller);
             editor.commit();
             venter = false;
             nyttRegnestykke();
         }, 1000);
    }

    void nyttRegnestykke(){
        /** Skriver nytt regnestykke til skjerm hvis man ikke har fullført alle*/
        if (teller < antallOppgaver) {
            oppgaveTextView.setTextSize(36);

            oppgaveTextView.setText(utvalgteOppgaver[teller]);

            erlikTextview.setText("= ");
            understrek.setVisibility(View.VISIBLE);
        } else {
            /** Lagrer antallRiktigeSvar, antallGaleSvar og antallOppgaver til statistikksiden.*/
            lagre();

            /**Bytter til resultatsiden*/
            //TODO: Legge inn parent i manifest og fjerne Intent.
            Intent byttTilNyActivity = new Intent(getApplicationContext(), ResultatActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        }
    }

    void lagre(){
        /**Oppdaterer "Forrige spill"*/
        editor.putInt("AntallOppgaverForrige", antallOppgaver);
        editor.putInt("AntallRiktigeForrige", antallRiktigeSvar);
        editor.putInt("AntallFeilForrige", antallGaleSvar);
        editor.putBoolean("aktivtSpill", false);

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

