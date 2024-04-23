package com.example.prova.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Table(name="piatto", schema = "laturra")
public class Piatto{

    public Piatto(String nomePiatto, String ingredienti, double prezzo, int quantita){
        this.nomePiatto=nomePiatto;
        this.ingredienti=ingredienti;
        this.prezzo=prezzo;
        this.quantita=quantita;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piatto", nullable = false)
    private int idPiatto;

    @Basic
    @Column(name = "nomePiatto", nullable = false)
    private String nomePiatto;

    @Basic
    @Column(name = "ingredienti", nullable = false)
    private String ingredienti;

    @Basic
    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    @Basic
    @Column(name = "quantita", nullable = false)
    private int quantita;

    @OneToMany(mappedBy = "piatto", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Carrello> piattoNelCarrello;

    public Piatto() {

    }
}
