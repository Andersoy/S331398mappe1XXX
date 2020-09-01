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

public class StatistikkActivity extends AppCompatActivity {

    private Button nullstillButton,  hovedmenyButton;
    TextView antOppgForrigeTextView, antRiktigeForrigeTextView, antFeilForrigeTextView, totAntSpillTextView, totAntOpgTextView, totAntRktgTextView, totAntFeilTextView;

    int antallOppgaverForrige;
    int antallRiktigSvarForrige;
    int antallFeilSvarForrige;
    int totAntSpill;
    int totAntOppg;
    int totAntRiktig;
    int totAntFeil;

    SharedPreferences deltePreferanser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistikk);

        /**Henter inn alle verdier fra sharedpreferences*/
        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);

        antallOppgaverForrige = deltePreferanser.getInt("AntallOppgaverForrige", 0);
        antallRiktigSvarForrige = deltePreferanser.getInt("AntallRiktigeForrige", 0);
        antallFeilSvarForrige = deltePreferanser.getInt("AntallFeilForrige", 0);

        totAntSpill = deltePreferanser.getInt("AntallSpill", 0);
        totAntOppg = deltePreferanser.getInt("TotaltAntOppgaver", 0);
        totAntRiktig = deltePreferanser.getInt("TotaltRiktig", 0);
        totAntFeil = deltePreferanser.getInt("TotaltFeil", 0);

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

        /**Legger inn korrekt tekst og tall til textview*/
        antOppgForrigeTextView.setText(getResources().getString(R.string.antallOppgaver)+antallOppgaverForrige);
        antRiktigeForrigeTextView.setText(getResources().getString(R.string.antallRiktigeSvar)+antallRiktigSvarForrige);
        antFeilForrigeTextView.setText(getResources().getString(R.string.antallFeilSvar)+antallFeilSvarForrige);

        totAntSpillTextView.setText(getResources().getString(R.string.antallSpill)+totAntSpill);
        totAntOpgTextView.setText(getResources().getString(R.string.antallOppgaver)+totAntOppg);
        totAntRktgTextView.setText(getResources().getString(R.string.antallRiktigeSvar)+totAntRiktig);
        totAntFeilTextView.setText(getResources().getString(R.string.antallFeilSvar)+totAntFeil);

        /** Gir knapper onClick-metoder: */

        /**Setter onClick med konfirmasjonsmeny til nullstillknappen*/
        nullstillButton.setOnClickListener(view -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            nullstillVerdier();
                            finish();
                            startActivity(getIntent());
                        case DialogInterface.BUTTON_NEGATIVE:
                            //do nothing
                            break;
                    }
                };
                String erDuSikker = getString(R.string.er_du_sikker);
                String ja= getString(R.string.ja);
                String nei = getString(R.string.nei);
                AlertDialog.Builder builder = new AlertDialog.Builder(StatistikkActivity.this);
                builder.setMessage(erDuSikker).setPositiveButton(ja, dialogClickListener).setNegativeButton(nei, dialogClickListener).show();
            });

        /**onClick-metode som bytter activity til hovedmeny*/
        hovedmenyButton.setOnClickListener(view -> {
            Intent byttTilHovedmeny = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(byttTilHovedmeny);
            finish();
        });
    }

    void nullstillVerdier(){
        SharedPreferences.Editor editor = deltePreferanser.edit();

        //Kjører remove på spesifikke verdier for å ikke slette antall oppgaver man har valgt og muligens språk(når vi får lagt det inn i sharedpref).
        /** Sletter spesifikke verdier for å nullstille statistikken, ikke preferanser*/
        editor.remove("AntallOppgaverForrige");
        editor.remove("AntallRiktigeForrige");
        editor.remove("AntallFeilForrige");
        editor.remove("AntallSpill");
        editor.remove("TotaltAntOppgaver");
        editor.remove("TotaltRiktig");
        editor.remove("TotaltFeil");

        editor.commit();
        Intent oppdater = new Intent(getApplicationContext(), StatistikkActivity.class);
        startActivity(oppdater);
        finish();
    }
}