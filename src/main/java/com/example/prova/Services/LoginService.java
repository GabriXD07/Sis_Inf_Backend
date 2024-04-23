package com.example.prova.Services;


import com.example.prova.Entities.Utente;
import com.example.prova.Repositories.UtenteRepository;
import com.example.prova.Support.Eccezioni.PasswordErrataException;
import com.example.prova.Support.Eccezioni.UtenteEsistenteException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional(readOnly = false, isolation= Isolation.READ_COMMITTED)
    public Utente addAndgetUser(Utente uth) throws UtenteEsistenteException{

        if (utenteRepository.existsByMail(uth.getMail()))
            throw new UtenteEsistenteException();
        utenteRepository.save(uth);
        return uth;
    }

    public Utente getUser(String mail, String pass) throws UtenteInesistenteException, PasswordErrataException{
        if(!utenteRepository.existsByMail(mail)){
            throw new UtenteInesistenteException();
        }
        Utente utente = utenteRepository.findByMail(mail);
        if(!utente.getPassword().equals(pass))
            throw new PasswordErrataException();
        System.out.println(utente.getPassword());
        System.out.println(pass);
        return utente;
    }
}
