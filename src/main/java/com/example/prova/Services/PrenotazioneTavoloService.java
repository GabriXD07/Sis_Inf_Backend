package com.example.prova.Services;

import com.example.prova.Entities.PrenotazioneTavolo;
import com.example.prova.Repositories.PrenotazioneTavoloRepository;
import com.example.prova.Repositories.UtenteRepository;
import com.example.prova.Support.Eccezioni.PrenotatoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrenotazioneTavoloService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PrenotazioneTavoloRepository prenotazioneTavoloRepository;

    private List<PrenotazioneTavolo> tavoli;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public PrenotazioneTavolo addReservation(Date data, String nome, String fasciaOraria, int numeroPersone) throws PrenotatoException {
        int contPranzo=0, contCena=0;
        tavoli = getAllReservations();
        for(int i=0; i<tavoli.size(); i++){
            if(tavoli.get(i).getFasciaOraria().equals("pranzo"))
                contPranzo+=tavoli.get(i).getNumeroPersone();
            else
                contCena+=tavoli.get(i).getNumeroPersone();
        }
        if(fasciaOraria.equals("pranzo")){
            if(contPranzo+numeroPersone>50)
                throw new PrenotatoException();
            else {
                contPranzo += numeroPersone;
                System.out.println(contPranzo);
            }
        }
        if(fasciaOraria.equals("cena")){
            if(contCena+numeroPersone>50)
                throw new PrenotatoException();
            else
                contCena+=numeroPersone;
        }
        PrenotazioneTavolo pt = new PrenotazioneTavolo(data, nome, fasciaOraria, numeroPersone);
        pt.setData(data);
        pt.setFasciaOraria(fasciaOraria);
        pt.setNome(nome);
        pt.setNumeroPersone(numeroPersone);
        tavoli.add(pt);
        prenotazioneTavoloRepository.save(pt);
        return pt;
    }

    public List<PrenotazioneTavolo> getAllReservations() {
        return prenotazioneTavoloRepository.findAll();
    }
}
