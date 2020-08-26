package com.example.s331398mappe1xxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button spillKnapp;
    Button preferanseKnapp;
    Button statistikkKnapp;
    static int antallOppgaver = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spillKnapp = findViewById(R.id.spillKnapp);
        preferanseKnapp = findViewById(R.id.preferanseKnapp);
        statistikkKnapp = findViewById(R.id.statistikkKnapp);


        spillKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), SpillActivity.class);
                startActivity(byttTilNyActivity);
            }
        });

        statistikkKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), StatistikkActivity.class);
                startActivity(byttTilNyActivity);
            }
        });

        preferanseKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent byttTilNyActivity = new Intent(getApplicationContext(), PreferanseActivity.class);
                startActivity(byttTilNyActivity);
            }
        });


    }

    public static int getAntallOppgaver() {
        return antallOppgaver;
    }

    public void setAntallOppgaver(int antallOppgaver) {
        this.antallOppgaver = antallOppgaver;
    }
}