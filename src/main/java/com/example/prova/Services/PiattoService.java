package com.example.prova.Services;


import com.example.prova.Entities.Piatto;
import com.example.prova.Entities.Carrello;
import com.example.prova.Entities.Utente;
import com.example.prova.Repositories.PiattoRepository;
import com.example.prova.Repositories.CarrelloRepository;
import com.example.prova.Repositories.UtenteRepository;
import com.example.prova.Support.Eccezioni.PiattoEsistenteExcepiton;
import com.example.prova.Support.Eccezioni.PiattoInesistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import java.util.List;

@Service
public class PiattoService {
    @Autowired
    private PiattoRepository piattoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Piatto> mostraTuttiIPiatti() { return piattoRepository.findAll(); }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Piatto aggiungiPiatto(Piatto piatto) throws PiattoEsistenteExcepiton {
        if(piattoRepository.existsByIdPiatto(piatto.getIdPiatto())){
            throw new PiattoEsistenteExcepiton();
        }
        Piatto res;
        res = piattoRepository.save(piatto);
        return res;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Piatto aggiornaPiatto(Piatto piattoCorrente) throws PiattoInesistenteException {
        if(!piattoRepository.existsByIdPiatto(piattoCorrente.getIdPiatto())){
            throw new PiattoInesistenteException();
        }
        Piatto piatto = entityManager.find(Piatto.class, piattoCorrente.getNomePiatto());
        piatto.setPrezzo(piattoCorrente.getPrezzo());
        piatto.setQuantita(piattoCorrente.getQuantita());
        return piattoRepository.save(piatto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void rimuoviPiatto(Integer idPiatto) throws PiattoInesistenteException {
        if(!piattoRepository.existsByIdPiatto(idPiatto)){
            throw new PiattoInesistenteException();
        }
        Piatto piatto = piattoRepository.findByIdPiatto(idPiatto);
        List<Utente> utenti = utenteRepository.findAll();
        for(Utente u: utenti){
            List<Carrello> carrello = u.getCarrello();
            for(Carrello opc: carrello){
                if(opc.getPiatto().equals(piatto)){
                    carrelloRepository.delete(opc);
                }
            }
        }
        entityManager.lock(piatto, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        List<Carrello> piattiNelCarrello = piatto.getPiattoNelCarrello();
        for(Carrello pa: piattiNelCarrello)
            carrelloRepository.delete(pa);
        piattoRepository.deleteByIdPiatto(idPiatto);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Piatto> ricercaAvanzata(String nomePiatto, String ingredienti) throws PiattoInesistenteException{
        if(!ingredienti.isEmpty())
            return piattoRepository.advanceResearch(nomePiatto,ingredienti);
        return piattoRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public Piatto ricercaPerNomePiatto(String NomePiatto)throws PiattoInesistenteException{
        return piattoRepository.findByNomePiatto(NomePiatto);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public Piatto ricercaPerIdPiatto(Integer idPiatto)throws PiattoInesistenteException{
        return piattoRepository.findByIdPiatto(idPiatto);
    }
}
