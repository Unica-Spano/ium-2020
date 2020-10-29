package com.example.esercitazione1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Bundle;

public class Exercises2 extends AppCompatActivity {


    Button incremento, decremento, plus5, minus5, reset;
    EditText input;
    SeekBar seekbar;

    int minValue =0;
    int maxValue =100;
    int modelValue=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exercises_2);

        //prende i riferimenti degli elementi grafici
        incremento = (Button) findViewById(R.id.incremento);
        decremento = (Button) findViewById(R.id.decremento);

        // aggiunta dei nuovi pulsanti
        plus5 = (Button) findViewById(R.id.plus5);
        minus5 = (Button) findViewById(R.id.minus5);
        reset = (Button) findViewById(R.id.reset);

        input = (EditText) findViewById(R.id.input2);
        seekbar = (SeekBar)  findViewById(R.id.seekbar2);

        //posiziona la seekbar su 50 e aggiorna i valori di partenza
        updateValue(modelValue);
        incremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input.setText("+1");
                updateValue(++modelValue);
            }
        });

        plus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input.setText("+1");
                updateValue(modelValue+5);
            }
        });

        minus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input.setText("+1");
                updateValue(modelValue-5);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input.setText("+1");
                updateValue(0);
            }
        });

        decremento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input.setText("-1");
                updateValue(--modelValue);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //avvisa l'utente ogni volta che c'è una modifica nella seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateValue(seekBar.getProgress());
            }
            //avvisa l'utente quando inizia il tocco
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //avvisa l'utente quando finisce il tocco
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateValue(seekBar.getProgress());
            }
        });
    }

    protected  void updateValue(int newValue){
        //verifico che newValue sia nel range e minore di 100
        newValue = newValue > maxValue ? maxValue : newValue;
        newValue = newValue < minValue ? minValue : newValue;

        //aggiorno il valore visualizzato della seekbar
        /*if(this.seekbar.getProgress() != modelValue){
            this.seekbar.setProgress(modelValue);
        }*/

        //aggiorno la variabile che indica il valore attuale della calcolatrice
        this.modelValue = newValue;
        input.setText(""+this.modelValue);

        //aggiorno il valore visualizzato della seekbar
        if(this.seekbar.getProgress() != modelValue){
            this.seekbar.setProgress(modelValue);
        }
    }

   /* @Override
    protected void onStop(){
        super.onStop();

        TextView testo = (TextView) findViewById(R.id.titolomodificabile);
        testo.setText("Siamo in stato di onStop");
    }*/
}