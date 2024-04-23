package com.example.prova.Services;

import com.example.prova.Entities.Carrello;
import com.example.prova.Entities.Ordine;
import com.example.prova.Entities.Utente;
import com.example.prova.Repositories.CarrelloRepository;
import com.example.prova.Repositories.OrdineRepository;
import com.example.prova.Repositories.UtenteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.example.prova.Support.Eccezioni.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CarrelloRepository CarrelloRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CarrelloRepository carrelloRepository;


    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = ProdottoEsauritoException.class)
    public Ordine effettuaOrdine(String mail) throws ProdottoEsauritoException, CarrelloVuotoException, UtenteInesistenteException, PiattoInesistenteException {
        Utente u = utenteRepository.findByMailContaining(mail);

        LinkedList<Carrello> piatti = new LinkedList<>();
        //risultato finale
        Ordine ordine = new Ordine();

        for ( Carrello onc : u.getCarrello()) {
            int nuovaQuantità = onc.getPiatto().getQuantita()-onc.getQuantita();
            if(nuovaQuantità < 0){
                //non posso effettuare l'acquisto
                throw new ProdottoEsauritoException();
            }
            onc.getPiatto().setQuantita(nuovaQuantità);

            //definisco il piatto che verrà mostrata nell'acquisto
            Carrello newOnc = new Carrello();

            newOnc.setQuantita( onc.getQuantita());
            newOnc.setPiatto(onc.getPiatto());
            newOnc.setUtente(u);
            //associo l'ordine al piatto nel carrello
            ordine.setIndirizzo(u.getIndirizzo());
            newOnc.setOrdine(ordine);
            //aggiungo il piattoNelCarrello alla lista di piattiNelCarrello
            piatti.add(newOnc);
            //elimino l'elemento dal carrello perché venduto
            carrelloRepository.delete(onc);
        }

        ordineRepository.save(ordine);

//        entityManager.refresh(ordine);
        ordine.setUtente(u);
        ordine.setCarrello(piatti);
//        entityManager.merge(ordine);

        return ordine;
    }


    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Ordine> getOrdineByUtente(String email) throws UtenteInesistenteException {
        if ( !utenteRepository.existsByMail(email) ) {
            throw new UtenteInesistenteException();
        }
        return ordineRepository.findByUtente(utenteRepository.findByMailContaining(email));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaOrdine(Integer codice) throws OrdineInesistenteException {
        if(!ordineRepository.existsByCodice(codice)){
            throw new OrdineInesistenteException();
        }
        Ordine o = ordineRepository.findByCodice(codice);

        if(o.getCarrello()!= null){
            for(Carrello piatto : o.getCarrello()){
                carrelloRepository.delete(piatto);
            }
        }

        ordineRepository.deleteByCodice(codice);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Ordine aggiorna(Ordine ordine) throws OrdineInesistenteException {
        if(!ordineRepository.existsByCodice(ordine.getCodice())){
            throw new OrdineInesistenteException();
        }
        return ordineRepository.save(ordine);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED) public List<Ordine> ricercaPerData(Date data){
        return ordineRepository.findByData(data);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Ordine> ricercaPerPeriodoDiAcquisto(Date inizio, Date fine, Utente utente){
        return ordineRepository.findByBuyerInPeriod(inizio, fine, utente);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public List<Ordine> findAll(){return ordineRepository.findAll();}

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void eliminaOrdine(Utente utente){
        List<Ordine> ordini = ordineRepository.findByUtente(utente);
        for( Ordine o : ordini){
            ordineRepository.deleteByCodice(o.getCodice());
        }
    }

}