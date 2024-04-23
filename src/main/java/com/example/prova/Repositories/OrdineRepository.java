package com.example.prova.Repositories;

import com.example.prova.Entities.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import com.example.prova.Entities.Utente;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Integer> {

    List<Ordine> findByUtente(Utente utente);

    boolean existsByUtente(Utente utente);

    //troviamo gli ordini effettuati in data x
    List<Ordine> findByData(Date data);

    //troviamo gli acquisti fatti dall'utente x in data y
    @Query("SELECT o "+
            "FROM Ordine o "+
            "WHERE o.data > :inizio AND o.data < :fine AND o.utente = :utente")
    List<Ordine> findByBuyerInPeriod(Date inizio, Date fine, Utente utente);


    boolean existsByCodice(Integer codice);

    Ordine findByCodice(Integer codice);

    void deleteByCodice(Integer codice);
}
