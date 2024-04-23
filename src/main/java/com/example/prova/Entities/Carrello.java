package com.example.prova.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Table(name="carrello",schema="LaTurra")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codice", nullable = false)
    private int codice;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    @Basic
    @Column(name = "quantita", nullable = false)
    private int quantita;

    @ManyToOne
    @JoinColumn(name = "utente", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name="piatto", nullable = false)
    private Piatto piatto;

    @ManyToOne
    @JoinColumn(name = "ordine_associato")
    @JsonIgnore
    @ToString.Exclude
    private Ordine ordine;

    public Carrello(Date data,Utente utente,Piatto piatto, int quantita) {
        this.data = data;
        this.utente=utente;
        this.piatto=piatto;
        this.piatto.setPrezzo(piatto.getPrezzo());
        this.quantita=quantita;
    }

    public Carrello() {
    }

}
