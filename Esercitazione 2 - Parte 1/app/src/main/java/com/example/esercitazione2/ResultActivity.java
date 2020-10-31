package com.example.esercitazione2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class ResultActivity extends AppCompatActivity {

    Persona persona;
    TextView nomeText, cognomeText, dataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        nomeText = findViewById(R.id.titleNome);
        cognomeText = findViewById(R.id.titleCognome);
        dataText = findViewById(R.id.titleData);

        Intent intent = getIntent();
        Serializable object = intent.getSerializableExtra(MainActivity.PERSONA_PATH);

        if(object instanceof Persona){
            this.persona = (Persona) object;
        }
        else {
            this.persona = new Persona();
        }

        nomeText.setText(persona.getNome());
        cognomeText.setText(persona.getCognome());
        dataText.setText(persona.getDataDiNascita());
    }
}