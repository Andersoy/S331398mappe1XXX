package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class StatistikkActivity extends AppCompatActivity implements KonfirmasjonsDialog.DialogClickListener{

    private Button nullstillButton,  hovedmenyButton;
    private TextView antOppgForrigeTextView, antRiktigeForrigeTextView, antFeilForrigeTextView;
    private TextView totAntSpillTextView, totAntOpgTextView, totAntRktgTextView, totAntFeilTextView;

    private int antallOppgaverForrige, antallRiktigSvarForrige, antallFeilSvarForrige;
    private int totAntSpill, totAntOppg, totAntRiktig, totAntFeil;

    private SharedPreferences deltePreferanser;
    private SharedPreferences.Editor editor;
    private DialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistikk);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);
        editor = deltePreferanser.edit();

        /**sjekker språket*/
        if(!getResources().getConfiguration().locale.toString().equals(deltePreferanser.getString("spraakKode", null))){
            forandreSpraak(deltePreferanser.getString("spraakKode", null));
        }

        /**Knytter textview og knapper til variabler*/
        antOppgForrigeTextView = findViewById(R.id.antOpgForrigeTextView);
        antRiktigeForrigeTextView = findViewById(R.id.antRiktigeSvarForrigeTextView);
        antFeilForrigeTextView = findViewById(R.id.antFeilSvarForrigeTextView);

        totAntSpillTextView = findViewById(R.id.totaltAntSpillAlleTextView);
        totAntOpgTextView = findViewById(R.id.totAntOpgAlleTextView);
        totAntRktgTextView = findViewById(R.id.totAntRiktigeAlleTextView);
        totAntFeilTextView = findViewById(R.id.totAntFeilSvarAlleTextView);

        nullstillButton = findViewById(R.id.nullstillButton);
        hovedmenyButton = findViewById(R.id.hovedmenyStatButton);

        /**Setter onClick med dialog til nullstillknappen*/
        nullstillButton.setOnClickListener(view -> {
                visKonfirmasjonsDialog();
            });

        /**onClick-metode som bytter activity til hovedmeny*/
        hovedmenyButton.setOnClickListener(view -> {
            finish();
        });

        hentStattestikk();

        visStattestikk();
    }

    private void hentStattestikk(){
        antallOppgaverForrige = deltePreferanser.getInt("AntallOppgaverForrige", 0);
        antallRiktigSvarForrige = deltePreferanser.getInt("AntallRiktigeForrige", 0);
        antallFeilSvarForrige = deltePreferanser.getInt("AntallFeilForrige", 0);

        totAntSpill = deltePreferanser.getInt("AntallSpill", 0);
        totAntOppg = deltePreferanser.getInt("TotaltAntOppgaver", 0);
        totAntRiktig = deltePreferanser.getInt("TotaltRiktig", 0);
        totAntFeil = deltePreferanser.getInt("TotaltFeil", 0);
    }

    private void visStattestikk(){
        antOppgForrigeTextView.setText(getResources().getString(R.string.antallOppgaver)+" "+antallOppgaverForrige);
        antRiktigeForrigeTextView.setText(""+antallRiktigSvarForrige);
        antFeilForrigeTextView.setText(""+antallFeilSvarForrige);

        totAntSpillTextView.setText(getResources().getString(R.string.antallSpill)+" "+totAntSpill);
        totAntOpgTextView.setText(getResources().getString(R.string.antallOppgaver)+" "+totAntOppg);
        totAntRktgTextView.setText(""+totAntRiktig);
        totAntFeilTextView.setText(""+totAntFeil);
    }

    /**Metode som bytter språk*/
    private void forandreSpraak(String landskode){
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

    private void nullstillVerdier(){
        /** Sletter spesifikke verdier for å nullstille statistikken, ikke preferanser*/
        editor.remove("AntallOppgaverForrige");
        editor.remove("AntallRiktigeForrige");
        editor.remove("AntallFeilForrige");
        editor.remove("AntallSpill");
        editor.remove("TotaltAntOppgaver");
        editor.remove("TotaltRiktig");
        editor.remove("TotaltFeil");
        editor.commit();
    }
    @Override
    public void onYesClick() {
        nullstillVerdier();
        dialog.dismiss();
        recreate();
    }

    @Override
    public void onNoClick() {
        return;
    }

    /**Dialogen gir brukeren muligheten til å kansellere avsluttingen av spillet*/
    //TODO: Vi får feil når vi roterer telefonen når dialogboks er oppe. Ser ut som det lett lar seg løse ved å for eksempel lukke dialogboks ved rotasjon
    private void visKonfirmasjonsDialog() {
        dialog = new KonfirmasjonsDialog("statistikk");
        dialog.show(getSupportFragmentManager(), "Avslutt");
    }
}