package com.example.esercitazione2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText nome, cognome, data;
    Button inserisci;
    TextView errorText;
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
        errorText = findViewById(R.id.errorText);
        persona = new Persona();

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDialog();
                }
            }
        });

        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showResult = new Intent(MainActivity.this, ResultActivity.class);
                if(checkInput()){
                    UpdatePerson();
                    showResult.putExtra(PERSONA_PATH, persona);
                    startActivity(showResult);
                }
            }
        });
    }

    void showDialog() {
        DialogFragment newFragment = DatePickerFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick(Calendar date) {
        // Do stuff here.
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        data.setText(format.format(date.getTime())); //10/10/2020
    }

    public void doNegativeClick() {
        // Do stuff here.

    }

    public void UpdatePerson(){
        this.persona.setNome(this.nome.getText().toString());
        this.persona.setCognome(this.cognome.getText().toString());

    }

    public void updateDataDiNascita(Calendar date){
        this.persona.setDataDiNascita(date);
    }

    //true se è andato a buon fine, false altrimenti
    public boolean checkInput(){
        int errors =0;
        if(nome.getText() == null || nome.getText().length() ==0){
            errors++;
            nome.setError("Inserire il nome");
        }
        else nome.setError(null);

        if(cognome.getText() == null || cognome.getText().length() ==0){
            errors++;
            cognome.setError("Inserire il cognome");
        }
        else cognome.setError(null);

        if(data.getText() == null || data.getText().length() ==0){
            errors++;
            data.setError("Inserire la data");
        }
        else data.setError(null);

        switch (errors){
            case 0:
                errorText.setVisibility(View.GONE);
                errorText.setText("");
                break;
            case 1:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si è verificato un errore");
                break;
            default:
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Si sono verificati"+ errors+" errori");
                break;
        }
        return errors==0;
    }
}