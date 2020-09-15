package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SpillActivity extends AppCompatActivity implements KonfirmasjonsDialog.DialogClickListener{

    private TextView oppgaveTextView, erlikTextview, innfyllingTextview, riktigeCounter, feilCounter;
    Button knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9;
    private ImageButton slettKnapp, avsluttSpillKnapp, leverKnapp;;
    private ImageView understrek;

    String[] utvalgteOppgaver;
    int[] utvalgteSvar;
    private int teller;
    private int antallOppgaver;
    private int antallRiktigeSvar;
    private int antallGaleSvar;
    private boolean venter = false;
    int riktigSvar;
    int brukerSvar;

    SharedPreferences deltePreferanser;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        editor = deltePreferanser.edit();

        /** Kontrollerer at riktig språk valgt ved onCreate, for å unngå at det skifter tilbake til default språk ved rotasjon.*/
        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        oppgaveTextView = findViewById(R.id.oppgaverTextView);
        erlikTextview = findViewById(R.id.erlikTextView);
        innfyllingTextview = findViewById(R.id.innfyllingTextview);
        riktigeCounter = findViewById(R.id.riktigeCounter);
        feilCounter = findViewById(R.id.feilCounter);
        understrek = findViewById(R.id.understrek);

        avsluttSpillKnapp = findViewById(R.id.avsluttSpillKnapp);
        slettKnapp = findViewById(R.id.slettKnapp);
        leverKnapp = findViewById(R.id.leverKnapp);

        /**Forløkke som løper gjennom en array av tallknappene og gir dem onClick() og knytter dem til view-IDene sine */
        Button[] tallknapper = {knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9};
        for(int j = 0; j <tallknapper.length; j++){
            int knappverdi = j;
            String knappnavn = "knapp"+j;
            tallknapper[j] = findViewById(getResources().getIdentifier(knappnavn, "id", getPackageName()));
            tallknapper[j].setOnClickListener((View v) -> { fyllInn(knappverdi); });
        }

        /**Sletter ett og ett siffer fra innføringsfeltet ved knapptrykk */
        slettKnapp.setOnClickListener(view -> {
            String svar = innfyllingTextview.getText().toString();
            if(svar.length() > 0){
                svar = svar.substring(0,svar.length()-1);
            }
            innfyllingTextview.setText(svar);
        });

        /** Kjører metode som sjekker om brukerens svar er riktig og gir tilbakemelding*/
        leverKnapp.setOnClickListener(view -> {
            sjekkBrukerSvar();
        });
        /** Kjører metode som åpner dialogboks og spør om spiller ønsker å avslutte spillet ved knapptrykk */
        avsluttSpillKnapp.setOnClickListener(view -> {
            visKonfirmasjonsDialog();
        });

        startSpill();
    }

    /**Overstyrer tilbakeknappen på mobilen til å åpne avsluttdialogen*/
    @Override
    public void onBackPressed() {
        visKonfirmasjonsDialog();
    }

    @Override
    public void onYesClick() {
        editor.putBoolean("aktivtSpill", false);
        finish();
    }

    @Override
    public void onNoClick() {
        return;
    }

    /**Dialogen gir brukeren muligheten til å kansellere avsluttingen av spillet*/
    public void visKonfirmasjonsDialog() {
        DialogFragment dialog = new KonfirmasjonsDialog();
        dialog.show(getSupportFragmentManager(), "Avslutt");
    }

    /**Metode som bytter språk*/
    public void forandreSpraak(String landskode){

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

    void startSpill(){

        /**Henter antall oppgaver fra sharedpreferences*/
        antallOppgaver = deltePreferanser.getInt("AntallOppgaver", 0);

        /**Sjekker om et aktivt spill er i gang for å unngå nullstilling ved rotasjon og går inn i if-setningen
         * dersom et spill ikke allerede er i gang*/
        if(!deltePreferanser.getBoolean("aktivtSpill", false)){

            teller = 0;
            String[] oppgaverOgSvarIFellesArray = LagNyttSettMedOppgaver();
            String valgteOppgaver = oppgaverOgSvarIFellesArray[0];
            String valgteSvar = oppgaverOgSvarIFellesArray[1];

            /**Lagrer oppgave-og svar-strenger, teller og spillstatus i sharedpreferences*/
            editor.putString("aktivRundeOppgaver", valgteOppgaver);
            editor.putString("aktivRundeSvar", valgteSvar);
            editor.putInt("oppgaveTeller", teller);
            editor.putBoolean("aktivtSpill", true);
            editor.commit();
        }
        /**Dersom et spill allerede er i gang(aktivtSpill==true) hentes aktive verdier fra sharedpreferences*/
        else{
            teller = deltePreferanser.getInt("oppgaveTeller", 0);
            antallRiktigeSvar = deltePreferanser.getInt("aktiveRiktige",0);
            antallGaleSvar = deltePreferanser.getInt("aktiveFeil",0);
            riktigeCounter.setText(String.valueOf(antallRiktigeSvar));
            feilCounter.setText(String.valueOf(antallGaleSvar));
        }

        /**Henter svar og oppgavestrenger og konverterer dem tilbake til arrays*/
        hentOppgaveneFraPreferanser();

        /**skriver første oppgave til skjerm*/
        nyttRegnestykke();
    }

    void hentOppgaveneFraPreferanser(){
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
    }

    String[] LagNyttSettMedOppgaver(){

        /**Oppretter array av oppgavene og svarene fra values/arrays.xml*/
        String [] arrayAlleOppgaver = getResources().getStringArray(R.array.Oppgaver);
        int[] arrayAlleSvar = getResources().getIntArray(R.array.Svar);

        /**lager en liste med tilfeldige tall mellom 1 og 25. Listen er like lang som antall oppgaver man har valgt*/
        /** og ingen tall gjentar seg.*/
        Random randomGenerator = new Random();
        ArrayList<Integer> tilfeldigeTall = new ArrayList<Integer>();
        while (tilfeldigeTall.size() < antallOppgaver) {
            int random = randomGenerator.nextInt(25);
            if (!tilfeldigeTall.contains(random)) {
                tilfeldigeTall.add(random);
            }
        }

        /** Henter oppgaver og svar fra oppgave-/svar-array ved hjelp av de ikke-repeterende tilfeldige
         * indeksene og legger dem inn i strenger,separert med komma, for lagring i sharedpreferences,
         * (Lagrer det som streng separert med komma istedenfor jason)*/
        String valgteOppgaver = "";
        String valgteSvar = "";
        for(int i = 0; i < antallOppgaver; i++){
            valgteOppgaver += arrayAlleOppgaver[tilfeldigeTall.get(i)]+",";
        }
        for(int i = 0; i < antallOppgaver; i++){
            valgteSvar += arrayAlleSvar[tilfeldigeTall.get(i)]+",";
        }

        /** Returnerer de valgte oppgavene og svarene i en felles array*/
        String[] oppgaverOgSvar = new String[2];
        oppgaverOgSvar[0] = valgteOppgaver;
        oppgaverOgSvar[1] = valgteSvar;

        return oppgaverOgSvar;
    }

    /**Fyller inn ønsket siffer bakerst i innføringsfeltet */
    void fyllInn(int tall){
        String svar = innfyllingTextview.getText().toString();
        /** sikrer at kanppen ikke gjør noe i ventemodus eller øker siffer antallet over int grensen(8)*/
        if(venter || svar.length() > 8){ return; }
        svar = svar + tall;
        innfyllingTextview.setText(svar);
    }

    /** kontrollerer om spilleren har trykket på riktig svar og gir tilbakemelding*/
     void sjekkBrukerSvar(){
         /** sikrer at kanppen ikke gjør noe når brukeren får tilbakemelding*/
         if(venter){
             return;
         }

         /**Sjekker om tekstfeltet ikke er tomt, setter flagg -1 for tomt felt siden det ikke kan parses til int*/
         String svarStreng = innfyllingTextview.getText().toString();
         brukerSvar = -1;
         if (!svarStreng.isEmpty()){
             brukerSvar = Integer.parseInt(svarStreng);
         }

         /**Gjemmer oppgaverelaterte views for å klargjøre for tilbakemelding */
         innfyllingTextview.setText("");
         erlikTextview.setText("");
         understrek.setVisibility(View.INVISIBLE);

         /**Sjekker om svaret er riktig og gir tilbakemelding basert på svaret. Øker også tellere som
          * holder styr på antall riktige og gale svar*/
         riktigSvar = utvalgteSvar[teller];

         /** sjekker om svaret er riktig fel eller blank og gir tilhørende tilbakemelding*/
         if (riktigSvar == brukerSvar) {
             giTilbakemelding("riktigSvar");
         } else if (brukerSvar == -1){
             giTilbakemelding("blanktSvar");
         } else{
             giTilbakemelding("feilSvar");
         }

    }

    void giTilbakemelding(String tilbakemeldingsType) {

        String tilbakemelding;

        switch (tilbakemeldingsType) {
            case "riktigSvar":
                tilbakemelding = brukerSvar + " " + getString(R.string.riktigSvarTilbakemelding);
                antallRiktigeSvar++;
                riktigeCounter.setText(String.valueOf(antallRiktigeSvar));
                editor.putInt("aktiveRiktige", antallRiktigeSvar);
                break;
            case "blanktSvar":
                tilbakemelding = getString(R.string.blanktSvarTilbakemelding) + " " + riktigSvar;
                antallGaleSvar++;
                feilCounter.setText(String.valueOf(antallGaleSvar));
                editor.putInt("aktiveFeil", antallGaleSvar);
                break;
            default:
                tilbakemelding = brukerSvar + " " + getString(R.string.feilSvarTilbakemelding) + " " + riktigSvar;
                antallGaleSvar++;
                feilCounter.setText(String.valueOf(antallGaleSvar));
                editor.putInt("aktiveFeil", antallGaleSvar);
                break;
        }
        editor.commit();
        oppgaveTextView.setText(tilbakemelding);
        oppgaveTextView.setTextSize(30);

        /** Følgende utføres for å få programmet til å vente 2 sekunder mellom spørsmålene slik at
         * spilleren kan se om de har svar riktig eller galt*/
        venter = true;
        final Handler barnevakt = new Handler();
        barnevakt.postDelayed(() -> {
            /**Øker teller som viser hvilken oppgave/svar man er på.*/
            teller++;
            editor.putInt("oppgaveTeller", teller);
            editor.commit();
            venter = false;
            nyttRegnestykke();
            //TODO: Øke ventetiden før levering av app?
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
            lagreFerdigSpill();

            /**Går til resultatsiden*/
            Intent byttTilNyActivity = new Intent(getApplicationContext(), ResultatActivity.class);
            startActivity(byttTilNyActivity);
            finish();
        }
    }

    void lagreFerdigSpill(){
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