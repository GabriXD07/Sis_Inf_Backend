package com.example.prova.Repositories;

import com.example.prova.Entities.Piatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PiattoRepository extends JpaRepository<Piatto, Integer> {

    //Ricerca Piatto
    @Query("SELECT p " +
            "FROM Piatto p " +
            "WHERE (p.nomePiatto LIKE %:nomePiatto%) AND" +
            "       (p.ingredienti LIKE %:ingredienti%)"
    )
    List<Piatto> advanceResearch(@Param("nomePiatto") String nomePiatto, @Param("ingredienti") String ingredienti);

    boolean existsByIdPiatto(Integer codice);

    void deleteByIdPiatto(Integer codice);

    Piatto findByIdPiatto(Integer codice);
    Piatto findByNomePiatto(String nomePiatto);
}
