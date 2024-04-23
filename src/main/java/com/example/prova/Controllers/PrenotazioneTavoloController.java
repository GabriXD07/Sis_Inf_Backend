package com.example.prova.Controllers;

import com.example.prova.Entities.PrenotazioneTavolo;
import com.example.prova.Entities.Utente;
import com.example.prova.Services.PrenotazioneTavoloService;
import com.example.prova.Support.Eccezioni.PrenotatoException;
import com.example.prova.Support.Messaggio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/tavolo")
public class PrenotazioneTavoloController {

    private PrenotazioneTavoloService prenotazioneTavoloService;

    public PrenotazioneTavoloController(PrenotazioneTavoloService prenotazioneTavoloService) {
        this.prenotazioneTavoloService = prenotazioneTavoloService;
    }

    @PutMapping("/creaPrenotazione")
    public ResponseEntity<PrenotazioneTavolo> creaPrenotazione(@RequestParam Date data,@RequestParam String nome, @RequestParam String fasciaOraria, @RequestParam int numeroPersone ) throws PrenotatoException {
        try {
            PrenotazioneTavolo pt = prenotazioneTavoloService.addReservation(data,nome,fasciaOraria,numeroPersone);
            return new ResponseEntity<>(pt, HttpStatus.OK);
        }catch (PrenotatoException ex){
            return new ResponseEntity(new Messaggio("Prenotazione Esistente"), HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/getAllPrenotazioni")
    public List<PrenotazioneTavolo> getAllPrenotazioni() {
        return prenotazioneTavoloService.getAllReservations();
    }*/
}
