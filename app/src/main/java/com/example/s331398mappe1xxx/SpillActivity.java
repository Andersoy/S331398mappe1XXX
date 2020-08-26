package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SpillActivity extends AppCompatActivity {

    boolean spillAvsluttet = false;
    int antallOppgaver = MainActivity.getAntallOppgaver();
    TextView oppgaveTextView;
    Button avsluttSpillKnapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);
        oppgaveTextView = findViewById(R.id.oppgaverTextView);
        avsluttSpillKnapp = findViewById(R.id.avsluttSpillKnapp);

        avsluttSpillKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(byttTilNyActivity);
            }
        });

        startSpill(antallOppgaver);
    }


    void startSpill(int antallOppgaver){

        oppgaveTextView.setText("test");

        spillAvsluttet = true;
    }
}

