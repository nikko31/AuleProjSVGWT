package com.auleSVGWT.client;

import java.io.Serializable;

/**
 * Created by darklinux on 04/03/16.
 */
public class Persona implements Serializable {
    private Integer id;
    private String nome;
    private String cognome;
    private String ruolo;

    public Persona(Integer id, String nome, String cognome, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    public Persona() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
