package com.example.prova.Support.Eccezioni;

public class CarrelloVuotoException extends Exception{


        private final String msg = "Carrello Vuoto";

        public CarrelloVuotoException(){
            super();
            System.out.println(msg);
        }


}
