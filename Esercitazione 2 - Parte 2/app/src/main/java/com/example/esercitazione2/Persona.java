package com.example.esercitazione2;

import java.io.Serializable;
import java.util.Calendar;

public class Persona implements Serializable {

    private String nome, cognome;
    private Calendar dataDiNascita;

    public Persona(){
        this.nome="";
        this.cognome="";

    }

    public Persona(String nome, String cognome, String dataDiNascita){
        this.nome=nome;
        this.cognome=cognome;

    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public Calendar getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(Calendar dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }
}
