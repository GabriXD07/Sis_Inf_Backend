package com.example.prova.Controllers;

import com.example.prova.Entities.Carrello;
import com.example.prova.Entities.Piatto;
import com.example.prova.Entities.Utente;
import com.example.prova.Services.PiattoService;
import com.example.prova.Services.CarrelloService;
import com.example.prova.Services.UtenteService;
import com.example.prova.Support.Eccezioni.PiattoInesistenteException;
import com.example.prova.Support.Eccezioni.ProdottoEsauritoException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import com.example.prova.Support.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/carrello")
public class CarrelloController
{
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PiattoService piattoService;


    @GetMapping("/getcarrello")
    public ResponseEntity<List<Carrello>> getCarrello(@RequestParam String mail){
        try{
            List<Carrello> ris = carrelloService.getCarrello(mail);
            return new ResponseEntity<>(ris, HttpStatus.OK);
        }catch (UtenteInesistenteException ex){
            return new ResponseEntity(new Messaggio("Utente inesistente"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/elimina")
    public ResponseEntity<Messaggio> eliminaDaCarrello(@RequestParam(value = "nomePiatto",required = true) String nomePiatto, @RequestParam String mail){
        try {
            Utente utente=utenteService.getUtente(mail);
            List<Carrello> lista = utente.getCarrello();
            for(Carrello piatto : lista){
                if(piatto.getPiatto().getNomePiatto().equals(nomePiatto)){
                    carrelloService.eliminaPiattoNelCarrello(piatto);
                    return new ResponseEntity<>(new Messaggio("Piatto eliminata dal carrello."), HttpStatus.OK);
                }
            }
        } catch (PiattoInesistenteException e) {
            return new ResponseEntity<>(new Messaggio("Piatto Inesistente"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Messaggio("Piatto nel carrello non eliminato."), HttpStatus.BAD_REQUEST)
                ;    }

    @DeleteMapping("/elimina_tutto")
    public ResponseEntity<Messaggio> eliminatutto(@RequestParam(value = "mail",required = true) String mail,@RequestParam(value = "nomePiatto",required = true) String nomePiatto){

        Utente utente=utenteService.getUtente(mail);
        List<Carrello> lista = utente.getCarrello();
        for(Carrello piatto : lista){
            if(piatto.getPiatto().getNomePiatto().equals(nomePiatto)){
                carrelloService.rimuoviPiatto(piatto);
                return new ResponseEntity<>(new Messaggio("Piatto eliminato dal carrello."), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new Messaggio("Piatto nel carrello non eliminato."), HttpStatus.BAD_REQUEST)
                ;    }



    @PutMapping("/addPiatto")
    public ResponseEntity<Carrello> addPiatto(@RequestParam(value = "data") Date data, @RequestParam(value="mail") String mail, @RequestParam(value = "idPiatto") Integer idPiatto, @RequestParam(value = "quantita")int quantita) throws PiattoInesistenteException {
        Utente u = utenteService.getUtente(mail);
        Piatto p = piattoService.ricercaPerIdPiatto(idPiatto);
        Carrello ris = null;
        try {
            ris = carrelloService.add(data,u,p,quantita);
            return new ResponseEntity<>(ris,HttpStatus.OK);
        } catch (PiattoInesistenteException e) {
            return new ResponseEntity(new Messaggio(" Piatto inesistente."),HttpStatus.BAD_REQUEST);
        } catch (ProdottoEsauritoException e) {
            return new ResponseEntity(new Messaggio(" Quantità non disponibile."),HttpStatus.BAD_REQUEST);
        } catch (UtenteInesistenteException e) {
            return new ResponseEntity(new Messaggio("Utente inesistente"), HttpStatus.BAD_REQUEST);
        }

    }
}
