package com.example.prova.Controllers;

import com.example.prova.Entities.Utente;
import com.example.prova.Services.CarrelloService;
import com.example.prova.Services.UtenteService;
import com.example.prova.Support.Eccezioni.OrdineInesistenteException;
import com.example.prova.Support.Eccezioni.UtenteEsistenteException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import com.example.prova.Support.Messaggio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    UtenteService utenteService;
    @Autowired
    CarrelloService carrelloService;

    @GetMapping("/ricerca_utente_mail")
    public ResponseEntity<Utente> ricercaUtente(@RequestParam(value = "mail") String mail){
        Utente risultato = utenteService.getUtente(mail);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/tutti_gli_utenti")
    public ResponseEntity<List<Utente>> tuttiGliUtenti(){
        List<Utente> risultato = utenteService.getAllUtenti();
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @PostMapping("/crea_utente")
    public ResponseEntity<Utente> registraUtente(@RequestBody @Valid Utente uth){
        try{
            Utente nuovo = utenteService.registraUtente(uth);
            if (!utenteService.verificaMail(uth.getMail())) {
                return new ResponseEntity(new Messaggio("Email non valida"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(nuovo, HttpStatus.OK);
        }catch (UtenteEsistenteException e){
            return new ResponseEntity(new Messaggio("Utente già esistente"), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/elimina_utente")
    //@PreAuthorize("hasAuthority('frontend-administrator')")
    public ResponseEntity<Messaggio> eliminaUtente(@RequestParam(value = "id_utente") int id){
        try{
            utenteService.eliminaUtente(id);
            return new ResponseEntity<Messaggio>(new Messaggio("Utente eliminato con successo"), HttpStatus.OK);
        } catch (UtenteInesistenteException | OrdineInesistenteException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Messaggio("Utente inesistente"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ricerca_avanzata")
    public ResponseEntity<List<Utente>> ricercaAvanzata(@RequestParam(value = "nome", required = false)String nome,@RequestParam(value = "cognome", required = false)String cognome,@RequestParam(value = "mail", required = false) String mail){
        List<Utente> risultato = utenteService.ricercaAvanzata(nome, cognome, mail);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }
}
