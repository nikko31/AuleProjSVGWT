package com.auleSVGWT.client;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by darklinux on 04/03/16.
 */
public class Stanza implements Serializable {
    private Integer id;
    private Integer numero;
    private Integer num_max_posti;
    private Integer mtq;
    private ArrayList<Persona> prof;

    public Stanza(Integer id, Integer numero, Integer num_max_posti, Integer mtq) {
        this.prof = new ArrayList<>();
        this.numero = numero;
        this.num_max_posti = num_max_posti;
        this.mtq = mtq;
        this.id = id;

    }

    public Stanza() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getNum_max_posti() {
        return num_max_posti;
    }

    public void setNum_max_posti(Integer num_max_posti) {
        this.num_max_posti = num_max_posti;
    }

    public Integer getMtq() {
        return mtq;
    }

    public void setMtq(Integer mtq) {
        this.mtq = mtq;
    }

    public ArrayList<Persona> getProf() {
        return prof;
    }

    public void setProf(ArrayList<Persona> prof) {
        this.prof.addAll(prof);
    }

    public void addProf(Persona prof) {
        this.prof.add(prof);
    }

    public void removeProf(int index) {
        this.prof.remove(index);
    }
}
