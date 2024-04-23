package com.example.prova.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Table(name="ordine", schema = "laturra")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codice", nullable = false)
    private int codice;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    @CreationTimestamp
    private Date data;

    @Basic
    @Column(name = "indirizzo")
    private String indirizzo;

    @ManyToOne
    @JoinColumn(name = "utente")
    private Utente utente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.MERGE)
    private List<Carrello> carrello;

    public Ordine(List<Carrello> carrello, Utente utente) {
        this.carrello = carrello;
        this.utente = utente;
    }

    public Ordine() {
    }
}
