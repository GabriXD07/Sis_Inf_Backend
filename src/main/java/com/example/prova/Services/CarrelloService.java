package com.example.prova.Services;

import com.example.prova.Entities.Piatto;
import com.example.prova.Entities.Carrello;
import com.example.prova.Entities.Utente;
import com.example.prova.Repositories.PiattoRepository;
import com.example.prova.Repositories.CarrelloRepository;
import com.example.prova.Repositories.UtenteRepository;
import com.example.prova.Support.Eccezioni.PiattoInesistenteException;
import com.example.prova.Support.Eccezioni.ProdottoEsauritoException;
import com.example.prova.Support.Eccezioni.UtenteInesistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarrelloService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PiattoRepository piattoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private CarrelloRepository carrelloRepository;

    private List<Carrello> carrello = new ArrayList<>();

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
      public List<Carrello> getCarrello(String mail) throws UtenteInesistenteException {
        if(!utenteRepository.existsByMail(mail))
            throw new UtenteInesistenteException();
        Utente utente = utenteService.getUtente(mail);
        return utente.getCarrello();
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public Carrello aggiorna(Carrello o) {
        return carrelloRepository.save(o);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Carrello add(Date data, Utente utente, Piatto piatto, int quantita) throws UtenteInesistenteException, PiattoInesistenteException, ProdottoEsauritoException {
        if(utente == null || !utenteRepository.existsByMail(utente.getMail()))
            throw new UtenteInesistenteException();
        if(!piattoRepository.existsByIdPiatto(piatto.getIdPiatto()))
            throw new PiattoInesistenteException();
        if(piatto.getQuantita()<=0)
            throw new ProdottoEsauritoException();
        Carrello ret;
        Piatto p=piattoRepository.findByIdPiatto(piatto.getIdPiatto());
        if (carrelloRepository.findByCodice(piatto.getIdPiatto())!=null) {//il prodotto è già presente nel carrello devo aggiornare la quantita'
            ret= carrelloRepository.findByCodice(piatto.getIdPiatto());
            ret.setPiatto(p);
            ret.getPiatto().setQuantita(ret.getPiatto().getQuantita() + quantita);
            ret.getPiatto().setPrezzo(p.getPrezzo());
            return entityManager.merge(ret);

        } else{ //se non è ancora presente nel carrello

            ret=new Carrello(data,utente,p,quantita);
            carrello.add(ret);
            carrelloRepository.save(ret);
        }
        return ret;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaPiattoNelCarrello(Carrello p) throws PiattoInesistenteException {
        if(!carrelloRepository.existsById(p.getCodice())){
            throw new PiattoInesistenteException();
        }
        Utente utente=p.getUtente();
        List<Carrello> carrello = utente.getCarrello();
        for(Carrello o : carrello){
            if(o.getPiatto().getNomePiatto().equals(p.getPiatto().getNomePiatto())){
                if(o.getPiatto().getQuantita()==1) carrelloRepository.delete(o);
                else o.getPiatto().setQuantita(p.getPiatto().getQuantita()-1);
            }
       }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void rimuoviPiatto(Carrello p) {
        carrelloRepository.delete(p);
    }



}
