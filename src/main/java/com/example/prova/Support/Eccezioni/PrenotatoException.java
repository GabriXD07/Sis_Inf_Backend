package com.example.prova.Support.Eccezioni;

public class PrenotatoException extends Exception{
    private final String msg = "Prenotazione già esistente";

    public PrenotatoException(){
        super();
        System.out.println(msg);
    }
}
