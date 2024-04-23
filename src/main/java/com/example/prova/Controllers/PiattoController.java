package com.example.prova.Controllers;

import com.example.prova.Entities.*;
import com.example.prova.Repositories.PiattoRepository;
import com.example.prova.Services.PiattoService;
import com.example.prova.Support.Eccezioni.PiattoEsistenteExcepiton;
import com.example.prova.Support.Eccezioni.PiattoInesistenteException;
import com.example.prova.Support.Messaggio;
import com.example.prova.Entities.Piatto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/piatti")
public class PiattoController {

    @Autowired
    private final PiattoRepository piattoRepository;

    @Autowired
    private PiattoService piattoService;

    public PiattoController(PiattoRepository piattoRepository){
        this.piattoRepository=piattoRepository;
    }

    @GetMapping("/tutti_i_piatti")
    public ResponseEntity<List<Piatto>> mostraTutti(){
        System.out.println("Tutti i piatti");
        return new ResponseEntity<>(piattoRepository.findAll(), HttpStatus.OK);
    }


    @PostMapping("/aggiungi_piatto")
    public ResponseEntity<Piatto> creaPiatto(@RequestBody Piatto piatto) {
        try {
            Piatto nuovo = piattoService.aggiungiPiatto(piatto);
            return new ResponseEntity<>(nuovo, HttpStatus.OK);
        } catch (PiattoEsistenteExcepiton e) {
            return new ResponseEntity(new Messaggio("Piatto già registrata!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifica_piatto")
    public ResponseEntity<Piatto> modificaPiatto(@RequestBody Piatto piatto){
        try{
            Piatto modificata = piattoService.aggiornaPiatto(piatto);
            return new ResponseEntity<>(modificata, HttpStatus.OK);
        }catch (PiattoInesistenteException e){
            return new ResponseEntity(new Messaggio("Piatto inesistente!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina_piatto")
    public ResponseEntity<Messaggio> eliminaPiatto(@RequestParam(value = "codice") Integer codice) {
        try{
            piattoService.rimuoviPiatto(codice);
            return new ResponseEntity<>(new Messaggio("Piatto eliminato con successo"), HttpStatus.OK);
        }catch (PiattoInesistenteException e){
            return new ResponseEntity<>(new Messaggio("Piatto inesistente"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ricerca_avanzata")
    public ResponseEntity<List<Piatto>> ricercaAvanzata( @RequestParam(value = "nomePiatto") String nomePiatto, @RequestParam(value = "ingredienti") String ingredienti){
        try {
            List<Piatto> risultato = piattoService.ricercaAvanzata(nomePiatto, ingredienti);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (PiattoInesistenteException p){
            return new ResponseEntity("Nessun piatto ha quel nome",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ricerca_per_nome")
    public ResponseEntity<Piatto> ricercaPerNomePiatto(@RequestParam(value = "nomePiatto")String nomePiatto){
        try {
            Piatto risultato = piattoService.ricercaPerNomePiatto(nomePiatto);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch(PiattoInesistenteException p){
            return new ResponseEntity("Nessun piatto ha quel nome",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ricercaPerCodice")
    public ResponseEntity<Piatto> ricercaPerCodice(@RequestParam(value = "idPiatto")Integer idPiatto){
        try{
            Piatto ris = piattoService.ricercaPerIdPiatto(idPiatto);
            return new ResponseEntity<>(ris, HttpStatus.OK);
        }catch (PiattoInesistenteException ex){
            return new ResponseEntity("Piatto inesistente", HttpStatus.BAD_REQUEST);
        }
    }


}
