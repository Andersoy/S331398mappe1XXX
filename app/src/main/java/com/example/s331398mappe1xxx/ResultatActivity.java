package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultatActivity extends AppCompatActivity {

    Button hovedmenyButton, nyttSpillButton;
    TextView antallOppgaverResTextView, antallRiktigeSvarResTextView, antallFeilSvarResTextView;
    SharedPreferences deltePreferanser;
    int antallOppgaver;
    int antallRiktigSvar;
    int antallFeilSvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        deltePreferanser = getApplicationContext().getSharedPreferences("StatistikkOgPreferanser", 0);

        antallOppgaver = deltePreferanser.getInt("AntallOppgaverForrige", 0);
        antallRiktigSvar = deltePreferanser.getInt("AntallRiktigeForrige", 0);
        antallFeilSvar = deltePreferanser.getInt("AntallFeilForrige", 0);


        hovedmenyButton = findViewById(R.id.hovedmenyResButton);
        nyttSpillButton = findViewById(R.id.spillPaaNyttResButton);
        antallOppgaverResTextView = findViewById(R.id.antallOpgResTextView);
        antallRiktigeSvarResTextView = findViewById(R.id.antRiktSvarResTextView);
        antallFeilSvarResTextView = findViewById(R.id.antFeilSvarResTextView);

        antallOppgaverResTextView.setText(getResources().getString(R.string.antallOppgaver)+antallOppgaver);
        antallRiktigeSvarResTextView.setText(getResources().getString(R.string.antallRiktigeSvar)+ antallRiktigSvar);
        antallFeilSvarResTextView.setText(getResources().getString(R.string.antallFeilSvar)+antallFeilSvar);

        hovedmenyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(byttTilNyActivity);
            }
        });

        nyttSpillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), SpillActivity.class);
                startActivity(byttTilNyActivity);
            }
        });




    }
}