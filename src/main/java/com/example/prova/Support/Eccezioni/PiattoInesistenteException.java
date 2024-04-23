package com.example.prova.Support.Eccezioni;

public class PiattoInesistenteException extends Exception{

    private final String msg = "Piatto non esistente";

    public PiattoInesistenteException(){
        System.out.println(msg);
    }
}
