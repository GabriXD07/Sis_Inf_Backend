package com.example.prova.Entities;

import com.example.prova.Repositories.UtenteRepository;
import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Table(name="prenotazioneTavolo", schema = "LaTurra")
public class PrenotazioneTavolo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numeroTavolo", unique = true, nullable = false)
    private int numeroTavolo;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    @Basic
    @Column(name = "fasciaOraria", nullable = false)
    private String fasciaOraria;

    @Basic
    @JoinColumn(name="numeroPersone",nullable = false)
    private int numeroPersone;

    @Basic
    @JoinColumn(name = "nome", nullable = false)
    private String nome;

    public PrenotazioneTavolo(Date data, String nome, String fasciaOraria, int numeroPersone) {
        this.data=data;
        this.nome = nome;
        this.fasciaOraria=fasciaOraria;
        this.numeroPersone=numeroPersone;
    }

    public PrenotazioneTavolo() {
    }
}
