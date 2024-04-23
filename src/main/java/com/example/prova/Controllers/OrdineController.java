package com.example.prova.Controllers;

import com.example.prova.Entities.Ordine;
import com.example.prova.Services.OrdineService;
import com.example.prova.Services.PiattoService;
import com.example.prova.Support.Eccezioni.*;
import com.example.prova.Support.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/ordine")
public class OrdineController {

    @Autowired
    OrdineService ordineService;

    @Autowired
    PiattoService piattoService;

    @PutMapping("/addOrdine")
    public ResponseEntity<Ordine> effettuaOridne(@RequestParam(value = "mail")String mail) {
        try {
            return new ResponseEntity<>(ordineService.effettuaOrdine(mail), HttpStatus.OK);
        } catch (CarrelloVuotoException e) {
            return new ResponseEntity(new Messaggio("Il carrello è vuoto."),HttpStatus.BAD_REQUEST);
        } catch (ProdottoEsauritoException e) {
            return new ResponseEntity(new Messaggio("Prodotto non disponibile."),HttpStatus.BAD_REQUEST);
        } catch (UtenteInesistenteException e) {
            return new ResponseEntity(new Messaggio("Utente inesistente"),HttpStatus.BAD_REQUEST);
        } catch (PiattoInesistenteException e) {
            return new ResponseEntity(new Messaggio("Piatto insesistente."),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAcquisti")
    public ResponseEntity<List<Ordine>> getOrdini(@RequestParam(value = "mail") String mail) {
        try {
            return new ResponseEntity<>(ordineService.getOrdineByUtente(mail), HttpStatus.OK);
        } catch (UtenteInesistenteException e) {
            return new ResponseEntity(new Messaggio("Utente inesistente."), HttpStatus.BAD_REQUEST);
        }
    }
}
