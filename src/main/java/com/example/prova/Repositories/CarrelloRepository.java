package com.example.prova.Repositories;

import com.example.prova.Entities.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {


    //troviamo gli ordini effettuati in data x
    List<CarrelloRepository> findByData(Date data);

    Carrello findByCodice(Integer codice);

}
