package com.example.prova.Support.Eccezioni;

public class OrdineInesistenteException extends Exception{

    private final String msg = "Utente già esistente";

    public OrdineInesistenteException(){
        super();
        System.out.println(msg);
    }

}
