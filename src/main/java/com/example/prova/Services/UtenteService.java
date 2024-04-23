package com.example.prova.Services;

import com.example.prova.Entities.Utente;
import com.example.prova.Repositories.CarrelloRepository;
import com.example.prova.Repositories.UtenteRepository;
import com.example.prova.Support.Eccezioni.OrdineInesistenteException;
import com.example.prova.Support.Eccezioni.UtenteEsistenteException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String mail_regex =  "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final Pattern pattern = Pattern.compile(mail_regex);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public boolean verificaMail(String mail) {
        return pattern.matcher(mail).matches();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Utente> getAllUtenti(){
        return utenteRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Utente registraUtente(Utente uth) throws UtenteEsistenteException {
        if (utenteRepository.existsByMail(uth.getMail())){
            throw new UtenteEsistenteException();
        }
        return utenteRepository.save(uth);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaUtente(Integer id) throws UtenteInesistenteException, OrdineInesistenteException {
        if (!utenteRepository.existsById(id)) {
            throw new UtenteInesistenteException();
        }

        utenteRepository.deleteByIdUtente(id);
    }

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public Utente getUtente(String email){
        System.out.println(utenteRepository.findByMail(email).getNome());
        return utenteRepository.findByMail(email);
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Utente> ricercaAvanzata(String nome, String cognome, String mail){
        return utenteRepository.advancedResearch(nome, cognome, mail);
    }
}
