package com.example.prova.Repositories;

import com.example.prova.Entities.PrenotazioneTavolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PrenotazioneTavoloRepository extends JpaRepository<PrenotazioneTavolo, Integer> {

    boolean existsByData(Date data);

    boolean existsByNumeroTavolo(Integer numeroTavolo);

    PrenotazioneTavoloRepository findByNumeroTavolo(Integer numeroTavolo);

    void deleteByNumeroTavolo(Integer numeroTavolo);
}
