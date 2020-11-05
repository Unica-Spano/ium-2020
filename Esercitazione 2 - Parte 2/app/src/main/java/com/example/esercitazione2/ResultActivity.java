package com.example.esercitazione2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class ResultActivity extends AppCompatActivity {

    Persona persona;
    TextView nomeText, cognomeText, dataText;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        nomeText = findViewById(R.id.titleNome);
        cognomeText = findViewById(R.id.titleCognome);
        dataText = findViewById(R.id.titleData);
        confirm = findViewById(R.id.Confirm);

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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataText.setText(format.format(persona.getDataDiNascita().getTime()));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}