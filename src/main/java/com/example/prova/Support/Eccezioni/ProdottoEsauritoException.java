package com.example.prova.Support.Eccezioni;

public class ProdottoEsauritoException extends Exception{
    private final String msg = "Prodotto esaurito";

    public ProdottoEsauritoException(){
        super();
        System.out.println(msg);
    }
}


