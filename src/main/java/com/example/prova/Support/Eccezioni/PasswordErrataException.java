package com.example.prova.Support.Eccezioni;

import org.springframework.http.ResponseEntity;

public class PasswordErrataException extends Exception {

    private final String msg = "Password Errata";

    public PasswordErrataException() {
        super();
        System.out.println(msg);
    }
}
