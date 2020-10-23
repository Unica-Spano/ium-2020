package com.example.esercitazione1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button incremento, decremento;
    EditText input;
    SeekBar seekBar;
    int minValue =0;
    int maxValue=100;
    int modelValue =50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incremento =findViewById(R.id.incremento);
        decremento = findViewById(R.id.decremento);
        input = findViewById(R.id.input);
        seekBar = findViewById(R.id.seekBar);

        updateValue(modelValue);

        incremento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // input.setText("+1");
                    updateValue(++modelValue);
                }
            }
        );

        decremento.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                    //input.setText("-1");
                    updateValue(--modelValue);
                }
            }
        );

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateValue(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateValue(seekBar.getProgress());
            }
        });
    }

    protected void updateValue(int newValue){
        if(this.seekBar.getProgress() != this.modelValue){
            this.seekBar.setProgress(this.modelValue);
        }
        newValue = newValue>maxValue ? maxValue : newValue;
        newValue = newValue<minValue ? minValue : newValue;
        this.modelValue=newValue;
        input.setText(""+this.modelValue);
    }

   /* @Override
    protected void onStop(){
        super.onStop();

        TextView helloworld = findViewById(R.id.helloworld);
        helloworld.setText("on stop!");
    }*/


}