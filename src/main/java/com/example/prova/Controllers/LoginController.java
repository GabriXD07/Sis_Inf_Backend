package com.example.prova.Controllers;


import com.example.prova.Entities.Utente;
import com.example.prova.Services.LoginService;
import com.example.prova.Support.Eccezioni.PasswordErrataException;
import com.example.prova.Support.Eccezioni.UtenteEsistenteException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(exposedHeaders = "Acces-Control-Allow-Origin")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/log")
    public  ResponseEntity<Utente> log(@RequestBody  Utente uth){
        System.out.println(uth.getMail() + ' ' + uth.getPassword());
        Utente user= null;
        try {
           user = loginService.getUser(uth.getMail(),uth.getPassword());
        } catch (UtenteInesistenteException ex) {
            return new ResponseEntity("Utente inesistente", HttpStatus.BAD_REQUEST);
        } catch (PasswordErrataException e) {
            return new ResponseEntity("Password Errata", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @GetMapping("/logged")
    public ResponseEntity<Utente> checkLogged() {
        System.out.println("sono loggato");
        return new ResponseEntity<Utente>( HttpStatus.OK);
    }


}
