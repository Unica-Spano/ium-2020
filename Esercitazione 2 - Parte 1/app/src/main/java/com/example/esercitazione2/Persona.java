package com.example.esercitazione2;

import java.io.Serializable;

public class Persona implements Serializable {

    private String nome, cognome, dataDiNascita;

    public Persona(){
        this.nome="";
        this.cognome="";
        this.dataDiNascita="";
    }

    public Persona(String nome, String cognome, String dataDiNascita){
        this.nome=nome;
        this.cognome=cognome;
        this.dataDiNascita=dataDiNascita;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

}
