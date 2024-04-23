package com.example.prova.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="utente", schema = "LaTurra")
public class Utente  {

    public Utente(String nome,String mail,String password, String ruolo){
        this.nome=nome;
        this.mail = mail;
        this.password = password;
        this.ruolo = ruolo;
    }

    public Utente(String nome){
        this.nome=nome;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente", nullable = false)
    private int idUtente;

    @Basic
    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Basic
    @Column(name = "cognome", length = 50)
    private String cognome;

    @Basic
    @Column(name = "indirizzo", length = 50)
    private String indirizzo;

    @Basic
    @Column(name = "e_mail", nullable = false,length = 50)
    private String mail;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "ruolo", nullable = false)
    private String ruolo;

    @OneToMany(mappedBy = "utente", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<Carrello> carrello;


    public Utente() {

    }
}
