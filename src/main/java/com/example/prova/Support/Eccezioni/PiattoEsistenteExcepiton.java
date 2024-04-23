package com.example.prova.Support.Eccezioni;

public class PiattoEsistenteExcepiton extends Exception{

    private final String msg = "Piatto già esistente";

    public PiattoEsistenteExcepiton(){
        super();
        System.out.println(msg);
    }
}
