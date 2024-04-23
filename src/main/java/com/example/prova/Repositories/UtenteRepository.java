package com.example.prova.Repositories;

import com.example.prova.Entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    @Query("SELECT u "+
            "FROM Utente u "+
            "WHERE (u.nome LIKE: nome OR :nome IS NULL) AND"+
            "       (u.cognome LIKE: cognome OR :cognome IS NULL) AND"+
            "       (u.mail LIKE: mail OR :mail IS NULL) "
    )
    List<Utente> advancedResearch(String nome, String cognome, String mail );

    boolean existsByMail(String mail);

    //vediamo se esiste un utente con questa mail
    Utente findByMailContaining(String mail);

    Utente findByMail(String mail);

    void deleteByIdUtente(Integer id);
}
