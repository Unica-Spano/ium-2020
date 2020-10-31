package com.example.esercitazione2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText nome, cognome, data;
    Button inserisci;
    Persona persona;
    public static final String PERSONA_PATH ="com.example.esercitazione2.Persona";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.attrNome);
        cognome = findViewById(R.id.attrCognome);
        data = findViewById(R.id.attrData);
        inserisci = findViewById(R.id.saveButton);
        persona = new Persona();

        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(MainActivity.this, ResultActivity.class);
                UpdatePerson();
                showResult.putExtra(PERSONA_PATH, persona);
                startActivity(showResult);
            }
        });
    }

    public void UpdatePerson(){
        this.persona.setNome(this.nome.getText().toString());
        this.persona.setCognome(this.cognome.getText().toString());
        this.persona.setDataDiNascita(this.data.getText().toString());
    }
}